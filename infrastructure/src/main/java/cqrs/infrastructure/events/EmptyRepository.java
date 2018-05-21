package cqrs.infrastructure.events;

import java.util.UUID;

import cqrs.domain.AnAggregateRoot;
import cqrs.domain.StoreAggregates;

/**
 *
 * 默认空仓储
 *
 */
final class EmptyRepository<RootT extends AnAggregateRoot>
    implements StoreAggregates<RootT> {

  /**
   */
  @Override
  public void store(RootT root) {
  }

  /**
   *
   */
  @Override
  public RootT retrieveWithId(UUID id) {
    return null;
  }
}
