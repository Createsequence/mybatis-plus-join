package top.xiajibagao.mybatis.plus.join.extend;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * @author huangchengxing
 * @date 2022/02/14 15:06
 */
public interface ExtendBaseMapper<T> extends BaseMapper<T> {

    // ==================== 扩展方法 ====================

    /**
     * 根据key字段查询数据，若key为空则返回空集合
     *
     * @param keys key值
     * @param column key字段
     * @param <K> key字段类型
     * @return java.util.List<cn.net.nova.charge.generate.dataobject.ChargeResourceCostDO>
     * @author huangchengxing
     * @date 2021/9/23 13:27
     */
    @Nonnull
    default <K> List<T> selectBatchByKeys(Collection<K> keys, @Nonnull SFunction<T, K> column) {
        return CollUtil.isEmpty(keys) ?
            new ArrayList<>() : selectList(wrapper().in(column, keys));
    }

    /**
     * 根据id查询数据，若key为空则返回空集合
     *
     * @param ids key值
     * @return java.util.List<cn.net.nova.charge.generate.dataobject.ChargeResourceCostDO>
     * @author huangchengxing
     * @date 2021/9/23 13:27
     */
    @Nonnull
    default List<T> deleteBatchByIds(Collection<Integer> ids) {
        return CollUtil.isEmpty(ids) ?
            new ArrayList<>() : deleteBatchByIds(ids);
    }

    /**
     * 根据key字段查询指定字段，若key为空则返回空集合
     *
     * @param keys key值
     * @param column key字段
     * @param selectColumns 查询的字段
     * @param <K> key字段类型
     * @return java.util.List<cn.net.nova.charge.generate.dataobject.ChargeResourceCostDO>
     * @author huangchengxing
     * @date 2021/9/23 13:27
     */
    @Nonnull
    default <K> List<T> selectSomeColumnsBatchByKeys(Collection<K> keys, @Nonnull SFunction<T, K> column, SFunction<T, ?>... selectColumns) {
        return CollUtil.isEmpty(keys) || ArrayUtil.isEmpty(selectColumns) ?
            new ArrayList<>() : selectList(wrapper().select(selectColumns).in(column, keys));
    }

    /**
     * 根据key字段查询数据，若key为空则返回空集合
     *
     * @param entities 实体数据
     * @param keyGenerator 实体到key的映射方法
     * @param column key字段
     * @param <E> 实体类型
     * @param <K> key字段类型
     * @return java.util.List<T>
     * @author huangchengxing
     * @date 2021/9/23 13:48
     */
    @Nonnull
    default <E, K> List<T> selectBatchByKeys(
        Collection<E> entities,
        @Nonnull Function<E, K> keyGenerator,
        @Nonnull SFunction<T, K> column) {
        if (entities == null || entities.isEmpty()) {
            return new ArrayList<>();
        }
        List<K> keys = CollUtil.map(entities, keyGenerator, true);
        return keys.isEmpty() ?
            new ArrayList<>() : selectList(wrapper().in(column, keys));
    }

    /**
     * 根据key字段查询数据，若key为空则返回空集合
     *
     * @param key key值
     * @param column key字段
     * @param <K> key字段类型
     * @return java.util.List<cn.net.nova.charge.generate.dataobject.ChargeResourceCostDO>
     * @author huangchengxing
     * @date 2021/9/23 13:27
     */
    @Nonnull
    default <K> List<T> selectByKey(@Nonnull K key, @Nonnull SFunction<T, K> column) {
        return selectList(wrapper().eq(column, key));
    }

    /**
     * 根据key字段查询指定字段，若key为空则返回空集合
     *
     * @param key key值
     * @param column key字段
     * @param selectColumns 查询的字段
     * @param <K> key字段类型
     * @return java.util.List<cn.net.nova.charge.generate.dataobject.ChargeResourceCostDO>
     * @author huangchengxing
     * @date 2021/9/23 13:27
     */
    @Nonnull
    default <K> List<T> selectSomeColumnsByKey(@Nonnull K key, @Nonnull SFunction<T, K> column, @Nonnull SFunction<T, ?>... selectColumns) {
        return selectList(wrapper().select(selectColumns).eq(column, key));
    }

    /**
     * 根据key字段查询数据，若无数据则返回null，若有不止一条数据则抛出异常
     *
     * @param key key值
     * @param column key字段
     * @param <K> key字段类型
     * @return T
     * @throws IllegalArgumentException 查询返回不止一条数据时抛出
     * @author huangchengxing
     * @date 2021/9/29 9:26
     */
    @Nullable
    default <K> T selectOneByKey(@Nonnull K key, @Nonnull SFunction<T, K> column) {
        List<T> results = selectList(wrapper().eq(column, key));
        Assert.isFalse(results.size() > 1, "期望查询1数据，但实际返回{}条", results.size());
        return CollUtil.getFirst(results);
    }

    /**
     * 根据key字段查询指定字段，若无数据则返回null，若有数据则返回第一条
     *
     * @param key key值
     * @param column key字段
     * @param selectColumns 查询的字段
     * @param <K> key字段类型
     * @return T
     * @throws IllegalArgumentException 查询返回不止一条数据时抛出
     * @author huangchengxing
     * @date 2021/9/29 9:26
     */
    @Nullable
    default <K> T selectOneSomeColumnsByKey(@Nonnull K key, @Nonnull SFunction<T, K> column, @Nonnull SFunction<T, ?>... selectColumns) {
        List<T> results = selectList(wrapper().select(selectColumns).eq(column, key));
        Assert.isFalse(results.size() > 1, "期望查询1数据，但实际返回{}条", results.size());
        return CollUtil.getFirst(results);
    }

    /**
     * 根据key查询条数
     *
     * @param key key值
     * @param column key字段
     * @param <K> key字段类型
     * @return T
     * @author huangchengxing
     * @date 2021/9/29 9:26
     */
    default <K> int countByKey(@Nonnull K key, @Nonnull SFunction<T, K> column) {
        return selectCount(wrapper().eq(column, key));
    }

    // ==================== 条件构造器 ====================

    /**
     * 构建链式查询
     *
     * @return com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper<T>
     * @author huangchengxing
     * @date 2021/9/23 17:18
     */
    default LambdaQueryChainWrapper<T> query() {
        return new LambdaQueryChainWrapper<>(this);
    }

    /**
     * 构建链式更新
     *
     * @return com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper<T>
     * @author huangchengxing
     * @date 2021/9/23 17:34
     */
    default LambdaUpdateChainWrapper<T> update() {
        return new LambdaUpdateChainWrapper<>(this);
    }

    /**
     * 构建查询条件
     *
     * @return com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<T>
     * @author huangchengxing
     * @date 2021/9/23 17:19
     */
    default LambdaQueryWrapper<T> wrapper() {
        return Wrappers.lambdaQuery();
    }

}
