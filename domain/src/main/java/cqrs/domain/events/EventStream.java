package cqrs.domain.events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Objects;
import java.util.UUID;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * 事件流
 */
public final class EventStream {

  private final UUID streamId;
  private final long initialVersion;
  private final LinkedList<EventMetadata> descriptors;

  /**
   */
  public EventStream(UUID streamId) {
    this(streamId, AnEventBasedAggregateRoot.DEFAULT_VERSION);
  }

  /**
   */
  public EventStream(UUID streamId, long initialVersion) {
    Objects.requireNonNull(streamId);
    this.streamId = streamId;
    this.initialVersion = initialVersion;
    descriptors = new LinkedList<>();
  }

  /**
   */
  public UUID getStreamId() {
    return streamId;
  }

  /**
   */
  public long getVersion() {
    if (descriptors.size() > 0) {
      return descriptors.getLast().getVersion();
    }
    return initialVersion;
  }

  /**
   */
  public Collection<EventMetadata> getEventMetadata() {
    return new ArrayList<>(descriptors);
  }

  /**
   */
  public void add(AnEvent event) {
    addAll(Collections.singleton(event));
  }

  /**
   */
  public synchronized void addAll(Collection<AnEvent> events) {
    Objects.requireNonNull(events);
    events.stream().forEachOrdered(
        event -> descriptors.add(new EventMetadata(streamId, getVersion() + 1L, event)));
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
