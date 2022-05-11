package top.xiajibagao.mybatis.plus.join;

import cn.hutool.core.text.CharSequenceUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.xiajibagao.mybatis.plus.join.constants.Condition;
import top.xiajibagao.mybatis.plus.join.example.mapper.ScoreMapper;
import top.xiajibagao.mybatis.plus.join.example.mapper.StudentMapper;
import top.xiajibagao.mybatis.plus.join.example.model.*;
import top.xiajibagao.mybatis.plus.join.wrapper.JoinWrapper;
import top.xiajibagao.mybatis.plus.join.wrapper.column.Columns;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@SpringBootTest
class MybatisPlusJoinTableApplicationTests {

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private ScoreMapper scoreMapper;

    @SneakyThrows
    protected void printObject(Object target) {
        System.out.println(JSON.toJSONString(target));
    }

    /**
     * 兼容原生普通查询
     *
     * @author huangchengxing
     * @date 2022/2/11 13:57
     */
    @Test
    void testMPSelect() {
        JoinWrapper<StudentDO, ExampleResult> wrapper = JoinWrapper.create(StudentDO.class, ExampleResult.class);
        // mp原生查询
        List<StudentDO> students = studentMapper.selectList(wrapper);
        printObject(students);
        List<BaseDO> expected = Arrays.asList(
            new StudentDO().setName("小明").setId(1),
            new StudentDO().setName("小红").setId(2),
            new StudentDO().setName("小雷").setId(3)
        );
        Assertions.assertEquals(JSON.toJSONString(expected), JSON.toJSONString(students));

        // 动态结果集正常查询
        List<ExampleResult> actual = studentMapper.selectListJoin(wrapper);
        printObject(actual);
        Assertions.assertEquals(JSON.toJSONString(expected), JSON.toJSONString(actual));
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
        JoinWrapper<StudentDO, ExampleResult> wrapper = JoinWrapper.create(StudentDO.class, ExampleResult.class)
            .selectAll()
            .select(StudentDO::getName, ExampleResult::getStudentName)
            .select(StudentDO::getId, ExampleResult::getStudentId);
        List<ExampleResult> actual = studentMapper.selectListJoin(wrapper);
        printObject(actual);
        // SELECT t1.*, t1.name AS student_name, t1.id AS student_id FROM student t1

        List<ExampleResult> expected = Arrays.asList(
            new ExampleResult().setName("小明").setStudentName("小明").setStudentId(1).setId(1),
            new ExampleResult().setName("小红").setStudentName("小红").setStudentId(2).setId(2),
            new ExampleResult().setName("小雷").setStudentName("小雷").setStudentId(3).setId(3)
        );
        Assertions.assertEquals(expected, actual);
    }

    /**
     * 扩展方法
     *
     * @author huangchengxing
     * @date 2022/2/11 13:57
     */
    @Test
    void testExtendCondition() {
        JoinWrapper<StudentDO, ExampleResult> wrapper = JoinWrapper.create(StudentDO.class, ExampleResult.class)
            // 为原方法直接整合校验
            .eqIfNotNull(StudentDO::getName, null)
            // 为原方法添加lambda表达式校验的重载
            .eq(Objects::nonNull, StudentDO::getName, null)
            // 补充缺少方法
            .notLikeRight(CharSequenceUtil::isNotBlank, StudentDO::getName, "小明")
            .limit(true, 1);

        // SELECT t1.* FROM student t1 WHERE (t1.name NOT LIKE '小明%') LIMIT 1
        List<ExampleResult> actual = studentMapper.selectListJoin(wrapper);
        printObject(actual);

        List<BaseDO> expected = Arrays.asList(new StudentDO().setName("小红").setId(2));
        Assertions.assertEquals(JSON.toJSONString(expected), JSON.toJSONString(actual));
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
        JoinWrapper<ScoreDO, ExampleResult> wrapper = JoinWrapper.create(ScoreDO.class, ExampleResult.class);
        wrapper.select(ScoreDO::getScore)
            .caseByCondition(ExampleResult::getRemark)
            .when(wrapper.toTableColumn(ScoreDO::getScore), Condition.GE, 90, "'优'")
            .when(wrapper.toTableColumn(ScoreDO::getScore), Condition.GE, 60, "'及格'")
            .el(() -> "'不及格'")
            .end();

        // SELECT t1.score, (CASE t1.score WHEN t1.score >= 90 THEN '优' WHEN t1.score >= 60 THEN '及格' ELSE '不及格' END) AS remark FROM score t1
        List<ExampleResult> actual = scoreMapper.selectListJoin(wrapper);
        printObject(actual);

        List<ExampleResult> expected = Arrays.asList(
            new ExampleResult().setScore(95).setRemark("优"),
            new ExampleResult().setScore(85).setRemark("及格"),
            new ExampleResult().setScore(53).setRemark("不及格"),
            new ExampleResult().setScore(81).setRemark("及格"),
            new ExampleResult().setScore(93).setRemark("优"),
            new ExampleResult().setScore(100).setRemark("优"),
            new ExampleResult().setScore(59).setRemark("不及格"),
            new ExampleResult().setScore(89).setRemark("及格"),
            new ExampleResult().setScore(52).setRemark("不及格")
        );
        Assertions.assertEquals(expected, actual);
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
        JoinWrapper<ScoreDO, ExampleResult> wrapper = JoinWrapper.create(ScoreDO.class, ExampleResult.class);
        wrapper.selectAll()
            .whereIfNotNull(Columns.plus(wrapper.toTableColumn(ScoreDO::getScore), 5), Condition.EQ, 100);

        // SELECT t1.* FROM score t1 WHERE ((t1.score + 5) = 100)
        List<ExampleResult> actual = scoreMapper.selectListJoin(wrapper);
        printObject(actual);

        List<ExampleResult> expected = Arrays.asList(
            new ExampleResult().setScore(95).setId(1).setCourseId("1").setStudentId(1)
        );
        Assertions.assertEquals(expected, actual);
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
        JoinWrapper<ScoreDO, ExampleResult> wrapper = JoinWrapper.create(ScoreDO.class, ExampleResult.class);
        wrapper.select(ScoreDO::getCourseId, ExampleResult::getCourseId)
            .select(Columns.count(), ExampleResult::getNum)
            .whereIfNotNull(ScoreDO::getScore, Condition.LT, 60)
            .groupBy(ScoreDO::getCourseId)
            .having(Columns.count(), Condition.GT, 1);

        // SELECT t1.course_id AS course_id, COUNT(*) AS num FROM score t1 WHERE (t1.score < 60) GROUP BY t1.course_id HAVING COUNT(*) > 1
        List<ExampleResult> actual = scoreMapper.selectListJoin(wrapper);
        printObject(actual);

        List<ExampleResult> expected = Arrays.asList(
            new ExampleResult().setCourseId("3").setNum(2)
        );
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testJoin() {
        JoinWrapper<StudentDO, ExampleResult> wrapper = JoinWrapper.create(StudentDO.class, ExampleResult.class)
            .selectAll()
            .eqIfNotNull(StudentDO::getName, "小明")
            .leftJoin(ScoreDO.class, w -> w
                .on(StudentDO::getId, Condition.EQ, ScoreDO::getStudentId)
                .select(ScoreDO::getScore, ExampleResult::getScore)
                .le(ScoreDO::getScore, 60)
                .leftJoin(CourseDO.class, w2 -> w2
                    .on(ScoreDO::getCourseId, Condition.EQ, CourseDO::getId)
                    .select(CourseDO::getName, ExampleResult::getCourseName)
                    .likeIfNotBank(CourseDO::getType, "文科")
                )
            );
        List<ExampleResult> actual = studentMapper.selectListJoin(wrapper);
        System.out.println(actual);

        List<ExampleResult> expected = Arrays.asList(
            new ExampleResult().setId(1).setName("小明").setCourseName("地理").setScore(53)
        );
        Assertions.assertEquals(expected, actual);
    }

    /**
     * 测试join
     */
    @Test
    void testJoinAndCondition() {
        // 查询学生成绩
        JoinWrapper<StudentDO, ExampleResult> wrapper = JoinWrapper.create(StudentDO.class, ExampleResult.class)
            .selectAll()
            .leftJoin(ScoreDO.class, w -> w
                .on(StudentDO::getId, Condition.EQ, ScoreDO::getStudentId)
                .select(ScoreDO::getScore, ExampleResult::getScore)
                .caseByCondition(ExampleResult::getRemark)
                .when(w.toTableColumn(ScoreDO::getScore), Condition.GE, 90, "'优'")
                .when(w.toTableColumn(ScoreDO::getScore), Condition.GE, 80, "'良'")
                .when(w.toTableColumn(ScoreDO::getScore), Condition.GE, 60, "'及格'")
                .el("'不及格'")
                .end()
                .leftJoin(CourseDO.class)
                .on(ScoreDO::getCourseId, Condition.EQ, CourseDO::getId)
                .select(CourseDO::getName, ExampleResult::getCourseName)
            );

        // SELECT t1.*, t2.score AS score, (CASE WHEN t2.score >= 90 THEN '优' WHEN t2.score >= 80 THEN '良' WHEN t2.score >= 60 THEN '及格' ELSE '不及格' END) AS remark, t3.name AS course_name FROM student t1 LEFT JOIN score t2 ON (t1.id = t2.student_id) LEFT JOIN course t3 ON (t2.course_id = t3.id)
        Page<ExampleResult> actual = scoreMapper.selectPageJoin(new Page<>(1, 3), wrapper);
        printObject(actual);

        Assertions.assertTrue(scoreMapper.selectExistsJoin(wrapper));
        Assertions.assertEquals(9, scoreMapper.selectCountJoin(wrapper));
        List<ExampleResult> expected = Arrays.asList(
            new ExampleResult().setId(1).setName("小明").setCourseName("地理").setScore(53).setRemark("不及格"),
            new ExampleResult().setId(1).setName("小明").setCourseName("数学").setScore(85).setRemark("良"),
            new ExampleResult().setId(1).setName("小明").setCourseName("物理").setScore(95).setRemark("优")
        );
        Assertions.assertEquals(expected, actual.getRecords());
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
        JoinWrapper<ScoreDO, ExampleResult> wrapper = JoinWrapper.create(ScoreDO.class, ExampleResult.class);
        wrapper.select(Columns.sum(wrapper.toTableColumn(ScoreDO::getScore)), ExampleResult::getScore)
            .leftJoin(CourseDO.class, w -> w
                .on(ScoreDO::getCourseId, Condition.EQ, CourseDO::getId)
                .select(CourseDO::getName, ExampleResult::getCourseName)
                .groupBy(CourseDO::getName)
            )
            .groupBy(ScoreDO::getCourseId);

        // SELECT SUM(t1.score) AS score, t2.name AS course_name FROM score t1 LEFT JOIN course t2 ON (t1.course_id = t2.id) GROUP BY t2.name,t1.course_id
        List<ExampleResult> actual = scoreMapper.selectListJoin(wrapper);
        printObject(actual);

        List<ExampleResult> expected = Arrays.asList(
            new ExampleResult().setCourseName("物理").setScore(235),
            new ExampleResult().setCourseName("数学").setScore(267),
            new ExampleResult().setCourseName("地理").setScore(205)
        );
        Assertions.assertEquals(expected, actual);
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
        JoinWrapper<ScoreDO, ExampleResult> wrapper = JoinWrapper.create(ScoreDO.class, ExampleResult.class);
        wrapper.select(ScoreDO::getCourseId, ExampleResult::getCourseId)
            .select(Columns.count(), ExampleResult::getNum)
            .whereIfNotNull(ScoreDO::getScore, Condition.LT, 60)
            .groupBy(ScoreDO::getCourseId)
            .having(Columns.count(), Condition.GT, 1);

        // 然后查询该科目名称
        JoinWrapper<ExampleResult, ExampleResult> logicTable = wrapper.toLogicTable()
            .selectAll()
            .leftJoin(CourseDO.class, w -> w
                .on(ExampleResult::getCourseId, Condition.EQ, CourseDO::getId)
                .select(CourseDO::getName, ExampleResult::getCourseName)
            );
        System.out.println(logicTable.getTable());

        // SELECT t1.*, t2.name AS course_name FROM (SELECT t1.course_id AS course_id, COUNT(*) AS num FROM score t1 WHERE (t1.score < 60) GROUP BY t1.course_id HAVING COUNT(*) > 1) t1 LEFT JOIN course t2 ON (t1.course_id = t2.id)
        List<ExampleResult> actual = scoreMapper.selectListJoin(logicTable);
        printObject(actual);

        List<ExampleResult> expected = Arrays.asList(
            new ExampleResult().setCourseId("3").setCourseName("地理").setNum(2)
        );
        Assertions.assertEquals(expected, actual);
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
        JoinWrapper<ScoreDO, ExampleResult> logicTable = JoinWrapper.create(ScoreDO.class, ExampleResult.class);
        logicTable.select(ScoreDO::getCourseId, ExampleResult::getCourseId)
            .select(Columns.count(), ExampleResult::getNum)
            .whereIfNotNull(ScoreDO::getScore, Condition.LT, 60)
            .groupBy(ScoreDO::getCourseId)
            .having(Columns.count(), Condition.GT, "1");

        JoinWrapper<CourseDO, ExampleResult> wrapper = JoinWrapper.create(CourseDO.class, ExampleResult.class);
        wrapper.selectAll()
            .innerJoin(logicTable)
            .on(CourseDO::getId, Condition.EQ, ExampleResult::getCourseId)
            .selectAll();

        // SELECT t1.*, t2.name AS course_name FROM (SELECT t1.course_id AS course_id, COUNT(*) AS num FROM score t1 WHERE (t1.score < 60) GROUP BY t1.course_id HAVING COUNT(*) > 1) t1 LEFT JOIN course t2 ON (t1.course_id = t2.id)
        List<ExampleResult> actual = scoreMapper.selectListJoin(wrapper);
        printObject(actual);

        List<ExampleResult> expected = Arrays.asList(
            new ExampleResult().setId(3).setCourseId("3").setName("地理").setNum(2)
        );
        Assertions.assertEquals(expected, actual);
    }

    /**
     * 连查逻辑表
     *
     * @author huangchengxing
     * @date 2022/2/11 13:57
     */
    @Test
    void testSub() {
        // 查询挂科了不止1人的科目
        JoinWrapper<CourseDO, ExampleResult> wrapper = JoinWrapper.create(CourseDO.class, ExampleResult.class);
        wrapper.selectAll()
            .where(wrapper.toTableColumn(CourseDO::getId), Condition.IN, Columns.subQuery(
                JoinWrapper.create(ScoreDO.class, ExampleResult.class)
                    .select(ScoreDO::getCourseId, ExampleResult::getCourseId)
                    .whereIfNotNull(ScoreDO::getScore, Condition.LT, 60)
                    .groupBy(ScoreDO::getCourseId)
                    .having(Columns.count(), Condition.GT, "1")
            ));

        // SELECT t1.*, t2.name AS course_name FROM (SELECT t1.course_id AS course_id, COUNT(*) AS num FROM score t1 WHERE (t1.score < 60) GROUP BY t1.course_id HAVING COUNT(*) > 1) t1 LEFT JOIN course t2 ON (t1.course_id = t2.id)
        List<ExampleResult> actual = scoreMapper.selectListJoin(wrapper);
        printObject(actual);

        List<ExampleResult> expected = Arrays.asList(
            new ExampleResult().setId(3).setName("地理")
        );
        Assertions.assertEquals(expected, actual);
    }
}
