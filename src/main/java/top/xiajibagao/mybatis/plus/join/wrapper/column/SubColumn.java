package top.xiajibagao.mybatis.plus.join.wrapper.column;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import top.xiajibagao.mybatis.plus.join.helper.SqlUtils;
import top.xiajibagao.mybatis.plus.join.wrapper.JoinWrapper;
import top.xiajibagao.mybatis.plus.join.wrapper.interfaces.ColumnSegment;

/**
 * 子查询字段
 *
 * @author huangchengxing
 * @date 2022/02/17 13:09
 */
@Getter
@RequiredArgsConstructor
public class SubColumn implements ColumnSegment {

    private final JoinWrapper<?, ?> wrapper;

    @Setter
    private String alisa;

    @Override
    public String getColumn() {
        return SqlUtils.concatBrackets(SqlUtils.wrapperToSql(wrapper));
    }

}
