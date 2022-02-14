package top.xiajibagao.mybatis.plus.join.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.xiajibagao.mybatis.plus.join.example.model.ScoreDO;
import top.xiajibagao.mybatis.plus.join.extend.JoinMapper;

/**
 * @author huangchengxing
 * @date 2022/01/01 0:13
 */
@Mapper
public interface ScoreMapper extends JoinMapper<ScoreDO> {
}
