[CmdletBinding()]
param(
    [Parameter(Mandatory = $true, Position = 0)]
    [string] $Problem
)

Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

$scriptRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
$repoRoot = (Resolve-Path (Join-Path $scriptRoot "../..")).Path
$matrixPath = Join-Path $repoRoot "dsa-review/horizontal/00_MASTER_MATRIX.md"
$crispPath = Join-Path $repoRoot "dsa-review/interview/03_CRISP_INTERVIEW_ANSWERS.md"

if (-not (Test-Path -LiteralPath $matrixPath)) {
    throw "Horizontal matrix not found. Run dsa-review/scripts/build-interview-cockpit.ps1 first."
}

function Strip-MarkdownLink {
    param([string] $Value)
    return ($Value -replace '\[([^\]]+)\]\([^)]+\)', '$1').Trim()
}

function Split-MarkdownRow {
    param([string] $Line)
    return @($Line.Trim("|").Split("|") | ForEach-Object { $_.Trim().Replace("\|", "|") })
}

function Get-CrispAnswer {
    param(
        [string] $Path,
        [string] $Problem
    )

    if (-not (Test-Path -LiteralPath $Path)) {
        return $null
    }

    $text = Get-Content -LiteralPath $Path -Raw
    $escapedProblem = [regex]::Escape($Problem)
    $match = [regex]::Match($text, "(?ms)^###\s+\d+\.\s+$escapedProblem\s*(?<body>.*?)(?=^###\s+\d+\.|\z)")
    if (-not $match.Success) {
        return $null
    }

    $body = $match.Groups["body"].Value
    $result = @{
        BruteForce = ""
        Bottleneck = ""
        Pattern = ""
        Invariant = ""
        CodeIdea = ""
    }

    foreach ($line in ($body -split "`r?`n")) {
        if ($line -match '^- Brute force:\s*(.+)$') { $result.BruteForce = $Matches[1].Trim() }
        elseif ($line -match '^- Bottleneck:\s*(.+)$') { $result.Bottleneck = $Matches[1].Trim() }
        elseif ($line -match '^- Pattern:\s*(.+)$') { $result.Pattern = $Matches[1].Trim() }
        elseif ($line -match '^- Invariant/state:\s*(.+)$') { $result.Invariant = $Matches[1].Trim() }
        elseif ($line -match '^- Code idea:\s*(.+)$') { $result.CodeIdea = $Matches[1].Trim() }
    }

    return [pscustomobject]$result
}

$rows = New-Object System.Collections.Generic.List[object]
foreach ($line in Get-Content -LiteralPath $matrixPath) {
    if ($line -notmatch '^\| \d+ \|') {
        continue
    }

    $cells = Split-MarkdownRow -Line $line
    if ($cells.Count -lt 8) {
        continue
    }

    $rows.Add([pscustomobject]@{
        Rank = [int] $cells[0]
        Problem = Strip-MarkdownLink $cells[1]
        Winner = $cells[2]
        WhyWinner = $cells[3]
        Switches = $cells[4]
        Guard = $cells[5]
        Java = $cells[6]
        LeetCode = $cells[7]
    })
}

$query = $Problem.Trim().ToLowerInvariant()
$matches = @($rows | Where-Object {
    $_.Problem.ToLowerInvariant() -eq $query
})

if ($matches.Count -eq 0) {
    $matches = @($rows | Where-Object {
        $_.Problem.ToLowerInvariant().Contains($query)
    } | Select-Object -First 20)
}

if ($matches.Count -eq 0) {
    Write-Host "No problem matched: $Problem"
    Write-Host ""
    Write-Host "Try a title from dsa-review/horizontal/00_MASTER_MATRIX.md"
    exit 2
}

if ($matches.Count -gt 1) {
    Write-Host "Multiple matches. Re-run with the exact title:"
    Write-Host ""
    foreach ($match in $matches) {
        Write-Host ("{0}. {1} [{2}]" -f $match.Rank, $match.Problem, $match.Winner)
    }
    exit 1
}

$row = $matches[0]
$crisp = Get-CrispAnswer -Path $crispPath -Problem $row.Problem

Write-Host ("# CROSSDRILL - {0}" -f $row.Problem)
Write-Host ""
Write-Host ("Rank: {0}" -f $row.Rank)
Write-Host ("Winner pattern: {0}" -f $row.Winner)
Write-Host ""
Write-Host "## 1. Problem Loop"
Write-Host ""
if ($null -ne $crisp) {
    Write-Host ("- Brute force: {0}" -f $crisp.BruteForce)
    Write-Host ("- Bottleneck/workload: {0}" -f $crisp.Bottleneck)
    Write-Host ("- Recognition signal: {0}" -f $crisp.Invariant)
} else {
    Write-Host "- Restate the required output in your own words."
    Write-Host "- Name the input structure: array/string/list/tree/graph/grid/design object."
    Write-Host "- Name the workload pressure: repeated lookup, contiguous region, shortest path, dependency order, repeated state, priority, or mutation."
}
Write-Host ""
Write-Host "## 2. Pattern Loop"
Write-Host ""
Write-Host ("- Winner: {0}" -f $row.Winner)
Write-Host ("- Why winner: {0}" -f $row.WhyWinner)
if ($null -ne $crisp -and -not [string]::IsNullOrWhiteSpace($crisp.CodeIdea)) {
    Write-Host ("- Code invariant: {0}" -f $crisp.CodeIdea)
}
Write-Host ("- Tempting wrong pattern guard: {0}" -f $row.Guard)
Write-Host "- Obviously irrelevant families: aggregate them; do not enumerate every pattern if the input/output has no matching signal."
Write-Host ""
Write-Host "## 3. Mutation Loop"
Write-Host ""
Write-Host "Speak this sequence for each near-miss:"
Write-Host ""
Write-Host "WHY NOT NOW? -> WHAT IS MISSING? -> MINIMAL CHANGE -> NOW WHY DOES IT WORK?"
Write-Host ""
$nearMisses = @($row.Switches -split '<br>' | Where-Object { -not [string]::IsNullOrWhiteSpace($_) })
if ($nearMisses.Count -eq 0) {
    Write-Host "- No strong near-miss switches captured."
} else {
    foreach ($nearMiss in $nearMisses) {
        Write-Host ("- {0}" -f $nearMiss.Trim())
    }
}
Write-Host ""
Write-Host "## Close"
Write-Host ""
Write-Host "brute force -> bottleneck -> pattern -> invariant -> code -> dry run"
Write-Host ""
Write-Host ("Java: {0}" -f $row.Java)
Write-Host ("LeetCode: {0}" -f $row.LeetCode)
