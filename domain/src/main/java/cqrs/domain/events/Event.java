package cqrs.domain.events;

import java.util.Objects;
import java.util.UUID;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * 事件基类
 */
public abstract class Event implements AnEvent {

  private static final long serialVersionUID = 1L;

  public static final int DEFAULT_VERSION = -1;

  private final UUID id;

  /**
   */
  public Event() {
    this(UUID.randomUUID());
  }

  /**
   */
  protected Event(final UUID id) {
    Objects.requireNonNull(id);
    this.id = id;
  }

  /**
   */
  @Override
  public final UUID getId() {
    return id;
  }

  @Override
  public boolean equals(Object other) {
    boolean equal = false;
    if (other instanceof Event) {
      Event that = (Event) other;
      equal = that.canEqual(this) && EqualsBuilder.reflectionEquals(this, that);
    }
    return equal;
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this);
  }

  /**
   */
  public boolean canEqual(Object other) {
    return (other instanceof Event);
  }
}
