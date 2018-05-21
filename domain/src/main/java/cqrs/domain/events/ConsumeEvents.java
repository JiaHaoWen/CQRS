package cqrs.domain.events;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * 消费者事件接口
 */
public interface ConsumeEvents extends Consumer<AnEvent> {

  /**
   * 将消费方事件分配到对应的消费着
   */
  @Override
  default void accept(AnEvent event) {
    Objects.requireNonNull(event);
    EventApplier.apply(this, event, "consume");
  }

  /**
   */
  default void consume(AnEvent event) {
  }
}
