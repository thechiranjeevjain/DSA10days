@echo off
powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0review-os.ps1" good %* --no-sync
exit /b %ERRORLEVEL%
