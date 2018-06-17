package com.ka_ka_xyz.jfr_thread_visualizer.reader.api;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import com.ka_ka_xyz.jfr_thread_visualizer.reader.AbsJfrReader;

import jdk.jfr.Recording;
import jdk.jfr.consumer.RecordedEvent;
import jdk.jfr.consumer.RecordingFile;

public class JfrApiReader extends AbsJfrReader {
    @Override
    public Path read(File file) throws IOException {
        if (!file.exists() || !file.getName().endsWith(".jfr")) {
            return null;
        }
        File jfr = null;
        if (isGzipCompressed(file)) {
            jfr = decompress(file);
        } else {
            jfr = file;
        }
        Path dump = Files.createTempFile("dump_temp_", null);
        
        try (Recording recording = new Recording()) {
            for (RecordedEvent event : RecordingFile.readAllEvents(jfr.toPath())) {
                if ("com.oracle.jdk.ThreadDump".equals(event.getEventType().getName())) {
                    Files.write(dump, event.toString().getBytes(), StandardOpenOption.APPEND);
                }
            }
        }
        return dump;
    }
}
