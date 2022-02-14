package top.xiajibagao.mybatis.plus.join.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author huangchengxing
 * @date 2022/01/01 0:06
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class StudentDTO {

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
