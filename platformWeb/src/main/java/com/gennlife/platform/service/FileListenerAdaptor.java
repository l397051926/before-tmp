package com.gennlife.platform.service;

import com.gennlife.platform.util.ZipUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.LinkedList;

public class FileListenerAdaptor extends FileAlterationListenerAdaptor {
    private static final Logger logger = LoggerFactory.getLogger(FileListenerAdaptor.class);
    private GeneDataService geneDataService;


    @Override
    public void onFileCreate(File file) {
        execUnZip(file);
        super.onFileCreate(file);

    }

    public void execUnZip(File file) {
        try {
            logger.info("start unZip "+file.getAbsolutePath());
            long s = System.currentTimeMillis();
            geneDataService.unZip(file.getAbsolutePath());
            FileUtils.deleteQuietly(file);
            logger.info("unzip " + file.getAbsolutePath() + " " + (System.currentTimeMillis() - s) + " ms");
        } catch (Exception e) {
            logger.error(file.getAbsolutePath() + " unzip error :", e);
        }
    }

    @Override
    public void onFileChange(File file) {
        execUnZip(file);
        super.onFileChange(file);
    }

    public void init(GeneDataService geneDataService, String listenDir) {
        this.geneDataService = geneDataService;
        LinkedList<File> list = ZipUtils.getAllZip(listenDir);
        if (list != null) {
            for (File zip : list) {
                execUnZip(zip);
            }
        }
    }
}