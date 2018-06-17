package com.ka_ka_xyz.jfr_thread_visualizer.reader.jdk8;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.zip.GZIPInputStream;

import java.util.Iterator;

import oracle.jrockit.jfr.parser.*;

public class JfrReader {
    @SuppressWarnings({ "deprecation", "restriction" })
    public Path extractThreadDumpAsTemp(File file) throws IOException {
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
                    if ("Thread Dump".equals(event.getName()))
                        Files.write(dump, event.toString().getBytes(), StandardOpenOption.APPEND);
                }
            }
        }
        return dump;
    }

    private File decompress(final File srcFile) throws IOException {
        byte[] buffer = new byte[1024];
        File target = File.createTempFile("flightrecorder_", null);
        target.deleteOnExit();

        try (FileOutputStream os = new FileOutputStream(target);
                GZIPInputStream zipIs = new GZIPInputStream(new FileInputStream(srcFile))) {
            int bytes;
            while ((bytes = zipIs.read(buffer)) > 0) {
                os.write(buffer, 0, bytes);
            }
        }
        return target;
    }

    private boolean isGzipCompressed(File file) throws IOException {
        try (FileInputStream ins = new FileInputStream(file)) {
            return (byte) ins.read() == (byte) GZIPInputStream.GZIP_MAGIC
                    && (byte) ins.read() == (byte) (GZIPInputStream.GZIP_MAGIC >> 8);
        }
    }
}
