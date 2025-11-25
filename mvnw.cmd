@REM ----------------------------------------------------------------------------
@REM Maven Wrapper startup script
@REM ----------------------------------------------------------------------------

@echo off

set MAVEN_WRAPPER_VERSION=3.2.0

set MAVEN_PROJECTBASEDIR=%cd%
set WRAPPER_JAR="%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar"
set WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain

if not exist %WRAPPER_JAR% (
    echo Error: Maven wrapper JAR not found
    echo Please run the setup script first or use system Maven
    exit /b 1
)

@REM Execute Maven
"%JAVA_HOME%\bin\java.exe" ^
  %MAVEN_OPTS% ^
  -classpath %WRAPPER_JAR% ^
  "-Dmaven.multiModuleProjectDirectory=%MAVEN_PROJECTBASEDIR%" ^
  %WRAPPER_LAUNCHER% %*

if ERRORLEVEL 1 goto error
goto end

:error
set ERROR_CODE=1

:end
@endlocal & set ERROR_CODE=%ERROR_CODE%

exit /b %ERROR_CODE%
