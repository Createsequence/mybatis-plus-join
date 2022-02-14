package top.xiajibagao.mybatis.plus.join.helper;

import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.ISqlSegment;
import com.baomidou.mybatisplus.core.toolkit.ArrayUtils;
import top.xiajibagao.mybatis.plus.join.constants.ExtendConstants;
import top.xiajibagao.mybatis.plus.join.wrapper.JoinWrapper;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author huangchengxing
 * @date 2022/02/07 11:18
 */
public class SqlUtils implements ExtendConstants {

    /**
     * 拼接空格，将忽略空字符串
     *
     * @param targets targets
     * @return java.lang.String
     * @author huangchengxing
     * @date 2022/2/7 11:23
     */
    public static String space(@Nullable String... targets) {
        if (ArrayUtils.isEmpty(targets)) {
            return EMPTY;
        }
        return Stream.of(targets)
            .filter(Objects::nonNull)
            .filter(CharSequenceUtil::isNotBlank)
            .collect(Collectors.joining(SPACE));
    }

    /**
     * 拼接空格，将忽略空字符串
     *
     * @param targets targets
     * @return java.lang.String
     * @author huangchengxing
     * @date 2022/2/7 11:23
     */
    public static String space(@Nullable ISqlSegment... targets) {
        if (ArrayUtils.isEmpty(targets)) {
            return EMPTY;
        }
        return Stream.of(targets)
            .filter(Objects::nonNull)
            .map(ISqlSegment::getSqlSegment)
            .filter(CharSequenceUtil::isNotBlank)
            .collect(Collectors.joining(SPACE));
    }

    /**
     * 仅当别名不为空时，拼接AS
     *
     * @param columnName 左值
     * @param alisaName 右值
     * @return java.lang.String
     * @author huangchengxing
     * @date 2022/2/7 11:23
     */
    public static String concatAs(@NotNull String columnName, @NotNull String alisaName) {
        return CharSequenceUtil.isNotBlank(alisaName) ? space(columnName, AS, alisaName) : columnName;
    }

    /**
     * 拼接括号
     *
     * @param target target
     * @return java.lang.String
     * @author huangchengxing
     * @date 2022/2/7 11:23
     */
    public static String concatBrackets(@Nullable String target) {
        return LEFT_BRACKET + CharSequenceUtil.emptyIfNull(target) + RIGHT_BRACKET;
    }
    
    /**
     * 拼接sql片段
     *
     * @param delimiter 分隔符
     * @param segments sql片段
     * @return java.lang.String
     * @author huangchengxing
     * @date 2022/2/7 13:32
     */
    public static String concatSegment(@Nullable String delimiter, @NotNull Collection<? extends ISqlSegment> segments) {
        return segments.stream()
            .map(ISqlSegment::getSqlSegment)
            .collect(Collectors.joining(CharSequenceUtil.nullToEmpty(delimiter)));
    }

    /**
     * 拼接sql片段
     *
     * @param segments sql片段
     * @return java.lang.String
     * @author huangchengxing
     * @date 2022/2/7 13:32
     */
    public static String concatSegment(@NotNull Collection<? extends ISqlSegment> segments) {
        return concatSegment(null, segments);
    }

    /**
     * 将wrapper转为可执行的sql片段
     *
     * @param wrapper wrapper
     * @return java.lang.String
     * @author huangchengxing
     * @date 2022/2/9 14:11
     */
    public static <W extends JoinWrapper<?, ?>> String wrapperToSql(@NotNull W wrapper) {
        return space(
            SELECT, wrapper.getSqlSelect(),
            FROM, space(wrapper.getTable(), wrapper.getAlisa()), NEWLINE,
            wrapper.getSqlJoin(), NEWLINE, wrapper.getCustomSqlSegment()
        );
    }

}
