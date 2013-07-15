package com.hnong.common.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;


/**
 * @author yunchat
 */
public class BaseDao<T> extends Dao {

    /**
     * @param componentName
     * @param environment
     */
    public BaseDao(String componentName, String environment) {
        super(componentName, environment);
    }

    protected List<T> queryForList(String statement) {
        return selectList(statement, null);
    }

    protected List<T> queryForList(String statement, Object parameter) {
        return selectList(statement, parameter);
    }

    protected List<T> queryForList(String statement, Object parameter,
            RowBounds rowBounds) {
        return selectList(statement, parameter, rowBounds);
    }

    protected T queryForObject(String statement) {
        return (T) super.selectOne(statement);
    }

    protected T queryForObject(String statement, Object parameter) {
        return (T) super.selectOne(statement, parameter);
    }
}
