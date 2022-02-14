package top.xiajibagao.mybatis.plus.join.injector.methods;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import top.xiajibagao.mybatis.plus.join.injector.JoinSqlMethod;

/**
 * @author huangchengxing
 * @date 2022/02/10 15:28
 */
public class SelectPageJoin extends SelectListJoin {

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        JoinSqlMethod method = JoinSqlMethod.SELECT_PAGE_JOIN;
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, getSql(method, tableInfo), modelClass);
        return addSelectMappedStatementForTable(mapperClass, method.getMethod(), sqlSource, tableInfo);
    }

}
