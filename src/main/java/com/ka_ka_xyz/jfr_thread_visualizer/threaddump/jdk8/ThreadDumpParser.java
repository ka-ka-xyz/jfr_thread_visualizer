package com.ka_ka_xyz.jfr_thread_visualizer.threaddump.jdk8;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.AbstractMap.SimpleEntry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ka_ka_xyz.jfr_thread_visualizer.threaddump.AbsThreadDumpParser;

public class ThreadDumpParser extends AbsThreadDumpParser {

    private static final Pattern THREAD_DUMP_HEADER_PATTERN = Pattern.compile("^Thread Dump@.* = (\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})");

    public ThreadDumpParser(Path path) {
        super(path);
    }

    @Override
    protected SimpleEntry<Boolean, String> extractTimestampFromThreadHeader(String firstLine, BufferedReader reader) throws IOException {
        Matcher matcher = THREAD_DUMP_HEADER_PATTERN.matcher(firstLine);
        Boolean result = matcher.matches();
        String matched = result? matcher.group(1): "";
        return new SimpleEntry<Boolean, String>(result, matched);
    }

}
