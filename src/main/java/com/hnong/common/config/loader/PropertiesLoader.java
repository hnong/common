package com.hnong.common.config.loader;

import com.hnong.common.config.Configuration;

import java.util.List;

public interface PropertiesLoader<T extends Configuration> {

    /**
     * Responsible for loading data for provided resource
     *
     * @return list of configuration
     */
    List<T> loadConfigurations();
}
