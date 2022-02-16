package top.xiajibagao.mybatis.plus.join;

import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.xiajibagao.mybatis.plus.join.constants.Condition;
import top.xiajibagao.mybatis.plus.join.example.mapper.ScoreMapper;
import top.xiajibagao.mybatis.plus.join.example.mapper.StudentMapper;
import top.xiajibagao.mybatis.plus.join.example.model.CourseDO;
import top.xiajibagao.mybatis.plus.join.example.model.ScoreDO;
import top.xiajibagao.mybatis.plus.join.example.model.StudentDO;
import top.xiajibagao.mybatis.plus.join.example.model.StudentDTO;
import top.xiajibagao.mybatis.plus.join.wrapper.JoinWrapper;
import top.xiajibagao.mybatis.plus.join.wrapper.column.Columns;

import java.util.List;
import java.util.Objects;

@SpringBootTest
class MybatisPlusJoinTableApplicationTests {

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private ScoreMapper scoreMapper;

    protected ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    protected void printObject(Object target) {
        System.out.println(objectMapper.writeValueAsString(target));
    }

    /**
     * 兼容原生普通查询
     *
     * @author huangchengxing
     * @date 2022/2/11 13:57
     */
    @Test
    void testMPSelect() {
        JoinWrapper<StudentDO, StudentDTO> wrapper = JoinWrapper.create(StudentDO.class, StudentDTO.class);
        // mp原生查询
        List<StudentDO> students = studentMapper.selectList(wrapper);
        printObject(students);
        printObject(students);

        // 动态结果集正常查询
        List<StudentDTO> studentDTOS = studentMapper.selectListJoin(wrapper);
        printObject(studentDTOS);
    }
    
    /**
     * 动态返回结果集
     *
     * @author huangchengxing
     * @date 2022/2/11 13:57
     */
    @Test
    void testDynamicResult() {
        // 指定别名
        JoinWrapper<StudentDO, StudentDTO> wrapper = JoinWrapper.create(StudentDO.class, StudentDTO.class)
            .selectAll()
            .select(StudentDO::getName, StudentDTO::getStudentName)
            .select(StudentDO::getId, StudentDTO::getStudentId);
        List<StudentDTO> studentDTOS = studentMapper.selectListJoin(wrapper);
        printObject(studentDTOS);
        // SELECT t1.*, t1.name AS student_name, t1.id AS student_id FROM student t1
    }

    /**
     * 扩展方法
     *
     * @author huangchengxing
     * @date 2022/2/11 13:57
     */
    @Test
    void testExtendCondition() {
        JoinWrapper<StudentDO, StudentDTO> wrapper = JoinWrapper.create(StudentDO.class, StudentDTO.class)
            // 为原方法直接整合校验
            .eqIfNotNull(StudentDO::getName, null)
            // 为原方法添加lambda表达式校验的重载
            .eq(Objects::nonNull, StudentDO::getName, null)
            // 补充缺少方法
            .notLikeRight(CharSequenceUtil::isNotBlank, StudentDO::getName, "小明")
            .limit(true, 1);

        // SELECT t1.* FROM student t1 WHERE (t1.name NOT LIKE '小明%') LIMIT 1
        List<StudentDTO> studentDTOS = studentMapper.selectListJoin(wrapper);
        printObject(studentDTOS);
    }

    /**
     * 函数字段方法
     *
     * @author huangchengxing
     * @date 2022/2/11 13:57
     */
    @Test
    void testFuncColumn() {
        // 查询分数，并根据分段给出评价
        JoinWrapper<ScoreDO, StudentDTO> wrapper = JoinWrapper.create(ScoreDO.class, StudentDTO.class);
        wrapper.select(ScoreDO::getScore)
            .caseByCondition(StudentDTO::getRemark)
                .when(wrapper.toTableColumn(ScoreDO::getScore), Condition.GE, 90, "'优'")
                .when(wrapper.toTableColumn(ScoreDO::getScore), Condition.GE, 60, "'及格'")
                .el(() -> "'不及格'")
            .end();

        // SELECT t1.score, (CASE t1.score WHEN t1.score >= 90 THEN '优' WHEN t1.score >= 60 THEN '及格' ELSE '不及格' END) AS remark FROM score t1
        List<StudentDTO> studentDTOS = scoreMapper.selectListJoin(wrapper);
        printObject(studentDTOS);
    }

    /**
     * 函数字段方法
     *
     * @author huangchengxing
     * @date 2022/2/11 13:57
     */
    @Test
    void testFuncColumnWhere() {
        // 查询差5分满分的人的考试成绩
        JoinWrapper<ScoreDO, StudentDTO> wrapper = JoinWrapper.create(ScoreDO.class, StudentDTO.class);
        wrapper.selectAll()
            .where(Columns.plus(wrapper.toTableColumn(ScoreDO::getScore), 5), Condition.EQ, 100);

        // SELECT t1.* FROM score t1 WHERE ((t1.score + 5) = 100)
        List<StudentDTO> studentDTOS = scoreMapper.selectListJoin(wrapper);
        printObject(studentDTOS);
    }

    /**
     * 测试分组与集合函数
     *
     * @author huangchengxing
     * @date 2022/2/11 13:57
     */
    @Test
    void testGroupHaving() {
        // 查询挂了不止1人的科目的挂科人数
        JoinWrapper<ScoreDO, StudentDTO> wrapper = JoinWrapper.create(ScoreDO.class, StudentDTO.class);
        wrapper.select(ScoreDO::getCourseId, StudentDTO::getCourseId)
            .select(Columns.count(), StudentDTO::getNum)
            .where(ScoreDO::getScore, Condition.LT, 60)
            .groupBy(ScoreDO::getCourseId)
            .having(Columns.count(), Condition.GT, 1);

        // SELECT t1.course_id AS course_id, COUNT(*) AS num FROM score t1 WHERE (t1.score < 60) GROUP BY t1.course_id HAVING COUNT(*) > 1
        List<StudentDTO> studentDTOS = scoreMapper.selectListJoin(wrapper);
        printObject(studentDTOS);
    }

    @Test
    void testJoin() {
        JoinWrapper<StudentDO, StudentDTO> wrapper = JoinWrapper.create(StudentDO.class, StudentDTO.class)
            .selectAll()
            .eqIfNotNull(StudentDO::getName, "小明")
            .leftJoin(ScoreDO.class, w -> w
                .on(StudentDO::getId, Condition.EQ, ScoreDO::getStudentId)
                .select(ScoreDO::getScore, StudentDTO::getScore)
                .le(ScoreDO::getScore, 60)
                .leftJoin(CourseDO.class, w2 -> w2
                    .on(ScoreDO::getCourseId, Condition.EQ, CourseDO::getId)
                    .select(CourseDO::getName, StudentDTO::getCourseName)
                    .likeIfNotBank(CourseDO::getType, "文科")
                )
            );
        List<StudentDTO> studentDTOS = studentMapper.selectListJoin(wrapper);
        System.out.println(studentDTOS);
    }

    /**
     * 测试join
     */
    @Test
    public void testJoinAndCondition() {
        // 查询学生成绩
        JoinWrapper<StudentDO, StudentDTO> wrapper = JoinWrapper.create(StudentDO.class, StudentDTO.class)
            .selectAll()
            .leftJoin(ScoreDO.class, w -> w
                .on(StudentDO::getId, Condition.EQ, ScoreDO::getStudentId)
                .select(ScoreDO::getScore, StudentDTO::getScore)
                .caseByCondition(StudentDTO::getRemark)
                    .when(w.toTableColumn(ScoreDO::getScore), Condition.GE, 90, "'优'")
                    .when(w.toTableColumn(ScoreDO::getScore), Condition.GE, 80, "'良'")
                    .when(w.toTableColumn(ScoreDO::getScore), Condition.GE, 60, "'及格'")
                    .el("'不及格'")
                .end()
                .leftJoin(CourseDO.class)
                .on(ScoreDO::getCourseId, Condition.EQ, CourseDO::getId)
                .select(CourseDO::getName, StudentDTO::getCourseName)
            );

        // SELECT t1.*, t2.score AS score, (CASE WHEN t2.score >= 90 THEN '优' WHEN t2.score >= 80 THEN '良' WHEN t2.score >= 60 THEN '及格' ELSE '不及格' END) AS remark, t3.name AS course_name FROM student t1 LEFT JOIN score t2 ON (t1.id = t2.student_id) LEFT JOIN course t3 ON (t2.course_id = t3.id)
        Page<StudentDTO> studentDTOS = scoreMapper.selectPageJoin(new Page<>(1, 3), wrapper);
        printObject(studentDTOS);
        System.out.println(scoreMapper.selectExistsJoin(wrapper));
        System.out.println(scoreMapper.selectCountJoin(wrapper));
    }

    /**
     * 测试连查的情况下的分组
     *
     * @author huangchengxing
     * @date 2022/2/11 17:41
     */
    @Test
    void testJoinGroupHaving() {
        // 查询各科目总成绩
        JoinWrapper<ScoreDO, StudentDTO> wrapper = JoinWrapper.create(ScoreDO.class, StudentDTO.class);
        wrapper.select(Columns.sum(wrapper.toTableColumn(ScoreDO::getScore)), StudentDTO::getScore)
            .leftJoin(CourseDO.class, w -> w
                .on(ScoreDO::getCourseId, Condition.EQ, CourseDO::getId)
                .select(CourseDO::getName, StudentDTO::getCourseName)
                .groupBy(CourseDO::getName)
            )
            .groupBy(ScoreDO::getCourseId);

        // SELECT SUM(t1.score) AS score, t2.name AS course_name FROM score t1 LEFT JOIN course t2 ON (t1.course_id = t2.id) GROUP BY t2.name,t1.course_id
        List<StudentDTO> studentDTOS = scoreMapper.selectListJoin(wrapper);
        printObject(studentDTOS);
    }

    /**
     * 基于逻辑表连查
     *
     * @author huangchengxing
     * @date 2022/2/11 13:57
     */
    @Test
    void testLogicJoin() {
        // 查询挂科了不止1人的科目的挂科人数
        JoinWrapper<ScoreDO, StudentDTO> wrapper = JoinWrapper.create(ScoreDO.class, StudentDTO.class);
        wrapper.select(ScoreDO::getCourseId, StudentDTO::getCourseId)
            .select(Columns.count(), StudentDTO::getNum)
            .where(ScoreDO::getScore, Condition.LT, 60)
            .groupBy(ScoreDO::getCourseId)
            .having(Columns.count(), Condition.GT, 1);

        // 然后查询该科目名称
        JoinWrapper<StudentDTO, StudentDTO> logicTable = wrapper.toLogicTable()
            .selectAll()
            .leftJoin(CourseDO.class, w -> w
                .on(StudentDTO::getCourseId, Condition.EQ, CourseDO::getId)
                .select(CourseDO::getName, StudentDTO::getCourseName)
            );
        System.out.println(logicTable.getTable());

        // SELECT t1.*, t2.name AS course_name FROM (SELECT t1.course_id AS course_id, COUNT(*) AS num FROM score t1 WHERE (t1.score < 60) GROUP BY t1.course_id HAVING COUNT(*) > 1) t1 LEFT JOIN course t2 ON (t1.course_id = t2.id)
        List<StudentDTO> studentDTOS = scoreMapper.selectListJoin(logicTable);
        printObject(studentDTOS);
    }

    /**
     * 连查逻辑表
     *
     * @author huangchengxing
     * @date 2022/2/11 13:57
     */
    @Test
    void testJoinLogic() {
        // 查询挂科了不止1人的科目的挂科人数
        JoinWrapper<ScoreDO, StudentDTO> logicTable = JoinWrapper.create(ScoreDO.class, StudentDTO.class);
        logicTable.select(ScoreDO::getCourseId, StudentDTO::getCourseId)
            .select(Columns.count(), StudentDTO::getNum)
            .where(ScoreDO::getScore, Condition.LT, 60)
            .groupBy(ScoreDO::getCourseId)
            .having(Columns.count(), Condition.GT, "1");

        JoinWrapper<CourseDO, StudentDTO> wrapper = JoinWrapper.create(CourseDO.class, StudentDTO.class);
        wrapper.selectAll()
            .innerJoin(logicTable)
            .on(CourseDO::getId, Condition.EQ, StudentDTO::getCourseId)
            .selectAll();

        // SELECT t1.*, t2.name AS course_name FROM (SELECT t1.course_id AS course_id, COUNT(*) AS num FROM score t1 WHERE (t1.score < 60) GROUP BY t1.course_id HAVING COUNT(*) > 1) t1 LEFT JOIN course t2 ON (t1.course_id = t2.id)
        List<StudentDTO> studentDTOS = scoreMapper.selectListJoin(wrapper);
        printObject(studentDTOS);
    }
}
