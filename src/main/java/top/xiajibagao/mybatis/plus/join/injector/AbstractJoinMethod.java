package top.xiajibagao.mybatis.plus.join.injector;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import top.xiajibagao.mybatis.plus.join.constants.ExtendConstants;

/**
 * @author huangchengxing
 * @date 2022/02/10 13:47
 */
public abstract class AbstractJoinMethod extends AbstractMethod implements ExtendConstants {

    /**
     * 获取join的sql
     *
     * @return java.lang.String
     * @author huangchengxing
     * @date 2022/2/10 14:32
     */
    protected String sqlJoin() {
        return SqlScriptUtils.convertIf(
            String.format(ExtendConstants.MP_PLACEHOLDER, Q_WRAPPER_SQL_JOIN), WRAPPER_HAS_JOIN, true
        );
    }

    /**
     * 获取表名及别名
     *
     * @return java.lang.String
     * @author huangchengxing
     * @date 2022/2/10 14:31
     */
    protected String sqlFromTable() {
        return String.format(ExtendConstants.MP_PLACEHOLDER + NEWLINE, Q_WRAPPER_SQL_TABLE_WITH_ALISA);
    }
    
    /**
     * 拼接where条件 <br />
     * 与父类方法区别在于不拼接Entity相关条件与逻辑删除字段条件。
     *
     * @param newLine 是否换行
     * @param table 表信息
     * @return java.lang.String
     * @author huangchengxing
     * @date 2022/2/10 14:32
     */
    @Override
    protected String sqlWhereEntityWrapper(boolean newLine, TableInfo table) {
        String sqlSegment = String.format("${%s}", WRAPPER_SQLSEGMENT);
        String sqlScript = SqlScriptUtils.convertChoose(
            String.format("%s", WRAPPER_NONEMPTYOFWHERE),
            SqlScriptUtils.convertWhere(sqlSegment),
            sqlSegment
        );
        sqlScript = SqlScriptUtils.convertIf(
            sqlScript,
            String.format("%s != null and %s != null and %s != ''", WRAPPER, WRAPPER_SQLSEGMENT, WRAPPER_SQLSEGMENT),
            true
        );
        return newLine ? NEWLINE + sqlScript + NEWLINE: sqlScript;
    }

    /**
     * 拼接查询字段 <br />
     * 与父类方法区别在于为字段添加了表别名
     *
     * @param table 表信息
     * @param queryWrapper 是否为使用 queryWrapper 查询
     * @return java.lang.String
     * @author huangchengxing
     * @date 2022/2/10 14:45
     */
    @Override
    protected String sqlSelectColumns(TableInfo table, boolean queryWrapper) {
        /* 假设存在用户自定义的 resultMap 映射返回 */
        String selectColumns = String.format("${%s}.%s", Q_WRAPPER_SQL_TABLE_IF_NON_ALISA, ASTERISK);
        if (!queryWrapper) {
            return selectColumns;
        }
        return SqlScriptUtils.convertChoose(
            String.format("%s != null and %s != null and %s != ''", WRAPPER, Q_WRAPPER_SQL_SELECT, Q_WRAPPER_SQL_SELECT),
            SqlScriptUtils.unSafeParam(Q_WRAPPER_SQL_SELECT), selectColumns
        );
    }
}
