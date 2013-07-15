package com.hnong.common.config.parser;

import java.util.Map;

public class MapParser extends AbstractParser {
    private final Map<String, String> config;

    public MapParser(Map<String, String> configData) {
        this.config = configData;
    }

    @Override
    protected Map<String, String> getConfigData() {
        return this.config;
    }
}
