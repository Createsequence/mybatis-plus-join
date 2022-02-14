package top.xiajibagao.mybatis.plus.join.wrapper.column;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.ISqlSegment;
import com.baomidou.mybatisplus.core.toolkit.ArrayUtils;
import lombok.Getter;
import lombok.Setter;
import top.xiajibagao.mybatis.plus.join.constants.ExtendConstants;
import top.xiajibagao.mybatis.plus.join.constants.FuncKeyword;
import top.xiajibagao.mybatis.plus.join.helper.SqlUtils;
import top.xiajibagao.mybatis.plus.join.wrapper.interfaces.ColumnSegment;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据库内置函数字段，比如“NOW(), MAX(xxx)等” <br />
 * 若函数传入参数类型为：
 * <ul>
 *     <li>是{@link ColumnSegment}类型：格式化时使用{@link ColumnSegment#getColumn()}；</li>
 *     <li>不为{@link ColumnSegment}类型：格式化时使用{@link ISqlSegment#getSqlSegment()} }；</li>
 * </ul>
 *
 * @author huangchengxing
 * @date 2022/02/10 10:09
 */
@Getter
public class FuncColumn implements ColumnSegment {

    private final FuncKeyword keyword;
    private final List<ISqlSegment> args;
    @Setter
    private String alisa;

    public FuncColumn(FuncKeyword keyword, ISqlSegment... args) {
        this(keyword, ExtendConstants.EMPTY, args);
    }

    public FuncColumn(FuncKeyword keyword, String alisa, ISqlSegment... args) {
        this.keyword = keyword;
        this.alisa = alisa;
        this.args = ArrayUtils.isEmpty(args) ?
            Collections.emptyList() : Arrays.asList(args);
    }

    @Override
    public String getColumn() {
        return keyword.getSqlSegment() + SqlUtils.concatBrackets(getArgsSqlSegment());
    }

    public String getArgsSqlSegment() {
        if (CollUtil.isEmpty(args)) {
            return ExtendConstants.EMPTY;
        }
        return args.stream()
            .map(ISqlSegment::getSqlSegment)
            .filter(CharSequenceUtil::isNotBlank)
            .collect(Collectors.joining(ExtendConstants.COMMA_SPACE));
    }

    @Override
    public String toString() {
        return getSqlSegment();
    }
}
