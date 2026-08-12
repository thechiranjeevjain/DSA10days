@echo off
powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0review-os.ps1" dashboard --port 7070 %*
exit /b %ERRORLEVEL%
