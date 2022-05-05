package top.xiajibagao.mybatis.plus.join.wrapper;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.ISqlSegment;
import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.baomidou.mybatisplus.core.enums.SqlLike;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import top.xiajibagao.mybatis.plus.join.constants.Condition;
import top.xiajibagao.mybatis.plus.join.constants.ExtendConstants;
import top.xiajibagao.mybatis.plus.join.helper.ColumnUtils;
import top.xiajibagao.mybatis.plus.join.helper.SqlUtils;
import top.xiajibagao.mybatis.plus.join.wrapper.column.TableColumn;
import top.xiajibagao.mybatis.plus.join.wrapper.interfaces.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 具有动态返回值的条件构造器 <br />
 * 注意：不支持直接传入实体的相关方法
 *
 * @author huangchengxing
 * @date 2022/02/09 10:00
 */
@Getter
@RequiredArgsConstructor
public abstract class AbstractDynamicResultWrapper<T, R, C extends AbstractDynamicResultWrapper<T, R, C>>
    extends AbstractWrapper<T, SFunction<T, ?>, C>
    implements TableSegment,
    PredicateCompare<C, SFunction<T, ?>>,
    PredicateFunc<C, SFunction<T, ?>>,
    FuncColumnSelect<SFunction<?, ?>, SFunction<T, ?>, SFunction<R, ?>, C> {

    /**
     * 查询主表别名与表信息
     */
    @Setter
    protected String alisa;
    protected TableInfo tableInfo;

    /**
     * 主表实体类型与返回实体类型
     */
    protected Class<T> targetClass;
    protected Class<R> resultClass;

    /**
     * 查询字段
     */
    protected List<ColumnSegment> selectColumns;

    /**
     * 查询主表是否为逻辑表
     */
    protected boolean isLogic;

    /**
     * 创建条件构造器
     *
     * @param targetClass 主表实体类型
     * @param resultClass 返回实体类型
     * @param isLogic 主表是否为逻辑表
     * @throws MybatisPlusException 当主表不为逻辑表，且找不到对应TableInfo时抛出
     * @author huangchengxing
     * @date 2022/2/9 15:04
     */
    protected AbstractDynamicResultWrapper(@Nonnull Class<T> targetClass, @Nonnull Class<R> resultClass, boolean isLogic) {
        this.targetClass = targetClass;
        this.resultClass = resultClass;
        // 临时的逻辑表没有对应实体信息
        this.isLogic = isLogic;
        if (isLogic) {
            this.alisa = ExtendConstants.EMPTY;
        } else {
            this.tableInfo = TableInfoHelper.getTableInfo(targetClass);
            Assert.notNull(this.tableInfo, "找不到类型[%s]对应的TableInfo缓存", targetClass.getName());
            this.alisa = tableInfo.getTableName();
        }
    }
    
    /**
     * 若主表不为逻辑表，则获取其逻辑删除字段并添加到条件中
     *
     * @author huangchengxing
     * @date 2022/2/10 9:21
     */
    protected void initLogicDelete() {
        if (isLogic || !tableInfo.isWithLogicDelete()) {
            return;
        }
        TableFieldInfo logicDeleteFieldInfo = tableInfo.getLogicDeleteFieldInfo();
        String notDeleteValue = tableInfo.getLogicDeleteFieldInfo().getLogicDeleteValue();
        whereIfNotNull(new TableColumn(this, logicDeleteFieldInfo.getColumn()), Condition.EQ, notDeleteValue);
    }

    // ============================== query ==============================

    /**
     * 查询全部字段，比如：“a.*”
     *
     * @return C
     * @author huangchengxing
     * @date 2022/2/9 15:36
     */
    @Override
    public C selectAll() {
        selectColumns.add(new TableColumn(this, ExtendConstants.ASTERISK));
        return typedThis;
    }

    /**
     * 查询字段
     *
     * @param column 字段
     * @return C
     * @author huangchengxing
     * @date 2022/2/9 16:23
     */
    @Override
    public C select(@Nonnull ColumnSegment column) {
        selectColumns.add(column);
        return typedThis;
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
    @Override
    public C where(boolean apply, ColumnSegment column, Condition condition, ISqlSegment valueColumn) {
        return condition.isHasNextParam() ?
            doIt(apply, column, condition, valueColumn) : doIt(true, column, column);
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
    @Override
    public C having(ColumnSegment column, Condition condition, ISqlSegment valueColumn) {
        return doIt(true, SqlKeyword.HAVING, column, condition, valueColumn);
    }

    // ============================== like ==============================

    /**
     * 模糊匹配，如“not like '%aaa'”
     *
     * @param condition 是否生效
     * @param column 字段
     * @param val 值
     * @return C
     * @author huangchengxing
     * @date 2022/2/10 9:27
     */
    @Override
    public C notLikeLeft(Predicate<String> condition, SFunction<T, ?> column, String val) {
        return likeValue(condition.test(val), SqlKeyword.NOT_LIKE, column, val, SqlLike.LEFT);
    }

    /**
     * 模糊匹配，如“not like 'aaa%'”
     *
     * @param condition 是否生效
     * @param column 字段
     * @param val 值
     * @return C
     * @author huangchengxing
     * @date 2022/2/10 9:27
     */
    @Override
    public C notLikeRight(Predicate<String> condition, SFunction<T, ?> column, String val) {
        return likeValue(condition.test(val), SqlKeyword.NOT_LIKE, column, val, SqlLike.RIGHT);
    }

    // ============================== limit ==============================
    
    /**
     * limit <br />
     * <b>注意：limit直接调用{@link #last(String)}方法，需要注意条件覆盖问题</b>
     *
     * @param condition 是否生效
     * @param limit 条数
     * @return C
     * @author huangchengxing
     * @date 2022/2/10 9:28
     */
    public C limit(boolean condition, int limit) {
        last(condition, SqlUtils.space(ExtendConstants.LIMIT, String.valueOf(limit)));
        return typedThis;
    }

    /**
     * limit <br />
     * <b>注意：limit直接调用{@link #last(String)}方法，需要注意条件覆盖问题</b>
     *
     * @param condition 是否生效
     * @param limit 条数
     * @param offset 偏移量
     * @return C
     * @author huangchengxing
     * @date 2022/2/10 9:28
     */
    public C limit(boolean condition, int limit, int offset) {
        last(condition, SqlUtils.space(ExtendConstants.LIMIT, String.valueOf(limit), String.valueOf(offset)));
        return typedThis;
    }

    // ============================== override ==============================

    /**
     * 获取主表名。若主表为逻辑表，则默认直接抛出异常，需要在子类重写该方法以兼容逻辑表的处理流程
     *
     * @throws IllegalArgumentException 当表为逻辑表时抛出
     * @return java.lang.String
     * @author huangchengxing
     * @date 2022/2/9 15:06
     */
    @Override
    public String getTable() {
        cn.hutool.core.lang.Assert.isFalse(isLogic, "逻辑表没有TableInfo");
        return tableInfo.getTableName();
    }

    /**
     * 将指定字段组装为当前所属主表的{@link ColumnSegment}
     *
     * @param column 字段
     * @param alisa 别名
     * @return com.xiajibagao.top.mybatis.plus.concatSegment.wrapper.interfaces.ColumnSegment
     * @author huangchengxing
     * @date 2022/2/10 16:24
     */
    @Override
    public ColumnSegment toTableColumn(SFunction<T, ?> column, SFunction<R, ?> alisa) {
        TableColumn tableColumn = new TableColumn(this, toStringColumn(column));
        if (Objects.nonNull(alisa)) {
            tableColumn.setAlisa(toStringColumn(alisa));
        }
        return tableColumn;
    }

    /**
     * 将lambda表达式转为表字段，用于查询条件，字段前带有表别名
     *
     * @throws IllegalArgumentException 表别名为null时抛出
     * @return java.lang.String
     * @author huangchengxing
     * @date 2022/2/9 15:06
     */
    @Override
    public String columnToString(SFunction<T, ?> column) {
        cn.hutool.core.lang.Assert.notNull(alisa, "表{}没有设置别名！", getTable());
        return alisa + ExtendConstants.DOT + ColumnUtils.getColumnName(column);
    }

    /**
     * 将lambda表达式转为字段，用于查询字段，字段前不带有表别名
     *
     * @return java.lang.String
     * @author huangchengxing
     * @date 2022/2/9 15:06
     */
    @Override
    public String toStringColumn(SFunction<?, ?> column) {
        return ColumnUtils.getColumnName(column);
    }

    @Override
    protected void initNeed() {
        super.initNeed();
        this.selectColumns = new ArrayList<>();
    }

    /**
     * 获取查询字段
     *
     * @return java.lang.String
     * @author huangchengxing
     * @date 2022/2/9 15:35
     */
    @Nullable
    @Override
    public String getSqlSelect() {
        return CollUtil.isNotEmpty(selectColumns) ?
            selectColumns.stream()
                .map(ColumnSegment::getColumnSql)
                .collect(Collectors.joining(ExtendConstants.COMMA_SPACE)) : null;
    }
    
    @Override
    public C setEntity(T entity) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("不支持传入实体");
    }

    @Override
    public C setEntityClass(Class<T> entityClass) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("不支持传入实体");
    }

    @Override
    public boolean nonEmptyOfEntity() {
        return false;
    }

}
