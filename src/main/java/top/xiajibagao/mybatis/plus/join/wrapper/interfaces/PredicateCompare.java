package top.xiajibagao.mybatis.plus.join.wrapper.interfaces;

import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.interfaces.Compare;

import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * 支持Lambda表达式过滤条件的{@link Compare}接口
 *
 * @author huangchengxing
 * @date 2022/01/01 23:22
 */
public interface PredicateCompare<C, R> extends Compare<C, R> {

    // ========================= eq & ne & lt & gt =========================

    default <V> C eq(Predicate<V> condition, R column, V val) {
        return eq(condition.test(val), column, val);
    }

    default <V> C eqIfNotNull(R column, V val) {
        return eq(Objects::nonNull, column, val);
    }

    default <V> C ne(Predicate<V> condition, R column, V val) {
        return ne(condition.test(val), column, val);
    }

    default <V> C neIfNotNull(R column, V val) {
        return ne(Objects::nonNull, column, val);
    }

    default <V> C ge(Predicate<V> condition, R column, V val) {
        return ge(condition.test(val), column, val);
    }

    default <V> C geIfNotNull(R column, V val) {
        return ge(Objects::nonNull, column, val);
    }

    default <V> C gt(Predicate<V> condition, R column, V val) {
        return gt(condition.test(val), column, val);
    }

    default <V> C gtIfNotNull(R column, V val) {
        return gt(Objects::nonNull, column, val);
    }

    default <V> C lt(Predicate<V> condition, R column, V val) {
        return lt(condition.test(val), column, val);
    }

    default <V> C ltIfNotNull(R column, V val) {
        return lt(Objects::nonNull, column, val);
    }

    default <V> C le(Predicate<V> condition, R column, V val) {
        return le(condition.test(val), column, val);
    }

    default <V> C leIfNotNull(R column, V val) {
        return le(Objects::nonNull, column, val);
    }

    // ========================= between =========================

    default <V> C between(BiPredicate<V, V> condition, R column, V left, V right) {
        return between(condition.test(left, right), column, left, right);
    }

    default <V> C betweenIfAllNotNull(R column, V left, V right) {
        return between((l, r) -> l != null && r != null, column, left, right);
    }

    default <V> C notBetween(BiPredicate<V, V> condition, R column, V left, V right) {
        return notBetween(condition.test(left, right), column, left, right);
    }

    default <V> C notBetweenIfAllNotNull(R column, V left, V right) {
        return notBetween((l, r) -> l != null && r != null, column, left, right);
    }

    // ========================= like =========================

    default C notLike(Predicate<String> condition, R column, String val) {
        return notLike(condition.test(val), column, val);
    }

    default C notLikeIfNotBank(R column, String val) {
        return notLike(CharSequenceUtil::isNotBlank, column, val);
    }

    C notLikeLeft(Predicate<String> condition, R column, String val);

    default C notLikeLeftIfNotBank(R column, String val) {
        return notLikeLeft(CharSequenceUtil::isNotBlank, column, val);
    }

    C notLikeRight(Predicate<String> condition, R column, String val);

    default C notLikeRightIfNotBank(R column, String val) {
        return notLikeRight(CharSequenceUtil::isNotBlank, column, val);
    }

    default C like(Predicate<String> condition, R column, String val) {
        return like(condition.test(val), column, val);
    }

    default C likeIfNotBank(R column, String val) {
        return like(CharSequenceUtil::isNotBlank, column, val);
    }

    default <V> C likeLeft(Predicate<V> condition, R column, V val) {
        return likeLeft(condition.test(val), column, val);
    }

    default C likeLeftIfNotBank(R column, String val) {
        return likeLeft(CharSequenceUtil::isNotBlank, column, val);
    }

    default <V> C likeRight(Predicate<V> condition, R column, V val) {
        return likeRight(condition.test(val), column, val);
    }

    default C likeRightIfNotBank(R column, String val) {
        return likeRight(CharSequenceUtil::isNotBlank, column, val);
    }

}
