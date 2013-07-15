package com.hnong.common.config.constants;

public final class RedisConfigConstant {
    public static final String REDIS_PROPERTIES_SUFFIX = "redis";

    public static final String REDIS_HOST = "hnong_%s_redis_%s_host";
    public static final String REDIS_PORT = "hnong_%s_redis_%s_port";
    public static final String REDIS_CLIENT_POOL_MAX_ACTIVE = "hnong_%s_redis_%s_client_pool_max_active";
    public static final String REDIS_CLIENT_POOL_MIN_IDLE = "hnong_%s_redis_%s_client_pool_min_idle";
    public static final String REDIS_CLIENT_POOL_MAX_IDLE = "hnong_%s_redis_%s_client_pool_max_idle";
    public static final String REDIS_CLIENT_POOL_MAX_WAIT = "hnong_%s_redis_%s_client_pool_max_wait";

    private RedisConfigConstant() {
    }

    /**
     * Redis file name has format ${componentName}_redis_${nodeName}_${language}_${environment}.properties
     * Each redis node create a single config file
     *
     * @param componentName
     * @param nodeName
     * @return prefix file name
     */
    public static String getRedisConfigFile(String componentName, String nodeName) {
        StringBuffer configFile = new StringBuffer();
        configFile.append(ConfigConstant.HNONG_CONFIG_PREFIX)
                .append("_").append(componentName)
                .append("_").append(REDIS_PROPERTIES_SUFFIX)
                .append("_").append(nodeName);
        return configFile.toString();
    }
}
