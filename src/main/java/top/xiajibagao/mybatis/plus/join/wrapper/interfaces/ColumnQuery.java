package top.xiajibagao.mybatis.plus.join.wrapper.interfaces;

import com.baomidou.mybatisplus.core.conditions.ISqlSegment;
import top.xiajibagao.mybatis.plus.join.constants.Condition;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * 基于{@link ColumnSegment}的查询条件
 *
 * @param <T> 主表字段入参类型
 * @param <R> 返回值字段入参类型
 * @param <P> 入参类型的父类
 * @param <C> 实现类
 * @author huangchengxing
 * @date 2022/02/10 16:58
 */
public interface ColumnQuery<P, T extends P, R extends P, C extends FuncColumnSelect<P, T, R, C>> {

    /**
     * 查询字段
     *
     * @param column 字段
     * @return C
     * @author huangchengxing
     * @date 2022/2/10 15:47
     */
    C select(ColumnSegment column);

    /**
     * 查询字段
     *
     * @param column 字段
     * @param alisa 字段别名
     * @return C
     * @author huangchengxing
     * @date 2022/2/10 15:47
     */
    default C select(ColumnSegment column, R alisa) {
        column.setAlisa(toStringColumn(alisa));
        return select(column);
    }

    /**
     * 查询字段
     *
     * @param column 字段
     * @return C
     * @author huangchengxing
     * @date 2022/2/10 15:47
     */
    default C select(T column, R alisa) {
        return select(toTableColumn(column, alisa));
    }

    /**
     * 查询字段
     *
     * @param column 字段
     * @return C
     * @author huangchengxing
     * @date 2022/2/10 15:47
     */
    default C select(T column) {
        return select(toTableColumn(column, null));
    }

    /**
     * where条件
     *
     * @param column 表字段
     * @param condition 条件
     * @param valueColumn 值字段
     * @return C
     * @author huangchengxing
     * @date 2022/2/11 9:25
     */
    C where(ColumnSegment column, Condition condition, ISqlSegment valueColumn);

    /**
     * where条件
     *
     * @param column 表字段
     * @param condition 条件
     * @param value 值
     * @return C
     * @author huangchengxing
     * @date 2022/2/11 9:25
     */
    default C where(ColumnSegment column, Condition condition, Object value) {
        return where(column, condition, value::toString);
    }

    /**
     * where条件
     *
     * @param column 表字段
     * @param condition 条件
     * @param value 值
     * @return C
     * @author huangchengxing
     * @date 2022/2/11 9:25
     */
    default C where(T column, Condition condition, Object value) {
        return where(toTableColumn(column), condition, value::toString);
    }

    /**
     * having条件
     *
     * @param column 表字段
     * @param condition 条件
     * @param valueColumn 值字段
     * @return C
     * @author huangchengxing
     * @date 2022/2/11 9:25
     */
    C having(ColumnSegment column, Condition condition, ISqlSegment valueColumn);

    /**
     * having条件
     *
     * @param column 表字段
     * @param condition 条件
     * @param value 值
     * @return C
     * @author huangchengxing
     * @date 2022/2/11 9:25
     */
    default C having(ColumnSegment column, Condition condition, Object value) {
        return having(column, condition, value::toString);
    }

    /**
     * having条件
     *
     * @param column 表字段
     * @param condition 条件
     * @param value 值
     * @return C
     * @author huangchengxing
     * @date 2022/2/11 9:25
     */
    default C having(T column, Condition condition, Object value) {
        return having(toTableColumn(column), condition, value::toString);
    }

    /**
     * 查询当前主表的全部字段
     *
     * @return C
     * @author huangchengxing
     * @date 2022/2/9 15:36
     */
    C selectAll();

    /**
     * 将指定字段组装为当前所属主表的{@link ColumnSegment}
     * <b>注意：所有需要表别名的字段，都需要通过对应Wrapper的该方法构建！</b>
     *
     * @param column 字段
     * @param alisa 别名，可能为null
     * @return com.xiajibagao.top.mybatis.plus.concatSegment.wrapper.interfaces.ColumnSegment
     * @author huangchengxing
     * @date 2022/2/10 15:42
     */
    ColumnSegment toTableColumn(@Nonnull T column, @Nullable R alisa);

    /**
     * 将指定字段组装为{@link ColumnSegment} <br />
     * <b>注意：所有需要表别名的字段，都需要通过对应Wrapper的该方法构建！</b>
     *
     * @param column 字段
     * @return com.xiajibagao.top.mybatis.plus.concatSegment.wrapper.interfaces.ColumnSegment
     * @author huangchengxing
     * @date 2022/2/10 15:42
     */
    default ColumnSegment toTableColumn(@Nonnull T column) {
        return toTableColumn(column, null);
    }

    /**
     * 将指定入参格式化为字符串
     *
     * @param column 字段
     * @return java.lang.String
     * @author huangchengxing
     * @date 2022/2/10 15:49
     */
    String toStringColumn(P column);

}
