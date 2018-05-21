package cqrs.domain;

import java.util.UUID;

import cqrs.domain.events.ConcurrencyException;

/**
 * 聚合根存储通用接口
 */
public interface StoreAggregates<RootT extends AnAggregateRoot> {

  /**
   * 存储指定的聚合根,若聚合根ID相同,则覆盖
   */
  void store(RootT root) throws ConcurrencyException;

  /**
   * 根据指定的聚合根ID进行遍历
   */
  RootT retrieveWithId(UUID id);
}
