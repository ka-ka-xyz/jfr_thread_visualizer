# About jfr_thread_visualizer v0.0.2

jfr_thread_visualizer is a tool for outputting time series thread dump information recorded in jfr file into html format.
It is an experimental tool and I does not guarantee the quality.

![Visualized Threads](readme_resources/visualized_threads.PNG "Visualized Threads")

## Requirement
- JDK 9 or higher
- It is necessary to specify the path of JDK 8 with the environment variable `JAVA8_HOME` when you refer jfr file which generated from java8 process.
- This tool depends on commercial feature of oracle JDK. Requires oracle commercial license in production.
    - https://docs.oracle.com/javase/9/docs/api/jdk/jfr/package-summary.html

## How To Execute
1. Edit bin/setenv.bat (or bin/setenv.sh) and enable Commercial Feature flag
2. Execute bin/jfr_thread_visualizer.bat (or bin/jfr_thread_visualizer.sh)

```
bin\jfr_thread_visualizer.bat ${jfr_file}
```

Example
```
jfr_thread_visualizer.bat C:\sample\sample/jfr
```

If execution is successful, `thread_table.html` is created.
When you click on the status column, the thread details are displayed in the new tab.

![Thread Detail](readme_resources/thread_detail.PNG "Thread Detail")
