package com.ka_ka_xyz.jfr_thread_visualizer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Date;
import java.util.Map;

import com.ka_ka_xyz.jfr_thread_visualizer.html.ReportHtmlGeneretor;
import com.ka_ka_xyz.jfr_thread_visualizer.reader.jdk8.JfrReader;
import com.ka_ka_xyz.jfr_thread_visualizer.threaddump.ThreadDumpParseException;
import com.ka_ka_xyz.jfr_thread_visualizer.threaddump.ThreadDumpParser;
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
        JfrReader jtdu = new JfrReader();
        Path tmp = jtdu.extractThreadDumpAsTemp(file);
        ThreadDumpParser parser = new ThreadDumpParser(tmp);
        Map<String, Map<Date, ThreadDumpEntry>> entries = parser.parse();
        ReportHtmlGeneretor gen = new ReportHtmlGeneretor(entries);
        gen.write(outFile);
    }
}
