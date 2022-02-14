package top.xiajibagao.mybatis.plus.join.constants;

import com.baomidou.mybatisplus.core.toolkit.Constants;

/**
 * SQL片段
 *
 * @author huangchengxing
 * @date 2021/12/28 21:02
 */
public interface ExtendConstants extends Constants {

    // ================================== 通用 ==================================

    String MP_PLACEHOLDER = "${%s}";

    // ================================== 查询条件 ==================================

    String IN = " IN ";
    String EQ = " = ";
    String NE = " <> ";
    String GT = " > ";
    String GE = " >= ";
    String LT = " < ";
    String LE = " <= ";
    String IS_NULL = " IS NULL ";
    String IS_NOT_NULL = " NULL ";

    // ================================== where条件关键字 ==================================

    String NONE = "";
    String SELECT = "SELECT";
    String AND = "AND";
    String OR = "OR";
    String WHERE = "WHERE";
    String FROM = "FROM";
    String AS = "AS";
    String LEFT_JOIN = "LEFT JOIN";
    String RIGHT_JOIN = "RIGHT JOIN";
    String INNER_JOIN = "INNER JOIN";
    String JOIN = "JOIN";
    String ON = "ON";
    String LIMIT = "LIMIT";

    // ================================== 扩展常量 ==================================

    String BRACKETS = LEFT_BRACKET + RIGHT_BRACKET;
    String BRACES = LEFT_BRACE + RIGHT_BRACE;
    String SQ_BRACKETS = LEFT_SQ_BRACKET + RIGHT_SQ_BRACKET;
    String COMMA_SPACE = COMMA + SPACE;

    // ================================== Wrapper ==================================

    String WRAPPER_HAS_JOIN = WRAPPER_DOT + "hasJoin";
    String Q_WRAPPER_SQL_JOIN = WRAPPER_DOT + "sqlJoin";
    String Q_WRAPPER_SQL_LAST = WRAPPER_DOT + "sqlLast";
    String Q_WRAPPER_SQL_TABLE_WITH_ALISA = WRAPPER_DOT + "tableWithAlisa";
    String Q_WRAPPER_SQL_TABLE_IF_NON_ALISA = WRAPPER_DOT + "tableIfNonAlisa";

}
