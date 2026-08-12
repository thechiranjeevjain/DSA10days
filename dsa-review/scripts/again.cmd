@echo off
powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0review-os.ps1" again %* --no-sync
exit /b %ERRORLEVEL%
