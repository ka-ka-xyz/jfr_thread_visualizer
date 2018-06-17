package com.ka_ka_xyz.jfr_thread_visualizer.threaddump.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.AbstractMap.SimpleEntry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ka_ka_xyz.jfr_thread_visualizer.threaddump.AbsThreadDumpParser;

public class ApiThredDumpParser extends AbsThreadDumpParser {

    
    private static final Pattern THREAD_START_PATTERN = Pattern.compile("^ThreadDump \\{$");
    private static final Pattern THREAD_STARTTIME_PATTERN = Pattern.compile("^  startTime = \\d*$");
    private static final Pattern THREAD_RESULT_PATTERN = Pattern.compile("^  result = \"?(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})$");
    public ApiThredDumpParser(Path path) {
        super(path);
    }

    @Override
    protected SimpleEntry<Boolean, String> extractTimestampFromThreadHeader(String firstLine, BufferedReader reader) throws IOException {
        if (!THREAD_START_PATTERN.matcher(firstLine).matches()
                || !THREAD_STARTTIME_PATTERN.matcher(reader.readLine()).matches()) {
            return new SimpleEntry<Boolean, String>(false, "");
        }
        String line = reader.readLine();
        Matcher matcher = THREAD_RESULT_PATTERN.matcher(line);
        if (matcher.find()) {
            return new SimpleEntry<Boolean, String>(true, matcher.group(1));
        } else {
            return new SimpleEntry<Boolean, String>(false, "");
        }
    }

}
