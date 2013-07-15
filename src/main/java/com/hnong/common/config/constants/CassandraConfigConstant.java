package com.hnong.common.config.constants;

import org.apache.commons.lang.StringUtils;

public final class CassandraConfigConstant {
    public static final String CASSANDRA_PROPERTIES_SUFFIX = "cassandra";

    public static final String CASSANDRA_HOST = "hosts";
    public static final String CASSANDRA_KEYSPACENAME = "strKeySpaceName";
    public static final String CASSANDRA_COLUMNFAMILYNAME = "strColumnFamilyName";
    public static final String CASSANDRA_MAX_ACTIVE = "maxActive";
    public static final String CASSANDRA_MAX_IDLE = "maxIdle";
    public static final String CASSANDRA_TIMESTAMP_RESOLUTION = "timestampResolution";
    public static final String CASSANDRA_THRIFT_SOCKETTIMEOUT = "cassandraThriftSocketTimeout";
    public static final String CASSANDRA_MAX_WAITIME_WHENEXHAUSTED = "maxWaitTimeWhenExhausted";
    public static final String CASSANDRA_USE_THRIFTFRAMEDTRANSPORT = "useThriftFramedTransport";
    public static final String CASSANDRA_EXHAUSTEDPOLICY = "exhaustedPolicy";
    public static final String CASSANDRA_BLOCK_MAX = "blockMax";
    public static final String CASSANDRA_NEED_GZIP = "needGzip";

    public static final String CASSANDRA_ITEM_HOST = "hnong_%s_cassandra_%s_" + CASSANDRA_HOST;
    public static final String CASSANDRA_ITEM_KEYSPACENAME = "hnong_%s_cassandra_%s_" + CASSANDRA_KEYSPACENAME;
    public static final String CASSANDRA_ITEM_COLUMNFAMILYNAME = "hnong_%s_cassandra_%s_" + CASSANDRA_COLUMNFAMILYNAME;
    public static final String CASSANDRA_ITEM_MAX_ACTIVE = "hnong_%s_cassandra_%s_" + CASSANDRA_MAX_ACTIVE;
    public static final String CASSANDRA_ITEM_MAX_IDLE = "hnong_%s_cassandra_%s_" + CASSANDRA_MAX_IDLE;
    public static final String CASSANDRA_ITEM_TIMESTAMP_RESOLUTION = "hnong_%s_cassandra_%s_" + CASSANDRA_TIMESTAMP_RESOLUTION;
    public static final String CASSANDRA_ITEM_THRIFT_SOCKETTIMEOUT = "hnong_%s_cassandra_%s_" + CASSANDRA_THRIFT_SOCKETTIMEOUT;
    public static final String CASSANDRA_ITEM_MAX_WAITIME_WHENEXHAUSTED = "hnong_%s_cassandra_%s_" + CASSANDRA_MAX_WAITIME_WHENEXHAUSTED;
    public static final String CASSANDRA_ITEM_USE_THRIFTFRAMEDTRANSPORT = "hnong_%s_cassandra_%s_" + CASSANDRA_USE_THRIFTFRAMEDTRANSPORT;
    public static final String CASSANDRA_ITEM_EXHAUSTEDPOLICY = "hnong_%s_cassandra_%s_" + CASSANDRA_EXHAUSTEDPOLICY;
    public static final String CASSANDRA_ITEM_BLOCK_MAX = "hnong_%s_cassandra_%s_" + CASSANDRA_BLOCK_MAX;
    public static final String CASSANDRA_ITEM_NEED_GZIP = "hnong_%s_cassandra_%s_" + CASSANDRA_NEED_GZIP;

    private CassandraConfigConstant() {
    }

    /**
     * Cassandra file name has format ${componentName}_cassandra_${clusterName}_${language}_${environment}.properties
     * Each cassandra cluster create a single config file
     *
     * @param componentName
     * @param clusterName
     * @return prefix file name
     */
    public static String getCassandraConfigFile(String componentName, String clusterName) {
        StringBuffer configFile = new StringBuffer();
        configFile.append(ConfigConstant.HNONG_CONFIG_PREFIX)
                .append("_").append(componentName)
                .append("_").append(CASSANDRA_PROPERTIES_SUFFIX)
                .append("_").append(clusterName);
        return configFile.toString();
    }

    /**
     * Get short item name, if fullName not matched with rules, log a error
     * @param fullName
     * @return short item name
     */
    public static String getItemShortName(String componentName, String clusterName, String fullName) {
        if (fullName == null || fullName.isEmpty()) {
            return StringUtils.EMPTY;
        }
        if (String.format(CASSANDRA_ITEM_HOST, componentName, clusterName).equals(fullName)) {
            return CASSANDRA_HOST;
        } else if (String.format(CASSANDRA_ITEM_KEYSPACENAME, componentName, clusterName).equals(fullName)) {
            return CASSANDRA_KEYSPACENAME;
        } else if (String.format(CASSANDRA_ITEM_COLUMNFAMILYNAME, componentName, clusterName).equals(fullName)) {
            return CASSANDRA_COLUMNFAMILYNAME;
        } else if (String.format(CASSANDRA_ITEM_MAX_ACTIVE, componentName, clusterName).equals(fullName)) {
            return CASSANDRA_MAX_ACTIVE;
        } else if (String.format(CASSANDRA_ITEM_MAX_IDLE, componentName, clusterName).equals(fullName)) {
            return CASSANDRA_MAX_IDLE;
        } else if (String.format(CASSANDRA_ITEM_TIMESTAMP_RESOLUTION, componentName, clusterName).equals(fullName)) {
            return CASSANDRA_TIMESTAMP_RESOLUTION;
        } else if (String.format(CASSANDRA_ITEM_THRIFT_SOCKETTIMEOUT, componentName, clusterName).equals(fullName)) {
            return CASSANDRA_THRIFT_SOCKETTIMEOUT;
        } else if (String.format(CASSANDRA_ITEM_MAX_WAITIME_WHENEXHAUSTED, componentName, clusterName).equals(fullName)) {
            return CASSANDRA_MAX_WAITIME_WHENEXHAUSTED;
        } else if (String.format(CASSANDRA_ITEM_USE_THRIFTFRAMEDTRANSPORT, componentName, clusterName).equals(fullName)) {
            return CASSANDRA_USE_THRIFTFRAMEDTRANSPORT;
        } else if (String.format(CASSANDRA_ITEM_EXHAUSTEDPOLICY, componentName, clusterName).equals(fullName)) {
            return CASSANDRA_EXHAUSTEDPOLICY;
        } else if (String.format(CASSANDRA_ITEM_BLOCK_MAX, componentName, clusterName).equals(fullName)) {
            return CASSANDRA_BLOCK_MAX;
        } else if (String.format(CASSANDRA_ITEM_NEED_GZIP, componentName, clusterName).equals(fullName)) {
            return CASSANDRA_NEED_GZIP;
        }
        // more cassandra config constant name match here
        return StringUtils.EMPTY;
    }
}
