package com.ka_ka_xyz.jfr_thread_visualizer.threaddump;

import java.lang.Thread.State;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Getter;

public class ThreadDumpEntry {
    @Getter
    final private Date timestamp;
    @Getter
    final private String headerLine;
    @Getter
    final private String tid;
    @Getter
    final private String threadName;
    @Getter
    final private String stateLine;
    @Getter
    final private String state;
    @Getter
    final private List<String> stack;
    @Getter
    final private List<String> lines;

    private static final Pattern THREAD_NAME_PATTERN = Pattern.compile("^\"(.*?)\"");
    private static final Pattern TID_PATTERN = Pattern.compile(" tid\\=(0x.*?) ");
    
    
    public ThreadDumpEntry(Date timestamp, String headerLine, String stateLine, List<String> stack) {
        this.timestamp = timestamp;
        this.headerLine = headerLine;
        this.stateLine = stateLine;
        this.threadName = find(THREAD_NAME_PATTERN.matcher(headerLine));
        this.tid = find(TID_PATTERN.matcher(headerLine));
        this.state = getState(stateLine, stack).toString();
        this.stack = new ArrayList<>();
        this.stack.addAll(stack);
        this.lines = new ArrayList<>();
        this.lines.add(headerLine);
        this.lines.add(stateLine);
        this.lines.addAll(this.stack);
    }

    private VisualVmState getState(String line, List<String> stack) {
        State state = null;
        for (State s : State.values()) {
            if (line.contains("java.lang.Thread.State: " + s.toString())) {
                state = s;
                break;
            }
        }
        if (state == null) {
            return VisualVmState.UNKNOWN;
        }

        switch (state) {
        case BLOCKED:
            return VisualVmState.MONITOR;
        case RUNNABLE:
            return VisualVmState.RUNNING;
        case TIMED_WAITING:
        case WAITING:
            if (stack.size() > 0) {
                String el = stack.get(0);
                if (el.contains("java.lang.Thread.sleep")) {
                    return VisualVmState.SLEEPING;
                } else if (el.contains("sun.misc.Unsafe.park")) {
                    return VisualVmState.PARK;
                }
            }
            return VisualVmState.WAIT;
        case TERMINATED:
        case NEW:
            return VisualVmState.ZOMBIE;
        default:
            return VisualVmState.UNKNOWN;
        }
    }
    
    private String find(Matcher matcher) {
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            throw new IllegalStateException("Not matched!");
        }
    }

    public static enum VisualVmState {
        MONITOR,
        RUNNING,
        SLEEPING,
        PARK,
        WAIT,
        ZOMBIE,
        UNKNOWN
    }
}
