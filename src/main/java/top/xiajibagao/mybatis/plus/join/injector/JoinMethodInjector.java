package top.xiajibagao.mybatis.plus.join.injector;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import top.xiajibagao.mybatis.plus.join.injector.methods.*;

import java.util.Arrays;
import java.util.List;

/**
 * @author huangchengxing
 * @date 2022/02/10 13:24
 */
public class JoinMethodInjector extends DefaultSqlInjector {

    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
        List<AbstractMethod> methods = super.getMethodList(mapperClass);
        methods.addAll(getMethods());
        return methods;
    }

    public static List<AbstractMethod> getMethods() {
        return Arrays.asList(new SelectListJoin(),
            new SelectCountJoin(),
            new SelectExistsJoin(),
            new SelectPageJoin(),
            new SelectMapsJoin(),
            new SelectMapsPageJoin());
    }

}
