@echo off

if not exist "%JAVA_HOME%\bin\jps.exe" echo 请先设置您的环境变量 & EXIT /B 1

rem 启动入口类,该脚本文件用于别的项目时要改这里
set MAIN_CLASS=com.autu.Application

setlocal

set "PATH=%JAVA_HOME%\bin;%PATH%"

echo killing jfinal server

for /f "tokens=1" %%i in ('jps -l ^| find %MAIN_CLASS%') do ( taskkill /F /PID %%i )

echo Done!