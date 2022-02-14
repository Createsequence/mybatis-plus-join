package top.xiajibagao.mybatis.plus.join.wrapper.column;

import com.baomidou.mybatisplus.core.conditions.ISqlSegment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import top.xiajibagao.mybatis.plus.join.constants.ExtendConstants;
import top.xiajibagao.mybatis.plus.join.helper.SqlUtils;
import top.xiajibagao.mybatis.plus.join.wrapper.interfaces.ColumnSegment;

/**
 * 加减乘除字段，如原字段“a.age concatAs actualAge”，包装后转为：“(a.age + 18) concatAs actualAge”
 *
 * @author huangchengxing
 * @date 2022/02/10 9:30
 */
@RequiredArgsConstructor
@Getter
public class ArithmeticColumn implements ColumnSegment {

    private final ColumnSegment left;
    private final String operator;
    private final ISqlSegment right;

    @Override
    public String getAlisa() {
        return left.getAlisa();
    }

    @Override
    public void setAlisa(String alisa) {
        left.setAlisa(alisa);
    }

    @Override
    public String getColumn() {
        return SqlUtils.concatBrackets(
            SqlUtils.space(left.getSqlSegment(), operator, right.getSqlSegment())
        );
    }

    @Override
    public String toString() {
        return getSqlSegment();
    }

    /**
     * 加
     *
     * @param left 左值
     * @param right 右值
     * @return com.xiajibagao.top.mybatis.plus.concatSegment.wrapper.column.ArithmeticColumn
     * @author huangchengxing
     * @date 2022/2/10 9:44
     */
    public static ArithmeticColumn plus(ColumnSegment left, ISqlSegment right) {
        return new ArithmeticColumn(left, ExtendConstants.PLUS, right);
    }

    /**
     * 减
     *
     * @param left 左值
     * @param right 右值
     * @return com.xiajibagao.top.mybatis.plus.concatSegment.wrapper.column.ArithmeticColumn
     * @author huangchengxing
     * @date 2022/2/10 9:44
     */
    public static ArithmeticColumn sub(ColumnSegment left, ISqlSegment right) {
        return new ArithmeticColumn(left, ExtendConstants.DASH, right);
    }

    /**
     * 乘
     *
     * @param left 左值
     * @param right 右值
     * @return com.xiajibagao.top.mybatis.plus.concatSegment.wrapper.column.ArithmeticColumn
     * @author huangchengxing
     * @date 2022/2/10 9:44
     */
    public static ArithmeticColumn mul(ColumnSegment left, ISqlSegment right) {
        return new ArithmeticColumn(left, ExtendConstants.ASTERISK, right);
    }

    /**
     * 除
     *
     * @param left 左值
     * @param right 右值
     * @return com.xiajibagao.top.mybatis.plus.concatSegment.wrapper.column.ArithmeticColumn
     * @author huangchengxing
     * @date 2022/2/10 9:44
     */
    public static ArithmeticColumn div(ColumnSegment left, ISqlSegment right) {
        return new ArithmeticColumn(left, ExtendConstants.SLASH, right);
    }

}
