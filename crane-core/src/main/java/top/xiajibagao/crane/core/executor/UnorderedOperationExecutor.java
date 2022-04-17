package top.xiajibagao.crane.core.executor;

import top.xiajibagao.crane.core.container.Container;
import top.xiajibagao.crane.core.helper.MultiValueTableMap;
import top.xiajibagao.crane.core.parser.interfaces.AssembleOperation;
import top.xiajibagao.crane.core.parser.interfaces.DisassembleOperation;

import javax.annotation.Nonnull;

/**
 * <p>无序的{@link OperationExecutor}同步实现。
 *
 * <p>处理时按照每个操作的容器分组，因此将不严格按照{@link AssembleOperation#getOrder()}或{@link DisassembleOperation#getOrder()}
 * 的大小顺序执行处理。<br />
 * 一次执行中，每个容器仅需被访问一次。
 *
 * @author huangchengxing
 * @date 2022/03/01 18:00
 */
public class UnorderedOperationExecutor extends AbstractOperationExecutor implements OperationExecutor {

    @Override
    protected void execute(@Nonnull MultiValueTableMap<Container, AssembleOperation, Object> pendingOperations) {
        pendingOperations.asMap().forEach(Container::process);
    }

}
