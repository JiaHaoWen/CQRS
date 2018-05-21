package cqrs.domain;

import java.util.Objects;
import java.util.UUID;

/**
 * 聚合根基类
 *
 */
public abstract class AggregateRoot implements AnAggregateRoot {

  private static final long serialVersionUID = 1L;

  private UUID id;

  /**
   * 构造函数
   */
  protected AggregateRoot() {
    this(UUID.randomUUID());
  }

  /**
   * 构造函数
   */
  protected AggregateRoot(final UUID id) {
    Objects.requireNonNull(id);
    this.id = id;
  }

  @Override
  public final UUID getId() {
    return id;
  }

}
