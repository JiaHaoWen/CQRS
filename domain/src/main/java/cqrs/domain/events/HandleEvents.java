package cqrs.domain.events;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * 事件处理器接口,识别给定的事件,并分发到对应的事件处理器
 */
public interface HandleEvents extends Consumer<AnEvent> {

  /**
   */
  @Override
  default void accept(AnEvent event) {
    Objects.requireNonNull(event);
    apply(this, event);
  }

  /**
   */
  default void apply(Object target, AnEvent event) {
    Objects.requireNonNull(target);
    Objects.requireNonNull(event);
    EventApplier.apply(target, event, "handle");
  }

  /**
   */
  default void handle(AnEvent event) {
  }
}
