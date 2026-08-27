[CmdletBinding()]
param(
    [ValidateSet("A", "B", "C", "All")]
    [string] $Priority = "All",

    [ValidateSet("Spread", "Today")]
    [string] $InitialDueMode = "Spread",

    [ValidateRange(1, 50)]
    [int] $DailyLimit = 5,

    [ValidateRange(0, 365)]
    [int] $StartOffsetDays = 1,

    [switch] $RebuildImported,

    [switch] $ExcludeDesign,

    [switch] $DryRun
)

Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

function Set-JsonProperty {
    param(
        [Parameter(Mandatory = $true)] $Object,
        [Parameter(Mandatory = $true)] [string] $Name,
        $Value
    )

    if ($Object.PSObject.Properties[$Name]) {
        $Object.$Name = $Value
    } else {
        Add-Member -InputObject $Object -NotePropertyName $Name -NotePropertyValue $Value
    }
}

function ConvertTo-DisplayTitle {
    param([string] $RelativeFile)

    $stem = [System.IO.Path]::GetFileNameWithoutExtension($RelativeFile)
    $title = $stem -replace "_", " "
    $title = $title -creplace "([A-Z]+)([A-Z][a-z])", '$1 $2'
    $title = $title -creplace "([a-z0-9])([A-Z])", '$1 $2'
    $title = $title -replace "\s+", " "
    return $title.Trim()
}

function ConvertTo-NormalizedTitle {
    param([string] $Title)
    return (($Title -replace "[^A-Za-z0-9]", "").ToUpperInvariant())
}

function ConvertTo-IdToken {
    param([string] $Value)

    $spaced = $Value -replace "_", " "
    $spaced = $spaced -creplace "([A-Z]+)([A-Z][a-z])", '$1 $2'
    $spaced = $spaced -creplace "([a-z0-9])([A-Z])", '$1 $2'
    $token = ($spaced -replace "[^A-Za-z0-9]+", "-").Trim("-").ToUpperInvariant()
    return $token
}

function ConvertTo-ProblemId {
    param([string] $RelativeFile)

    $parts = $RelativeFile -split "[\\/]"
    $stem = [System.IO.Path]::GetFileNameWithoutExtension($RelativeFile)
    $day = $null
    $session = $null
    foreach ($part in $parts) {
        if ($part -match "^day(\d+)$") {
            $day = "D" + $Matches[1]
        }
        if ($part -match "^session(\d+)$") {
            $session = "S" + $Matches[1]
        }
    }

    $tokens = New-Object System.Collections.Generic.List[string]
    $tokens.Add("DSA10")
    if ($parts[0] -eq "design") {
        $tokens.Add("DESIGN")
    } elseif ($day) {
        $tokens.Add($day)
        if ($session) {
            $tokens.Add($session)
        }
    }
    $tokens.Add((ConvertTo-IdToken $stem))
    return (($tokens | Where-Object { -not [string]::IsNullOrWhiteSpace($_) }) -join "-")
}

function Get-Difficulty {
    param(
        [string] $SourcePath,
        [string] $Priority
    )

    if (Test-Path -LiteralPath $SourcePath) {
        $hit = Select-String -LiteralPath $SourcePath -Pattern "(?i)\bDifficulty\s*:?\s*(Easy|Medium|Hard)" -List | Select-Object -First 1
        if ($hit) {
            $match = [regex]::Match($hit.Line, "(?i)\bDifficulty\s*:?\s*(Easy|Medium|Hard)")
            if ($match.Success) {
                return $match.Groups[1].Value.ToUpperInvariant()
            }
        }
    }

    if ($Priority -eq "C") {
        return "MEDIUM"
    }
    return "MEDIUM"
}

function Get-Tags {
    param(
        [string] $RelativeFile,
        [string] $Pattern,
        [string] $Priority
    )

    $tags = New-Object System.Collections.Generic.List[string]
    $tags.Add(("priority-" + $Priority.ToLowerInvariant()))

    foreach ($part in ($RelativeFile -split "[\\/]")) {
        $clean = ($part -replace "\.java$", "" -replace "[^A-Za-z0-9]+", "-").Trim("-").ToLowerInvariant()
        if (-not [string]::IsNullOrWhiteSpace($clean)) {
            $tags.Add($clean)
        }
    }

    foreach ($part in ($Pattern -split "[^A-Za-z0-9]+")) {
        $clean = $part.Trim().ToLowerInvariant()
        if (-not [string]::IsNullOrWhiteSpace($clean)) {
            $tags.Add($clean)
        }
    }

    return @($tags | Select-Object -Unique)
}

function Get-IndexRows {
    param(
        [string] $RepoRoot,
        [string] $IndexPath
    )

    if (-not (Test-Path -LiteralPath $IndexPath)) {
        throw "Problem index not found: $IndexPath"
    }

    $rows = New-Object System.Collections.Generic.List[object]
    $pattern = '^\|\s*`([^`]+\.java)`\s*\|\s*([^|]+?)\s*\|\s*([ABC])\s*\|'
    foreach ($line in Get-Content -LiteralPath $IndexPath) {
        $match = [regex]::Match($line, $pattern)
        if (-not $match.Success) {
            continue
        }

        $relativeFile = $match.Groups[1].Value.Trim()
        $rowPriority = $match.Groups[3].Value.Trim()
        if ($Priority -ne "All" -and $rowPriority -ne $Priority) {
            continue
        }
        if ($ExcludeDesign -and $relativeFile -match "^design[\\/]") {
            continue
        }
        if ($relativeFile -in @("Main.java", "CheatSheet.java")) {
            continue
        }

        $normalizedRelative = $relativeFile.Replace("\", "/")
        $sourcePath = Join-Path $RepoRoot ("src/main/java/org/chijai/" + $normalizedRelative)
        $title = ConvertTo-DisplayTitle $relativeFile
        $row = [pscustomobject]@{
            File = $relativeFile
            Title = $title
            NormalizedTitle = ConvertTo-NormalizedTitle $title
            Pattern = $match.Groups[2].Value.Trim()
            Priority = $rowPriority
            Difficulty = Get-Difficulty -SourcePath $sourcePath -Priority $rowPriority
            Tags = Get-Tags -RelativeFile $relativeFile -Pattern $match.Groups[2].Value.Trim() -Priority $rowPriority
            CodePath = "src/main/java/org/chijai/" + $relativeFile.Replace("\", "/")
            NotesPath = "dsa-review/notes/PROBLEM_PATTERN_INDEX.md"
            SourceExists = Test-Path -LiteralPath $sourcePath
            SuggestedId = ConvertTo-ProblemId $relativeFile
        }
        $rows.Add($row)
    }

    return @($rows | Sort-Object @{Expression = { switch ($_.Priority) { "A" { 0 } "B" { 1 } "C" { 2 } default { 3 } } } }, File)
}

function New-DefaultDeck {
    return [pscustomobject]@{
        schemaVersion = 1
        generatedAt = (Get-Date).ToUniversalTime().ToString("o")
        settings = [pscustomobject]@{
            timeZone = "Asia/Kolkata"
            desiredRetention = 0.9
            maximumIntervalDays = 36500
            enableFuzzing = $false
            estimatedMinutesPerProblem = 15
            githubIssueTitle = "Today's DSA Review Queue"
            learningStepsMinutes = @()
            relearningStepsMinutes = @(10)
        }
        problems = @()
        reviewEvents = @()
    }
}

function Get-Deck {
    param([string] $ReviewPath)

    if (Test-Path -LiteralPath $ReviewPath) {
        return (Get-Content -Raw -LiteralPath $ReviewPath | ConvertFrom-Json)
    }
    return New-DefaultDeck
}

function Merge-Tags {
    param(
        $ExistingTags,
        $NewTags
    )

    return @(@($ExistingTags) + @($NewTags) | Where-Object { $_ } | Select-Object -Unique)
}

$RepoRoot = (Resolve-Path (Join-Path $PSScriptRoot "../..")).Path
$IndexPath = Join-Path $RepoRoot "dsa-review/notes/PROBLEM_PATTERN_INDEX.md"
$ReviewDir = Join-Path $RepoRoot "review"
$ReviewPath = Join-Path $ReviewDir "review.json"

$rows = @(Get-IndexRows -RepoRoot $RepoRoot -IndexPath $IndexPath)
if ($rows.Count -eq 0) {
    throw "No importable rows found in $IndexPath"
}

$deck = Get-Deck -ReviewPath $ReviewPath
Set-JsonProperty $deck "schemaVersion" 1
if (-not $deck.PSObject.Properties["settings"] -or $null -eq $deck.settings) {
    Set-JsonProperty $deck "settings" (New-DefaultDeck).settings
}
if (-not $deck.PSObject.Properties["problems"] -or $null -eq $deck.problems) {
    Set-JsonProperty $deck "problems" @()
}
if (-not $deck.PSObject.Properties["reviewEvents"] -or $null -eq $deck.reviewEvents) {
    Set-JsonProperty $deck "reviewEvents" @()
}

$existingProblems = New-Object System.Collections.Generic.List[object]
$removedImported = 0
foreach ($problem in @($deck.problems)) {
    if ($RebuildImported) {
        $id = if ($problem.PSObject.Properties["id"]) { [string] $problem.id } else { "" }
        $contentType = if ($problem.PSObject.Properties["contentType"]) { [string] $problem.contentType } else { "" }
        $attempts = if ($problem.PSObject.Properties["attempts"] -and $problem.attempts) { [int] $problem.attempts } else { 0 }
        $lastReviewed = if ($problem.PSObject.Properties["lastReviewed"]) { $problem.lastReviewed } else { $null }
        $isImported = $id -like "DSA10-*" -or $contentType -eq "dsa10days"
        if ($isImported -and $attempts -eq 0 -and $null -eq $lastReviewed) {
            $removedImported++
            continue
        }
    }
    $existingProblems.Add($problem)
}

$byId = @{}
$byTitle = @{}
foreach ($problem in $existingProblems) {
    if ($problem.PSObject.Properties["id"] -and $problem.id) {
        $byId[$problem.id.ToUpperInvariant()] = $problem
    }
    if ($problem.PSObject.Properties["title"] -and $problem.title) {
        $normalized = ConvertTo-NormalizedTitle $problem.title
        if (-not $byTitle.ContainsKey($normalized)) {
            $byTitle[$normalized] = $problem
        }
    }
}

$today = (Get-Date).Date.AddDays($StartOffsetDays)
$added = 0
$updated = 0
$missingSources = 0
$newProblemIndex = 0

foreach ($row in $rows) {
    if (-not $row.SourceExists) {
        $missingSources++
    }

    $matched = $null
    if ($byTitle.ContainsKey($row.NormalizedTitle)) {
        $matched = $byTitle[$row.NormalizedTitle]
    } elseif ($byId.ContainsKey($row.SuggestedId.ToUpperInvariant())) {
        $matched = $byId[$row.SuggestedId.ToUpperInvariant()]
    }

    if ($matched) {
        Set-JsonProperty $matched "title" $matched.title
        Set-JsonProperty $matched "contentType" $(if ($matched.id -match "^LC\d+$") { "leetcode" } else { "dsa10days" })
        Set-JsonProperty $matched "pattern" $row.Pattern
        Set-JsonProperty $matched "difficulty" $row.Difficulty
        Set-JsonProperty $matched "tags" (Merge-Tags -ExistingTags $matched.tags -NewTags $row.Tags)
        Set-JsonProperty $matched "codePath" $row.CodePath
        Set-JsonProperty $matched "notesPath" $row.NotesPath
        Set-JsonProperty $matched "githubUrl" $(if ($matched.PSObject.Properties["githubUrl"]) { $matched.githubUrl } else { "" })
        $updated++
        continue
    }

    $id = $row.SuggestedId
    $suffix = 2
    while ($byId.ContainsKey($id.ToUpperInvariant())) {
        $id = $row.SuggestedId + "-" + $suffix
        $suffix++
    }

    if ($InitialDueMode -eq "Today") {
        $nextReview = $today
    } else {
        $nextReview = $today.AddDays([Math]::Floor($newProblemIndex / [double] $DailyLimit))
    }
    $newProblemIndex++

    $problem = [pscustomobject]@{
        id = $id
        title = $row.Title
        contentType = "dsa10days"
        pattern = $row.Pattern
        difficulty = $row.Difficulty
        tags = $row.Tags
        codePath = $row.CodePath
        notesPath = $row.NotesPath
        githubUrl = ""
        repetitions = 0
        interval = 0
        easeFactor = 2.5
        stability = 0.0
        difficultyScore = 0.0
        fsrsState = "LEARNING"
        fsrsStep = 0
        nextReview = $nextReview.ToString("yyyy-MM-dd")
        attempts = 0
        averageSolveTimeSeconds = 0
        hintUsedCount = 0
        compileFailures = 0
        mistakes = @()
    }

    $existingProblems.Add($problem)
    $byId[$id.ToUpperInvariant()] = $problem
    $byTitle[$row.NormalizedTitle] = $problem
    $added++
}

Set-JsonProperty $deck "generatedAt" (Get-Date).ToUniversalTime().ToString("o")
Set-JsonProperty $deck "problems" @($existingProblems | Sort-Object id)

$summary = [pscustomobject]@{
    repoRoot = $RepoRoot
    reviewPath = $ReviewPath
    indexRows = $rows.Count
    added = $added
    updated = $updated
    totalProblems = @($deck.problems).Count
    missingSourceFiles = $missingSources
    removedImported = $removedImported
    initialDueMode = $InitialDueMode
    dailyLimit = $DailyLimit
    startOffsetDays = $StartOffsetDays
    dryRun = [bool] $DryRun
}

if (-not $DryRun) {
    if (-not (Test-Path -LiteralPath $ReviewDir)) {
        New-Item -ItemType Directory -Path $ReviewDir | Out-Null
    }
    $json = $deck | ConvertTo-Json -Depth 40
    Set-Content -LiteralPath $ReviewPath -Value $json -Encoding UTF8
}

$summary | Format-List
