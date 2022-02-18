## 一、简介

本项目基于 mybatis-plus，提供通过条件构造器以代码方式构造 join 查询的相关功能。

开发的初衷是为了解决mp日常使用中感觉到的一些痛点的，比如条件构造器不支持join语法，lambda表达式版本的group...having支持不够、查询字段与条件字段都不支持数据库函数，不支持逻辑表，像in或eq这类的方法需要重复添加判空条件......等等。

本框架旨保留mp原功能的基础上，基于`Wrapper`类扩展一个新的`JoinWrapper`以在不修改已有代码的基础上支持上述功能。

## 二、快速开始

1. 引入 mybatis-plus-boot-starter 与 mybatis-plus-join 依赖：

   ~~~xml
   <dependency>
       <groupId>top.xiajibagao</groupId>
       <artifactId>mybatis-plus-join</artifactId>
       <version>${version}</version>
   </dependency>
   
   <depeendency>
       <groupId>com.baomidou</groupId>
       <artifactId>mybatis-plus-boot-starter</artifactId>
       <version>${mybatis-plus.version}</version>
   </depeendency>
   ~~~

   > 本项目需要自行引入依赖 `mybatis-plus-boot-starter`，此外其他依赖皆不向下传递

2. 将动态返回值插件`DynamicResultInterceptor`注册到 `com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean` 中 ，

2. 然后将扩展 SQL 注入器 `JoinMethodInjector`注入到 `com.baomidou.mybatisplus.core.config.GlobalConfig`中。

   这里给出一个最简单配置：

   ~~~java
   @Bean
   public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
       MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
       sqlSessionFactory.setDataSource(dataSource);
   
       // 注册动态返回值插件
       sqlSessionFactory.setPlugins(new DynamicResultInterceptor());
   
       // 注册扩展sql注入器
       MybatisConfiguration configuration = new MybatisConfiguration();
       GlobalConfig globalConfig = GlobalConfigUtils.getGlobalConfig(configuration);
       globalConfig.setSqlInjector(new JoinMethodInjector());
       sqlSessionFactory.setConfiguration(configuration);
   
       return sqlSessionFactory.getObject();
   }
   ~~~

3. 令 `mapper`接口从继承 mp 提供的 `BaseMapper`换为 `JoinMapper`：

   ~~~java
   @Mapper
   public interface FooMapper<T> extend JoinMapper<T> {
       // ... ...
   }
   ~~~



## 三、核心功能

分别创建学生表 student，课程表 course 与考试分数表 score 三张表，其对应数据库脚本如下：

~~~sql
-- 课程表
CREATE TABLE `course`  (
    `id` int(0) NOT NULL AUTO_INCREMENT,
    `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
)

-- 考试分数表
CREATE TABLE `score`  (
    `id` int(0) NOT NULL AUTO_INCREMENT,
    `student_id` int(0) NULL DEFAULT NULL,
    `course_id` int(0) NULL DEFAULT NULL,
    `score` int(0) NULL DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
)

-- 学生表
CREATE TABLE `student`  (
    `id` int(0) NOT NULL AUTO_INCREMENT,
    `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
)
~~~

以下示例皆基于上述三表及对应实体。

### 1、字段别名

条件构造器可以指定主表与可以指定对象类型，需要指定返回值对象类型，并指定别名：

```java
// 查询学生id，学生名称并指定别名，其余与默认字段相同
JoinWrapper<StudentDO, StudentDTO> wrapper = JoinWrapper.create(StudentDO.class, StudentDTO.class)
    .select(StudentDO::getName, StudentDTO::getStudentName)
    .select(StudentDO::getId, StudentDTO::getStudentId)
    .selectAll();
List<StudentDTO> studentDTOS = studentMapper.selectListJoin(wrapper);
```

该条件构造器构造的 SQL 等同：

~~~sql
SELECT t1.*, t1.name AS student_name, t1.id AS student_id FROM student t1
~~~

### 2、扩展条件

`JoinWrapper`基于 mp 的条件构造器原有方法额外提供三个方向的扩展：

- 基于 Lambda 表达式的应用条件；
- 预设的应用条件：包括 `in/notInIfNotEmpty`，`eqIfNotNull`，`likeIfNotBank`，`between/notBetweenIfAllNotNull`等；
- 补充方法：包括 `notLikeRight`，`notLikeLeft`，`limit`等；

如：

~~~java
JoinWrapper<StudentDO, StudentDTO> wrapper = JoinWrapper.create(StudentDO.class, StudentDTO.class)
    // 基于lambda表达式的应用条件
    .eq(Objects::nonNull, StudentDO::getName, null)
    .in(t -> !t.isEmpty(), StudentDO::getId, Arrays.asList(1, 2, 3))
    // 预设应用条件
    .eqIfNotNull(StudentDO::getName, null)
    .inIfNotEmpty(StuedntDO::getId, Collections.emptyList())
    // 补充方法
    .notLikeRight(CharSequenceUtil::isNotBlank, StudentDO::getName, "小明")
    .limit(true, 1);

List<StudentDTO> studentDTOS = studentMapper.selectListJoin(wrapper);
~~~

最终构造的 SQL同：

~~~sql
SELECT t1.* FROM student t1 WHERE (t1.name NOT LIKE '小明%' and t1.id in (1, 2, 3)) LIMIT 1
~~~

### 3、连表查询

`JoinWrapper`支持构造关联查询：

~~~java
// 查询学生成绩
JoinWrapper<StudentDO, StudentDTO> wrapper = JoinWrapper.create(StudentDO.class, StudentDTO.class)
    .selectAll()
    .leftJoin(ScoreDO.class, w -> w
		.on(StudentDO::getId, Condition.EQ, ScoreDO::getStudentId)
		.select(ScoreDO::getScore, StudentDTO::getScore)
		.leftJoin(CourseDO.class)
		.on(ScoreDO::getCourseId, Condition.EQ, CourseDO::getId)
		.select(CourseDO::getName, StudentDTO::getCourseName)
     );

// 该写法等同于
JoinWrapper<StudentDO, StudentDTO> wrapper = JoinWrapper.create(StudentDO.class, StudentDTO.class)
	.selectAll()
	.leftJoin(ScoreDO.class, w -> w
		.on(StudentDO::getId, Condition.EQ, ScoreDO::getStudentId)
		.select(ScoreDO::getScore, StudentDTO::getScore)
		.leftJoin(CourseDO.class, w2 -> w2
			.on(ScoreDO::getCourseId, Condition.EQ, CourseDO::getId)
			.select(CourseDO::getName, StudentDTO::getCourseName)
		)
	);
~~~

该条件构造器构造的 SQL 等同：

~~~sql
SELECT t1.*, t2.score AS score, t3.name AS course_name 
FROM student t1 
LEFT JOIN score t2 ON (t1.id = t2.student_id) 
LEFT JOIN course t3 ON (t2.course_id = t3.id)
~~~

支持的关联查询包括 `fulljoin`、`left join`、`right join`、`inner join` 四种，关联的每张表都可以添加复数的普通条件、关联条件(on)以及查询字段，比如：

~~~java
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
~~~

该条件构造器构造的 SQL 如下：

~~~sql
 SELECT t1.*, t2.score AS score, t3.name AS course_name
 FROM student t1
 LEFT JOIN score t2
 ON (t1.id = t2.student_id)
 LEFT JOIN course t3
 ON (t2.course_id = t3.id)
 WHERE (t1.name = '小明' AND t2.score <= 60 AND t3.type LIKE '%文科%');
~~~

### 4、数据库函数字段

`JoinWrapper`支持将数据库函数作为字段，可以有三种用法：

- 作为查询字段，如：`select ifNull(a.name, 'fack name')`；
- 作为查询条件，包括 where 与 having 条件；
- 用于函数嵌套，如 `concat('user: ', ifNull(a.name, 'fack name'))`；

由于在关联查询时必须指定表字段来源表的别名，因此创建表字段需要通过 `JoinWrapper.toTableColumn()`将字段与表进行绑定，然后可通过函数字段工厂类`top.xiajibagao.mybatis.plus.join.wrapper.column.Columns`对获取的字段进行函数化。

支持的函数：

- 日期类：now, currentTimestamp, currentDate, currentTime, dateFormat, day, month, year;
- 数学：abs, avg, max, min, sum, rand, count;
- 字符串：ifNull, concat, format, replace, upper, lower;
- 控制流：case..then...when...else;

> **注意**：部分函数可能不受某些数据库支持，请根据自己项目使用的数据库选择性使用

#### Select

如：

```java
// 查询分数，并根据分段给出评价
JoinWrapper<ScoreDO, StudentDTO> wrapper = JoinWrapper.create(ScoreDO.class, StudentDTO.class);
wrapper.select(ScoreDO::getScore)
    .caseByCondition(StudentDTO::getRemark)
        .when(wrapper.toTableColumn(ScoreDO::getScore), Condition.GE, 90, "'优'")
        .when(wrapper.toTableColumn(ScoreDO::getScore), Condition.GE, 60, "'及格'")
        .el(() -> "'不及格'")
    .end();
```

该条件构造器构造的 SQL 同：

~~~sql
SELECT t1.score, (
    CASE t1.score 
    WHEN t1.score >= 90 THEN '优' 
    WHEN t1.score >= 60 THEN '及格' 
    ELSE '不及格' END
) AS remark 
FROM score t1
~~~

#### Where

像函数这类特殊的字段需要依靠`where()`方法构建：

~~~java
JoinWrapper<ScoreDO, StudentDTO> wrapper = JoinWrapper.create(ScoreDO.class, StudentDTO.class);
wrapper.selectAll()
    .where(Columns.plus(wrapper.toTableColumn(ScoreDO::getScore), 5), Condition.EQ, 100);
~~~

构建的 SQL 同：

~~~sql
SELECT t1.* FROM score t1 WHERE ((t1.score + 5) = 100)
~~~

#### Having

Having 关键字需要配合 group by 使用：

~~~java
// 查询挂了不止1人的科目的挂科人数
JoinWrapper<ScoreDO, StudentDTO> wrapper = JoinWrapper.create(ScoreDO.class, StudentDTO.class);
wrapper.select(ScoreDO::getCourseId, StudentDTO::getCourseId)
    .select(Columns.count(), StudentDTO::getNum)
    .where(ScoreDO::getScore, Condition.LT, 60)
    .groupBy(ScoreDO::getCourseId)
    .having(Columns.count(), Condition.GT, 1);
~~~

构建的 SQL 同：

~~~sql
SELECT t1.course_id AS course_id, COUNT(*) AS num 
FROM score t1 
WHERE (t1.score < 60) 
GROUP BY t1.course_id 
HAVING COUNT(*) > 1
~~~

### 5、子查询

JoinWrapper 允许将一个已经构造好的条件构造器转为一张逻辑表/临时表，并用于子查询。

#### JOIN

~~~java
// 查询挂科超过1人的科目的挂科人数
JoinWrapper<ScoreDO, StudentDTO> logicTable = JoinWrapper.create(ScoreDO.class, StudentDTO.class);
logicTable.select(ScoreDO::getCourseId, StudentDTO::getCourseId)
    .select(Columns.count(), StudentDTO::getNum)
    .where(ScoreDO::getScore, Condition.LT, 60)
    .groupBy(ScoreDO::getCourseId)
    .having(Columns.count(), Condition.GT, "1");

// 查询挂科超过1人的科目的科目信息与挂科人数
JoinWrapper<CourseDO, StudentDTO> wrapper = JoinWrapper.create(CourseDO.class, StudentDTO.class);
wrapper.selectAll()
    // 关联逻辑表
    .innerJoin(logicTable)
    .on(CourseDO::getId, Condition.EQ, StudentDTO::getCourseId)
    .selectAll();
~~~

该条件构造器构造的 SQL 同：

~~~sql
 SELECT t1.*, t2.*
 FROM course t1
 INNER JOIN (
     SELECT t1.course_id AS course_id, COUNT(*) AS num
     FROM score t1
     WHERE (t1.score < 60) GROUP BY t1.course_id HAVING COUNT(*) > 1
 ) t2 ON (t1.id = t2.course_id);
~~~

#### FROM

~~~java
// 查询挂科超过1人的科目及挂科人数
JoinWrapper<ScoreDO, StudentDTO> wrapper = JoinWrapper.create(ScoreDO.class, StudentDTO.class);
wrapper.select(ScoreDO::getCourseId, StudentDTO::getCourseId)
    .select(Columns.count(), StudentDTO::getNum)
    .where(ScoreDO::getScore, Condition.LT, 60)
    .groupBy(ScoreDO::getCourseId)
    .having(Columns.count(), Condition.GT, 1);

// 将上一查询转为逻辑表，然后查询该科目名称
JoinWrapper<StudentDTO, StudentDTO> logicTable = wrapper.toLogicTable()
    .selectAll()
    .leftJoin(CourseDO.class, w -> w
		.on(StudentDTO::getCourseId, Condition.EQ, CourseDO::getId)
		.select(CourseDO::getName, StudentDTO::getCourseName)
	);
~~~

该条件构造器构造的 SQL 同：

~~~sql
SELECT t1.*, t2.name AS course_name 
FROM (
    SELECT t1.course_id AS course_id, COUNT(*) AS num 
    FROM score t1 
    WHERE (t1.score < 60) 
    GROUP BY t1.course_id 
    HAVING COUNT(*) > 1
) t1 
LEFT JOIN course t2 ON (t1.course_id = t2.id)
~~~

#### WHERE

~~~java
// 查询挂科人数超过1人的科目
JoinWrapper<CourseDO, StudentDTO> wrapper = JoinWrapper.create(CourseDO.class, StudentDTO.class);
wrapper.selectAll()
    .where(wrapper.toTableColumn(CourseDO::getId), Condition.IN, Columns.subQuery(
        JoinWrapper.create(ScoreDO.class, StudentDTO.class)
            .select(ScoreDO::getCourseId, StudentDTO::getCourseId)
            .where(ScoreDO::getScore, Condition.LT, 60)
            .groupBy(ScoreDO::getCourseId)
            .having(Columns.count(), Condition.GT, "1")
    ));
~~~

该条件构造器构造的 SQL 同：

~~~sql
SELECT t1.*
FROM course t1
WHERE (
    t1.id IN (
        SELECT t1.course_id AS course_id
        FROM score t1
        WHERE (t1.score < 60) GROUP BY t1.course_id HAVING COUNT(*) > 1
    )
);
~~~



### 6、原生方法适配

#### 兼容BaseMapper方法

JoinWrapper 兼容 mp 原生 Wrapper 中**除`setEntity()`外**的全部查询方法，并且也可以直接作为参数传入 BaseMapper 的方法中：

~~~java
// BaseMapper.selectList(Wrapper<T> wrapper)
JoinWrapper<StudentDO, StudentDTO> wrapper = JoinWrapper.create(StudentDO.class, StudentDTO.class);
List<StudentDO> students = studentMapper.selectList(wrapper);
~~~

不过这样使用时，除 Join 条件将不生效外，其余扩展功能仍可以正常使用。

#### 逻辑删除

当配置了逻辑删除时（具体配置参见[mybaits plus逻辑删除](https://baomidou.com/pages/6b03c5/)），JoinWrapper 将在初始化时，自动逻辑删除字段作为查询条件添加到 Where 条件后，关联表亦同。

假设现在已有配置：

~~~yml
mybatis-plus:
 global-config:
  db-config:
   logic-delete-field: isDelete
   logic-delete-value: 1
   logic-not-delete-value: 0
~~~

当我们使用条件构造成构建一个查询时，会自动从 `com.baomidou.mybatisplus.core.metadata.TableInfo`获取逻辑删除相关配置，并自动添加条件 `logic-delete-field = login-not-delete-value`，如：

~~~java
JoinWrapper<StudentDO, StudentDTO> wrapper = JoinWrapper.create(StudentDO.class, StudentDTO.class);
        List<StudentDO> students = studentMapper.selectList(wrapper);
~~~

若 `StudentDO`及对于表存在字段`is_delete`，且已有相关逻辑删除配置，则实际构造出的 SQL 为：

~~~sql
SELECT * FROM student t1 where t1.is_delete = 0
~~~

#### 分页

参见[mybtis-plus分页插件](https://baomidou.com/pages/97710a/#paginationinnerinterceptor)，该插件基于 SQL 分析生效，因此不受影响。

但是要注意，与当使用`JoinWrapper`构建关联查询时，与原写法一样，若 join 的表没有 where 条件，则生成的 countSql 会忽略 join 部分的表导致查询数据行数与实际待分页数据行数不一致。 