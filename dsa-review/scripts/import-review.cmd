@echo off
powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0import-review.ps1" %*
exit /b %ERRORLEVEL%
