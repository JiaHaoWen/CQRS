package cqrs.domain.events;

import java.util.Comparator;
import java.util.Objects;
import java.util.UUID;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * 单一事件元数据,用与EventStream中描述单一事件
 */
public final class EventMetadata {

  /**
   */
  public static final Comparator<? super EventMetadata> BY_VERSION_COMPARATOR =
      (e1, e2) -> Long.compare(e1.getVersion(), e2.getVersion());

  private final UUID streamId;
  private final long version;
  private final AnEvent event;

  /**
   */
  EventMetadata(final UUID streamId, final long version, final AnEvent event) {
    Objects.requireNonNull(streamId);
    Objects.requireNonNull(event);
    this.streamId = streamId;
    this.version = version;
    this.event = event;
  }

  /**
   */
  public AnEvent getEvent() {
    return event;
  }

  /**
   */
  public UUID getStreamId() {
    return streamId;
  }

  /**
   */
  public long getVersion() {
    return version;
  }

  @Override
  public boolean equals(Object that) {
    return EqualsBuilder.reflectionEquals(this, that);
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this);
  }
}
