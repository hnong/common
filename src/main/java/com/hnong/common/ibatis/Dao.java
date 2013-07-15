package com.hnong.common.ibatis;

import org.apache.ibatis.session.RowBounds;

import java.util.List;

public abstract class Dao {
    private SqlSessionWrapper sessionWrapper;

    public Dao(String componentName, String environment) {
        sessionWrapper = SqlSessionWrapper.getInstance(componentName, environment);
    }

    public SqlSessionWrapper getSessionWrapper() {
        return sessionWrapper;
    }

    protected Object selectOne(String statement) {
        return selectOne(statement, null);
    }

    protected Object selectOne(String statement, Object parameter) {
        String operationId = statement.substring(statement.lastIndexOf(".") + 1);
        if (!operationId.equals("get")) {
            List list = selectList(statement, parameter, RowBounds.DEFAULT);
            return list.size() == 0 ? null : list.get(0);
        } else {
            return sessionWrapper.selectOne(statement, parameter);
        }
    }

    protected List selectList(String statement) {
        return selectList(statement, null);
    }

    protected List selectList(String statement, Object parameter) {
        return selectList(statement, parameter, RowBounds.DEFAULT);
    }

    protected List selectList(String statement, Object parameter, RowBounds rowBounds) {
        return sessionWrapper.selectList(statement, parameter, rowBounds);
    }

    protected int insert(String statement) {
        return insert(statement, null);
    }

    protected int insert(String statement, Object parameter) {
        return sessionWrapper.insert(statement, parameter);
    }

    protected int update(String statement) {
        return update(statement, null);
    }

    protected int update(String statement, Object parameter) {
        return sessionWrapper.update(statement, parameter);
    }

    protected int delete(String statement) {
        return delete(statement, null);
    }

    protected int delete(String statement, Object parameter) {
        return sessionWrapper.delete(statement, parameter);
    }

    protected void initDataSource(int splitKey) {
    }
}
