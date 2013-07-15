package com.hnong.common.config.loader.impl;

import com.hnong.common.config.ConfigurationResourceFilter;
import com.hnong.common.config.parser.PropertiesParser;
import com.hnong.common.config.loader.PropertiesLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ClassesPropertiesLoader implements PropertiesLoader<PropertiesParser> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClassesPropertiesLoader.class);

    private final ConfigurationResourceFilter<File> fileFilter;

    public ClassesPropertiesLoader(ConfigurationResourceFilter<File> fileFilter) {
        this.fileFilter = fileFilter;
    }

    public List<PropertiesParser> loadConfigurations() {
        List<PropertiesParser> configurations = new ArrayList<PropertiesParser>();
        LOGGER.info("load properties from class path.");
        Set<File> classesPath = getFilesFromClassesPath();
        for (File dirRoot : classesPath) {
            LOGGER.info("scan {} for properties file.", dirRoot.getAbsolutePath());
            FileFilter filter = new FileFilter() {
                public boolean accept(File pathName) {
                    return fileFilter.filter(pathName);
                }
            };

            for (File file : dirRoot.listFiles(filter)) {
                PropertiesParser configuration = new PropertiesParser(file.getName());
                configurations.add(configuration);
                LOGGER.info("load from {}, config:{}", file.getName(), configuration);
            }
        }
        return configurations;
    }

    private Set<File> getFilesFromClassesPath() {
        Map<String, File> classesDirs = new HashMap<String, File>();
        ClassLoader classLoader = getClassLoader();
        Enumeration<URL> dirUrls = null;
        try {
            dirUrls = classLoader.getResources("");
            while (dirUrls.hasMoreElements()) {
                URL classesRoot = (URL) dirUrls.nextElement();
                if (classesRoot != null) {
                    String dirFileName = classesRoot.getFile();
                    File dirRoot = new File(dirFileName);
                    if (dirRoot.exists() && dirRoot.isDirectory()) {
                        classesDirs.put(dirRoot.getAbsolutePath(), dirRoot);
                    } else {
                        LOGGER.error("{} is not exists or is a file.", dirFileName);
                    }
                } else {
                    LOGGER.error("can not find classpath");
                }
            }
        } catch (IOException e) {
            LOGGER.error("load from classes path error,e={}", e);
        }
        return new HashSet(classesDirs.values());
    }

    private ClassLoader getClassLoader() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            classLoader = getClass().getClassLoader();
        }
        return classLoader;
    }
}
