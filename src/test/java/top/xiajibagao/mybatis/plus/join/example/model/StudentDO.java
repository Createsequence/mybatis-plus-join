package top.xiajibagao.mybatis.plus.join.example.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author huangchengxing
 * @date 2021/12/28 14:34
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("student")
public class StudentDO extends BaseDO {

    @TableField("name")
    String name;

}
