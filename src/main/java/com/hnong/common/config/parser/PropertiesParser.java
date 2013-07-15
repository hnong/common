package com.hnong.common.config.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Collections;
import java.util.Map;

public class PropertiesParser extends AbstractParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesParser.class);

    private Map<String, String> configData;

    private final String configName;

    public PropertiesParser(String configName) {
        this.configName = configName;
        this.configData = buildConfig();
    }

    public String getFileName() {
        return configName;
    }

    @Override
    protected Map<String, String> getConfigData() {
        return this.configData;
    }

    private Map<String, String> buildConfig() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            classLoader = getClass().getClassLoader();
        }
        return loadFromClassLoader(classLoader);
    }

    private Map<String, String> loadFromClassLoader(ClassLoader classLoader) {
        LOGGER.debug("load config from class loader.", classLoader);
        if (classLoader != null) {
            try {
                URL url = classLoader.getResource(this.configName);
                return readProperty(url);
            } catch (Exception e) {
                LOGGER.error("load from class loader error, e:{}.", e);
            }
        }
        return Collections.emptyMap();
    }
}
