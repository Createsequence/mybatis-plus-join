package top.xiajibagao.mybatis.plus.join.example.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author huangchengxing
 * @date 2021/12/28 14:36
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("score")
public class ScoreDO extends BaseDO {

    @TableField("student_id")
    Integer studentId;

    @TableField("course_id")
    Integer courseId;

    @TableField("score")
    Integer score;
}
