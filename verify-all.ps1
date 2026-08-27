[CmdletBinding()]
param()

Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

$repoRoot = (Resolve-Path $PSScriptRoot).Path
Push-Location $repoRoot
try {
    $isWindowsHost = [System.Environment]::OSVersion.Platform -eq [System.PlatformID]::Win32NT
    $mavenCommand = if ($isWindowsHost -and (Test-Path -LiteralPath (Join-Path $repoRoot "mvnw.cmd"))) {
        Join-Path $repoRoot "mvnw.cmd"
    } elseif (Test-Path -LiteralPath (Join-Path $repoRoot "mvnw")) {
        Join-Path $repoRoot "mvnw"
    } else {
        "mvn"
    }

    Write-Host "[1/3] Running Maven tests..."
    & $mavenCommand test
    if ($LASTEXITCODE -ne 0) {
        exit $LASTEXITCODE
    }

    Write-Host "[2/3] Rebuilding DSA interview cockpit..."
    & (Join-Path $repoRoot "dsa-review/scripts/build-interview-cockpit.ps1")
    if ($LASTEXITCODE -ne 0) {
        exit $LASTEXITCODE
    }

    Write-Host "[3/3] Validating DSA interview cockpit..."
    & (Join-Path $repoRoot "dsa-review/scripts/validate-interview-cockpit.ps1")
    if ($LASTEXITCODE -ne 0) {
        exit $LASTEXITCODE
    }

    Write-Host ""
    Write-Host "All verification checks passed."
} finally {
    Pop-Location
}
