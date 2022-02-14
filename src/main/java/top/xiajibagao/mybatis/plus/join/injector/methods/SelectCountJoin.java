package top.xiajibagao.mybatis.plus.join.injector.methods;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import top.xiajibagao.mybatis.plus.join.injector.AbstractJoinMethod;
import top.xiajibagao.mybatis.plus.join.injector.JoinSqlMethod;

/**
 * @author huangchengxing
 * @date 2022/02/10 13:25
 */
public class SelectCountJoin extends AbstractJoinMethod {

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        JoinSqlMethod method = JoinSqlMethod.SELECT_COUNT_JOIN;
        String sql = String.format(
            method.getSql(),
            sqlFirst(),
            sqlFromTable(),
            sqlJoin(),
            sqlWhereEntityWrapper(true, tableInfo),
            sqlComment()
        );
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        return addSelectMappedStatementForOther(mapperClass, method.getMethod(), sqlSource, Integer.class);
    }
}
