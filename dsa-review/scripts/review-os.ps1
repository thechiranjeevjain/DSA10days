[CmdletBinding()]
param(
    [Parameter(ValueFromRemainingArguments = $true)]
    [string[]] $ReviewArgs
)

Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

$RepoRoot = (Resolve-Path (Join-Path $PSScriptRoot "..\..")).Path
$ToolRoot = $env:DSA_REVIEW_OS_ROOT
if ([string]::IsNullOrWhiteSpace($ToolRoot)) {
    $ToolRoot = "G:\TechStudyNotes\dsa-review"
}

$ToolRoot = (Resolve-Path $ToolRoot).Path
$ReviewRepoCmd = Join-Path $ToolRoot "scripts\review-repo.cmd"

if (-not (Test-Path -LiteralPath $ReviewRepoCmd)) {
    throw "Could not find DSA Review OS wrapper: $ReviewRepoCmd"
}

& $ReviewRepoCmd $RepoRoot @ReviewArgs
exit $LASTEXITCODE
