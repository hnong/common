package com.hnong.common.config;

import java.util.Map;
import java.util.Properties;

public interface Configuration {

    Double getDouble(String key);

    Float getFloat(String key);

    Integer getInt(String key);

    Long getLong(String key);

    String getString(String key);

    Properties toProperties();

    Map<String, String> toMap();
}
