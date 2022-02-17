package top.xiajibagao.mybatis.plus.join.constants;

import com.baomidou.mybatisplus.core.conditions.ISqlSegment;
import lombok.RequiredArgsConstructor;

/**
 * @author huangchengxing
 * @date 2022/02/09 13:13
 */
@RequiredArgsConstructor
public enum JoinType implements ISqlSegment {

    /**
     * INNER JOIN
     */
    INNER_JOIN("INNER JOIN"),

    /**
     * LEFT JOIN
     */
    LEFT_JOIN("LEFT JOIN"),

    /**
     * RIGHT JOIN
     */
    RIGHT_JOIN("RIGHT JOIN"),

    /**
     * FULL JOIN
     */
    FULL_JOIN("FULL JOIN");

    private final String keyword;

    @Override
    public String getSqlSegment() {
        return this.keyword;
    }

}
