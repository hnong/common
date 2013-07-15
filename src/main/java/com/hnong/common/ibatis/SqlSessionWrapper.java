package com.hnong.common.ibatis;

import org.apache.ibatis.exceptions.IbatisException;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public final class SqlSessionWrapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(SqlSessionWrapper.class);

    private static final String PRE_COMPONENT_NAME = "hnong_";
    private static ConcurrentHashMap<String, SqlSessionWrapper> instanceMap = new ConcurrentHashMap<String, SqlSessionWrapper>();

    private String componentName;
    private String environment;

    public static SqlSessionWrapper getInstance(String cName, String environment) {
        String componentName = getComponentName(cName);
        String key = componentName + "::" + environment;

        if (!instanceMap.containsKey(key)) {
            //ConcurrentHashMap.putIfAbsent会做同步处理，这里不需要加同步块了
            SqlSessionWrapper instance = new SqlSessionWrapper(componentName, environment);
            instanceMap.putIfAbsent(key, instance);
        }
        return instanceMap.get(key);
    }

    protected SqlSessionWrapper(String componentName, String environment) {
        this.componentName = componentName;
        this.environment = environment;
    }

    public Object selectOne(String statement, Object parameter) {
        SqlSession sqlSession = null;
        try {
            sqlSession = getSqlSession(true);
            return sqlSession.selectOne(statement, parameter);
        } catch (Throwable t) {
            LOGGER.error("select one {} statement error : ", statement, t);
            throw new IbatisException("selectOne error:" + statement);
        } finally {
            if (sqlSession != null) {
                sqlSession.close();
                LOGGER.debug("oops, close_session_4_{}", statement);
            }
        }
    }

    public List selectList(String statement, Object parameter, RowBounds rowBounds) {
        SqlSession sqlSession = null;
        try {
            sqlSession = getSqlSession(true);
            return sqlSession.selectList(statement, parameter, rowBounds);
        } catch (Throwable t) {
            LOGGER.error("selectList {} statement error : ", statement, t);
            throw new IbatisException("selectList error:" + statement);
        } finally {
            if (sqlSession != null) {
                sqlSession.close();
                LOGGER.debug("oops, close_session_4_{}", statement);
            }
        }
    }

    public int insert(String statement, Object parameter) {
        SqlSession sqlSession = null;
        try {
            sqlSession = getSqlSession(false);
            return sqlSession.insert(statement, parameter);
        } catch (Throwable t) {
            LOGGER.error("insert {} statement error : ", statement, t);
            throw new IbatisException("insert error:" + statement);
        } finally {
            if (sqlSession != null) {
                sqlSession.close();
                LOGGER.debug("oops, close_session_4_{}", statement);
            }
        }
    }

    public int update(String statement, Object parameter) {
        SqlSession sqlSession = null;
        try {
            sqlSession = getSqlSession(false);
            return sqlSession.update(statement, parameter);
        } catch (Throwable t) {
            LOGGER.error("update {} statement error : ", statement, t);
            throw new IbatisException("update error:" + statement);
        } finally {
            if (sqlSession != null) {
                sqlSession.close();
                LOGGER.debug("oops, close_session_4_{}", statement);
            }
        }
    }

    public int delete(String statement, Object parameter) {
        SqlSession sqlSession = null;
        try {
            sqlSession = getSqlSession(false);
            return sqlSession.delete(statement, parameter);
        } catch (Throwable t) {
            LOGGER.error("delete {} statement error : ", statement, t);
            throw new IbatisException("delete error:" + statement);
        } finally {
            if (sqlSession != null) {
                sqlSession.close();
                LOGGER.debug("oops, close_session_4_{}", statement);
            }
        }
    }

    public SqlSession getBatchSqlSession(boolean isReadOnly) {
        SqlSession sqlSession = null;
        try {
            sqlSession = SqlSessionFactoryBuilder.getInstance().getBatchSqlSessionByEnvironment(this.componentName, this.environment);
        } catch (Exception e) {
            LOGGER.error("get sqlSession error: {}", e.getMessage());
        }
        return sqlSession;
    }

    private SqlSession getSqlSession(boolean isRead) {
        SqlSession sqlSession = null;
        try {
            sqlSession = isRead ? SqlSessionFactoryBuilder.getInstance().getReaderSqlSessionByEnvironment(this.componentName, this.environment)
                    : SqlSessionFactoryBuilder.getInstance().getSqlSessionByEnvironment(this.componentName, this.environment);
        } catch (Exception e) {
            LOGGER.error("get sqlSession error: {}", e.getMessage());
        }
        return sqlSession;
    }

    private static String getComponentName(String componentName) {
        if (!componentName.startsWith(PRE_COMPONENT_NAME)) {
            return PRE_COMPONENT_NAME + componentName;
        }
        return componentName;
    }
}
