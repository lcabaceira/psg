@echo off
set JAREXE="%JAVA_HOME%\bin\jar.exe"
set AWD_MR_BASE=..\workdesk
set AWD_DEPLOY_BASE=workdesk
set WARFILENAME=workdesk.war

if not exist %AWD_MR_BASE% goto :CHECK_DEPLOY
set AWD_BASE=%AWD_MR_BASE%
echo Creating web archive from location: %AWD_BASE%.
goto :CREATE_WAR

:CHECK_DEPLOY
if not exist %AWD_DEPLOY_BASE% goto :INVALID_LOCATION
set AWD_BASE=%AWD_DEPLOY_BASE%
echo Creating web archive from location: %AWD_BASE%.
goto :CREATE_WAR

:CREATE_WAR
rem *** Check if the JAVA_HOME environment variable is empty. ***
rem *** If it is, tell the user to set it and exit. ***
if "%JAVA_HOME%"=="" goto :SHOWHINT1

rem *** Check if the jar.exe can be found. ***
rem *** If it cannot be found, tell the user and exit. ***
if not exist %JAREXE% goto :SHOWHINT2

rem *** At this point the jar.exe can be found.***
rem *** Tell user about overwriting if the war file already exists. ***
if exist %WARFILENAME% (echo Overwriting existing web archive.)

rem *** Execute the jar command. ***
%JAREXE% -cf %WARFILENAME% -C %AWD_BASE% .

rem *** Tell user about successful war file creation. ***
echo Web archive created successfully.
goto :EOF

:SHOWHINT1
echo The JAVA_HOME environment variable is empty. Please make sure it is pointing to the root directory of a JDK installation (i.e. "c:\Program Files\Java\jdk1.6.0_39").
goto :EOF

:SHOWHINT2
echo The jar tool "%JAREXE%" does not exist. Please make sure your JAVA_HOME environment variable is set correctly (i.e. "c:\Program Files\Java\jdk1.6.0_39").
goto :EOF

:INVALID_LOCATION
echo Could not find directory %AWD_MR_BASE% nor %AWD_DEPLOY_BASE%. This script is supposed to be executed from within the <distribution_zip>/bin/build folder.
goto :EOF