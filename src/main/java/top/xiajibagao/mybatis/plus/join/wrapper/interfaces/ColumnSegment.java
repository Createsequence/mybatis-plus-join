package top.xiajibagao.mybatis.plus.join.wrapper.interfaces;

import com.baomidou.mybatisplus.core.conditions.ISqlSegment;
import top.xiajibagao.mybatis.plus.join.helper.SqlUtils;

/**
 * <p>查询字段sql片段，表示一个作为查询条件的字段。
 *
 * <p>约定，若字段name为表A字段，且本次查询别名为aname，则：
 * <ul>
 *     <li>使用{@link #getColumn()}获取：“name”;</li>
 *     <li>使用{@link #getSqlSegment()} 获取：“A.name”;</li>
 *     <li>使用{@link #getColumnSql()}获取：“A.name concatAs aname”;</li>
 * </ul>
 *
 * 若字段本身为嵌套查询或特殊函数，如：“IFNULL(a.full_name, a.surname) concatAs name”，则：
 * <ul>
 *     <li>使用{@link #getColumn()}获取：“IFNULL(a.full_name, a.surname)”;</li>
 *     <li>使用{@link #getSqlSegment()} 获取：“IFNULL(a.full_name, a.surname)”;</li>
 *     <li>使用{@link #getColumnSql()}获取：“IFNULL(a.full_name, a.surname) concatAs name”;</li>
 * </ul>
 *
 * <p>实现类的{@link Object#toString()}方法返回值应与其{@link #getSqlSegment()}保持一致
 *
 * @author huangchengxing
 * @date 2022/02/09 10:11
 */
public interface ColumnSegment extends ISqlSegment {

    /**
     * 获取字段别名
     *
     * @return java.lang.String
     * @author huangchengxing
     * @date 2022/2/9 10:12
     */
    String getAlisa();

    /**
     * 设置字段别名
     *
     * @param alisa 别名
     * @author huangchengxing
     * @date 2022/2/9 10:12
     */
    void setAlisa(String alisa);

    /**
     * 获取表字段
     *
     * @return java.lang.String
     * @author huangchengxing
     * @date 2022/2/9 10:29
     */
    String getColumn();

    /**
     * 获取字段sql，若字段别名不为空，则返回“字段名 concatAs 字段别名”
     *
     * @return java.lang.String
     * @author huangchengxing
     * @date 2022/2/11 9:38
     */
    default String getColumnSql() {
        return SqlUtils.concatAs(getSqlSegment(), getAlisa());
    }

    /**
     * 若存在表前缀，则获取带表前缀的字段，返回“表.字段”格式
     *
     * @return java.lang.String
     * @author huangchengxing
     * @date 2022/2/10 9:58
     */
    @Override
    default String getSqlSegment() {
        return getColumn();
    }

}
