package cqrs.infrastructure.events;

import java.util.Collection;
import java.util.function.Consumer;

import cqrs.domain.events.AnEvent;
import cqrs.infrastructure.InMemoryBus;

/**
 * 内存事件总线
 */
public final class InMemoryEventBus extends InMemoryBus<AnEvent> implements TransferEvents {

  /**
   * 将指定的事件分配到对应的处理器中
   */
  @Override
  protected void handle(final AnEvent event, final Collection<Consumer<AnEvent>> handlers) {
    handlers.parallelStream().forEach(handler -> handler.accept(event));
  }

}
