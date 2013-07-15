package com.hnong.common.config.parser;

import com.hnong.common.config.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public abstract class AbstractParser implements Configuration {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractParser.class);

    protected abstract Map<String, String> getConfigData();

    public Double getDouble(String key) {
        return getString(key) != null ? Double.parseDouble(getString(key)) : null;
    }

    public Float getFloat(String key) {
        return getString(key) != null ? Float.parseFloat(getString(key)) : null;
    }

    public Integer getInt(String key) {
        return getString(key) != null ? Integer.parseInt(getString(key)) : null;
    }

    public Long getLong(String key) {
        return getString(key) != null ? Long.parseLong(getString(key)) : null;
    }

    public String getString(String key) {
        return getConfigData().get(key);
    }

    public Properties toProperties() {
        Properties properties = new Properties();
        for (Map.Entry<String, String> e : getConfigData().entrySet()) {
            properties.put(e.getKey(), e.getValue());
        }
        return properties;
    }

    public String toString() {
        if (getConfigData() != null) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("Configuration ::").append("\r\n");
            for (String key : getConfigData().keySet()) {
                stringBuffer.append("[")
                        .append(key).append("=").append(getConfigData().get(key))
                        .append("]")
                        .append("\r\n");
            }
            return stringBuffer.toString();
        } else {
            return "[]";
        }
    }

    public Map<String, String> toMap() {
        return Collections.unmodifiableMap(getConfigData());
    }

    protected Map<String, String> readProperty(URL url) {
        try {
            return loadFromStream(url.openStream());
        } catch (IOException e) {
            LOGGER.error("load from {} error, e:{}.", url, e);
        }
        return Collections.EMPTY_MAP;
    }

    protected Map<String, String> loadFromStream(InputStream inputStream) {
        Map<String, String> config = new HashMap<String, String>();
        try {
            Properties props = new Properties();
            props.load(inputStream);
            for (Map.Entry<Object, Object> e : props.entrySet()) {
                if (e.getKey() != null || e.getValue() != null) {
                    config.put(String.valueOf(e.getKey()), String.valueOf(e.getValue()));
                }
            }
        } catch (Exception e) {
            LOGGER.error("load properties from input stream error, e:{}.", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    LOGGER.error("close stream error, e:{}", e);
                }
            }
        }
        return config;
    }
}
