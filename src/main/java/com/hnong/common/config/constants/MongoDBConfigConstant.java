package com.hnong.common.config.constants;

public final class MongoDBConfigConstant {
    public static final int DEFAULT_PORT = 0;
    public static final String MONGO_PROPERTIES_SUFFIX = "mongodb";

    public static final String MONGO_ITEM_HOST = "hnong_%s_mongodb_%s_hosts";
    public static final String MONGO_ITEM_AUTOCONNECTRETRY = "hnong_%s_mongodb_%s_autoConnectRetry";
    public static final String MONGO_ITEM_CONNECTION_PER_HOST = "hnong_%s_mongodb_%s_connectionsPerHost";
    public static final String MONGO_ITEM_CONNECTION_TIMEOUT = "hnong_%s_mongodb_%s_connectTimeout";
    public static final String MONGO_ITEM_MAX_WAITTIME = "hnong_%s_mongodb_%s_maxWaitTime";
    public static final String MONGO_ITEM_SAFE = "hnong_%s_mongodb_%s_safe";
    public static final String MONGO_ITEM_SLAVEOF = "hnong_%s_mongodb_%s_slaveOk";
    public static final String MONGO_ITEM_SOCKET_KEEPALIVE = "hnong_%s_mongodb_%s_socketKeepAlive";
    public static final String MONGO_ITEM_SOCKET_TIMEOUT = "hnong_%s_mongodb_%s_socketTimeout";
    public static final String MONGO_ITEM_W_TIMEOUT = "hnong_%s_mongodb_%s_wtimeout";
    //private static final String MONGO_ITEM_MAX_AUTO_CONNECTION_RETRY_TIME = "easou_%s_mongodb_%s_maxAutoConnectRetryTime";

    private MongoDBConfigConstant() {
    }

    /**
     * Mongo file name have format ${componentName}_mongodb_${clusterName}_${language}_${environment}.properties
     * Each mongo cluster create a single config file
     * @param componentName
     * @param clusterName
     * @return prefix file name
     */
    public static String getMongodbConfigFile(String componentName, String clusterName) {
        StringBuffer configFile = new StringBuffer();
        configFile.append(ConfigConstant.HNONG_CONFIG_PREFIX)
                .append("_").append(componentName)
                .append("_").append(MONGO_PROPERTIES_SUFFIX)
                .append("_").append(clusterName);
        return configFile.toString();
    }
}
