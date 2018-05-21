package cqrs.domain;

import java.util.function.Consumer;

/**
 * command通用接口
 */
public interface ACommand extends AMessage {

  /**
   * 将特定的command分配到指定的handler
   */
  default void dispatch(Consumer<ACommand> handler) {
    handler.accept(this);
  }
}
