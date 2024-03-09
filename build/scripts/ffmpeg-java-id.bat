@rem
@rem Copyright 2015 the original author or authors.
@rem
@rem Licensed under the Apache License, Version 2.0 (the "License");
@rem you may not use this file except in compliance with the License.
@rem You may obtain a copy of the License at
@rem
@rem      https://www.apache.org/licenses/LICENSE-2.0
@rem
@rem Unless required by applicable law or agreed to in writing, software
@rem distributed under the License is distributed on an "AS IS" BASIS,
@rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@rem See the License for the specific language governing permissions and
@rem limitations under the License.
@rem

@if "%DEBUG%"=="" @echo off
@rem ##########################################################################
@rem
@rem  ffmpeg-java-id startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%"=="" set DIRNAME=.
@rem This is normally unused
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Resolve any "." and ".." in APP_HOME to make it shorter.
for %%i in ("%APP_HOME%") do set APP_HOME=%%~fi

@rem Add default JVM options here. You can also use JAVA_OPTS and FFMPEG_JAVA_ID_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if %ERRORLEVEL% equ 0 goto execute

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto execute

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\ffmpeg-java-id-1.1.6.jar;%APP_HOME%\lib\annotations-23.0.0.jar;%APP_HOME%\lib\google-cloud-texttospeech-2.38.0.jar;%APP_HOME%\lib\gson-2.10.1.jar;%APP_HOME%\lib\slf4j-api-1.7.5.jar;%APP_HOME%\lib\commons-io-2.11.0.jar;%APP_HOME%\lib\commons-lang3-3.8.1.jar;%APP_HOME%\lib\commons-exec-1.3.jar;%APP_HOME%\lib\auto-value-annotations-1.10.4.jar;%APP_HOME%\lib\google-auth-library-credentials-1.23.0.jar;%APP_HOME%\lib\google-auth-library-oauth2-http-1.23.0.jar;%APP_HOME%\lib\google-http-client-1.44.1.jar;%APP_HOME%\lib\google-http-client-gson-1.44.1.jar;%APP_HOME%\lib\guava-32.1.3-jre.jar;%APP_HOME%\lib\protobuf-java-3.25.2.jar;%APP_HOME%\lib\protobuf-java-util-3.25.2.jar;%APP_HOME%\lib\grpc-alts-1.62.2.jar;%APP_HOME%\lib\grpc-api-1.62.2.jar;%APP_HOME%\lib\grpc-auth-1.62.2.jar;%APP_HOME%\lib\grpc-context-1.62.2.jar;%APP_HOME%\lib\grpc-core-1.62.2.jar;%APP_HOME%\lib\grpc-googleapis-1.62.2.jar;%APP_HOME%\lib\grpc-grpclb-1.62.2.jar;%APP_HOME%\lib\grpc-inprocess-1.62.2.jar;%APP_HOME%\lib\grpc-netty-shaded-1.62.2.jar;%APP_HOME%\lib\grpc-protobuf-1.62.2.jar;%APP_HOME%\lib\grpc-protobuf-lite-1.62.2.jar;%APP_HOME%\lib\grpc-services-1.62.2.jar;%APP_HOME%\lib\grpc-stub-1.62.2.jar;%APP_HOME%\lib\grpc-util-1.62.2.jar;%APP_HOME%\lib\grpc-xds-1.62.2.jar;%APP_HOME%\lib\gax-2.45.0.jar;%APP_HOME%\lib\gax-grpc-2.45.0.jar;%APP_HOME%\lib\gax-httpjson-2.45.0.jar;%APP_HOME%\lib\api-common-2.28.0.jar;%APP_HOME%\lib\proto-google-common-protos-2.36.0.jar;%APP_HOME%\lib\proto-google-cloud-texttospeech-v1-2.38.0.jar;%APP_HOME%\lib\proto-google-cloud-texttospeech-v1beta1-0.127.0.jar;%APP_HOME%\lib\jsr305-3.0.2.jar;%APP_HOME%\lib\error_prone_annotations-2.24.1.jar;%APP_HOME%\lib\javax.annotation-api-1.3.2.jar;%APP_HOME%\lib\failureaccess-1.0.1.jar;%APP_HOME%\lib\listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar;%APP_HOME%\lib\checker-qual-3.42.0.jar;%APP_HOME%\lib\j2objc-annotations-2.8.jar;%APP_HOME%\lib\opencensus-api-0.31.1.jar;%APP_HOME%\lib\annotations-4.1.1.4.jar;%APP_HOME%\lib\animal-sniffer-annotations-1.23.jar;%APP_HOME%\lib\conscrypt-openjdk-uber-2.5.2.jar;%APP_HOME%\lib\perfmark-api-0.27.0.jar;%APP_HOME%\lib\opencensus-proto-0.2.0.jar;%APP_HOME%\lib\re2j-1.7.jar;%APP_HOME%\lib\httpclient-4.5.14.jar;%APP_HOME%\lib\commons-codec-1.16.1.jar;%APP_HOME%\lib\httpcore-4.4.16.jar;%APP_HOME%\lib\opencensus-contrib-http-util-0.31.1.jar;%APP_HOME%\lib\threetenbp-1.6.8.jar


@rem Execute ffmpeg-java-id
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %FFMPEG_JAVA_ID_OPTS%  -classpath "%CLASSPATH%"  %*

:end
@rem End local scope for the variables with windows NT shell
if %ERRORLEVEL% equ 0 goto mainEnd

:fail
rem Set variable FFMPEG_JAVA_ID_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
set EXIT_CODE=%ERRORLEVEL%
if %EXIT_CODE% equ 0 set EXIT_CODE=1
if not ""=="%FFMPEG_JAVA_ID_EXIT_CONSOLE%" exit %EXIT_CODE%
exit /b %EXIT_CODE%

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
