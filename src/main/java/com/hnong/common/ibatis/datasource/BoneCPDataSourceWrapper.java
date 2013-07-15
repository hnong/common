package com.hnong.common.ibatis.datasource;

import com.jolbox.bonecp.BoneCPConfig;
import com.jolbox.bonecp.BoneCPDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

public class BoneCPDataSourceWrapper implements DataSource {
    public static final Log LOGGER = LogFactory.getLog(BoneCPDataSourceWrapper.class);

    private BoneCPDataSource dataSource;

    public BoneCPDataSourceWrapper() {
        BoneCPConfig config;
        try {
            config = new BoneCPConfig("hnong");
        } catch (Exception e) {
            LOGGER.warn("cannot init the bonecp-config.xml:" + e);
            config = new BoneCPConfig();
            config.setMaxConnectionsPerPartition(10);
            config.setMinConnectionsPerPartition(5);
            config.setPartitionCount(2);
            config.setAcquireIncrement(2);
            config.setReleaseHelperThreads(0);
            config.setConnectionTestStatement("/* ping */ SELECT 1");
            config.setIdleConnectionTestPeriod(240);
            config.setIdleMaxAge(60);
        }
        this.dataSource = new BoneCPDataSource(config);
        LOGGER.warn(dataSource.getConfig().toString());
    }

    public Connection getConnection() throws SQLException {
        long step0 = System.currentTimeMillis();
        Connection connection = dataSource.getConnection();
        long step1 = System.currentTimeMillis();
        if ((step1 - step0) > 500) {
            LOGGER.warn("getConnection too slow:" + (step1 - step0) + ",leased connections:" + dataSource.getTotalLeased());
        }
        return connection;
    }

    public Connection getConnection(String username, String password) throws SQLException {
        return dataSource.getConnection(username, password);
    }

    public PrintWriter getLogWriter() throws SQLException {
        return dataSource.getLogWriter();
    }

    public int getLoginTimeout() throws SQLException {
        return dataSource.getLoginTimeout();
    }

    public void setLogWriter(PrintWriter out) throws SQLException {
        dataSource.setLogWriter(out);
    }

    public void setLoginTimeout(int seconds) throws SQLException {
        dataSource.setLoginTimeout(seconds);
    }

    public synchronized void setDriver(String driver) {
        try {
            dataSource.setDriverClass(driver);
        } catch (Exception ignored) {
            LOGGER.error("Failed to set drive:" + ignored.getMessage());
        }
    }

    public void setUrl(String url) {
        dataSource.setJdbcUrl(url);
        LOGGER.info("the pool's url is: " + url);
    }

    public void setUsername(String username) {
        dataSource.setUsername(username);
    }

    public void setPassword(String password) {
        dataSource.setPassword(password);
    }

    public void setPartitionCount(int count) {
        dataSource.setPartitionCount(count);
    }

    public void setMinPoolSize(int min) {
        dataSource.setMinConnectionsPerPartition(min);
        LOGGER.info("the pool's min pool size is: " + min);
    }

    public void setMaxPoolSize(int max) {
        dataSource.setMaxConnectionsPerPartition(max);
        LOGGER.info("the pool's max pool size is: " + max);
    }

    public void setIdleConnectionTestPeriod(long period) {
        dataSource.setIdleConnectionTestPeriod(period);
    }

    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        throw new UnsupportedOperationException();
    }
}