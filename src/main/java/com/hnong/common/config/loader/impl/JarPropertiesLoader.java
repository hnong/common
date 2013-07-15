package com.hnong.common.config.loader.impl;

import com.hnong.common.config.ConfigurationResourceFilter;
import com.hnong.common.config.constants.ConfigConstant;
import com.hnong.common.config.parser.JarPropertiesParser;
import com.hnong.common.config.loader.PropertiesLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Loader properties files from specified jar under current classpath
 */
public class JarPropertiesLoader implements PropertiesLoader<JarPropertiesParser> {
    private static final Logger LOGGER = LoggerFactory.getLogger(JarPropertiesLoader.class);

    private final ConfigurationResourceFilter<JarEntry> jarEntryFilter;

    public JarPropertiesLoader(ConfigurationResourceFilter<JarEntry> jarEntryFilter) {
        this.jarEntryFilter = jarEntryFilter;
    }

    public List<JarPropertiesParser> loadConfigurations() {
        LOGGER.info("load configurations from jars , use [{}] to scan the jars.", ConfigConstant.JAR_SCAN_BASE_PACKAGE);
        List<JarPropertiesParser> result = new ArrayList<JarPropertiesParser>();
        Set<JarFile> jarFiles = getLoadAllJarFiles();
        if (jarFiles != null && !jarFiles.isEmpty()) {
            List<JarPropertiesParser> tmp = null;
            for (JarFile jarFile : jarFiles) {
                tmp = loadFromJarFile(jarFile);
                if (tmp != null && !tmp.isEmpty()) {
                    LOGGER.info("load configurations from {} , configs [{}].", jarFile.getName(), Arrays.toString(tmp.toArray()));
                    result.addAll(tmp);
                } else {
                    LOGGER.info("not config file in {}.", jarFile.getName());
                }
            }
        } else {
            LOGGER.info("not jar file to load.");
        }
        return new ArrayList<JarPropertiesParser>(result);
    }

    private Set<JarFile> getLoadAllJarFiles() {
        HashMap<String, JarFile> jars = new HashMap<String, JarFile>();
        ClassLoader classLoader = getClassLoader();
        LOGGER.info(classLoader.toString());
        try {
            Enumeration<URL> dirUrls = classLoader.getResources(ConfigConstant.JAR_SCAN_BASE_PACKAGE);
            while (dirUrls.hasMoreElements()) {
                URL dir = dirUrls.nextElement();
                URLConnection con = dir.openConnection();
                if (con instanceof JarURLConnection) {
                    JarURLConnection jarCon = (JarURLConnection) con;
                    if (!jars.containsKey(jarCon.getJarFile().getName())) {
                        jars.put(jarCon.getJarFile().getName(), jarCon.getJarFile());
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("scan jars error,base package:{}, e:{}", ConfigConstant.JAR_SCAN_BASE_PACKAGE, e);
        }

        // adapter resin 3.1.6 try to get jar from WEB-INF/lib folder
        try {
            Enumeration<URL> dirUrls = classLoader.getResources("");
            while (dirUrls.hasMoreElements()) {
                URL classesRoot = dirUrls.nextElement();
                if (classesRoot == null) {
                    continue;
                }
                File dirRoot = new File(classesRoot.getFile());
                if (!dirRoot.exists() || !dirRoot.isDirectory()) {
                    continue;
                }
                LOGGER.info(dirRoot.getAbsolutePath());
                if (dirRoot.getAbsolutePath().contains("WEB-INF")) {
                    File libRoot = new File(dirRoot.getAbsolutePath().replace("classes", "lib"));
                    File[] subFiles = libRoot.listFiles();
                    for (File file : subFiles) {
                        if (file.getName().startsWith("easou-") && file.getName().endsWith(".jar")) {
                            LOGGER.info(file.getName());
                            URL url = new URL("jar:file:" + file.getAbsolutePath() + "!/");
                            JarURLConnection jarCon = (JarURLConnection) url.openConnection();
                            if (!jars.containsKey(jarCon.getJarFile().getName())) {
                                jars.put(jarCon.getJarFile().getName(), jarCon.getJarFile());
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("failed to check WEB-INF/lib {}", e.getMessage());
        }

        return new HashSet<JarFile>(jars.values());
    }


    private List<JarPropertiesParser> loadFromJarFile(JarFile jarFile) {
        List<JarPropertiesParser> configs = new ArrayList<JarPropertiesParser>();
        String jarName = parseJarName(jarFile.getName());
        LOGGER.info("load configurations from {}.", jarName);
        Enumeration entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = (JarEntry) entries.nextElement();
            String entryPath = entry.getName();
            if (jarEntryFilter.filter(entry)) {
                String fileName = parseFileName(entryPath);
                try {
                    JarPropertiesParser configuration = new JarPropertiesParser(jarName, fileName);
                    configuration.load(jarFile.getInputStream(entry));
                    configs.add(configuration);
                    LOGGER.info("load configurations from {}!/{}, config:{}", new Object[]{jarName, entryPath, configuration});
                } catch (IOException e) {
                    LOGGER.error("load configurations from {} error, e:{}", jarName, e);
                }
            }
        }
        return configs;
    }

    private String parseFileName(String pathName) {
        int startIndex = pathName.lastIndexOf("/");
        if (startIndex == -1) {
            startIndex = 0;
        } else {
            startIndex++;
        }
        return pathName.substring(startIndex, pathName.length());
    }

    private String parseJarName(String pathName) {
        String path = pathName.replaceAll("\\\\", "/");
        int index = path.lastIndexOf("/");
        if (index < 0) {
            index = path.length();
        } else {
            index++;
        }
        return path.substring(index);
    }

    private ClassLoader getClassLoader() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            classLoader = getClass().getClassLoader();
        }
        return classLoader;
    }
}
