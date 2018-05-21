package cqrs.infrastructure.events;

import java.util.Collection;
import java.util.UUID;

import cqrs.domain.events.AnEvent;
import cqrs.domain.events.AnEventBasedAggregateRoot;
import cqrs.domain.events.ConcurrencyException;
import cqrs.domain.events.EventStream;

/**
 * 事件存储接口
 */
public interface StoreEvents {

  /**
   * 存储指定的事件到事件流中
   */
  void save(UUID streamId, Collection<AnEvent> events, long expectedVersion)
      throws ConcurrencyException;

  /**
   * 根据指定id返回符合要求的事件流
   */
  default EventStream getEventsForAggregate(UUID streamId) {
    return getEventsForAggregate(streamId, AnEventBasedAggregateRoot.DEFAULT_VERSION);
  }

  /**
   * 返回指定事件流中大于指定版本的子事件流
   */
  EventStream getEventsForAggregate(UUID streamId, long lowerVersionExclusive);
}
