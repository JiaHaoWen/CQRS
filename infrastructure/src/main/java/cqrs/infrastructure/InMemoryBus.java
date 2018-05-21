package cqrs.infrastructure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import cqrs.domain.AMessage;
import net.jodah.typetools.TypeResolver;

/**
 * InMemory事件总线基类,内存中维护相关聚合根\事件\消息
 */
public abstract class InMemoryBus<MsgT extends AMessage> implements TransferMessages<MsgT> {

  private final Map<Class<?>, List<Consumer<MsgT>>> routes =
      new HashMap<Class<?>, List<Consumer<MsgT>>>();

  protected InMemoryBus() {
  }

  @Override
  public <T extends MsgT> void send(T msg) {
    final Set<Consumer<MsgT>> handlers = resolveHandlersForMsg(msg);
    handle(msg, handlers);
  }

  /**
   * 将指定的消息分发到指定的处理器中
   */
  protected abstract void handle(MsgT msg, Collection<Consumer<MsgT>> handlers);

  /**
   * 注册消息处理器
   */
  @SuppressWarnings("unchecked")
  public final <T extends MsgT> void registerHandler(final Consumer<T> handler) {
    final Class<?> tMessageType =
        TypeResolver.resolveRawArgument(Consumer.class, handler.getClass());
    routes.merge(tMessageType, Collections.singletonList((Consumer<MsgT>) handler),
        (associated, given) -> {
          List<Consumer<MsgT>> handlers = new ArrayList<>(associated);
          handlers.addAll(given);
          return handlers;
        });
  }

  /**
   * 获得消息对应的处理器
   */
  private <T extends MsgT> Set<Consumer<MsgT>> resolveHandlersForMsg(final T msg) {
    Set<Consumer<MsgT>> msgHandlers = routes.entrySet().stream()
        .filter(route -> route.getKey().isAssignableFrom(msg.getClass()))
        .map(route -> route.getValue())
        .flatMap(handlers -> handlers.stream())
        .collect(Collectors.toSet());
    return msgHandlers;
  }

}
