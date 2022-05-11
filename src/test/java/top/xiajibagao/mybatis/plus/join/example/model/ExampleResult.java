package top.xiajibagao.mybatis.plus.join.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author huangchengxing
 * @date 2022/01/01 0:06
 */
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ExampleResult {

    // student
    Integer id;
    String name;

    Integer studentId;
    String studentName;

    // course
    String courseId;
    String courseName;

    // score
    Integer score;
    String remark;
    Integer num;
}
