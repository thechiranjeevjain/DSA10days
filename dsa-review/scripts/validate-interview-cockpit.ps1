$ErrorActionPreference = "Stop"

$scriptRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
$reviewRoot = Split-Path -Parent $scriptRoot
$repoRoot = Split-Path -Parent $reviewRoot
$interviewRoot = Join-Path $reviewRoot "interview"

function Fail {
    param([string] $Message)
    throw "Interview cockpit validation failed: $Message"
}

$requiredFiles = @(
    "README.md",
    "00_PATTERN_RECOGNITION_80_20.md",
    "01_ZERO_TO_HERO_RANKED_TABLE.md",
    "02_ONE_LINE_RECALL_ALL_PROBLEMS.md",
    "03_CRISP_INTERVIEW_ANSWERS.md",
    "04_TWO_DAY_AND_SEVEN_DAY_PLANS.md"
)

foreach ($name in $requiredFiles) {
    $path = Join-Path $interviewRoot $name
    if (-not (Test-Path -LiteralPath $path)) {
        Fail "missing $path"
    }
}

$rankedPath = Join-Path $interviewRoot "01_ZERO_TO_HERO_RANKED_TABLE.md"
$rankedText = Get-Content -LiteralPath $rankedPath -Raw
$rankLines = @(Select-String -LiteralPath $rankedPath -Pattern '^\| (?<rank>\d+) \|' | ForEach-Object { $_.Line })
$topRankMatch = [regex]::Match($rankedText, '^\| 1 \| (?<title>[^|]+?) \|' , [System.Text.RegularExpressions.RegexOptions]::Multiline)

if ($rankLines.Count -lt 150) {
    Fail "expected at least 150 ranked rows, found $($rankLines.Count)"
}

$ranks = @($rankLines | ForEach-Object {
    if ($_ -match '^\| (?<rank>\d+) \|') { [int] $Matches.rank }
})

if ($ranks.Count -ne $rankLines.Count) {
    Fail "could not parse all ranks"
}

$expectedPhaseHeaders = @(
    "## Phase 1 - No Red Flags",
    "## Phase 2 - Strong Core",
    "## Phase 3 - Important",
    "## Phase 4 - Secondary",
    "## Phase 5 - If Time"
)

function Assert-PhaseHeaders {
    param(
        [string] $Path,
        [string[]] $ExpectedHeaders
    )

    $actualHeaders = @(Select-String -LiteralPath $Path -Pattern '^## Phase ' | ForEach-Object { $_.Line.Trim() })
    if ($actualHeaders.Count -ne $ExpectedHeaders.Count) {
        Fail "expected $($ExpectedHeaders.Count) phase headers in $Path, found $($actualHeaders.Count): $($actualHeaders -join ', ')"
    }

    for ($i = 0; $i -lt $ExpectedHeaders.Count; $i++) {
        if ($actualHeaders[$i] -ne $ExpectedHeaders[$i]) {
            Fail "phase header mismatch in $Path at position $($i + 1): expected '$($ExpectedHeaders[$i])', found '$($actualHeaders[$i])'"
        }
    }
}

Assert-PhaseHeaders -Path $rankedPath -ExpectedHeaders $expectedPhaseHeaders

$rankSet = @{}
foreach ($rank in $ranks) {
    if ($rankSet.ContainsKey($rank)) {
        Fail "duplicate rank $rank"
    }
    $rankSet[$rank] = $true
}

for ($i = 1; $i -le $ranks.Count; $i++) {
    if (-not $rankSet.ContainsKey($i)) {
        Fail "missing rank $i"
    }
}

$javaMatches = @(Select-String -LiteralPath $rankedPath -Pattern '\[Java\]\(([^)]+)\)' -AllMatches)
$javaLinkCount = 0
$missingJavaLinks = @()
foreach ($line in $javaMatches) {
    foreach ($match in $line.Matches) {
        $javaLinkCount++
        $target = $match.Groups[1].Value
        $fullPath = [System.IO.Path]::GetFullPath((Join-Path (Split-Path -Parent $rankedPath) $target))
        if (-not (Test-Path -LiteralPath $fullPath)) {
            $missingJavaLinks += $target
        }
    }
}

if ($javaLinkCount -ne $rankLines.Count) {
    Fail "expected one Java link per ranked row, found $javaLinkCount links for $($rankLines.Count) rows"
}

if ($missingJavaLinks.Count -gt 0) {
    Fail "missing Java links: $($missingJavaLinks -join ', ')"
}

$leetcodeLinkCount = ([regex]::Matches($rankedText, 'https://leetcode\.com/problems/')).Count
$localOnlyCount = $rankLines.Count - $leetcodeLinkCount
if ($leetcodeLinkCount -lt 100) {
    Fail "expected substantial LeetCode coverage, found $leetcodeLinkCount links"
}

if (-not $topRankMatch.Success) {
    Fail "could not parse the current rank 1 title"
}

$patternText = Get-Content -LiteralPath (Join-Path $interviewRoot "00_PATTERN_RECOGNITION_80_20.md") -Raw
if ($patternText -notmatch 'brute force -> bottleneck -> pattern -> invariant -> code -> dry run') {
    Fail "pattern recognition rhythm is missing"
}

$crispText = Get-Content -LiteralPath (Join-Path $interviewRoot "03_CRISP_INTERVIEW_ANSWERS.md") -Raw
$topRankTitle = [regex]::Escape($topRankMatch.Groups["title"].Value.Trim())
if ($crispText -notmatch "### 1\. $topRankTitle") {
    Fail "crisp answer deck is missing the current rank 1 answer"
}

Assert-PhaseHeaders -Path (Join-Path $interviewRoot "02_ONE_LINE_RECALL_ALL_PROBLEMS.md") -ExpectedHeaders $expectedPhaseHeaders
Assert-PhaseHeaders -Path (Join-Path $interviewRoot "03_CRISP_INTERVIEW_ANSWERS.md") -ExpectedHeaders $expectedPhaseHeaders

$interviewText = (Get-ChildItem -LiteralPath $interviewRoot -Recurse -Filter "*.md" |
    Sort-Object FullName |
    ForEach-Object { Get-Content -LiteralPath $_.FullName -Raw }) -join "`n"
$badGeneratedPhrases = @(
    "Stack stores unresolved candidates",
    "Fix dp state meaning",
    "State API contract",
    "Use the algebra, bit, or string invariant",
    "Mark visited before recursion and explore one component/path completely",
    "For unweighted minimum steps, mark when enqueuing",
    "Heap top is the next best candidate",
    "Each node is a prefix; branch only",
    "Start from brute force",
    "Try all candidate states or combinations directly",
    "Repeated work appears as rescanning, recomputing, or revisiting state",
    "Maintain a monotonic search space and discard the half that cannot contain the answer"
)

foreach ($phrase in $badGeneratedPhrases) {
    $escapedPhrase = [regex]::Escape($phrase)
    if ($interviewText -match $escapedPhrase) {
        Fail "generated interview docs still contain generic fallback phrase: $phrase"
    }
}

$readmeText = Get-Content -LiteralPath (Join-Path $interviewRoot "README.md") -Raw
if ($readmeText -notmatch 'patterns/README\.md') {
    Fail "main README does not link to pattern files"
}

$patternDir = Join-Path $interviewRoot "patterns"
if (-not (Test-Path -LiteralPath $patternDir)) {
    Fail "missing pattern directory: $patternDir"
}

$patternIndexPath = Join-Path $patternDir "README.md"
if (-not (Test-Path -LiteralPath $patternIndexPath)) {
    Fail "missing pattern index: $patternIndexPath"
}

$patternFiles = @(Get-ChildItem -LiteralPath $patternDir -File -Filter "*.md" | Where-Object { $_.Name -ne "README.md" } | Sort-Object Name)
if ($patternFiles.Count -lt 10) {
    Fail "expected at least 10 pattern files, found $($patternFiles.Count)"
}

$patternIndexText = Get-Content -LiteralPath $patternIndexPath -Raw
if ($patternIndexText -notmatch 'HashMap / Frequency / Set') {
    Fail "pattern index is missing the expected display category"
}

$patternProblemRows = 0
$patternRankSet = @{}
$missingPatternJavaLinks = @()
foreach ($patternFile in $patternFiles) {
    $problemLines = @(Select-String -LiteralPath $patternFile.FullName -Pattern '^\| (?<rank>\d+) \|')
    if ($problemLines.Count -eq 0) {
        Fail "pattern file has no problem rows: $($patternFile.FullName)"
    }

    foreach ($line in $problemLines) {
        $patternProblemRows++
        $rank = [int] $line.Matches[0].Groups["rank"].Value
        if ($patternRankSet.ContainsKey($rank)) {
            Fail "duplicate rank $rank across pattern files"
        }
        $patternRankSet[$rank] = $true
    }

    $patternJavaMatches = @(Select-String -LiteralPath $patternFile.FullName -Pattern '\[Java\]\(([^)]+)\)' -AllMatches)
    foreach ($line in $patternJavaMatches) {
        foreach ($match in $line.Matches) {
            $target = $match.Groups[1].Value
            $fullPath = [System.IO.Path]::GetFullPath((Join-Path $patternFile.DirectoryName $target))
            if (-not (Test-Path -LiteralPath $fullPath)) {
                $missingPatternJavaLinks += "$($patternFile.Name):$target"
            }
        }
    }
}

if ($patternProblemRows -ne $rankLines.Count) {
    Fail "expected pattern files to contain $($rankLines.Count) problem rows, found $patternProblemRows"
}

for ($i = 1; $i -le $rankLines.Count; $i++) {
    if (-not $patternRankSet.ContainsKey($i)) {
        Fail "pattern files are missing rank $i"
    }
}

if ($missingPatternJavaLinks.Count -gt 0) {
    Fail "missing pattern Java links: $($missingPatternJavaLinks -join ', ')"
}

$asciiFiles = @(Get-ChildItem -LiteralPath $interviewRoot -Recurse -File)
$nonAsciiFiles = @()
$controlByteFiles = @()
foreach ($file in $asciiFiles) {
    $bytes = [System.IO.File]::ReadAllBytes($file.FullName)
    if ($bytes | Where-Object { $_ -gt 127 } | Select-Object -First 1) {
        $nonAsciiFiles += $file.FullName
    }
    if ($bytes | Where-Object { ($_ -lt 32) -and ($_ -notin @(9, 10, 13)) } | Select-Object -First 1) {
        $controlByteFiles += $file.FullName
    }
}

if ($nonAsciiFiles.Count -gt 0) {
    Fail "non-ASCII generated files: $($nonAsciiFiles -join ', ')"
}

if ($controlByteFiles.Count -gt 0) {
    Fail "control bytes in generated files: $($controlByteFiles -join ', ')"
}

[pscustomobject]@{
    repoRoot = $repoRoot
    interviewRoot = $interviewRoot
    rankedRows = $rankLines.Count
    javaLinks = $javaLinkCount
    missingJavaLinks = $missingJavaLinks.Count
    leetcodeLinks = $leetcodeLinkCount
    localOnlyEntries = $localOnlyCount
    patternFiles = $patternFiles.Count
    patternRows = $patternProblemRows
    nonAsciiFiles = $nonAsciiFiles.Count
    controlByteFiles = $controlByteFiles.Count
}
