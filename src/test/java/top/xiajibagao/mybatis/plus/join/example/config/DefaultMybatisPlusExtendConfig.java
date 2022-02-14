package top.xiajibagao.mybatis.plus.join.example.config;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.toolkit.GlobalConfigUtils;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import lombok.AllArgsConstructor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.xiajibagao.mybatis.plus.join.injector.JoinMethodInjector;
import top.xiajibagao.mybatis.plus.join.interceptor.DynamicResultInterceptor;

import javax.sql.DataSource;

/**
 * @author huangchengxing
 * @date 2022/01/03 16:30
 */
@AllArgsConstructor
@Configuration
public class DefaultMybatisPlusExtendConfig {

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);

        // 插件
        sqlSessionFactory.setPlugins(new DynamicResultInterceptor());

        MybatisConfiguration configuration = new MybatisConfiguration();
        GlobalConfig globalConfig = GlobalConfigUtils.getGlobalConfig(configuration);

        // 自定义sql注入
        globalConfig.setSqlInjector(new JoinMethodInjector());
        sqlSessionFactory.setConfiguration(configuration);

        // 插件
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        configuration.addInterceptor(interceptor);

        return sqlSessionFactory.getObject();
    }
}
