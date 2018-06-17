package com.ka_ka_xyz.jfr_thread_visualizer.threaddump;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ThreadDumpParser {
    
    private final Path path;
    private static final Pattern THREAD_DUMP_HEADER_PATTERN = Pattern.compile("^Thread Dump@.* = (\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})");
    private static final Pattern THREAD_HEADER_PATTERN = Pattern.compile(" prio=.*? tid=.*? nid=.*? ");
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    
    public Map<String, Map<Date, ThreadDumpEntry>> parse() throws IOException, ThreadDumpParseException {
        //キーはスレッド名！
        Map<String, Map<Date, ThreadDumpEntry>> entries = new TreeMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {

            String threadHeader = null;
            String stateStr = null;
            List<String> callstack = null;
            Date timestamp = null;
            boolean tail = false;
            
            String line = reader.readLine();
            while(line != null) {
                Matcher matcher = THREAD_DUMP_HEADER_PATTERN.matcher(line);
                if (matcher.matches()) {
                    try {
                        timestamp = sdf.parse(matcher.group(1));
                        threadHeader = null;
                        stateStr = null;
                        tail = false;
                        line = reader.readLine();
                        continue;
                    } catch (ParseException e) {
                        throw new ThreadDumpParseException(e);
                    }
                }

                matcher = THREAD_HEADER_PATTERN.matcher(line);
                if (matcher.find()) {
                    threadHeader = line;
                    stateStr = reader.readLine();
                    String stackLine;
                    callstack = new ArrayList<String>();
                    do {
                        stackLine = reader.readLine();
                        callstack.add(stackLine);
                    } while(!"".equals(stackLine));
                    tail = true;
                }

                if (tail) {
                    ThreadDumpEntry entry = new ThreadDumpEntry(timestamp, threadHeader, stateStr, callstack);
                    String threadId = entry.getTid();
                    final Date ts = timestamp;
                    entries.compute(threadId, (k, v) -> {
                        Map<Date, ThreadDumpEntry> map = (v == null) ? new TreeMap<>() : v;
                        map.put(ts, entry);
                        return map;
                    });
                    tail = false;
                }
                line = reader.readLine();
            }
        }
        return entries;
    }

}
