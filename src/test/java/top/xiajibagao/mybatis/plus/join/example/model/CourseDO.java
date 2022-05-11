package top.xiajibagao.mybatis.plus.join.example.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author huangchengxing
 * @date 2021/12/28 14:32
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("course")
public class CourseDO extends BaseDO {

    @TableField(value = "classroom_id")
    Integer classroomId;

    @TableField(value = "name")
    String name;

    @TableField(value = "type")
    String type;

}
