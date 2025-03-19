@echo off
rem This is the Gradle Wrapper script for Windows
rem See https://docs.gradle.org/current/userguide/gradle_wrapper.html for more details.

rem JVM options to be passed when running Gradle
set DEFAULT_JVM_OPTS=-Xmx1024m

rem Gradle version to use
set GRADLE_VERSION=7.5  rem Adjust this version if needed

rem Set Gradle home and other environment variables
set GRADLE_HOME=%CD%\gradle
set GRADLE_BIN=%GRADLE_HOME%\bin

rem Check if the wrapper is present in the project
if not exist "%GRADLE_BIN%" (
    echo Gradle wrapper files not found! Running "gradle wrapper" to generate them.
    gradle wrapper --gradle-version %GRADLE_VERSION%
)

rem Execute Gradle command
"%GRADLE_BIN%\gradle" %*
