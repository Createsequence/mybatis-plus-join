package top.xiajibagao.mybatis.plus.join.injector.methods;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import top.xiajibagao.mybatis.plus.join.injector.JoinSqlMethod;

import java.util.Map;

/**
 * @author huangchengxing
 * @date 2022/02/14 14:55
 */
public class SelectMapsJoin extends SelectListJoin {

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        JoinSqlMethod method = JoinSqlMethod.SELECT_MAPS;
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, getSql(method, tableInfo), modelClass);
        return addSelectMappedStatementForOther(mapperClass, method.getMethod(), sqlSource, Map.class);
    }

}
