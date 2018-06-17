package com.ka_ka_xyz.jfr_thread_visualizer.html;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.ka_ka_xyz.jfr_thread_visualizer.threaddump.ThreadDumpEntry;

import freemarker.ext.beans.BeansWrapperBuilder;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class ReportHtmlGeneretor {

    private final Configuration cfg;
    private final Set<String> tIds;
    private final Map<String, Map<Date, ThreadDumpEntry>> entries;
    
    public ReportHtmlGeneretor(Map<String, Map<Date, ThreadDumpEntry>> entries) {
        this.cfg = new Configuration(Configuration.VERSION_2_3_28);
        cfg.setClassForTemplateLoading(this.getClass(), "/template");
        cfg.setObjectWrapper(new BeansWrapperBuilder(Configuration.VERSION_2_3_28).build());
        this.tIds = entries.keySet();
        this.entries = entries;
    }
    
    public void write(final File outFile) throws IOException, TemplateException {
        outFile.createNewFile();
        Template threadTableTpl = cfg.getTemplate("thread_table.tpl");
        try (BufferedWriter writer = Files.newBufferedWriter(outFile.toPath(), StandardOpenOption.TRUNCATE_EXISTING)) {
            Map<String, Object> params = new HashMap<>();
            params.put("tIds", tIds);
            params.put("nameMap", entries.values().stream()
                    .flatMap(v -> v.values().stream())
                    .collect(Collectors.toMap(ThreadDumpEntry::getTid, entry -> entry.getThreadName(), (e1, e2) -> e1)));
            params.put("entries", entries);
            params.put("timestamps", entries.values().stream().flatMap(m -> m.keySet().stream()).sorted().collect(Collectors.toSet()));
            threadTableTpl.process(params, writer);
        }
    }
}
