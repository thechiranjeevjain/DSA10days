[CmdletBinding()]
param(
    [Parameter(ValueFromRemainingArguments = $true)]
    [string[]] $ReviewArgs
)

Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

$RepoRoot = (Resolve-Path (Join-Path $PSScriptRoot "../..")).Path
$ToolRoot = $env:DSA_REVIEW_OS_ROOT
if ([string]::IsNullOrWhiteSpace($ToolRoot)) {
    $candidateRoots = @(
        (Join-Path $RepoRoot "../../dsa-review"),
        (Join-Path $RepoRoot "../dsa-review"),
        "G:\TechStudyNotes\dsa-review"
    )
    $ToolRoot = @($candidateRoots | Where-Object { Test-Path -LiteralPath $_ } | Select-Object -First 1)
}

if ([string]::IsNullOrWhiteSpace($ToolRoot)) {
    throw "Set DSA_REVIEW_OS_ROOT to the local dsa-review Review OS checkout."
}

$ToolRoot = (Resolve-Path $ToolRoot).Path
$reviewRepoCandidates = @(
    (Join-Path $ToolRoot "scripts/review-repo.ps1"),
    (Join-Path $ToolRoot "scripts/review-repo.sh"),
    (Join-Path $ToolRoot "scripts/review-repo.cmd")
)
$ReviewRepoCmd = @($reviewRepoCandidates | Where-Object { Test-Path -LiteralPath $_ } | Select-Object -First 1)

if ([string]::IsNullOrWhiteSpace($ReviewRepoCmd)) {
    throw "Could not find DSA Review OS wrapper under $ToolRoot/scripts. Expected review-repo.ps1, review-repo.sh, or review-repo.cmd."
}

if ($ReviewRepoCmd.EndsWith(".ps1", [System.StringComparison]::OrdinalIgnoreCase)) {
    & $ReviewRepoCmd $RepoRoot @ReviewArgs
} elseif ($ReviewRepoCmd.EndsWith(".sh", [System.StringComparison]::OrdinalIgnoreCase)) {
    & bash $ReviewRepoCmd $RepoRoot @ReviewArgs
} else {
    & $ReviewRepoCmd $RepoRoot @ReviewArgs
}
exit $LASTEXITCODE
