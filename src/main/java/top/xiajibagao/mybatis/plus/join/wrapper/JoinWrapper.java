package top.xiajibagao.mybatis.plus.join.wrapper;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import lombok.Getter;
import top.xiajibagao.mybatis.plus.join.constants.Condition;
import top.xiajibagao.mybatis.plus.join.constants.ExtendConstants;
import top.xiajibagao.mybatis.plus.join.constants.JoinType;
import top.xiajibagao.mybatis.plus.join.helper.SqlUtils;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * 支持join的条件构造器
 *
 * @param <T> 查询主表实体类型
 * @param <R> 查询返回实体类型
 * @author huangchengxing
 * @date 2022/02/09 10:38
 */
public class JoinWrapper<T, R> extends AbstractDynamicResultWrapper<T, R, JoinWrapper<T, R>> {

    /**
     * 当前join表数量
     */
    protected AtomicInteger joinTableSeq;

    /**
     * join的表
     */
    protected List<JoinTable<?, ?, R>> joinTableList;
    
    /**
     * 是否存在join语句
     *
     * @return boolean
     * @author huangchengxing
     * @date 2022/2/10 13:18
     */
    public boolean hasJoin() {
        return CollUtil.isNotEmpty(joinTableList);
    }

    /**
     * 创建条件构造器
     *
     * @param targetClass 查询主表类型
     * @param resultClass 返回类型
     * @return com.xiajibagao.top.mybatis.plus.concatSegment.wrapper.JoinMapper<T,R>
     * @author huangchengxing
     * @date 2022/2/9 15:37
     */
    public static <T, R> JoinWrapper<T, R> create(@Nonnull Class<T> targetClass, @Nonnull Class<R> resultClass) {
        JoinWrapper<T, R> result = new JoinWrapper<>(targetClass, resultClass, false);
        result.initNeed();
        result.setAlisaByJoinSeq();
        result.initLogicDelete();
        return result;
    }

    protected JoinWrapper(@Nonnull Class<T> targetClass, @Nonnull Class<R> resultClass, boolean isLogic) {
        super(targetClass, resultClass, isLogic);
    }
    
    /**
     * 设置表别名，为连表时保证别名不重复，故不允许修改别名
     *
     * @param alisa 表别名
     * @author huangchengxing
     * @throws UnsupportedOperationException 调用时抛出
     * @date 2022/2/10 16:12
     */
    @Override
    public void setAlisa(String alisa) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("无允许自行设置别名");
    }

    /**
     * 设置当前表别名
     *
     * @author huangchengxing
     * @date 2022/2/10 13:06
     */
    protected void setAlisaByJoinSeq() {
        this.alisa = "t" + joinTableSeq.incrementAndGet();
    }

    /**
     * 获取“LEFT JOIN xxx a on a.id = b.aid”格式的join语句
     *
     * @return java.lang.String
     * @author huangchengxing
     * @date 2022/2/9 13:32
     */
    public String getSqlJoin() {
        if (CollUtil.isEmpty(joinTableList)) {
            return ExtendConstants.EMPTY;
        }
        return joinTableList.stream()
            .map(JoinTable::getSqlJoin)
            .filter(CharSequenceUtil::isNotBlank)
            .collect(Collectors.joining(ExtendConstants.NEWLINE));
    }

    // ============================== concatSegment ==============================

    /**
     * 全连接
     *
     * @param logicTable 待连接的逻辑表
     * @return com.xiajibagao.top.mybatis.plus.concatSegment.wrapper.JoinWrapper.JoinTable<T,J,R>
     * @author huangchengxing
     * @date 2022/2/11 10:56
     */
    public LogicJoinTable<T, R> fullJoin(JoinWrapper<?, R> logicTable) {
        return addJoin(JoinType.FULL_JOIN, logicTable);
    }

    /**
     * 全连接
     *
     * @param joinTable 待连接的表
     * @return com.xiajibagao.top.mybatis.plus.concatSegment.wrapper.JoinWrapper.JoinTable<T,J,R>
     * @author huangchengxing
     * @date 2022/2/11 10:57
     */
    public <J> JoinTable<T, J, R> fullJoin(Class<J> joinTable) {
        return addJoin(JoinType.FULL_JOIN, joinTable);
    }
    
    /**
     * 全连接
     *
     * @param joinTable 待连接的表
     * @param consumer 操作
     * @return com.xiajibagao.top.mybatis.plus.concatSegment.wrapper.JoinWrapper<T,R>
     * @author huangchengxing
     * @date 2022/2/11 10:57
     */
    public <J> JoinWrapper<T, R> fullJoin(Class<J> joinTable, Consumer<JoinTable<T, J, R>> consumer) {
        return addJoin(JoinType.FULL_JOIN, joinTable, consumer);
    }

    /**
     * 内连接
     *
     * @param logicTable 待连接的逻辑表
     * @return com.xiajibagao.top.mybatis.plus.concatSegment.wrapper.JoinWrapper.JoinTable<T,J,R>
     * @author huangchengxing
     * @date 2022/2/11 10:56
     */
    public LogicJoinTable<T, R> innerJoin(JoinWrapper<?, R> logicTable) {
        return addJoin(JoinType.INNER_JOIN, logicTable);
    }
    
    /**
     * 内连接
     *
     * @param joinTable 待连接的表
     * @return com.xiajibagao.top.mybatis.plus.concatSegment.wrapper.JoinWrapper.JoinTable<T,J,R>
     * @author huangchengxing
     * @date 2022/2/11 10:57
     */
    public <J> JoinTable<T, J, R> innerJoin(Class<J> joinTable) {
        return addJoin(JoinType.INNER_JOIN, joinTable);
    }
    
    /**
     * 内连接
     *
     * @param joinTable 待连接的表
     * @param consumer 操作
     * @return com.xiajibagao.top.mybatis.plus.concatSegment.wrapper.JoinWrapper<T,R>
     * @author huangchengxing
     * @date 2022/2/11 10:57
     */
    public <J> JoinWrapper<T, R> innerJoin(Class<J> joinTable, Consumer<JoinTable<T, J, R>> consumer) {
        return addJoin(JoinType.INNER_JOIN, joinTable, consumer);
    }

    /**
     * 右连接
     *
     * @param logicTable 待连接的逻辑表
     * @return com.xiajibagao.top.mybatis.plus.concatSegment.wrapper.JoinWrapper.JoinTable<T,J,R>
     * @author huangchengxing
     * @date 2022/2/11 10:56
     */
    public LogicJoinTable<T, R> rightJoin(JoinWrapper<?, R> logicTable) {
        return addJoin(JoinType.RIGHT_JOIN, logicTable);
    }
    
    /**
     * 右连接
     *
     * @param joinTable 待连接的表
     * @return com.xiajibagao.top.mybatis.plus.concatSegment.wrapper.JoinWrapper.JoinTable<T,J,R>
     * @author huangchengxing
     * @date 2022/2/11 10:57
     */
    public <J> JoinTable<T, J, R> rightJoin(Class<J> joinTable) {
        return addJoin(JoinType.RIGHT_JOIN, joinTable);
    }
    
    /**
     * 右连接
     *
     * @param joinTable 待连接的表
     * @param consumer 操作
     * @return com.xiajibagao.top.mybatis.plus.concatSegment.wrapper.JoinWrapper<T,R>
     * @author huangchengxing
     * @date 2022/2/11 10:56
     */
    public <J> JoinWrapper<T, R> rightJoin(Class<J> joinTable, Consumer<JoinTable<T, J, R>> consumer) {
        return addJoin(JoinType.RIGHT_JOIN, joinTable, consumer);
    }

    /**
     * 左连接
     *
     * @param logicTable 待连接的逻辑表
     * @return com.xiajibagao.top.mybatis.plus.concatSegment.wrapper.JoinWrapper.JoinTable<T,J,R>
     * @author huangchengxing
     * @date 2022/2/11 10:56
     */
    public LogicJoinTable<T, R> leftJoin(JoinWrapper<?, R> logicTable) {
        return addJoin(JoinType.LEFT_JOIN, logicTable);
    }
    
    /**
     * 左连接
     *
     * @param joinTable 待连接的表
     * @return com.xiajibagao.top.mybatis.plus.concatSegment.wrapper.JoinWrapper.JoinTable<T,J,R>
     * @author huangchengxing
     * @date 2022/2/11 10:56
     */
    public <J> JoinTable<T, J, R> leftJoin(Class<J> joinTable) {
        return addJoin(JoinType.LEFT_JOIN, joinTable);
    }
    
    /**
     * 左连接
     *
     * @param joinTable 待连接的表
     * @param consumer 操作
     * @return com.xiajibagao.top.mybatis.plus.concatSegment.wrapper.JoinWrapper<T,R>
     * @author huangchengxing
     * @date 2022/2/11 10:56
     */
    public <J> JoinWrapper<T, R> leftJoin(Class<J> joinTable, Consumer<JoinTable<T, J, R>> consumer) {
        return addJoin(JoinType.LEFT_JOIN, joinTable, consumer);
    }

    /**
     * 添加关联查询
     *
     * @param joinType 连接类型
     * @param logicTable 要关联查询的逻辑表
     * @return com.xiajibagao.top.mybatis.plus.concatSegment.wrapper.JoinMapper.JoinTable<T,J,R>
     * @author huangchengxing
     * @date 2022/2/9 15:38
     */
    protected LogicJoinTable<T, R> addJoin(JoinType joinType, JoinWrapper<?, R> logicTable) {
        return new LogicJoinTable<>(joinType, this, logicTable);
    }
    
    /**
     * 添加关联查询
     *
     * @param joinType 连接类型
     * @param joinTable 连接表类型
     * @return com.xiajibagao.top.mybatis.plus.concatSegment.wrapper.JoinMapper.JoinTable<T,J,R>
     * @author huangchengxing
     * @date 2022/2/9 15:38
     */
    protected <J> JoinTable<T, J, R> addJoin(JoinType joinType, Class<J> joinTable) {
        return new JoinTable<>(joinType, joinTable, this, false);
    }
    
    /**
     * 添加关联查询
     *
     * @param joinType 连接类型
     * @param joinTable 连接表类型
     * @param consumer 操作
     * @return com.xiajibagao.top.mybatis.plus.concatSegment.wrapper.JoinMapper<T,R>
     * @author huangchengxing
     * @date 2022/2/9 15:38
     */
    protected <J> JoinWrapper<T, R> addJoin(JoinType joinType, Class<J> joinTable, Consumer<JoinTable<T, J, R>> consumer) {
        JoinTable<T, J, R> join = new JoinTable<>(joinType, joinTable, this, false);
        consumer.accept(join);
        return typedThis;
    }

    // ============================== override ==============================

    /**
     * 获取实例用于or或and的嵌套查询
     *
     * @return com.xiajibagao.top.mybatis.plus.concatSegment.wrapper.JoinMapper<T,R>
     * @author huangchengxing
     * @date 2022/2/9 15:39
     */
    @Override
    protected JoinWrapper<T, R> instance() {
        JoinWrapper<T, R> instance = new JoinWrapper<>(targetClass, resultClass, false);
        instance.alisa = this.alisa;
        instance.joinTableSeq = this.joinTableSeq;
        instance.joinTableList = this.joinTableList;
        instance.paramNameSeq = this.paramNameSeq;
        instance.paramNameValuePairs = this.paramNameValuePairs;
        instance.expression = new MergeSegments();
        instance.selectColumns = Collections.emptyList();
        instance.lastSql = lastSql;
        instance.sqlComment = sqlComment;
        instance.sqlFirst = sqlFirst;
        return instance;
    }

    @Override
    protected void initNeed() {
        super.initNeed();
        this.joinTableSeq = new AtomicInteger(0);
        this.joinTableList = new ArrayList<>();
    }

    // ============================== JoinTable ==============================

    /**
     * 将当前查询转为一张逻辑表
     *
     * @return com.xiajibagao.top.mybatis.plus.concatSegment.wrapper.JoinWrapper.LogicTable<R>
     * @author huangchengxing
     * @date 2022/2/11 16:20
     */
    public LogicTable<R> toLogicTable() {
        return new LogicTable<>(this);
    }

    /**
     * 要join的关联表
     *
     * @param <T> 主查询主表实体类型
     * @param <J> join表实体类型
     * @param <R> 主查询返回实体类型
     * @author huangchengxing
     * @date 2022/2/9 14:26
     */
    @Getter
    public static class JoinTable<T, J, R> extends JoinWrapper<J, R> {

        private final JoinType joinType;
        private final JoinWrapper<T, R> source;
        private final MergeSegments joinCondition;

        public JoinTable(JoinType joinType, @Nonnull Class<J> targetClass, JoinWrapper<T, R> source, boolean isLogic) {
            super(targetClass, source.getResultClass(), isLogic);

            this.joinType = joinType;
            this.source = source;

            this.joinCondition = new MergeSegments();
            this.joinTableSeq = source.joinTableSeq;
            this.joinTableList = source.joinTableList;
            this.paramNameSeq = source.paramNameSeq;
            this.paramNameValuePairs = source.paramNameValuePairs;
            this.expression = source.expression;
            this.selectColumns = source.selectColumns;
            this.lastSql = source.lastSql;
            this.sqlComment = source.sqlComment;
            this.sqlFirst = source.sqlFirst;

            // 加入join集合，并修改表别名
            source.joinTableList.add(this);
            setAlisaByJoinSeq();
            initLogicDelete();
        }

        public <C> JoinTable<T, J, R> on(SFunction<T, C> sourceColumn, Condition condition, SFunction<J, C> targetColumn) {
            joinCondition.add(() -> source.columnToString(sourceColumn), condition, () -> this.columnToString(targetColumn));
            return this;
        }

        /**
         * 获取“LEFT JOIN xxx a on a.id = b.aid”格式的join语句
         *
         * @return java.lang.String join语句，若不存在on条件，则返回空字符串
         * @author huangchengxing
         * @date 2022/2/9 13:32
         */
        @Override
        public String getSqlJoin() {
            if (joinCondition.getNormal().isEmpty()) {
                return ExtendConstants.EMPTY;
            }
            return SqlUtils.space(
                joinType.getSqlSegment(),
                getTable(), alisa,
                ExtendConstants.ON, joinCondition.getSqlSegment()
            );
        }

    }

    /**
     * 要join的逻辑表
     *
     * @param <T> 主查询主表实体类型
     * @param <R> 主查询返回实体类型
     * @author huangchengxing
     * @date 2022/2/11 16:47
     */
    public static class LogicJoinTable<T, R> extends JoinTable<T, R, R> {

        private final JoinWrapper<?, R> logicTable;

        public LogicJoinTable(JoinType joinType, JoinWrapper<T, R> source, JoinWrapper<?, R> logicTable) {
            super(joinType, source.getResultClass(), source, true);
            initNeed();
            this.selectColumns = source.selectColumns;
            this.logicTable = logicTable;
        }

        /**
         * 获取"(select * from A)"格式的查询语句
         *
         * @return java.lang.String
         * @author huangchengxing
         * @date 2022/2/9 15:41
         */
        @Override
        public String getTable() {
            return SqlUtils.concatBrackets(SqlUtils.wrapperToSql(logicTable));
        }

    }

    /**
     * 不存在对应数据库表的逻辑表
     *
     * @author huangchengxing
     * @date 2022/02/11 16:06
     */
    public static class LogicTable<T> extends JoinWrapper<T, T> {

        private final JoinWrapper<?, T> table;

        protected LogicTable(JoinWrapper<?, T> logicTable) {
            super(logicTable.getResultClass(), logicTable.getResultClass(), true);
            initNeed();
            setAlisaByJoinSeq();
            this.table = logicTable;
        }

        /**
         * 获取"(select * from A)"格式的查询语句
         *
         * @return java.lang.String
         * @author huangchengxing
         * @date 2022/2/9 15:41
         */
        @Override
        public String getTable() {
            return SqlUtils.concatBrackets(SqlUtils.wrapperToSql(table));
        }

    }

}
