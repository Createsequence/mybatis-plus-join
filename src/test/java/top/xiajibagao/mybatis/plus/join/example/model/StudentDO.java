package top.xiajibagao.mybatis.plus.join.example.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author huangchengxing
 * @date 2021/12/28 14:34
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("student")
public class StudentDO extends BaseDO {

    @TableField("name")
    String name;

}
