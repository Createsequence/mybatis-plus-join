package top.xiajibagao.crane.jackson.impl.annotation;

import top.xiajibagao.crane.core.annotation.MateAnnotation;
import top.xiajibagao.crane.core.executor.OperationExecutor;
import top.xiajibagao.crane.core.executor.UnorderedOperationExecutor;
import top.xiajibagao.crane.core.operator.interfaces.OperatorFactory;
import top.xiajibagao.crane.core.parser.BeanOperateConfigurationParser;
import top.xiajibagao.crane.core.parser.interfaces.OperateConfigurationParser;
import top.xiajibagao.crane.jackson.impl.operator.JacksonOperatorFactory;

import java.lang.annotation.*;

/**
 * 表明注解对象在通过jackson序列化时，需要进行数据填充
 *
 * @author huangchengxing
 * @date 2022/04/12 17:52
 */
@MateAnnotation
@Target(ElementType.TYPE_USE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ProcessJacksonNode {

    /**
     * 要使用的配置解析器
     */
    Class<? extends OperateConfigurationParser<?>> parser() default BeanOperateConfigurationParser.class;

    /**
     * 要使用的操作者工厂
     */
    Class<? extends OperatorFactory> operatorFactory() default JacksonOperatorFactory.class;

    /**
     * 要使用的执行器
     */
    Class<? extends OperationExecutor> executor() default UnorderedOperationExecutor.class;

}
