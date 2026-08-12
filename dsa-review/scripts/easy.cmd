@echo off
powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0review-os.ps1" easy %* --no-sync
exit /b %ERRORLEVEL%
