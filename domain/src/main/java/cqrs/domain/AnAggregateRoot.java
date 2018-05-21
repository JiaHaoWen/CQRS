package cqrs.domain;

import java.io.Serializable;

/**
 * 标记聚合根
 */
public interface AnAggregateRoot extends CanBeIdentified, Serializable {
}
