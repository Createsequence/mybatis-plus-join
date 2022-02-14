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
     * JOIN
     */
    JOIN("JOIN"),

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
    RIGHT_JOIN("RIGHT JOIN");

    private final String keyword;

    @Override
    public String getSqlSegment() {
        return this.keyword;
    }

}
