package com.ka_ka_xyz.jfr_thread_visualizer.reader.jdk8;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import com.ka_ka_xyz.jfr_thread_visualizer.reader.AbsJfrReader;

import java.util.Iterator;

import oracle.jrockit.jfr.parser.*;

public class JfrReader extends AbsJfrReader {
    
    
    
    @Override
    @SuppressWarnings({ "deprecation", "restriction" })
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
        
        try (Parser parser = new Parser(jfr)) {
            Iterator<ChunkParser> chunkIter = parser.iterator();
            while (chunkIter.hasNext()) {
                ChunkParser chunkParser = chunkIter.next();
                for (FLREvent event : chunkParser) {
                    if ("Thread Dump".equals(event.getName())) {
                        Files.write(dump, event.toString().getBytes(), StandardOpenOption.APPEND);
                    }
                }
            }
        }
        return dump;
    }

}
