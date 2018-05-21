package cqrs.domain.events;

import java.util.Collection;

import cqrs.domain.AnAggregateRoot;

/**
 * ES聚合根基类
 */
public interface AnEventBasedAggregateRoot extends AnAggregateRoot, HandleEvents {

  /**
   * 默认版本
   */
  public static final long DEFAULT_VERSION = -1L;

  /**
   * 当前聚合根版本
   */
  long getVersion();

  /**
   * 得到该聚合根中未提交到Event Sourcing的事件
   */
  Collection<AnEvent> getUncommittedEvents();

  /**
   * 将所有event标记为已提交
   */
  void markEventsAsCommitted();

  /**
   * 从事件流中得到某聚合根的状态
   */
  void rebuildFromHistory(EventStream history);
}
