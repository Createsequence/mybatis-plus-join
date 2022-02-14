package top.xiajibagao.mybatis.plus.join.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.xiajibagao.mybatis.plus.join.example.model.CourseDO;
import top.xiajibagao.mybatis.plus.join.extend.JoinMapper;

/**
 * @author huangchengxing
 * @date 2022/01/01 0:12
 */
@Mapper
public interface CourseMapper extends JoinMapper<CourseDO> {
}
