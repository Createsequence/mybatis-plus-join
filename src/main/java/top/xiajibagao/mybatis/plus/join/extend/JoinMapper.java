package top.xiajibagao.mybatis.plus.join.extend;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import top.xiajibagao.mybatis.plus.join.wrapper.JoinWrapper;

import java.util.List;
import java.util.Map;

/**
 * 提供join的查询方法
 *
 * @author huangchengxing
 * @date 2022/02/10 14:21
 */
public interface JoinMapper<T> extends ExtendBaseMapper<T> {

    /**
     * 关联查询
     *
     * @param wrapper 条件
     * @return java.util.List<R>
     * @author huangchengxing
     * @date 2022/1/3 16:26
     */
    <R> List<R> selectListJoin(@Param(Constants.WRAPPER) JoinWrapper<?, R> wrapper);

    /**
     * 关联查询，若返回不止一条数据则抛出异常
     *
     * @param wrapper 条件
     * @return java.util.List<R>
     * @throws IllegalArgumentException 当返回数据大于1条时抛出
     * @author huangchengxing
     * @date 2022/1/3 16:26
     */
    default <R> R selectOneJoin(@Param(Constants.WRAPPER) JoinWrapper<?, R> wrapper) {
        List<R> result = selectListJoin(wrapper);
        Assert.isFalse(result.size() > 1, "预期查找1条，但是实际返回了{}条数据", result.size());
        return CollUtil.getFirst(result);
    }

    /**
     * 关联查询条数
     *
     * @param wrapper 条件
     * @return java.lang.Integer
     * @author huangchengxing
     * @date 2022/1/3 16:27
     */
    <R> Integer selectCountJoin(@Param(Constants.WRAPPER) JoinWrapper<?, R> wrapper);

    /**
     * 关联查询指定数据是否存在
     *
     * @param wrapper 条件
     * @return java.lang.Boolean
     * @author huangchengxing
     * @date 2022/1/3 16:29
     */
    <R> boolean selectExistsJoin(@Param(Constants.WRAPPER) JoinWrapper<?, R> wrapper);

    /**
     * 关联查询
     *
     * @param page 分页查询条件
     * @param queryWrapper 实体对象封装操作类
     * @author huangchengxing
     * @date 2022/2/14 15:03
     */
    <R, E extends IPage<R>> E selectPageJoin(E page, @Param(Constants.WRAPPER) JoinWrapper<?, R> queryWrapper);

    /**
     * 关联查询
     *
     * @param queryWrapper 实体对象封装操作类
     * @return E
     * @author huangchengxing
     * @date 2022/2/14 15:02
     */
    List<Map<String, Object>> selectMaps(@Param(Constants.WRAPPER) JoinWrapper<?, ?> queryWrapper);

    /**
     * 分页查询
     *
     * @param page 分页查询条件
     * @param queryWrapper 实体对象封装操作类
     * @return E
     * @author huangchengxing
     * @date 2022/2/14 15:02
     */
    <E extends IPage<Map<String, Object>>> E selectMapsPage(E page, @Param(Constants.WRAPPER) JoinWrapper<?, ?> queryWrapper);

}
