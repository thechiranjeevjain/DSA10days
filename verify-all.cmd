@echo off
setlocal

pushd "%~dp0" >nul

echo [1/3] Running Maven tests...
call mvn test
if errorlevel 1 goto failed

echo [2/3] Rebuilding DSA interview cockpit...
call dsa-review\scripts\build-interview-cockpit.cmd
if errorlevel 1 goto failed

echo [3/3] Validating DSA interview cockpit...
call dsa-review\scripts\validate-interview-cockpit.cmd
if errorlevel 1 goto failed

echo.
echo All verification checks passed.
popd >nul
exit /b 0

:failed
echo.
echo Verification failed.
popd >nul
exit /b 1
