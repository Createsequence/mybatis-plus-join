package top.xiajibagao.mybatis.plus.join.constants;

import com.baomidou.mybatisplus.core.conditions.ISqlSegment;
import lombok.AllArgsConstructor;

/**
 * Mysql内置函数关键字
 *
 * @author huangchengxing
 * @date 2022/02/10 10:34
 */
@AllArgsConstructor
public enum FuncKeyword implements ISqlSegment {

    /**
     * 时间
     */
    NOW("NOW"),
    CURRENT_TIMESTAMP("CURRENT_TIMESTAMP"),
    CURRENT_DATE("CURRENT_DATE"),
    CURRENT_TIME("CURRENT_TIME"),
    DATE_FORMAT("DATE_FORMAT"),
    DAY("DAY"),
    MONTH("MONTH"),
    YEAR("YEAR"),

    /**
     * 数学
     */
    ABS("ABS"),
    AVG("AVG"),
    MAX("MAX"),
    MIN("MIN"),
    SUM("SUM"),
    RAND("RAND"),
    COUNT("COUNT"),

    /**
     * 字符串
     */
    IFNULL("IFNULL"),
    CONCAT("CONCAT"),
    FORMAT("FORMAT"),
    REPLACE("REPLACE"),
    LOWER("LOWER"),
    UPPER("UPPER"),

    /**
     * 函数
     */
    CASE("CASE"),
    THEN("THEN"),
    END("END"),
    WHEN("WHEN"),
    ELSE("ELSE");

    private final String keyword;

    @Override
    public String getSqlSegment() {
        return this.keyword;
    }
}
