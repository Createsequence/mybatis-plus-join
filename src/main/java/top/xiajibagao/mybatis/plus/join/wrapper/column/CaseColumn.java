package top.xiajibagao.mybatis.plus.join.wrapper.column;

import com.baomidou.mybatisplus.core.conditions.ISqlSegment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import top.xiajibagao.mybatis.plus.join.constants.Condition;
import top.xiajibagao.mybatis.plus.join.constants.ExtendConstants;
import top.xiajibagao.mybatis.plus.join.constants.FuncKeyword;
import top.xiajibagao.mybatis.plus.join.helper.SqlUtils;
import top.xiajibagao.mybatis.plus.join.wrapper.interfaces.ColumnQuery;
import top.xiajibagao.mybatis.plus.join.wrapper.interfaces.ColumnSegment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * case..when语句
 *
 * @author huangchengxing
 * @date 2022/02/10 17:34
 */
@RequiredArgsConstructor
public class CaseColumn<T extends ColumnQuery<?, ?, ?, ?>> implements ColumnSegment {

    private final boolean onlyMatchingValue;
    private final T result;
    private final List<CaseCondition> whenConditions = new ArrayList<>();
    private CaseCondition elseCondition;
    private final ColumnSegment column;
    @Getter
    @Setter
    private String alisa;

    /**
     * 添加一对“when...then...”语句
     *
     * @param when when条件
     * @param then then返回值
     * @return com.xiajibagao.top.mybatis.plus.concatSegment.wrapper.column.CaseColumn<T>
     * @author huangchengxing
     * @date 2022/2/10 18:09
     */
    public CaseColumn<T> when(Object when, Object then) {
        return when(when::toString, then::toString);
    }

    /**
     * 添加一对“when...then...”语句
     *
     * @param when when条件
     * @param then then返回值
     * @return com.xiajibagao.top.mybatis.plus.concatSegment.wrapper.column.CaseColumn<T>
     * @author huangchengxing
     * @date 2022/2/10 18:09
     */
    public CaseColumn<T> when(ISqlSegment when, ISqlSegment then) {
        whenConditions.add(new CaseCondition(FuncKeyword.WHEN, when, then));
        return this;
    }

    /**
     * 添加一对“when...then...”语句
     *
     * @param conditionColumn when条件字段
     * @param condition when条件字段
     * @param conditionValue when条件值字段
     * @param then then返回值
     * @return com.xiajibagao.top.mybatis.plus.concatSegment.wrapper.column.CaseColumn<T>
     * @author huangchengxing
     * @date 2022/2/10 18:09
     */
    public CaseColumn<T> when(ColumnSegment conditionColumn, Condition condition, ISqlSegment conditionValue, ISqlSegment then) {
        whenConditions.add(new CaseCondition(
            FuncKeyword.WHEN, () -> SqlUtils.space(
                conditionColumn, condition, condition.isHasNextParam() ? conditionValue : null
            ), then
        ));
        return this;
    }

    /**
     * 添加一对“when...then...”语句
     *
     * @param conditionColumn when条件字段
     * @param condition when条件字段
     * @param conditionValue when条件值字段
     * @param then then返回值
     * @return com.xiajibagao.top.mybatis.plus.concatSegment.wrapper.column.CaseColumn<T>
     * @author huangchengxing
     * @date 2022/2/10 18:09
     */
    public CaseColumn<T> when(ColumnSegment conditionColumn, Condition condition, Object conditionValue, Object then) {
        return when(conditionColumn, condition, conditionValue::toString, then::toString);
    }

    /**
     * 添加“else...”语句
     *
     * @param elseValue then返回值
     * @return com.xiajibagao.top.mybatis.plus.concatSegment.wrapper.column.CaseColumn<T>
     * @author huangchengxing
     * @date 2022/2/10 18:09
     */
    public CaseColumn<T> el(ISqlSegment elseValue) {
        elseCondition = new CaseCondition(FuncKeyword.ELSE, () -> ExtendConstants.EMPTY, elseValue);
        return this;
    }

    /**
     * 添加“else...”语句
     *
     * @param elseValue then返回值
     * @return com.xiajibagao.top.mybatis.plus.concatSegment.wrapper.column.CaseColumn<T>
     * @author huangchengxing
     * @date 2022/2/10 18:09
     */
    public CaseColumn<T> el(Object elseValue) {
        return Objects.nonNull(elseValue) ? el(elseValue::toString) : this;
    }
    
    /**
     * 完成构建，并将当前字段添加到所属条件构造器中
     *
     * @return T
     * @author huangchengxing
     * @date 2022/2/11 15:23
     */
    public T end() {
        result.select(this);
        return result;
    }

    @Override
    public String getColumn() {
        String when = SqlUtils.concatSegment(ExtendConstants.NEWLINE, whenConditions);
        String el = Objects.nonNull(elseCondition) ? elseCondition.getSqlSegment() : ExtendConstants.EMPTY;
        return SqlUtils.concatBrackets(SqlUtils.space(
            FuncKeyword.CASE.getSqlSegment(),
            onlyMatchingValue ? column.getSqlSegment() : null,
            when,
            el,
            FuncKeyword.END.getSqlSegment()
        ));
    }

    @Getter
    @RequiredArgsConstructor
    private static class CaseCondition implements ISqlSegment {
        private final FuncKeyword keyword;
        private final ISqlSegment when;
        private final ISqlSegment then;
        @Override
        public String getSqlSegment() {
            if (FuncKeyword.ELSE.equals(keyword)) {
                return SqlUtils.space(keyword, then);
            }
            return SqlUtils.space(
                keyword, when,
                FuncKeyword.THEN, then
            );
        }
    }

    @Override
    public String toString() {
        return getSqlSegment();
    }

}
