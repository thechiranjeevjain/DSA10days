@echo off
powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0random-drill.ps1" %*
exit /b %ERRORLEVEL%
