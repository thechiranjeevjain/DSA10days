$ErrorActionPreference = "Stop"

$scriptRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
$reviewRoot = Split-Path -Parent $scriptRoot
$repoRoot = Split-Path -Parent $reviewRoot
$interviewRoot = Join-Path $reviewRoot "interview"
$horizontalRoot = Join-Path $reviewRoot "horizontal"

function Fail {
    param([string] $Message)
    throw "Interview cockpit validation failed: $Message"
}

$requiredFiles = @(
    "README.md",
    "00_DSA_MIND_MAP.md",
    "00_PATTERN_RECOGNITION_80_20.md",
    "01_ZERO_TO_HERO_RANKED_TABLE.md",
    "02_ONE_LINE_RECALL_ALL_PROBLEMS.md",
    "03_CRISP_INTERVIEW_ANSWERS.md",
    "04_TWO_DAY_AND_SEVEN_DAY_PLANS.md",
    "05_RANKING_METHODOLOGY_AND_AUDIT.md",
    "06_REVIEW_DASHBOARD.md",
    "07_LEETCODE_SOLVED_INDEX.md",
    "08_PROJECT_STRUCTURE_AND_PATTERN_TREE.md",
    "09_LEETCODE_CURRICULUM_TOC.md",
    "10_AFTER_7_DAY_EXTENSION_PLAN.md",
    "11_ACTIVE_90_PLAN_CUTOFF_AND_EXTENSION.md",
    "DSA_7-Day_Interview_Performance_Sprint.md",
    "DSA_170_Brain_Map_FINAL.md"
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
$rankedByRank = @{}
foreach ($line in $rankLines) {
    $cells = $line.Trim("|").Split("|") | ForEach-Object { $_.Trim() }
    if ($cells.Count -ge 6 -and $cells[0] -match '^\d+$') {
        $rankedByRank[[int] $cells[0]] = [pscustomobject]@{
            Rank = [int] $cells[0]
            Title = $cells[1].Replace("\|", "|")
            Recall = $cells[4].Replace("\|", "|")
            Hook = $cells[5].Replace("\|", "|")
        }
    }
}

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

function Get-ExcludedSlugsForFile {
    param([string] $RelativeFile)

    $fileKey = $RelativeFile.Replace("\", "/").ToLowerInvariant()
    switch ($fileKey) {
        "design/lld/designurlshortner.java" { return @("two-sum") }
        default { return @() }
    }
}

function Get-LeetCodeIdCatalog {
    $catalogPath = Join-Path $repoRoot "dsa-review/notes/LEETCODE_ID_CATALOG.csv"
    if (-not (Test-Path -LiteralPath $catalogPath)) {
        Fail "missing LeetCode ID catalog: $catalogPath"
    }

    $catalog = @{}
    foreach ($row in (Import-Csv -LiteralPath $catalogPath)) {
        if ([string]::IsNullOrWhiteSpace($row.id) -or [string]::IsNullOrWhiteSpace($row.slug)) {
            continue
        }
        $catalog[[string] $row.id] = $row.slug.Trim().ToLowerInvariant()
    }
    return $catalog
}

function Get-RecursiveLeetCodeSlugs {
    $javaRoot = Join-Path $repoRoot "src/main/java/org/chijai"
    $catalog = Get-LeetCodeIdCatalog
    $slugs = New-Object System.Collections.Generic.HashSet[string]
    foreach ($file in (Get-ChildItem -LiteralPath $javaRoot -Recurse -File -Filter "*.java")) {
        $relativeFile = $file.FullName.Substring($javaRoot.Length).TrimStart("\", "/").Replace("\", "/")
        $excluded = @(Get-ExcludedSlugsForFile -RelativeFile $relativeFile)
        $text = Get-Content -LiteralPath $file.FullName -Raw
        foreach ($match in [regex]::Matches($text, "leetcode\.com/problems/([A-Za-z0-9-]+)(/[^\s\)]*)?")) {
            if ($match.Groups[2].Value -match '^/discuss\b') {
                continue
            }
            $slug = $match.Groups[1].Value.Trim().ToLowerInvariant()
            if ($slug -and $slug -notin $excluded) {
                [void] $slugs.Add($slug)
            }
        }

        foreach ($match in [regex]::Matches($text, "(?i)\b(?:leetcode|lc)\s*(?:#)?\s*(\d{1,5})\b")) {
            $id = [string] $match.Groups[1].Value
            if (-not $catalog.ContainsKey($id)) {
                Fail "LeetCode ID $id is referenced in $($file.FullName) but missing from dsa-review/notes/LEETCODE_ID_CATALOG.csv"
            }
            $slug = $catalog[$id]
            if ($slug -and $slug -notin $excluded) {
                [void] $slugs.Add($slug)
            }
        }
    }
    return @($slugs)
}

$leetcodeIndexPath = Join-Path $interviewRoot "07_LEETCODE_SOLVED_INDEX.md"
$leetcodeIndexText = Get-Content -LiteralPath $leetcodeIndexPath -Raw
$leetcodeIndexRows = @([regex]::Matches($leetcodeIndexText, '(?m)^\| (?<index>\d+) \|'))
$leetcodeIndexSlugs = @([regex]::Matches($leetcodeIndexText, 'https://leetcode\.com/problems/([a-z0-9-]+)/') | ForEach-Object {
    $_.Groups[1].Value
})
$recursiveLeetCodeSlugs = @(Get-RecursiveLeetCodeSlugs)
if ($leetcodeIndexRows.Count -ne $recursiveLeetCodeSlugs.Count) {
    Fail "expected LeetCode solved index to contain $($recursiveLeetCodeSlugs.Count) recursive rows, found $($leetcodeIndexRows.Count)"
}

$duplicateIndexSlugs = @($leetcodeIndexSlugs | Group-Object | Where-Object { $_.Count -gt 1 } | ForEach-Object { $_.Name })
if ($duplicateIndexSlugs.Count -gt 0) {
    Fail "LeetCode solved index contains duplicate slugs: $($duplicateIndexSlugs -join ', ')"
}

$missingIndexSlugs = @($recursiveLeetCodeSlugs | Where-Object { $_ -notin $leetcodeIndexSlugs })
$extraIndexSlugs = @($leetcodeIndexSlugs | Where-Object { $_ -notin $recursiveLeetCodeSlugs })
if ($missingIndexSlugs.Count -gt 0 -or $extraIndexSlugs.Count -gt 0) {
    Fail "LeetCode solved index slug mismatch. Missing: $($missingIndexSlugs -join ', '); Extra: $($extraIndexSlugs -join ', ')"
}

if ($leetcodeIndexText -notmatch 'Recursive source scan') {
    Fail "LeetCode solved index is missing recursive scan summary"
}

$curriculumTocPath = Join-Path $interviewRoot "09_LEETCODE_CURRICULUM_TOC.md"
$curriculumTocText = Get-Content -LiteralPath $curriculumTocPath -Raw
if ($curriculumTocText -notmatch '## Curriculum Hierarchy' -or $curriculumTocText -notmatch '\*\*1\.1\.1\*\*') {
    Fail "LeetCode curriculum TOC is missing the expected nested hierarchy"
}
$curriculumProblemLines = @([regex]::Matches($curriculumTocText, '(?m)^\s{4}- \*\*\d+\.\d+\.\d+\*\* \['))
if ($curriculumProblemLines.Count -ne $leetcodeIndexRows.Count) {
    Fail "expected LeetCode curriculum TOC to contain $($leetcodeIndexRows.Count) problem rows, found $($curriculumProblemLines.Count)"
}
$curriculumSlugs = @([regex]::Matches($curriculumTocText, 'https://leetcode\.com/problems/([a-z0-9-]+)/') | ForEach-Object {
    $_.Groups[1].Value
})
$missingCurriculumSlugs = @($recursiveLeetCodeSlugs | Where-Object { $_ -notin $curriculumSlugs })
$extraCurriculumSlugs = @($curriculumSlugs | Where-Object { $_ -notin $recursiveLeetCodeSlugs })
if ($missingCurriculumSlugs.Count -gt 0 -or $extraCurriculumSlugs.Count -gt 0) {
    Fail "LeetCode curriculum TOC slug mismatch. Missing: $($missingCurriculumSlugs -join ', '); Extra: $($extraCurriculumSlugs -join ', ')"
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
if ($readmeText -notmatch '00_DSA_MIND_MAP\.md') {
    Fail "main README does not link to the generated Mermaid mind map"
}
if ($readmeText -notmatch '08_PROJECT_STRUCTURE_AND_PATTERN_TREE\.md') {
    Fail "main README does not link to the project structure guide"
}
if ($readmeText -notmatch 'DSA_170_Brain_Map_FINAL\.md') {
    Fail "main README does not link to the canonical brain map"
}
if ($readmeText -notmatch 'DSA_7-Day_Interview_Performance_Sprint\.md') {
    Fail "main README does not link to the weekly sprint"
}
if ($readmeText -notmatch '06_REVIEW_DASHBOARD\.md') {
    Fail "main README does not link to the review dashboard"
}
if ($readmeText -notmatch '07_LEETCODE_SOLVED_INDEX\.md') {
    Fail "main README does not link to the recursive LeetCode solved index"
}
if ($readmeText -notmatch '09_LEETCODE_CURRICULUM_TOC\.md') {
    Fail "main README does not link to the nested LeetCode curriculum TOC"
}
if ($readmeText -notmatch '10_AFTER_7_DAY_EXTENSION_PLAN\.md') {
    Fail "main README does not link to the post-7-day extension plan"
}
if ($readmeText -notmatch '11_ACTIVE_90_PLAN_CUTOFF_AND_EXTENSION\.md') {
    Fail "main README does not link to the active 90-problem cutoff plan"
}

$extensionPath = Join-Path $interviewRoot "10_AFTER_7_DAY_EXTENSION_PLAN.md"
$extensionText = Get-Content -LiteralPath $extensionPath -Raw
foreach ($requiredExtensionPhrase in @("## Cutoff Rule", "## Day 8", "## Day 12", "source-only", "## Recircling Rule")) {
    if ($extensionText -notmatch [regex]::Escape($requiredExtensionPhrase)) {
        Fail "post-7-day extension plan is missing: $requiredExtensionPhrase"
    }
}
$extensionRankRows = @([regex]::Matches($extensionText, '(?m)^\| (?<rank>\d{3}) \|'))
$expectedExtensionRows = [Math]::Max(0, $rankLines.Count - 150)
if ($extensionRankRows.Count -ne $expectedExtensionRows) {
    Fail "expected extension plan to contain $expectedExtensionRows ranked tail rows, found $($extensionRankRows.Count)"
}

$activeNinetyPath = Join-Path $interviewRoot "11_ACTIVE_90_PLAN_CUTOFF_AND_EXTENSION.md"
$activeNinetyText = Get-Content -LiteralPath $activeNinetyPath -Raw
foreach ($requiredNinetyPhrase in @("## Main Decision", "## Cutoff Numbers", "## After The 90 Problems", "Do not edit that file while it is in use", "ranks 91-150", "ranks 151+")) {
    if ($activeNinetyText -notmatch [regex]::Escape($requiredNinetyPhrase)) {
        Fail "active 90-problem cutoff plan is missing: $requiredNinetyPhrase"
    }
}

$mindMapText = Get-Content -LiteralPath (Join-Path $interviewRoot "00_DSA_MIND_MAP.md") -Raw
if ($mindMapText -notmatch '```mermaid' -or $mindMapText -notmatch 'DSA Interview Retrieval Tree') {
    Fail "master mind map is missing Mermaid retrieval tree"
}
if ($mindMapText -notmatch 'patterns/') {
    Fail "master mind map does not link to generated pattern files"
}

$structureText = Get-Content -LiteralPath (Join-Path $interviewRoot "08_PROJECT_STRUCTURE_AND_PATTERN_TREE.md") -Raw
if ($structureText -notmatch 'Do not physically move Java files' -or $structureText -notmatch 'PROBLEM -> BASELINE -> RECOGNITION -> INVARIANT -> TRAPS -> FALLBACK -> OPTIMAL -> DEFEND') {
    Fail "project structure guide is missing source-of-truth or chapter-pattern guidance"
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
$patternByRank = @{}
$missingPatternJavaLinks = @()
foreach ($patternFile in $patternFiles) {
    $patternText = Get-Content -LiteralPath $patternFile.FullName -Raw
    if ($patternText -notmatch '## Pattern Taxonomy Map' -or $patternText -notmatch '```mermaid') {
        Fail "pattern file is missing generated Mermaid taxonomy map: $($patternFile.FullName)"
    }

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

        $cells = $line.Line.Trim("|").Split("|") | ForEach-Object { $_.Trim() }
        if ($cells.Count -ge 8) {
            $patternByRank[$rank] = [pscustomobject]@{
                Rank = $rank
                Phase = $cells[1]
                Title = $cells[2].Replace("\|", "|")
                Pattern = $cells[3].Replace("\|", "|")
                Recall = $cells[6].Replace("\|", "|")
            }
        }
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

$sprintPath = Join-Path $interviewRoot "DSA_7-Day_Interview_Performance_Sprint.md"
$sprintText = Get-Content -LiteralPath $sprintPath -Raw
foreach ($requiredColumn in @("Signal / Invariant", "Score", "Failure", "Attempts", "Last Review", "Next Review")) {
    if ($sprintText -notmatch [regex]::Escape($requiredColumn)) {
        Fail "weekly sprint is missing review column: $requiredColumn"
    }
}

$sprintRows = @()
foreach ($line in Get-Content -LiteralPath $sprintPath) {
    if ($line -notmatch '^\|') { continue }
    $cells = $line.Trim("|").Split("|") | ForEach-Object { $_.Trim() }
    if ($cells.Count -lt 13) { continue }
    if ($cells[1] -notmatch '^\d+$' -or $cells[2] -notmatch '^\d+$') { continue }
    $sprintRows += [pscustomobject]@{
        SprintRank = [int] $cells[1]
        SourceRank = [int] $cells[2]
        Problem = $cells[3].Replace("\|", "|")
        Family = $cells[5].Replace("\|", "|")
        Pattern = $cells[6].Replace("\|", "|")
        Recall = $cells[7].Replace("\|", "|")
    }
}

if ($sprintRows.Count -ne 150) {
    Fail "expected 150 weekly sprint rows, found $($sprintRows.Count)"
}

$sprintRankSet = @{}
$sprintSourceRankSet = @{}
foreach ($row in $sprintRows) {
    if ($sprintRankSet.ContainsKey($row.SprintRank)) {
        Fail "duplicate sprint rank $($row.SprintRank)"
    }
    $sprintRankSet[$row.SprintRank] = $true

    if ($sprintSourceRankSet.ContainsKey($row.SourceRank)) {
        Fail "duplicate sprint source rank $($row.SourceRank)"
    }
    $sprintSourceRankSet[$row.SourceRank] = $true

    if (-not $rankedByRank.ContainsKey($row.SourceRank)) {
        Fail "weekly sprint source rank not found in ranked table: $($row.SourceRank)"
    }
    if (-not $patternByRank.ContainsKey($row.SourceRank)) {
        Fail "weekly sprint source rank not found in pattern files: $($row.SourceRank)"
    }

    $rankedRow = $rankedByRank[$row.SourceRank]
    $patternRow = $patternByRank[$row.SourceRank]
    if ($row.Problem -ne $rankedRow.Title) {
        Fail "weekly sprint title mismatch for source rank $($row.SourceRank): '$($row.Problem)' vs '$($rankedRow.Title)'"
    }
    if ($row.Pattern -ne $patternRow.Pattern) {
        Fail "weekly sprint pattern mismatch for source rank $($row.SourceRank): '$($row.Pattern)' vs '$($patternRow.Pattern)'"
    }
    if ($row.Recall -ne $rankedRow.Recall) {
        Fail "weekly sprint signal mismatch for source rank $($row.SourceRank)"
    }
}

for ($i = 1; $i -le 150; $i++) {
    if (-not $sprintRankSet.ContainsKey($i)) {
        Fail "weekly sprint is missing sprint rank $i"
    }
    if (-not $sprintSourceRankSet.ContainsKey($i)) {
        Fail "weekly sprint is missing source rank $i"
    }
}

$dashboardPath = Join-Path $interviewRoot "06_REVIEW_DASHBOARD.md"
$dashboardText = Get-Content -LiteralPath $dashboardPath -Raw
foreach ($requiredSection in @("## Due Today", "## RED Repair Queue", "## YELLOW Stabilization Queue", "## Mastered Queue", "## Untracked Ranked Rows", "## Repeated Failure Pattern Heatmap", "## Master Review Ledger")) {
    if ($dashboardText -notmatch [regex]::Escape($requiredSection)) {
        Fail "review dashboard is missing section: $requiredSection"
    }
}

$ledgerMatch = [regex]::Match($dashboardText, '(?s)## Master Review Ledger\s+(.+)$')
if (-not $ledgerMatch.Success) {
    Fail "review dashboard ledger section could not be parsed"
}
$dashboardRows = @([regex]::Matches($ledgerMatch.Groups[1].Value, '(?m)^\| (?<rank>\d+) \|'))
if ($dashboardRows.Count -ne $rankLines.Count) {
    Fail "expected review dashboard ledger to contain $($rankLines.Count) rows, found $($dashboardRows.Count)"
}

if ($dashboardText -notmatch 'Review-state matches') {
    Fail "review dashboard is missing dynamic review-state summary"
}

$requiredHorizontalFiles = @(
    "README.md",
    "00_MASTER_MATRIX.md",
    "01_CROSSDRILL_PROTOCOL.md",
    "02_MUTATION_SWITCHBOARD.md",
    "03_ARRAY_HASH_POINTERS.md",
    "04_SLIDING_WINDOW.md",
    "05_BINARY_SEARCH.md",
    "06_LINKED_LIST.md",
    "07_TREE_DFS_BFS.md",
    "08_GRAPH_DFS_BFS.md",
    "09_TOPO_UNION_FIND.md",
    "10_STACK_HEAP.md",
    "11_INTERVALS_GREEDY.md",
    "12_DYNAMIC_PROGRAMMING.md",
    "13_BACKTRACKING_TRIE.md",
    "14_MATH_BIT_STRING.md",
    "15_DESIGN_DATA_STRUCTURES.md"
)

foreach ($name in $requiredHorizontalFiles) {
    $path = Join-Path $horizontalRoot $name
    if (-not (Test-Path -LiteralPath $path)) {
        Fail "missing horizontal mastery file: $path"
    }
}

$horizontalMdFiles = @(Get-ChildItem -LiteralPath $horizontalRoot -File -Filter "*.md")
if ($horizontalMdFiles.Count -gt 21) {
    Fail "horizontal mastery should stay under 21 human-facing Markdown files, found $($horizontalMdFiles.Count)"
}

$horizontalReadmeText = Get-Content -LiteralPath (Join-Path $horizontalRoot "README.md") -Raw
foreach ($requiredHorizontalPhrase in @("DSA Horizontal Mastery", "CROSSDRILL", "under 21 human-facing Markdown files", "Why this pattern")) {
    if ($horizontalReadmeText -notmatch [regex]::Escape($requiredHorizontalPhrase)) {
        Fail "horizontal README is missing: $requiredHorizontalPhrase"
    }
}

$crossdrillText = Get-Content -LiteralPath (Join-Path $horizontalRoot "01_CROSSDRILL_PROTOCOL.md") -Raw
foreach ($requiredCrossdrillPhrase in @("Problem -> Patterns -> Mutation", "WHY NOT NOW?", "WHAT IS MISSING?", "MINIMAL CHANGE", "NOW WHY DOES IT WORK?")) {
    if ($crossdrillText -notmatch [regex]::Escape($requiredCrossdrillPhrase)) {
        Fail "CROSSDRILL protocol is missing: $requiredCrossdrillPhrase"
    }
}

$switchboardText = Get-Content -LiteralPath (Join-Path $horizontalRoot "02_MUTATION_SWITCHBOARD.md") -Raw
foreach ($requiredSwitchboardPhrase in @("Why not now", "What is missing", "Minimal change", "Now why it works")) {
    if ($switchboardText -notmatch [regex]::Escape($requiredSwitchboardPhrase)) {
        Fail "mutation switchboard is missing column/phrase: $requiredSwitchboardPhrase"
    }
}

$horizontalMatrixPath = Join-Path $horizontalRoot "00_MASTER_MATRIX.md"
$horizontalMatrixText = Get-Content -LiteralPath $horizontalMatrixPath -Raw
$horizontalRows = @(Select-String -LiteralPath $horizontalMatrixPath -Pattern '^\| (?<rank>\d+) \|' | ForEach-Object { $_.Line })
if ($horizontalRows.Count -ne $rankLines.Count) {
    Fail "expected horizontal matrix to contain $($rankLines.Count) rows, found $($horizontalRows.Count)"
}
if ($horizontalMatrixText -notmatch 'Near-miss switches' -or $horizontalMatrixText -notmatch 'Wrong-pattern guard') {
    Fail "horizontal master matrix is missing discrimination columns"
}

$horizontalRankSet = @{}
$horizontalByTitle = @{}
foreach ($line in $horizontalRows) {
    $cells = $line.Trim("|").Split("|") | ForEach-Object { $_.Trim() }
    if ($cells.Count -lt 8 -or $cells[0] -notmatch '^\d+$') {
        Fail "horizontal matrix row has unexpected shape: $line"
    }
    $rank = [int] $cells[0]
    if ($horizontalRankSet.ContainsKey($rank)) {
        Fail "duplicate rank $rank in horizontal matrix"
    }
    $horizontalRankSet[$rank] = $true
    if (-not $rankedByRank.ContainsKey($rank)) {
        Fail "horizontal matrix rank not found in ranked table: $rank"
    }
    if ($cells[1].Replace("\|", "|") -ne $rankedByRank[$rank].Title) {
        Fail "horizontal matrix title mismatch for rank $rank"
    }
    $horizontalByTitle[$cells[1].Replace("\|", "|")] = [pscustomobject]@{
        Rank = $rank
        Problem = $cells[1].Replace("\|", "|")
        Winner = $cells[2].Replace("\|", "|")
        WhyWinner = $cells[3].Replace("\|", "|")
        Switches = $cells[4].Replace("\|", "|")
        Guard = $cells[5].Replace("\|", "|")
        Java = $cells[6]
        LeetCode = $cells[7]
    }
}
for ($i = 1; $i -le $rankLines.Count; $i++) {
    if (-not $horizontalRankSet.ContainsKey($i)) {
        Fail "horizontal matrix missing rank $i"
    }
}

$horizontalJavaMatches = @(Select-String -LiteralPath $horizontalMatrixPath -Pattern '\[Java\]\(([^)]+)\)' -AllMatches)
$missingHorizontalJavaLinks = @()
foreach ($line in $horizontalJavaMatches) {
    foreach ($match in $line.Matches) {
        $target = $match.Groups[1].Value
        $fullPath = [System.IO.Path]::GetFullPath((Join-Path (Split-Path -Parent $horizontalMatrixPath) $target))
        if (-not (Test-Path -LiteralPath $fullPath)) {
            $missingHorizontalJavaLinks += $target
        }
    }
}
if ($horizontalJavaMatches.Matches.Count -ne $rankLines.Count) {
    Fail "expected one Java link per horizontal matrix row, found $($horizontalJavaMatches.Matches.Count)"
}
if ($missingHorizontalJavaLinks.Count -gt 0) {
    Fail "missing horizontal Java links: $($missingHorizontalJavaLinks -join ', ')"
}

$expectedHorizontalSemantics = @(
    @{ Title = "Merge K Sorted Lists"; Winner = "Heap / Priority Queue"; MustContain = "minimum among k heads"; MustNotContain = "node repeats/intersects" },
    @{ Title = "Meeting Rooms II"; Winner = "Heap / Priority Queue"; MustContain = "active end"; MustNotContain = "Do not heap this" },
    @{ Title = "Meeting Rooms"; Winner = "Intervals / Sorting Greedy"; MustContain = "output asks for room count"; MustNotContain = "Heap / Priority Queue" },
    @{ Title = "First Unique Number"; Winner = "Design Data Structures"; MustContain = "first unique in arrival order"; MustNotContain = "node repeats/intersects" },
    @{ Title = "Moving Average From Data Stream"; Winner = "Sliding Window"; MustContain = "fixed-window sum"; MustNotContain = "node repeats/intersects" },
    @{ Title = "Design Circular Queue"; Winner = "Design Data Structures"; MustContain = "fixed-capacity FIFO"; MustNotContain = "most-recent unresolved" },
    @{ Title = "Maximum XOR With an Element From Array"; Winner = "Trie"; MustContain = "nums <= mi"; MustNotContain = "word-prefix" },
    @{ Title = "Maximum Genetic Difference Query"; Winner = "Trie"; MustContain = "current ancestors"; MustNotContain = "word-prefix" },
    @{ Title = "Count Pairs With XOR in a Range"; Winner = "Trie"; MustContain = "less-than counts by bit"; MustNotContain = "word-prefix" },
    @{ Title = "Path Sum III"; Winner = "Tree DFS / Recursion"; MustContain = "prefix counts on the current root path"; MustNotContain = "minimum-level answers" }
)

foreach ($expectation in $expectedHorizontalSemantics) {
    if (-not $horizontalByTitle.ContainsKey($expectation.Title)) {
        Fail "horizontal semantic audit missing title: $($expectation.Title)"
    }
    $row = $horizontalByTitle[$expectation.Title]
    $combined = "$($row.Winner) $($row.WhyWinner) $($row.Switches) $($row.Guard)"
    if ($row.Winner -ne $expectation.Winner) {
        Fail "horizontal semantic audit winner mismatch for $($expectation.Title): expected '$($expectation.Winner)', found '$($row.Winner)'"
    }
    if ($combined -notmatch [regex]::Escape($expectation.MustContain)) {
        Fail "horizontal semantic audit missing expected phrase for $($expectation.Title): $($expectation.MustContain)"
    }
    if ($combined -match [regex]::Escape($expectation.MustNotContain)) {
        Fail "horizontal semantic audit still contains bad phrase for $($expectation.Title): $($expectation.MustNotContain)"
    }
}

$horizontalFamilyFiles = @($horizontalMdFiles | Where-Object { $_.Name -match '^\d{2}_' -and $_.Name -notin @("00_MASTER_MATRIX.md", "01_CROSSDRILL_PROTOCOL.md", "02_MUTATION_SWITCHBOARD.md") })
$horizontalFamilyRows = 0
foreach ($file in $horizontalFamilyFiles) {
    $text = Get-Content -LiteralPath $file.FullName -Raw
    if ($text -notmatch '```mermaid' -or $text -notmatch '## Problems' -or $text -notmatch 'Near-miss mutation') {
        Fail "horizontal family file is missing required sections: $($file.FullName)"
    }
    $horizontalFamilyRows += @(Select-String -LiteralPath $file.FullName -Pattern '^\| (?<rank>\d+) \|').Count
}
if ($horizontalFamilyRows -ne $rankLines.Count) {
    Fail "expected horizontal family files to contain $($rankLines.Count) problem rows, found $horizontalFamilyRows"
}

$generatedAsciiFiles = @(
    $requiredFiles |
        Where-Object { $_ -ne "DSA_170_Brain_Map_FINAL.md" } |
        ForEach-Object { Get-Item -LiteralPath (Join-Path $interviewRoot $_) }
) + $patternFiles + $horizontalMdFiles
$allInterviewFiles = @(Get-ChildItem -LiteralPath $interviewRoot -Recurse -File)
$allHorizontalFiles = @(Get-ChildItem -LiteralPath $horizontalRoot -Recurse -File)
$nonAsciiFiles = @()
$controlByteFiles = @()
foreach ($file in $generatedAsciiFiles) {
    $bytes = [System.IO.File]::ReadAllBytes($file.FullName)
    $hasNonAscii = $false
    foreach ($byte in $bytes) {
        if ($byte -gt 127) {
            $hasNonAscii = $true
            break
        }
    }
    if ($hasNonAscii) {
        $nonAsciiFiles += $file.FullName
    }
}

foreach ($file in ($allInterviewFiles + $allHorizontalFiles)) {
    $bytes = [System.IO.File]::ReadAllBytes($file.FullName)
    $hasControlByte = $false
    foreach ($byte in $bytes) {
        if (($byte -lt 32) -and ($byte -notin @(9, 10, 13))) {
            $hasControlByte = $true
            break
        }
    }
    if ($hasControlByte) {
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
    recursiveLeetCodeIndex = $leetcodeIndexRows.Count
    localOnlyEntries = $localOnlyCount
    patternFiles = $patternFiles.Count
    patternRows = $patternProblemRows
    horizontalFiles = $horizontalMdFiles.Count
    horizontalRows = $horizontalRows.Count
    nonAsciiFiles = $nonAsciiFiles.Count
    controlByteFiles = $controlByteFiles.Count
}
