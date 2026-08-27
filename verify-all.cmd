@echo off
setlocal

pushd "%~dp0" >nul

where pwsh >nul 2>nul
if not errorlevel 1 (
  pwsh -NoProfile -File "%~dp0verify-all.ps1"
) else (
  powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0verify-all.ps1"
)
if errorlevel 1 goto failed

popd >nul
exit /b 0

:failed
echo.
echo Verification failed.
popd >nul
exit /b 1
