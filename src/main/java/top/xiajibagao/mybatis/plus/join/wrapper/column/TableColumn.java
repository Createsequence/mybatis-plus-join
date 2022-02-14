package top.xiajibagao.mybatis.plus.join.wrapper.column;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import top.xiajibagao.mybatis.plus.join.constants.ExtendConstants;
import top.xiajibagao.mybatis.plus.join.wrapper.interfaces.ColumnSegment;
import top.xiajibagao.mybatis.plus.join.wrapper.interfaces.TableSegment;

/**
 * 表字段
 *
 * @author huangchengxing
 * @date 2022/02/09 10:28
 */
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class TableColumn implements ColumnSegment {

    private final TableSegment table;
    private final String column;
    @Setter
    private String alisa;

    /**
     * 获取字段sql，若字段别名不为空，则返回“表别名/表名.字段名 concatAs 字段别名”
     *
     * @return java.lang.String
     * @author huangchengxing
     * @date 2022/2/10 9:58
     */
    @Override
    public String getSqlSegment() {
        return table.getTableIfNonAlisa() + ExtendConstants.DOT + column;
    }

    @Override
    public String toString() {
        return getSqlSegment();
    }

}
