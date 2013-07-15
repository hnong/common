package com.hnong.common.ibatis.plugin;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.*;

@Intercepts({@Signature(type = Executor.class, method = "query",
        args = { MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class }) })
public class QueryInterceptor implements Interceptor {
    private static final Log LOGGER = LogFactory.getLog(QueryInterceptor.class);

    public Object intercept(Invocation invocation) throws Throwable {
        Object[] queryArgs = invocation.getArgs();
        MappedStatement ms = (MappedStatement) queryArgs[0];
        Object parameter = queryArgs[1];
        //mysql的分页查询，默认是通过游标实现，query("select * from tb_name ",5,10)==>query("select * from tb_name limit 5,10",0,0);
        final RowBounds rowBounds = (RowBounds) queryArgs[2];
        int offset = rowBounds.getOffset();
        int limit = rowBounds.getLimit();
        BoundSql boundSql = ms.getBoundSql(parameter);
        String sql = boundSql.getSql().trim();
        if (offset != RowBounds.NO_ROW_OFFSET || limit != RowBounds.NO_ROW_LIMIT) {
            if (offset > 0) {
                sql += " limit " + offset + "," + limit;
            } else {
                sql += " limit " + limit;
            }
            offset = RowBounds.NO_ROW_OFFSET;
            limit = RowBounds.NO_ROW_LIMIT;

            queryArgs[2] = new RowBounds(offset, limit);
            BoundSql newBoundSql = new BoundSql(ms.getConfiguration(), sql, boundSql.getParameterMappings(), boundSql.getParameterObject());
            MappedStatement newMs = copyFromMappedStatement(ms, new BoundSqlSqlSource(newBoundSql));
            queryArgs[0] = newMs;
        }
        long start = System.currentTimeMillis();
        Object object = invocation.proceed();
        long end = System.currentTimeMillis();
        LOGGER.debug(sql + " time:" + (end - start) + "ms");
        return object;
    }

    private MappedStatement copyFromMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource, ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        builder.keyProperty(ms.getKeyProperty());
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.cache(ms.getCache());
        return builder.build();
    }

    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

    public void setProperties(Properties properties) {
    }

    public static class BoundSqlSqlSource implements SqlSource {
        BoundSql boundSql;

        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }
    }
}
