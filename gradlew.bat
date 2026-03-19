@ECHO OFF
REM Lightweight Windows shim when Gradle wrapper files are unavailable.
IF "%JAVA21_HOME%"=="" GOTO run
SET "JAVA_HOME=%JAVA21_HOME%"
SET "PATH=%JAVA_HOME%\bin;%PATH%"
:run
gradle %*
