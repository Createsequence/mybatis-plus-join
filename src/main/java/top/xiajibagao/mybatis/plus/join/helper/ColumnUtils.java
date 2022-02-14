package top.xiajibagao.mybatis.plus.join.helper;

import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import org.apache.ibatis.reflection.property.PropertyNamer;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 函数式接口字段工具类
 *
 * @author huangchengxing
 * @date 2021/12/28 17:50
 */
public class ColumnUtils {

    private ColumnUtils() {
    }

    private static final Map<String, String> PROPERTY_COLUMN_NAME_CACHE = new ConcurrentHashMap<>();

    /**
     * 将lambda表达式转为表字段名
     *
     * @param column 字段
     * @return java.lang.String
     * @author huangchengxing
     * @date 2021/12/29 18:21
     */
    public static <T, R> String getColumnName(@NotNull SFunction<T, R> column) {
        return PROPERTY_COLUMN_NAME_CACHE.computeIfAbsent(
            LambdaUtils.resolve(column).getImplMethodName(),
            methodName -> StringUtils.camelToUnderline(PropertyNamer.methodToProperty(methodName))
        );
    }

    /**
     * 将lambda表达式转为实体类属性名
     *
     * @param column 字段
     * @return java.lang.String
     * @author huangchengxing
     * @date 2021/12/29 18:21
     */
    public static <T, R> String getPropertyName(@NotNull SFunction<T, R> column) {
        return PropertyNamer.methodToProperty(
            LambdaUtils.resolve(column).getImplMethodName()
        );
    }

}
