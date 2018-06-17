package com.ka_ka_xyz.jfr_thread_visualizer.threaddump;

public class ThreadDumpParseException extends Exception {

    private static final long serialVersionUID = -1565841091638604272L;

    public ThreadDumpParseException() {
        super();
    }

    public ThreadDumpParseException(String msg) {
        super(msg);
    }
    public ThreadDumpParseException(Throwable t) {
        super(t);
    }
    public ThreadDumpParseException(String msg, Throwable t) {
        super(msg, t);
    }
}
