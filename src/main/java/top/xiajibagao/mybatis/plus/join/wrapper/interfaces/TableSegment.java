package top.xiajibagao.mybatis.plus.join.wrapper.interfaces;

import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.ISqlSegment;
import top.xiajibagao.mybatis.plus.join.helper.SqlUtils;

/**
 * <p>表sql片段，表明作为查询主体的一张数据库物理/逻辑表。
 *
 * <p>约定，若表A为存在对应实体的物理表，且本次查询中别名为a，则：
 * <ul>
 *     <li>使用{@link #getTable()} 获取：“A”;</li>
 *     <li>使用{@link #getTableWithAlisa()} 获取：“A a”，若不存在别名则获取“A”;</li>
 *     <li>使用{@link #getTableIfNonAlisa()} 获取：“a”，若不存在别名则获取“A”;</li>
 *     <li>使用{@link #getSqlSegment()} 获取where后的查询条件片段</li>
 * </ul>
 *
 * 若表本身为不存在对应实体的逻辑表，如：“(select id, name from B) b”，则：
 * <ul>
 *     <li>使用{@link #getTable()} 获取：“(select id, name from B)”;</li>
 *     <li>使用{@link #getTableWithAlisa()} 获取：“(select id, name from B) b”，若不存在别名则获取“(select id, name from B)”;</li>
 *     <li>使用{@link #getTableIfNonAlisa()} 获取：“b”，若不存在别名则获取“(select id, name from B)”;</li>
 *     <li>使用{@link #getSqlSegment()} 获取where后的查询条件片段</li>
 * </ul>
 *
 * @author huangchengxing
 * @date 2022/02/09 10:03
 */
public interface TableSegment extends ISqlSegment {

    /**
     * 获取表
     *
     * @return java.lang.String
     * @author huangchengxing
     * @date 2022/2/9 14:08
     */
    String getTable();

    /**
     * 获取表别名
     *
     * @return java.lang.String
     * @author huangchengxing
     * @date 2022/2/9 10:03
     */
    String getAlisa();

    /**
     * 获取表别名
     *
     * @param alisa 表别名
     * @author huangchengxing
     * @date 2022/2/9 10:03
     */
    void setAlisa(String alisa);

    /**
     * 获取“tableName alisa”格式的表名与别名
     *
     * @return java.lang.String
     * @author huangchengxing
     * @date 2022/2/10 13:31
     */
    default String getTableWithAlisa() {
        return SqlUtils.space(getTable(), getAlisa());
    }
    
    /**
     * 获取别名，若为空则获取表名
     *
     * @return java.lang.String
     * @author huangchengxing
     * @date 2022/2/10 14:51
     */
    default String getTableIfNonAlisa() {
        return CharSequenceUtil.blankToDefault(getAlisa(), getTable());
    }

}
