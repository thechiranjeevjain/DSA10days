@echo off
powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0validate-interview-cockpit.ps1" %*
exit /b %ERRORLEVEL%
