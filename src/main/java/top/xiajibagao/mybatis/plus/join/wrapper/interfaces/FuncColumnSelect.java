package top.xiajibagao.mybatis.plus.join.wrapper.interfaces;

import com.baomidou.mybatisplus.core.conditions.ISqlSegment;
import top.xiajibagao.mybatis.plus.join.constants.ExtendConstants;
import top.xiajibagao.mybatis.plus.join.constants.FuncKeyword;
import top.xiajibagao.mybatis.plus.join.wrapper.column.ArithmeticColumn;
import top.xiajibagao.mybatis.plus.join.wrapper.column.CaseColumn;
import top.xiajibagao.mybatis.plus.join.wrapper.column.FuncColumn;

/**
 * 基于{@link ColumnQuery}，提供使用内置函数的字段查询。该接口主要用于提供默认实现
 *
 * @param <T> 主表字段入参类型
 * @param <R> 返回值字段入参类型
 * @param <P> 入参类型的父类
 * @param <C> 实现类
 * @author huangchengxing
 * @date 2022/02/10 11:13
 */
public interface FuncColumnSelect<P, T extends P, R extends P, C extends FuncColumnSelect<P, T, R, C>>
    extends ColumnQuery<P, T, R, C> {

    // ============================= 运算 =============================

    /**
     * 效果同：<code>(left + right) concatAs alisa</code>
     *
     * @param left 左值
     * @param right 右值
     * @param alisa 别名
     * @return C
     * @author huangchengxing
     * @date 2022/2/11 15:31
     */
    default C plus(T left, R alisa, ISqlSegment right) {
        ColumnSegment columnSegment = toTableColumn(left, alisa);
        ArithmeticColumn column = ArithmeticColumn.plus(columnSegment, right);
        column.setAlisa(column.getAlisa());
        return select(column);
    }

    /**
     * 效果同：<code>(left - right) concatAs alisa</code>
     *
     * @param left 左值
     * @param right 右值
     * @param alisa 别名
     * @return C
     * @author huangchengxing
     * @date 2022/2/11 15:31
     */
    default C sub(T left, R alisa, ISqlSegment right) {
        ColumnSegment columnSegment = toTableColumn(left, alisa);
        ArithmeticColumn column = ArithmeticColumn.sub(columnSegment, right);
        column.setAlisa(column.getAlisa());
        return select(column);
    }

    /**
     * 效果同：<code>(left * right) concatAs alisa</code>
     *
     * @param left 左值
     * @param right 右值
     * @param alisa 别名
     * @return C
     * @author huangchengxing
     * @date 2022/2/11 15:31
     */
    default C mul(T left, R alisa, ISqlSegment right) {
        ColumnSegment columnSegment = toTableColumn(left, alisa);
        ArithmeticColumn column = ArithmeticColumn.mul(columnSegment, right);
        column.setAlisa(column.getAlisa());
        return select(column);
    }

    /**
     * 效果同：<code>(left / right) concatAs alisa</code>
     *
     * @param left 左值
     * @param right 右值
     * @param alisa 别名
     * @return C
     * @author huangchengxing
     * @date 2022/2/11 15:31
     */
    default C div(T left, R alisa, ISqlSegment right) {
        ColumnSegment columnSegment = toTableColumn(left, alisa);
        ArithmeticColumn column = ArithmeticColumn.div(columnSegment, right);
        column.setAlisa(column.getAlisa());
        return select(column);
    }

    // ============================= 日期函数 =============================

    /**
     * 效果同：<code>NOW() concatAs alisa</code>
     *
     * @param alisa 别名
     * @return C
     * @author huangchengxing
     * @date 2022/2/11 15:31
     */
    default C now(R alisa) {
        FuncColumn column = new FuncColumn(FuncKeyword.NOW);
        column.setAlisa(toStringColumn(alisa));
        return select(column);
    }

    /**
     * 效果同：<code>CURRENT_TIMESTAMP() concatAs alisa</code>
     *
     * @param alisa 别名
     * @return C
     * @author huangchengxing
     * @date 2022/2/11 15:31
     */
    default C currentTimestamp(R alisa) {
        FuncColumn column = new FuncColumn(FuncKeyword.CURRENT_TIMESTAMP);
        column.setAlisa(toStringColumn(alisa));
        return select(column);
    }

    /**
     * 效果同：<code>CURRENT_DATE() concatAs alisa</code>
     *
     * @param alisa 别名
     * @return C
     * @author huangchengxing
     * @date 2022/2/11 15:31
     */
    default C currentDate(R alisa) {
        FuncColumn column = new FuncColumn(FuncKeyword.CURRENT_DATE);
        column.setAlisa(toStringColumn(alisa));
        return select(column);
    }

    /**
     * 效果同：<code>CURRENT_TIME() concatAs alisa</code>
     *
     * @param alisa 别名
     * @return C
     * @author huangchengxing
     * @date 2022/2/11 15:31
     */
    default C currentTime(R alisa) {
        FuncColumn column = new FuncColumn(FuncKeyword.CURRENT_TIME);
        column.setAlisa(toStringColumn(alisa));
        return select(column);
    }

    /**
     * 效果同：<code>DATE_FORMAT(column, formatter) concatAs alisa</code>
     *
     * @param column 字段
     * @param alisa 别名
     * @return C
     * @author huangchengxing
     * @date 2022/2/11 15:31
     */
    default C dateFormat(T column, R alisa, String formatter) {
        FuncColumn funcColumn = new FuncColumn(FuncKeyword.DATE_FORMAT, toTableColumn(column, alisa), () -> formatter);
        funcColumn.setAlisa(funcColumn.getAlisa());
        return select(funcColumn);
    }

    /**
     * 效果同：<code>DAY(column) concatAs alisa</code>
     *
     * @param column 字段
     * @param alisa 别名
     * @return C
     * @author huangchengxing
     * @date 2022/2/11 15:31
     */
    default C day(T column, R alisa) {
        FuncColumn funcColumn = new FuncColumn(FuncKeyword.DAY, toTableColumn(column, alisa));
        funcColumn.setAlisa(funcColumn.getAlisa());
        return select(funcColumn);
    }

    /**
     * 效果同：<code>MONTH(column) concatAs alisa</code>
     *
     * @param column 字段
     * @param alisa 别名
     * @return C
     * @author huangchengxing
     * @date 2022/2/11 15:31
     */
    default C month(T column, R alisa) {
        FuncColumn funcColumn = new FuncColumn(FuncKeyword.MONTH, toTableColumn(column, alisa));
        funcColumn.setAlisa(funcColumn.getAlisa());
        return select(funcColumn);
    }

    /**
     * 效果同：<code>YEAR(column) concatAs alisa</code>
     *
     * @param column 字段
     * @param alisa 别名
     * @return C
     * @author huangchengxing
     * @date 2022/2/11 15:31
     */
    default C year(T column, R alisa) {
        FuncColumn funcColumn = new FuncColumn(FuncKeyword.YEAR, toTableColumn(column, alisa));
        funcColumn.setAlisa(funcColumn.getAlisa());
        return select(funcColumn);
    }

    // ============================= 数学函数 =============================

    /**
     * 效果同：<code>ABS(column) concatAs alisa</code>
     *
     * @param column 字段
     * @param alisa 别名
     * @return C
     * @author huangchengxing
     * @date 2022/2/11 15:31
     */
    default C abs(T column, R alisa) {
        ColumnSegment columnSegment = toTableColumn(column, alisa);
        FuncColumn funcColumn = new FuncColumn(FuncKeyword.ABS, columnSegment);
        funcColumn.setAlisa(columnSegment.getAlisa());
        return select(funcColumn);
    }

    /**
     * 效果同：<code>AVG(column) concatAs alisa</code>
     *
     * @param column 字段
     * @param alisa 别名
     * @return C
     * @author huangchengxing
     * @date 2022/2/11 15:31
     */
    default C avg(T column, R alisa) {
        ColumnSegment columnSegment = toTableColumn(column, alisa);
        FuncColumn funcColumn = new FuncColumn(FuncKeyword.AVG, columnSegment);
        funcColumn.setAlisa(columnSegment.getAlisa());
        return select(funcColumn);
    }

    /**
     * 效果同：<code>MAX(column) concatAs alisa</code>
     *
     * @param column 字段
     * @param alisa 别名
     * @return C
     * @author huangchengxing
     * @date 2022/2/11 15:31
     */
    default C max(T column, R alisa) {
        ColumnSegment columnSegment = toTableColumn(column, alisa);
        FuncColumn funcColumn = new FuncColumn(FuncKeyword.MAX, columnSegment);
        funcColumn.setAlisa(columnSegment.getAlisa());
        return select(funcColumn);
    }

    /**
     * 效果同：<code>MIN(column) concatAs alisa</code>
     *
     * @param column 字段
     * @param alisa 别名
     * @return C
     * @author huangchengxing
     * @date 2022/2/11 15:31
     */
    default C min(T column, R alisa) {
        ColumnSegment columnSegment = toTableColumn(column, alisa);
        FuncColumn funcColumn = new FuncColumn(FuncKeyword.MIN, columnSegment);
        funcColumn.setAlisa(columnSegment.getAlisa());
        return select(funcColumn);
    }

    /**
     * 效果同：<code>SUM(column) concatAs alisa</code>
     *
     * @param column 字段
     * @param alisa 别名
     * @return C
     * @author huangchengxing
     * @date 2022/2/11 15:31
     */
    default C sum(T column, R alisa) {
        ColumnSegment columnSegment = toTableColumn(column, alisa);
        FuncColumn funcColumn = new FuncColumn(FuncKeyword.SUM, columnSegment);
        funcColumn.setAlisa(columnSegment.getAlisa());
        return select(funcColumn);
    }

    /**
     * 效果同：<code>RAND() concatAs alisa</code>
     *
     * @param alisa 别名
     * @return C
     * @author huangchengxing
     * @date 2022/2/11 15:31
     */
    default C rand(R alisa) {
        FuncColumn funcColumn = new FuncColumn(FuncKeyword.RAND);
        funcColumn.setAlisa(toStringColumn(alisa));
        return select(funcColumn);
    }

    /**
     * 效果同：<code>COUNT(column) concatAs alisa</code>
     *
     * @param column 字段
     * @param alisa 别名
     * @return C
     * @author huangchengxing
     * @date 2022/2/11 15:31
     */
    default C count(T column, R alisa) {
        ColumnSegment columnSegment = toTableColumn(column, alisa);
        FuncColumn funcColumn = new FuncColumn(FuncKeyword.COUNT, columnSegment);
        funcColumn.setAlisa(columnSegment.getAlisa());
        return select(funcColumn);
    }

    /**
     * 效果同：<code>COUNT() concatAs alisa</code>
     *
     * @param alisa 别名
     * @return C
     * @author huangchengxing
     * @date 2022/2/11 15:31
     */
    default C count(R alisa) {
        FuncColumn funcColumn = new FuncColumn(FuncKeyword.COUNT, () -> ExtendConstants.ASTERISK);
        funcColumn.setAlisa(toStringColumn(alisa));
        return select(funcColumn);
    }

    // ============================= 字符串函数 =============================

    /**
     * 效果同：<code>CONCAT(appendValues1, appendValues2...) concatAs alisa</code>
     *
     * @param alisa 别名
     * @param appendValues 字段
     * @return C
     * @author huangchengxing
     * @date 2022/2/11 15:31
     */
    default C concat(R alisa, ISqlSegment... appendValues) {
        FuncColumn funcColumn = new FuncColumn(FuncKeyword.CONCAT, appendValues);
        funcColumn.setAlisa(toStringColumn(alisa));
        return select(funcColumn);
    }

    /**
     * 效果同：<code>FORMAT(number, accuracy) concatAs alisa</code>
     *
     * @param alisa 别名
     * @return C
     * @author huangchengxing
     * @date 2022/2/11 15:31
     */
    default C format(R alisa, Number number, Integer accuracy) {
        FuncColumn funcColumn = new FuncColumn(FuncKeyword.FORMAT, number::toString, accuracy::toString);
        funcColumn.setAlisa(toStringColumn(alisa));
        return select(funcColumn);
    }

    /**
     * 效果同：<code>REPLACE(column, oldStr, newStr) concatAs alisa</code>
     *
     * @param alisa 别名
     * @return C
     * @author huangchengxing
     * @date 2022/2/11 15:31
     */
    default C replace(T column, R alisa, String oldStr, String newStr) {
        ColumnSegment columnSegment = toTableColumn(column, alisa);
        FuncColumn funcColumn = new FuncColumn(FuncKeyword.REPLACE, columnSegment, () -> oldStr, () -> newStr);
        funcColumn.setAlisa(toStringColumn(alisa));
        return select(funcColumn);
    }

    /**
     * 效果同：<code>LOWER(column) concatAs alisa</code>
     *
     * @param column 字段
     * @param alisa 别名
     * @return C
     * @author huangchengxing
     * @date 2022/2/11 15:31
     */
    default C lower(T column, R alisa) {
        ColumnSegment columnSegment = toTableColumn(column, alisa);
        FuncColumn funcColumn = new FuncColumn(FuncKeyword.LOWER, columnSegment);
        funcColumn.setAlisa(columnSegment.getAlisa());
        return select(funcColumn);
    }

    /**
     * 效果同：<code>UPPER(column) concatAs alisa</code>
     *
     * @param column 字段
     * @param alisa 别名
     * @return C
     * @author huangchengxing
     * @date 2022/2/11 15:31
     */
    default C upper(T column, R alisa) {
        ColumnSegment columnSegment = toTableColumn(column, alisa);
        FuncColumn funcColumn = new FuncColumn(FuncKeyword.UPPER, columnSegment);
        funcColumn.setAlisa(columnSegment.getAlisa());
        return select(columnSegment);
    }

    // ============================= 控制流 =============================

    /**
     * 效果同：<code>IFNULL(column, defaultValue) concatAs alisa</code>
     *
     * @param column 字段
     * @param alisa 别名
     * @param defaultValue 默认值
     * @return C
     * @author huangchengxing
     * @date 2022/2/11 15:31
     */
    default C ifNull(T column, R alisa, ISqlSegment defaultValue) {
        ColumnSegment columnSegment = toTableColumn(column, alisa);
        FuncColumn funcColumn = new FuncColumn(FuncKeyword.IFNULL, columnSegment, defaultValue);
        funcColumn.setAlisa(columnSegment.getAlisa());
        return select(funcColumn);
    }

    /**
     * 效果同：<code>IFNULL(column, defaultColumn) concatAs alisa</code>
     *
     * @param column 字段
     * @param alisa 别名
     * @param defaultColumn 默认值
     * @return C
     * @author huangchengxing
     * @date 2022/2/11 15:31
     */
    default C ifNull(T column, R alisa, T defaultColumn) {
        ColumnSegment columnSegment = toTableColumn(column, alisa);
        FuncColumn funcColumn = new FuncColumn(FuncKeyword.IFNULL, columnSegment, toTableColumn(defaultColumn, null));
        funcColumn.setAlisa(columnSegment.getAlisa());
        return select(funcColumn);
    }

    /**
     * 查询根据字段值的匹配的case then函数。<br />
     * 效果同：
     * <code>
     *     (CASE column
     *     WHEM 1 THEN 'column is one'
     *     ELSE THEN 'column not one') concatAs alisa
     * </code>
     *
     * @param column 字段
     * @param alisa 别名
     * @return com.xiajibagao.top.mybatis.plus.concatSegment.wrapper.column.CaseColumn<C>
     * @author huangchengxing
     * @date 2022/2/11 15:24
     */
    default CaseColumn<C> caseByValue(T column, R alisa) {
        ColumnSegment columnSegment = toTableColumn(column, alisa);
        CaseColumn<C> caseColumn = new CaseColumn<>(true, (C) this, columnSegment);
        caseColumn.setAlisa(columnSegment.getAlisa());
        return caseColumn;
    }

    /**
     * 查询根据根据条件匹配的case then函数。<br />
     * 效果同：
     * <code>
     *     (CASE
     *     WHEM score > 60 THEN '及格'
     *     ELSE THEN '不及格') concatAs alisa
     * </code>
     *
     * @param alisa 别名
     * @return com.xiajibagao.top.mybatis.plus.concatSegment.wrapper.column.CaseColumn<C>
     * @author huangchengxing
     * @date 2022/2/11 15:24
     */
    default CaseColumn<C> caseByCondition(R alisa) {
        CaseColumn<C> caseColumn = new CaseColumn<>(false, (C) this, null);
        caseColumn.setAlisa(toStringColumn(alisa));
        return caseColumn;
    }

}
