package com.hnong.common.config;

public interface ConfigurationResourceFilter<T> {
    boolean filter(T resource);
}
