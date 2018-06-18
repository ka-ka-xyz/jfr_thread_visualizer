#!/bin/bash
pushd `dirname $0`
pushd ../


if [ -r "bin/setenv.sh" ]; then
  . "bin/setenv.sh"
fi


if [ -z "$JAVA_HOME" ]; then
    echo "the JAVA_HOME environment variable is NOT defined"
    exit 1
fi

if [ -e "$JAVA_HOME/jre/lib/jfr.jar" ]; then
  JFR_JAR=$JAVA_HOME/jre/lib/jfr.jar
elif [ -e "$JAVA_HOME/lib/jfr.jar" ]; then
  JFR_JAR=$JAVA_HOME/lib/jfr.jar
elif [ -e "$JAVA8_HOME/jre/lib/jfr.jar" ]; then
  JFR_JAR=$JAVA8_HOME/jre/lib/jfr.jar
elif [ -e "$JAVA8_HOME/lib/jfr.jar" ]; then
  JFR_JAR=$JAVA8_HOME/lib/jfr.jar
else
    echo "jfr.jar was not found. the JDK8 home path must be defined as JAVA8_HOME environment variable when you use JDK9 or above."
    popd
    popd
    exit 1
fi

CLASSPATH=lib/"*":$JFR_JAR

echo "=========================="
echo "This tool use classes in jdk.jfr package. These classes requires a commercial license for use in production."
echo "ref. https://docs.oracle.com/javase/9/docs/api/jdk/jfr/package-summary.html "
echo "=========================="
echo "$JAVA_HOME/bin/java $JAVA_OPTS -cp $CLASSPATH com.ka_ka_xyz.jfr_thread_visualizer.Main $1 $2"
$JAVA_HOME/bin/java $JAVA_OPTS -cp $CLASSPATH com.ka_ka_xyz.jfr_thread_visualizer.Main $1 $2
popd
popd
