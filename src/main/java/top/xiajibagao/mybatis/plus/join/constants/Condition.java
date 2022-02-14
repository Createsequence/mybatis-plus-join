package top.xiajibagao.mybatis.plus.join.constants;

import com.baomidou.mybatisplus.core.conditions.ISqlSegment;
import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 条件sql
 *
 * @author huangchengxing
 * @date 2022/02/14 14:23
 */
@RequiredArgsConstructor
public enum Condition implements ISqlSegment {

    IN(SqlKeyword.IN.getSqlSegment(), true),
    NOT_IN(SqlKeyword.NOT_IN.getSqlSegment(), true),
    LIKE(SqlKeyword.LIKE.getSqlSegment(), true),
    NOT_LIKE(SqlKeyword.NOT_LIKE.getSqlSegment(), true),
    EQ(SqlKeyword.EQ.getSqlSegment(), true),
    NE(SqlKeyword.NE.getSqlSegment(), true),
    GT(SqlKeyword.GT.getSqlSegment(), true),
    GE(SqlKeyword.GE.getSqlSegment(), true),
    LT(SqlKeyword.LT.getSqlSegment(), true),
    LE(SqlKeyword.LE.getSqlSegment(), true),
    IS_NULL(SqlKeyword.IS_NULL.getSqlSegment(), false),
    IS_NOT_NULL(SqlKeyword.IS_NOT_NULL.getSqlSegment(), false);

    private final String keyword;

    /**
     * 是否为is null这样不带参数的条件
     */
    @Getter
    private final boolean hasNextParam;

    @Override
    public String getSqlSegment() {
        return this.keyword;
    }

}
