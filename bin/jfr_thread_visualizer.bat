@echo off
pushd %~dp0
pushd ..\

if "%JAVA_HOME%" == "" goto noJavaHome
if not exist "%JAVA_HOME%\bin\java.exe" goto noJavaHome

if exist "%JAVA_HOME%\jre\lib\jfr.jar" (
  set JFR_JAR=%JAVA_HOME%\jre\lib\jfr.jar
) else if exist "%JAVA_HOME%\lib\jfr.jar" (
  set JFR_JAR=%JAVA_HOME%\lib\jfr.jar
) else if exist "%JAVA8_HOME%\jre\lib\jfr.jar" (
  set JFR_JAR=%JAVA8_HOME%\jre\lib\jfr.jar
) else if exist "%JAVA8_HOME%\lib\jfr.jar" (
  set JFR_JAR=%JAVA8_HOME%\lib\jfr.jar
) else (
  goto noJfrJar
)

set CLASSPATH=lib\*;%JFR_JAR%

echo ==========================
echo This tool use classes in jdk.jfr package. These classes requires a commercial license for use in production.
echo ref. https://docs.oracle.com/javase/9/docs/api/jdk/jfr/package-summary.html
echo ==========================
echo "%JAVA_HOME%\bin\java" -XX:+UnlockCommercialFeatures -cp "%CLASSPATH%" com.ka_ka_xyz.jfr_thread_visualizer.Main %1 %2
"%JAVA_HOME%\bin\java" -XX:+UnlockCommercialFeatures -cp "%CLASSPATH%" com.ka_ka_xyz.jfr_thread_visualizer.Main %1 %2
goto end

:noJavaHome
echo The JAVA_HOME environment variable is not defined.
goto exit

:noJfrJar
echo jfr.jar was not found. the JDK8 home path must be defined as JAVA8_HOME environment variable when you use JDK9 or above.
goto exit

:exit
popd
popd
exit /b 1

:end
popd
popd
exit /b 0
