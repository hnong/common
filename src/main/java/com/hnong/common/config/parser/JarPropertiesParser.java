package com.hnong.common.config.parser;

import com.hnong.common.config.constants.ConfigConstant;

import java.io.InputStream;
import java.util.Map;

public class JarPropertiesParser extends AbstractParser {
    private static final int HASH_CODE_BASE = 31;
    private Map<String, String> configData;
    private final String jarName;
    private final String envName;
    private final String fileName;

    public JarPropertiesParser(String jarName, String fileName) {
        this.jarName = jarName;
        this.fileName = fileName;
        this.envName = parseEnvName(fileName);
    }

    public void load(InputStream inputStream) {
        this.configData = loadFromStream(inputStream);
    }

    public String getJarName() {
        return jarName;
    }

    public String getFileName() {
        return fileName;
    }

    public String getEnvName() {
        return envName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        JarPropertiesParser that = (JarPropertiesParser) o;
        return stringCompare(fileName, that.getFileName()) && stringCompare(jarName, that.getJarName());
    }

    @Override
    public int hashCode() {
        int result = jarName != null ? jarName.hashCode() : 0;
        result = HASH_CODE_BASE * result + (fileName != null ? fileName.hashCode() : 0);
        return result;
    }

    @Override
    protected Map<String, String> getConfigData() {
        return this.configData;
    }

    private String parseEnvName(String fullFileName) {
        int index = fullFileName.lastIndexOf(ConfigConstant.HNONG_CONFIG_SUFFIX);
        if (index < 0) {
            index = fullFileName.length();
        }
        return fullFileName.substring(0, index);
    }

    private boolean stringCompare(String string, String other) {
        if (string != null) {
            return string.equals(other);
        } else {
            return other == null;
        }
    }
}
