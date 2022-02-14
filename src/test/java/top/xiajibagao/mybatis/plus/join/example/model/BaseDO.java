package top.xiajibagao.mybatis.plus.join.example.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author huangchengxing
 * @date 2021/12/28 14:29
 */
@Data
public abstract class BaseDO {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField(value = "is_delete", exist = false)
    private String isDelete;

}
