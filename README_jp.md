# jfr_thread_visualizerについて
jfrファイルに記録された時系列スレッドダンプ情報をhtml形式で出力するためのツールです。
実験的ツールであり、動作の品質は保証しません。

![Visualized Threads](readme_resources/visualized_threads.PNG "Visualized Threads")

## 動作要件
- JDK8以上
- JDK9以上の環境では、JDK8のパスを環境変数`JAVA8_HOME`で指定する必要があります


## 実行方法
```
bin\jfr_thread_visualizer.bat ${jfrファイル}
```

例
```
jfr_thread_visualizer.bat C:\sample\sample/jfr
```

実行に成功すると`thread_table.html`が作成されます。
ステータスカラムをクリックすると、新しいタブにスレッドの詳細が表示されます。

![Thread Detail](readme_resources/thread_detail.PNG "Thread Detail")
