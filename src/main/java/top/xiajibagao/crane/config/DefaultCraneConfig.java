package top.xiajibagao.crane.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import top.xiajibagao.crane.container.EnumDictContainer;
import top.xiajibagao.crane.container.KeyValueContainer;
import top.xiajibagao.crane.helper.EnumDict;
import top.xiajibagao.crane.impl.bean.BeanReflexOperatorFactory;
import top.xiajibagao.crane.impl.json.JacksonOperatorFactory;
import top.xiajibagao.crane.impl.json.module.CraneDynamicJsonModule;
import top.xiajibagao.crane.operator.SequentialOperationExecutor;
import top.xiajibagao.crane.operator.UnorderedOperationExecutor;
import top.xiajibagao.crane.parse.BeanOperateConfigurationParser;

/**
 * 默认配置
 *
 * @author huangchengxing
 * @date 2022/03/03 13:36
 */
public class DefaultCraneConfig {

    // ==================== 解析器 ====================

    @Order
    @ConditionalOnMissingBean(BeanOperateConfigurationParser.class)
    @Bean("DefaultCraneBeanOperateConfigurationParser")
    public BeanOperateConfigurationParser beanOperateConfigurationParser(CraneGlobalConfiguration configuration, BeanFactory beanFactory) {
        return new BeanOperateConfigurationParser(configuration, beanFactory);
    }

    // ==================== 操作者 ====================

    @Order
    @ConditionalOnMissingBean(ObjectMapper.class)
    @Bean("DefaultCraneObjectMapper")
    public ObjectMapper objectMapper(BeanFactory beanFactory) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.registerModule(new CraneDynamicJsonModule(new ObjectMapper(), beanFactory));
        return objectMapper;
    }

    @Order
    @ConditionalOnMissingBean(JacksonOperatorFactory.class)
    @Bean("DefaultCraneJacksonOperatorFactory")
    public JacksonOperatorFactory jacksonOperatorFactory(ObjectMapper objectMapper) {
        return new JacksonOperatorFactory(objectMapper);
    }

    @Order
    @ConditionalOnMissingBean(BeanReflexOperatorFactory.class)
    @Bean("DefaultCraneBeanReflexOperatorFactory")
    public BeanReflexOperatorFactory reflexOperatorFactory() {
        return new BeanReflexOperatorFactory();
    }


    // ==================== 容器 ====================

    @Order
    @ConditionalOnMissingBean(EnumDict.class)
    @Bean("DefaultCraneEnumDict")
    public EnumDict enumDict() {
        return EnumDict.instance();
    }

    @Order
    @ConditionalOnMissingBean(EnumDictContainer.class)
    @Bean("DefaultCraneEnumDictContainer")
    public EnumDictContainer enumDictContainer(EnumDict enumDict) {
        return new EnumDictContainer(enumDict);
    }

    @Order
    @ConditionalOnMissingBean(KeyValueContainer.class)
    @Bean("DefaultCraneKeyValueContainer")
    public KeyValueContainer simpleKeyValueActuator() {
        return new KeyValueContainer();
    }

    // ==================== 执行器 ====================

    @Order
    @ConditionalOnMissingBean(UnorderedOperationExecutor.class)
    @Bean("DefaultCraneUnorderedOperationExecutor")
    public UnorderedOperationExecutor unorderedOperationExecutor() {
        return new UnorderedOperationExecutor();
    }

    @Order
    @ConditionalOnMissingBean(SequentialOperationExecutor.class)
    @Bean("DefaultCraneSequentialOperationExecutor")
    public SequentialOperationExecutor operationExecutor() {
        return new SequentialOperationExecutor();
    }

}
