package top.xiajibagao.mybatis.plus.join.wrapper.interfaces;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.interfaces.Func;

import java.util.Collection;
import java.util.function.Predicate;

/**
 * 支持Lambda表达式过滤条件的{@link Func}接口
 *
 * @author huangchengxing
 * @date 2022/01/01 23:42
 */
public interface PredicateFunc <C, R> extends Func<C, R> {

    // ========================= in =========================

    default <V extends Collection<?>> C in(Predicate<V> condition, R column, V val) {
        return in(condition.test(val), column, val);
    }

    default <V extends Collection<?>> C inIfNotEmpty(R column, V val) {
        return in(CollUtil::isNotEmpty, column, val);
    }

    default <V extends Collection<?>> C notIn(Predicate<V> condition, R column, V val) {
        return notIn(condition.test(val), column, val);
    }

    default <V extends Collection<?>> C notInIfNotEmpty(R column, V val) {
        return notIn(CollUtil::isNotEmpty, column, val);
    }

}
