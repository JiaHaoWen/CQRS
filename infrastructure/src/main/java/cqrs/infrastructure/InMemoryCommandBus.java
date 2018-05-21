package cqrs.infrastructure;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Consumer;

import cqrs.domain.ACommand;

/**
 * 内存中的命令事件总线
 */
public final class InMemoryCommandBus extends InMemoryBus<ACommand>
    implements TransferCommands {

  /**
   * 将指定的消息分发到对应的处理器中
   */
  @Override
  protected void handle(final ACommand msg, final Collection<Consumer<ACommand>> handlers) {
    Objects.requireNonNull(handlers);
    if (handlers.size() > 1) {
      throw new IllegalStateException("命令处理器不能大于一个");
    }
    handlers.stream().forEach(handler -> msg.dispatch(handler));
  }

}
