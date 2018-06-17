package com.ka_ka_xyz.jfr_thread_visualizer.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.zip.GZIPInputStream;

public abstract class AbsJfrReader {
    
    abstract public Path read(File file) throws IOException;
    
    protected File decompress(final File srcFile) throws IOException {
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

    protected boolean isGzipCompressed(File file) throws IOException {
        try (FileInputStream ins = new FileInputStream(file)) {
            return (byte) ins.read() == (byte) GZIPInputStream.GZIP_MAGIC
                    && (byte) ins.read() == (byte) (GZIPInputStream.GZIP_MAGIC >> 8);
        }
    }
}
