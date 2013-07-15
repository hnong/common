package com.hnong.common.ibatis;

import com.hnong.common.config.Configuration;
import com.hnong.common.config.ConfigurationManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.io.IOException;
import java.io.Reader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class is a wrapper for the db operations between business data access layout and database.
 * It controls the ibatis databse connection resources based on the specified business component name.
 * Each business component should have individual ibatis config and properties files that contain the database connection information
 * Each business component connection only be created once time and cache after that.
 */
public final class SqlSessionFactoryBuilder {
    private static final Log LOG = LogFactory.getLog(SqlSessionFactoryBuilder.class);

    // each business component has it's own individual ibatis config file, e.g feed_ibatis.xml
    private static final String IBATIS_CONFIG_SUFFIX = "_ibatis.xml";

    // each business component has it's own individual ibatis properties file, e.g feed_ibatis.properties
    private static final String IBATIS_PROPERTIES_SUFFIX = "_ibatis";

    // singleton instance that maintain the global sql session
    private static SqlSessionFactoryBuilder instance = new SqlSessionFactoryBuilder();

    private static final Object LOCK = new Object();

    // cache to store tha instanced objects for sql session factories
    private final Map<String, SqlSessionFactory> sqlSessionFactoryMap = new ConcurrentHashMap<String, SqlSessionFactory>();

    private Reader reader;

    private SqlSessionFactoryBuilder() {
    }

    public static SqlSessionFactoryBuilder getInstance() {
        return instance;
    }

    public SqlSession getSqlSessionByEnvironment(String componentName, String environment) {
        return this.getSqlSession(componentName, environment, false);
    }

    public SqlSession getReaderSqlSessionByEnvironment(String componentName, String environment) {
        return this.getSqlSession(componentName, environment, true);
    }

    public SqlSession getBatchSqlSessionByEnvironment(String componentName, String environment) {
        return this.getBatchSqlSession(componentName, environment);
    }

    private SqlSession getSqlSession(String componentName, String environment, boolean isRead) {
        SqlSessionFactory sqlSessionFactory = this.getSqlSessionFactory(componentName, environment, isRead);
        long start = System.currentTimeMillis();
        SqlSession session = sqlSessionFactory.openSession(true);
        long end = System.currentTimeMillis();
        if (end - start > 100) {
            LOG.warn("open sql session:" + environment + " " + (end - start) + " millionSeconds");
        }
        return session;
    }

    private SqlSession getBatchSqlSession(String componentName, String environment) {
        SqlSessionFactory sqlSessionFactory = this.getSqlSessionFactory(componentName, environment, false);
        long start = System.currentTimeMillis();
        SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        long end = System.currentTimeMillis();
        if (end - start > 100) {
            LOG.warn("open sql session:" + environment + " " + (end - start) + " millionSeconds");
        }
        return session;
    }

    private SqlSessionFactory getSqlSessionFactory(String componentName, String environment, boolean isRead) {
        String ibatisConfigFile = componentName + IBATIS_CONFIG_SUFFIX;
        String ibatisConfigProperties = componentName + IBATIS_PROPERTIES_SUFFIX;
        // use componentName + "::" + environment as the key in void that environment conflict in the ibatis runtime
        String environmentKey = componentName + "::" + environment;

        Map<String, SqlSessionFactory> tempSqlSessionFactoryMap = sqlSessionFactoryMap;

        if (!tempSqlSessionFactoryMap.containsKey(environmentKey)) {
            synchronized (LOCK) {
                if (!tempSqlSessionFactoryMap.containsKey(environmentKey)) {
                    try {
                        LOG.info(String.format("build datasource:%s", environmentKey));
                        long start = System.currentTimeMillis();
                        org.apache.ibatis.session.SqlSessionFactoryBuilder builder = new org.apache.ibatis.session.SqlSessionFactoryBuilder();
                        reader = Resources.getResourceAsReader(ibatisConfigFile);
                        Configuration configuration = ConfigurationManager.getInstance().getByFileName(ibatisConfigProperties);
                        SqlSessionFactory factory = builder.build(reader, environment, configuration.toProperties());
                        LOG.info("Init database connection pool " + factory.getConfiguration().getVariables().toString());
                        tempSqlSessionFactoryMap.put(environmentKey, factory);

                        long end = System.currentTimeMillis();
                        if (end - start > 1000) {
                            LOG.info(String.format("build datasource %s cost %d ms", environmentKey, (end - start)));
                        }
                        return factory;
                    } catch (Exception e) {
                        e.printStackTrace();
                        LOG.error("build environment error " + environmentKey + " with error " + e.getMessage());
                        return null;
                    } finally {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            LOG.error(e.getMessage());
                        }
                    }
                }
            }
        }
        return tempSqlSessionFactoryMap.get(environmentKey);
    }
}
