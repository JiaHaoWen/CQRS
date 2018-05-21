package cqrs.infrastructure;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import cqrs.domain.AnAggregateRoot;
import cqrs.domain.StoreAggregates;

/**
 * 内存中的聚合根存储器
 */
public class InMemoryRepository<RootT extends AnAggregateRoot>
    implements StoreAggregates<RootT> {

  private final Map<UUID, RootT> storedAggregateRoots;

  /**
   * 初始化
   */
  public InMemoryRepository() {
    storedAggregateRoots = new HashMap<>();
  }

  /**
   * 存储聚合根
   */
  @Override
  public final void store(final RootT root) {
    Objects.requireNonNull(root);
    storedAggregateRoots.put(root.getId(), root);
  }

  /**
   * 根据聚合根id遍历
   */
  @Override
  public final RootT retrieveWithId(final UUID aggregateRootId) {
    return storedAggregateRoots.get(aggregateRootId);
  }
}
