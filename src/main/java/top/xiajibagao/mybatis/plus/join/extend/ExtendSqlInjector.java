package top.xiajibagao.mybatis.plus.join.extend;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 扩展sql注入器，使用追加而非重写的方式添加方法
 *
 * @author huangchengxing
 * @date 2022/02/14 15:17
 */
public class ExtendSqlInjector extends DefaultSqlInjector {

    List<AbstractMethod> methods = new ArrayList<>();

    public ExtendSqlInjector addMethod(AbstractMethod method) {
        methods.add(method);
        return this;
    }

    public ExtendSqlInjector addMethods(Collection<AbstractMethod> methods) {
        this.methods.addAll(methods);
        return this;
    }

    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
        List<AbstractMethod> supperMethods = super.getMethodList(mapperClass);
        supperMethods.addAll(methods);
        return supperMethods;
    }

}
