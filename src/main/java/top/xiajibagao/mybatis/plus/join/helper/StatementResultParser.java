package top.xiajibagao.mybatis.plus.join.helper;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.apache.ibatis.type.UnknownTypeHandler;

import javax.annotation.Nonnull;

/**
 * 解析对象，将其转为{@link ResultMap}与{@link TableInfo}，并将对象相关数据添加到{@link LambdaUtils}缓存中
 *
 * @author huangchengxing
 * @date 2021/12/28 16:15
 */
@RequiredArgsConstructor
@Getter
public class StatementResultParser<T> {

    private final Configuration configuration;

    private final Class<T> targetClass;

    private TableInfo tableInfo;

    private ResultMap resultMap;
    
    /**
     * 解析类并获取其对应的TableInfo与ResultMap
     *
     * @param conf Mybatis配置
     * @param targetClass 解析类型
     * @return com.xiajiabagao.mybaits.extend.helper.StatementResultParser<T>
     * @author huangchengxing
     * @date 2021/12/28 17:05
     */
    public static <T> StatementResultParser<T> parse(@Nonnull Configuration conf, @Nonnull Class<T> targetClass) {
        return new StatementResultParser<>(conf, targetClass).parse(
            "additional_info_" + targetClass.getName()
        );
    }

    /**
     * 解析类并获取其对应的TableInfo与ResultMap
     *
     * @param targetClass 解析类型
     * @return com.xiajiabagao.mybaits.extend.helper.StatementResultParser<T>
     * @author huangchengxing
     * @date 2021/12/28 17:05
     */
    public static <T> StatementResultParser<T> parse(Class<T> targetClass) {
        Configuration conf = SpringUtil.getBean(Configuration.class);
        return new StatementResultParser<>(conf, targetClass).parse(
            "_DYNAMIC_RESULT_INFO" + targetClass.getName()
        );
    }

    /**
     * 解析
     *
     * @param resultMapId resultMapId
     * @return com.xiajiabagao.mybaits.extend.helper.StatementResultParser<T>
     * @author huangchengxing
     * @date 2021/12/28 17:08
     */
    public StatementResultParser<T> parse(String resultMapId) {
        parseTableInfo();
        LambdaUtils.installCache(tableInfo);
        parseResultMap(resultMapId);
        return this;
    }

    /**
     * 解析类获取TableInfo
     *
     * @author huangchengxing
     * @date 2021/12/28 16:58
     */
    private void parseTableInfo() {
        MapperBuilderAssistant assistant = new MapperBuilderAssistant(
            configuration,
            targetClass.getName().replace('.', '/') + ".java (best guess)"
        );
        this.tableInfo = TableInfoHelper.initTableInfo(assistant, targetClass);
    }

    /**
     * 将TableInfo解析为ResultMap
     *
     * @param resultMapId ResultMapId
     * @author huangchengxing
     * @date 2021/12/28 16:50
     */
    private void parseResultMap(String resultMapId) {
        String id = resultMapId + StringPool.UNDERSCORE + tableInfo.getEntityType().getName();
        this.resultMap = new ResultMap.Builder(
            tableInfo.getConfiguration(),
            id, tableInfo.getEntityType(),
            CollUtil.map(tableInfo.getFieldList(), this::parseResultMapping, true)
        ).build();
    }

    /**
     * 解析字段属性为ResultMapping
     *
     * @param tableFieldInfo 字段属性
     * @return org.apache.ibatis.mapping.ResultMapping
     * @author huangchengxing
     * @date 2021/12/28 16:47
     */
    private ResultMapping parseResultMapping(TableFieldInfo tableFieldInfo) {
        ResultMapping.Builder builder = new ResultMapping.Builder(
            tableInfo.getConfiguration(), tableFieldInfo.getProperty(),
            StringUtils.getTargetColumn(tableFieldInfo.getColumn()),
            tableFieldInfo.getPropertyType()
        );

        // 配置字段类型与数据库类型映射
        if (tableFieldInfo.getJdbcType() != null && tableFieldInfo.getJdbcType() != JdbcType.UNDEFINED) {
            builder.jdbcType(tableFieldInfo.getJdbcType());
        }

        // 配置字段处理器
        TypeHandlerRegistry typeHandlerRegistry = tableInfo.getConfiguration().getTypeHandlerRegistry();
        if (tableFieldInfo.getTypeHandler() != null && tableFieldInfo.getTypeHandler() != UnknownTypeHandler.class) {
            TypeHandler<?> typeHandler = typeHandlerRegistry.getMappingTypeHandler(tableFieldInfo.getTypeHandler());
            if (typeHandler == null) {
                typeHandler = typeHandlerRegistry.getInstance(
                    tableFieldInfo.getPropertyType(), tableFieldInfo.getTypeHandler()
                );
            }
            builder.typeHandler(typeHandler);
        }

        return builder.build();
    }

}
