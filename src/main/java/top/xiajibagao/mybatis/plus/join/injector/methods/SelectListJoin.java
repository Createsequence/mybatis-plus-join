package top.xiajibagao.mybatis.plus.join.injector.methods;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import top.xiajibagao.mybatis.plus.join.injector.AbstractJoinMethod;
import top.xiajibagao.mybatis.plus.join.injector.JoinSqlMethod;

/**
 * 查询列表
 *
 * @author huangchengxing
 * @date 2022/02/10 13:22
 */
public class SelectListJoin extends AbstractJoinMethod {

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        JoinSqlMethod method = JoinSqlMethod.SELECT_LIST_JOIN;
        String sql = getSql(method, tableInfo);
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        return addSelectMappedStatementForTable(mapperClass, method.getMethod(), sqlSource, tableInfo);
    }

    public String getSql(JoinSqlMethod sqlMethod, TableInfo tableInfo) {
        return String.format(
            sqlMethod.getSql(),
            sqlFirst(),
            sqlSelectColumns(tableInfo, true),
            sqlFromTable(),
            sqlJoin(),
            sqlWhereEntityWrapper(true, tableInfo),
            sqlComment()
        );
    }

}
