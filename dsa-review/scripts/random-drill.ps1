[CmdletBinding()]
param(
    [ValidateSet('A', 'B', 'C', 'All')]
    [string]$Priority = 'A',

    [ValidateRange(1, 20)]
    [int]$Count = 1,

    [int]$Seed,

    [switch]$IncludeDesign
)

Set-StrictMode -Version Latest
$ErrorActionPreference = 'Stop'

function Get-RepoRoot {
    $scriptDir = Split-Path -Parent $PSCommandPath
    return (Resolve-Path (Join-Path $scriptDir '..\..')).Path
}

function Get-PatternRows {
    param(
        [string]$RepoRoot
    )

    $indexPath = Join-Path $RepoRoot 'dsa-review\notes\PROBLEM_PATTERN_INDEX.md'
    $rows = New-Object System.Collections.Generic.List[object]

    if (Test-Path -LiteralPath $indexPath) {
        $pattern = '^\|\s*`([^`]+\.java)`\s*\|\s*([^|]+?)\s*\|\s*([ABC])\s*\|'
        foreach ($line in Get-Content -LiteralPath $indexPath) {
            $match = [regex]::Match($line, $pattern)
            if ($match.Success) {
                $relativeFile = $match.Groups[1].Value.Trim()
                $normalizedRelativeFile = $relativeFile.Replace('/', '\')
                $sourcePath = Join-Path $RepoRoot ('src\main\java\org\chijai\' + $normalizedRelativeFile)
                $rows.Add([pscustomobject]@{
                    File = $relativeFile
                    Pattern = $match.Groups[2].Value.Trim()
                    Priority = $match.Groups[3].Value.Trim()
                    SourcePath = $sourcePath
                    Exists = Test-Path -LiteralPath $sourcePath
                })
            }
        }
    }

    if ($rows.Count -eq 0) {
        $sourceRoot = Join-Path $RepoRoot 'src\main\java\org\chijai'
        if (-not (Test-Path -LiteralPath $sourceRoot)) {
            throw "Could not find source root: $sourceRoot"
        }

        Get-ChildItem -LiteralPath $sourceRoot -Recurse -File -Filter '*.java' |
            Where-Object { $_.Name -notin @('Main.java', 'CheatSheet.java') } |
            ForEach-Object {
                $relativeFile = $_.FullName.Substring($sourceRoot.Length + 1)
                $rows.Add([pscustomobject]@{
                    File = $relativeFile
                    Pattern = Get-PatternGuess -RelativeFile $relativeFile
                    Priority = 'B'
                    SourcePath = $_.FullName
                    Exists = $true
                })
            }
    }

    return $rows
}

function Get-PatternGuess {
    param(
        [string]$RelativeFile
    )

    $path = $RelativeFile.ToLowerInvariant()

    if ($path -match 'sliding|substring|anagram|window|atmost|minimumwindow') { return 'Sliding window / frequency' }
    if ($path -match 'binarysearch|searchrange|koko|aggrcow') { return 'Binary search' }
    if ($path -match 'linkedlist|cycle|merge2|mergek|middle|reverse') { return 'Linked list pointers' }
    if ($path -match 'tree|bst|lca') { return 'Tree DFS/BFS' }
    if ($path -match 'graph|island|ladder|course|matrix01|oranges|flood') { return 'Graph BFS/DFS' }
    if ($path -match 'heap|kclosest|median|topk|kth') { return 'Heap / top K' }
    if ($path -match 'stack|parentheses|calculator|rpn|temperatures|rectangle|rainwater') { return 'Stack / monotonic stack' }
    if ($path -match 'dp|coin|robber|paths|lis|edit|partition') { return 'Dynamic programming' }
    if ($path -match 'trie|worddictionary|xor') { return 'Trie' }
    if ($path -match 'backtracking|combination|permutation|subsets') { return 'Backtracking' }
    if ($path -match 'interval|meeting|platform') { return 'Intervals / greedy' }
    if ($path -match 'twosum|threesum|palindrome|container') { return 'Two pointers' }

    return 'Identify pattern from problem statement'
}

function Get-VariantPrompt {
    param(
        [string]$Pattern
    )

    $p = $Pattern.ToLowerInvariant()

    if ($p -match 'sliding|window') { return 'Variant: change at most K to exactly K, or return the actual substring instead of length.' }
    if ($p -match 'binary search') { return 'Variant: convert exact search to first true / minimum feasible answer.' }
    if ($p -match 'linked') { return 'Variant: handle head change, single node, empty list, and group-size boundary.' }
    if ($p -match 'tree') { return 'Variant: solve recursive and iterative, then explain the helper return contract.' }
    if ($p -match 'graph|bfs|dfs') { return 'Variant: switch single-source to multi-source, or return distance/path instead of boolean.' }
    if ($p -match 'heap|top k|kth') { return 'Variant: handle streaming input or keep only K candidates.' }
    if ($p -match 'stack') { return 'Variant: ask for previous smaller/greater instead of next smaller/greater.' }
    if ($p -match 'dynamic|dp|knapsack') { return 'Variant: change existence to count ways, or minimum count to boolean possible.' }
    if ($p -match 'trie') { return 'Variant: add wildcard search or return all words with prefix.' }
    if ($p -match 'backtracking') { return 'Variant: add duplicates or change reuse allowed vs use once.' }
    if ($p -match 'interval|greedy') { return 'Variant: change merge overlaps to minimum removals or minimum rooms.' }
    if ($p -match 'two pointers') { return 'Variant: add duplicates, return all unique pairs/triplets, or preserve original indices.' }
    if ($p -match 'hash|frequency') { return 'Variant: return indices, counts, grouped results, or handle streaming updates.' }

    return 'Variant: change the return type or one constraint, then re-derive the invariant.'
}

function Get-EdgeCases {
    param(
        [string]$Pattern
    )

    $p = $Pattern.ToLowerInvariant()

    if ($p -match 'linked') { return 'Edge cases: empty list, one node, two nodes, cycle/no cycle, head changes.' }
    if ($p -match 'tree') { return 'Edge cases: null root, one node, skewed tree, duplicate-like values, missing child.' }
    if ($p -match 'graph|bfs|dfs') { return 'Edge cases: disconnected graph, unreachable target, cycle, repeated state, empty grid.' }
    if ($p -match 'sliding|window') { return 'Edge cases: empty string, k=0, all same chars, all unique chars, no valid window.' }
    if ($p -match 'binary search') { return 'Edge cases: one element, target absent, answer at left/right boundary, duplicates if allowed.' }
    if ($p -match 'dp|dynamic') { return 'Edge cases: zero target, impossible target, first row/column/base state, large values.' }
    if ($p -match 'heap') { return 'Edge cases: k=1, k=n, ties, empty stream, comparator ordering.' }
    if ($p -match 'stack') { return 'Edge cases: empty stack, duplicates, unresolved tail, invalid expression/brackets.' }

    return 'Edge cases: empty input, one element, duplicates, no answer, answer at boundary, overflow risk.'
}

function Select-RandomRows {
    param(
        [object[]]$Rows,
        [int]$Count,
        [System.Random]$Random
    )

    $pool = New-Object System.Collections.Generic.List[object]
    foreach ($row in $Rows) {
        $pool.Add($row)
    }

    $selected = New-Object System.Collections.Generic.List[object]
    $take = [Math]::Min($Count, $pool.Count)

    for ($i = 0; $i -lt $take; $i++) {
        $index = $Random.Next(0, $pool.Count)
        $selected.Add($pool[$index])
        $pool.RemoveAt($index)
    }

    return $selected
}

$repoRoot = Get-RepoRoot
$rows = @(Get-PatternRows -RepoRoot $repoRoot)

if (-not $IncludeDesign) {
    $rows = @($rows | Where-Object { $_.File -notmatch '^design[\\/]' })
}

if ($Priority -ne 'All') {
    $rows = @($rows | Where-Object { $_.Priority -eq $Priority })
}

$rows = @($rows | Where-Object { $_.File -notin @('Main.java', 'CheatSheet.java') })

if ($rows.Count -eq 0) {
    throw "No drill rows found for priority '$Priority'."
}

if ($PSBoundParameters.ContainsKey('Seed')) {
    $random = [System.Random]::new($Seed)
} else {
    $random = [System.Random]::new()
}

$selected = @(Select-RandomRows -Rows $rows -Count $Count -Random $random)

Write-Host ""
Write-Host "DSA Random Drill"
Write-Host "Repo: $repoRoot"
Write-Host "Priority: $Priority"
Write-Host "Count: $($selected.Count)"
if ($PSBoundParameters.ContainsKey('Seed')) {
    Write-Host "Seed: $Seed"
}
Write-Host ""
Write-Host "Rule: do not open the source file until after your blank attempt."
Write-Host ""

$number = 1
foreach ($item in $selected) {
    $sourceStatus = if ($item.Exists) { 'found' } else { 'missing' }
    Write-Host ("[{0}] {1}" -f $number, $item.File)
    Write-Host ("    Priority : {0}" -f $item.Priority)
    Write-Host ("    Pattern  : {0}" -f $item.Pattern)
    Write-Host ("    Source   : {0} ({1})" -f $item.SourcePath, $sourceStatus)
    Write-Host "    Timer    : 5 min recall, 25 min blank implementation, 5 min dry run"
    Write-Host "    Recall   : problem, brute force, bottleneck, invariant, complexity"
    Write-Host ("    Edge     : {0}" -f (Get-EdgeCases -Pattern $item.Pattern))
    Write-Host ("    Variant  : {0}" -f (Get-VariantPrompt -Pattern $item.Pattern))
    Write-Host ""
    $number++
}

Write-Host "After attempt: open the Java chapter, compare, and update PROBLEM_PATTERN_INDEX.md grades."
