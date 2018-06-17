package com.ka_ka_xyz.jfr_thread_visualizer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Date;
import java.util.Map;

import com.ka_ka_xyz.jfr_thread_visualizer.html.ReportHtmlGeneretor;
import com.ka_ka_xyz.jfr_thread_visualizer.reader.AbsJfrReader;
import com.ka_ka_xyz.jfr_thread_visualizer.reader.api.JfrApiReader;
import com.ka_ka_xyz.jfr_thread_visualizer.reader.jdk8.JfrReader;
import com.ka_ka_xyz.jfr_thread_visualizer.threaddump.ThreadDumpParseException;
import com.ka_ka_xyz.jfr_thread_visualizer.threaddump.api.ApiThredDumpParser;
import com.ka_ka_xyz.jfr_thread_visualizer.threaddump.jdk8.ThreadDumpParser;
import com.ka_ka_xyz.jfr_thread_visualizer.threaddump.AbsThreadDumpParser;
import com.ka_ka_xyz.jfr_thread_visualizer.threaddump.ThreadDumpEntry;

import freemarker.template.TemplateException;

public class Main {
    private static final String THREAD_TABLE_HTML_OUTFILE = "thread_table.html";
    
    
    public static void main(String[] args) throws IOException, ThreadDumpParseException, TemplateException {
        File file = new File(args[0]);
        
        String outStr;
        if (args.length < 2) {
            outStr = "./";
        } else {
            outStr = args[1];
        }
        File outDir = new File(outStr);

        if (!outDir.exists() && !outDir.mkdirs()) {
            throw new IOException("failed to create " + outDir.getAbsolutePath());
        }

        System.out.println("outdir:" + outDir.getAbsolutePath());

        File outFile = new File(outDir, THREAD_TABLE_HTML_OUTFILE);
        AbsJfrReader jtdu = new JfrApiReader();
        Path tmp = null;
        AbsThreadDumpParser parser = null;
        try {
            tmp = jtdu.read(file);
            parser = new ApiThredDumpParser(tmp);
        } catch(IOException ioe) {
            jtdu = new JfrReader();
            try {
                tmp = jtdu.read(file);
                parser = new ThreadDumpParser(tmp);
            } catch(Throwable t) {
                t.printStackTrace();
            }
        }
        
        if (tmp == null || parser == null) {
            System.err.println("Failed to read" + file);
            System.exit(1);
        }

        Map<String, Map<Date, ThreadDumpEntry>> entries = parser.parse();
        ReportHtmlGeneretor gen = new ReportHtmlGeneretor(entries);
        gen.write(outFile);
    }
}
