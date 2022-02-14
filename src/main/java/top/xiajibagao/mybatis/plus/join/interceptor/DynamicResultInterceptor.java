package top.xiajibagao.mybatis.plus.join.interceptor;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ClassUtil;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;
import top.xiajibagao.mybatis.plus.join.helper.StatementResultParser;
import top.xiajibagao.mybatis.plus.join.wrapper.AbstractDynamicResultWrapper;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 将查询方法的返回值替换为指定的类型
 *
 * @author huangchengxing 
 * @date 2021/12/28 17:44
 */
@Intercepts(@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}))
@Component
public class DynamicResultInterceptor implements Interceptor {

    /**
     * 数据源绑定配置-方法声明id-方法声明实例缓存
     */
    private static final Map<Configuration, Map<String, MappedStatement>> REDIRECTED_STATEMENT_CACHE = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        if (!(args[0] instanceof MappedStatement)) {
            return invocation.proceed();
        }

        // 无参数
        MappedStatement statement = (MappedStatement) args[0];

        if (!(args[1] instanceof Map)) {
            return invocation.proceed();
        }
        // 若有参数且返回值不为空，将查询返回结果动态替换为新类型
        Map<String, Object> params = (Map<String, Object>) args[1];
        if (CollUtil.isNotEmpty(params) && CollUtil.isNotEmpty(statement.getResultMaps())) {
            redirectMappedStatementResult(args, statement, params);
        }

        return invocation.proceed();
    }
    
    /**
     * 创建新的方法声明，返回值为指定的自定义类型
     *
     * @param args 入参数组
     * @param originStatement 旧方法声明
     * @param params 查询参数
     * @author huangchengxing
     * @date 2021/12/29 12:06
     */
    private void redirectMappedStatementResult(Object[] args, MappedStatement originStatement, Map<String, Object> params) {
        // count、exists这类返回基本类型的不做处理
        Class<?> resultType = originStatement.getResultMaps().get(0).getType();
        if (ClassUtil.isBasicType(resultType)) {
            return;
        }
        params.values().stream()
            .filter(AbstractDynamicResultWrapper.class::isInstance)
            .findFirst()
            .map(AbstractDynamicResultWrapper.class::cast)
            .map(wrapper -> convertStatement(originStatement, wrapper.getResultClass()))
            .ifPresent(newStatement -> args[0] = newStatement);
    }

    /**
     * 基于原有方法声明，创建一个新的方法声明，该方法声明中将旧返回值类型替换JoinWrapper的返回值
     *
     * @param originStatement 旧方法声明
     * @param originResultType 旧返回值
     * @return org.apache.ibatis.mapping.MappedStatement
     * @author huangchengxing
     * @date 2021/12/28 17:49
     */
    private MappedStatement convertStatement(MappedStatement originStatement, Class<?> originResultType) {
        String id = originStatement.getId() + "_dynamic_result_" + originResultType.getSimpleName();
        Map<String, MappedStatement> mappedCached = REDIRECTED_STATEMENT_CACHE.computeIfAbsent(
            originStatement.getConfiguration(), config -> new ConcurrentHashMap<>()
        );
        return mappedCached.computeIfAbsent(id, statementId -> {
            MappedStatement.Builder statementBuilder = new MappedStatement.Builder(
                originStatement.getConfiguration(), statementId, originStatement.getSqlSource(), originStatement.getSqlCommandType()
            );
            // 除返回值外其他配置不改变
            statementBuilder.resource(originStatement.getResource())
                .fetchSize(originStatement.getFetchSize())
                .statementType(originStatement.getStatementType())
                .keyGenerator(originStatement.getKeyGenerator())
                .timeout(originStatement.getTimeout())
                .parameterMap(originStatement.getParameterMap())
                .resultSetType(originStatement.getResultSetType())
                .cache(originStatement.getCache())
                .flushCacheRequired(originStatement.isFlushCacheRequired())
                .useCache(originStatement.isUseCache());
            StatementResultParser<?> parser = StatementResultParser.parse(originStatement.getConfiguration(), originResultType);
            statementBuilder.resultMaps(Collections.singletonList(parser.getResultMap()));
            return statementBuilder.build();
        });
    }

}