package top.xiajibagao.mybatis.plus.join.wrapper.column;

import com.baomidou.mybatisplus.core.conditions.ISqlSegment;
import top.xiajibagao.mybatis.plus.join.constants.ExtendConstants;
import top.xiajibagao.mybatis.plus.join.constants.FuncKeyword;
import top.xiajibagao.mybatis.plus.join.wrapper.interfaces.ColumnQuery;
import top.xiajibagao.mybatis.plus.join.wrapper.interfaces.ColumnSegment;
import top.xiajibagao.mybatis.plus.join.wrapper.interfaces.TableSegment;

import javax.validation.constraints.NotNull;

/**
 * 字段工厂
 *
 * @author huangchengxing
 * @date 2022/02/10 11:21
 */
public class Columns {

    public static TableColumn tableColumn(@NotNull TableSegment tableSegment, @NotNull String column) {
        return new TableColumn(tableSegment, column);
    }

    // ============================= 运算 =============================

    public static ArithmeticColumn plus(@NotNull ColumnSegment left, @NotNull ISqlSegment right) {
        return ArithmeticColumn.plus(left, right);
    }

    public static ArithmeticColumn plus(@NotNull ColumnSegment left, @NotNull Object right) {
        return ArithmeticColumn.plus(left, right::toString);
    }

    public static ArithmeticColumn sub(@NotNull ColumnSegment left, @NotNull ISqlSegment right) {
        return ArithmeticColumn.sub(left, right);
    }

    public static ArithmeticColumn sub(@NotNull ColumnSegment left, @NotNull Object right) {
        return ArithmeticColumn.sub(left, right::toString);
    }

    public static ArithmeticColumn mul(@NotNull ColumnSegment left, @NotNull ISqlSegment right) {
        return ArithmeticColumn.mul(left, right);
    }

    public static ArithmeticColumn mul(@NotNull ColumnSegment left, @NotNull Object right) {
        return ArithmeticColumn.mul(left, right::toString);
    }

    public static ArithmeticColumn div(@NotNull ColumnSegment left, @NotNull ISqlSegment right) {
        return ArithmeticColumn.div(left, right);
    }

    public static ArithmeticColumn div(@NotNull ColumnSegment left, @NotNull Object right) {
        return ArithmeticColumn.div(left, right::toString);
    }

    // ============================= 日期函数 =============================

    public static FuncColumn now() {
        return new FuncColumn(FuncKeyword.NOW);
    }

    public static FuncColumn currentTimestamp() {
        return new FuncColumn(FuncKeyword.CURRENT_TIMESTAMP);
    }

    public static FuncColumn currentDate() {
        return new FuncColumn(FuncKeyword.CURRENT_DATE);
    }

    public static FuncColumn currentTime() {
        return new FuncColumn(FuncKeyword.CURRENT_TIME);
    }

    public static FuncColumn dateFormat(@NotNull ColumnSegment column, @NotNull String formatter) {
        return new FuncColumn(FuncKeyword.DATE_FORMAT, column, () -> formatter);
    }

    public static FuncColumn day(@NotNull ColumnSegment column) {
        return new FuncColumn(FuncKeyword.DAY, column);
    }

    public static FuncColumn month(@NotNull ColumnSegment column) {
        return new FuncColumn(FuncKeyword.MONTH, column);
    }

    public static FuncColumn year(@NotNull ColumnSegment column) {
        return new FuncColumn(FuncKeyword.YEAR, column);
    }

    // ============================= 数学函数 =============================

    public static FuncColumn abs(@NotNull ColumnSegment column) {
        return new FuncColumn(FuncKeyword.ABS, column);
    }

    public static FuncColumn avg(@NotNull ColumnSegment column) {
        return new FuncColumn(FuncKeyword.AVG, column);
    }

    public static FuncColumn max(@NotNull ColumnSegment column) {
        return new FuncColumn(FuncKeyword.MAX, column);
    }

    public static FuncColumn min(@NotNull ColumnSegment column) {
        return new FuncColumn(FuncKeyword.MIN, column);
    }

    public static FuncColumn sum(@NotNull ColumnSegment column) {
        return new FuncColumn(FuncKeyword.SUM, column);
    }

    public static FuncColumn rand() {
        return new FuncColumn(FuncKeyword.RAND);
    }

    public static FuncColumn count(@NotNull ColumnSegment column) {
        return new FuncColumn(FuncKeyword.COUNT, column);
    }

    public static FuncColumn count() {
        return new FuncColumn(FuncKeyword.COUNT, () -> ExtendConstants.ASTERISK);
    }

    // ============================= 字符串函数 =============================

    public static FuncColumn ifNull(@NotNull ColumnSegment column, @NotNull ISqlSegment defaultValue) {
        return new FuncColumn(FuncKeyword.IFNULL, column, defaultValue);
    }

    public static FuncColumn ifNull(@NotNull ColumnSegment column, Object defaultValue) {
        return new FuncColumn(FuncKeyword.IFNULL, column, defaultValue::toString);
    }

    public static FuncColumn concat(@NotNull ISqlSegment... appendValues) {
        return new FuncColumn(FuncKeyword.CONCAT, appendValues);
    }

    public static FuncColumn format(@NotNull Number number, @NotNull Integer accuracy) {
        return new FuncColumn(FuncKeyword.FORMAT, number::toString, accuracy::toString);
    }

    public static FuncColumn replace(@NotNull ColumnSegment column, @NotNull String oldStr, @NotNull String newStr) {
        return new FuncColumn(FuncKeyword.REPLACE, column, () -> oldStr, () -> newStr);
    }

    public static FuncColumn lower(@NotNull ColumnSegment column) {
        return new FuncColumn(FuncKeyword.LOWER, column);
    }

    public static FuncColumn upper(@NotNull ColumnSegment column) {
        return new FuncColumn(FuncKeyword.UPPER, column);
    }

    public static <T extends ColumnQuery<?, ?, ?, ?>> CaseColumn<T> caseByCondition(ColumnSegment column, @NotNull T table) {
        return new CaseColumn<>(false, table, column);
    }

    public static <T extends ColumnQuery<?, ?, ?, ?>> CaseColumn<T> caseByValue(ColumnSegment column, @NotNull T table) {
        return new CaseColumn<>(true, table, column);
    }

}
