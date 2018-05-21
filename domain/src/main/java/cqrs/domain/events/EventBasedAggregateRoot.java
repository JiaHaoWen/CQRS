package cqrs.domain.events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import cqrs.domain.AggregateRoot;
import cqrs.domain.AnAggregateRoot;

/**
 * 支持Event Sourcing的聚合根基类
 */
public abstract class EventBasedAggregateRoot extends AggregateRoot
    implements AnAggregateRoot, AnEventBasedAggregateRoot {

  private static final long serialVersionUID = 1L;

  private final List<AnEvent> uncommittedEvents;
  private long version;

  /**
   */
  protected EventBasedAggregateRoot() {
    this(UUID.randomUUID());
  }

  /**
   */
  protected EventBasedAggregateRoot(final UUID id) {
    this(id, DEFAULT_VERSION);
  }

  /**
   */
  protected EventBasedAggregateRoot(final UUID id, final long version) {
    super(id);
    this.version = version;
    uncommittedEvents = new ArrayList<AnEvent>();
  }

  /**
   */
  @Override
  public long getVersion() {
    return version;
  }

  /**
   */
  @Override
  public final Collection<AnEvent> getUncommittedEvents() {
    return new ArrayList<>(uncommittedEvents);
  }

  /**
   */
  @Override
  public final void markEventsAsCommitted() {
    uncommittedEvents.clear();
  }

  /**
   */
  @Override
  public final void rebuildFromHistory(final EventStream history) {
    Objects.requireNonNull(history);
    if (history.getEventMetadata().isEmpty() && version != history.getVersion()) {
      throw new IllegalStateException(
          String.format("历史版本 %d 与期望版本 %d 不匹配.",
              history.getVersion(), version));
    }
    for (EventMetadata eventDescriptor : history.getEventMetadata()) {
      applyHistoryEvent(eventDescriptor);
    }
  }

  private void applyHistoryEvent(EventMetadata eventDescriptor) {
    long expectedEventVersion = version + 1L;
    if (eventDescriptor.getVersion() == expectedEventVersion) {
      apply(eventDescriptor.getEvent(), false);
      version = eventDescriptor.getVersion();
    } else {
      throw new IllegalStateException(
          String.format("事件版本 %d 与期望版本 %d 不匹配.",
              eventDescriptor.getVersion(), expectedEventVersion));
    }
  }

  /**
   * 历史事件处理
   */
  @Override
  public final void accept(AnEvent event) {
    apply(event, false);
  }

  /**
   * 新增事件处理
   */
  protected final void apply(final AnEvent event) {
    apply(event, true);
  }

  private final void apply(final AnEvent event, final boolean isNew) {
    if (isNew) {
      if (uncommittedEvents.contains(event)) {
        throw new IllegalStateException(
            String.format("'%s' 对应的事件已处理.", event.getId()));
      } else {
        uncommittedEvents.add(event);
      }
    }
    apply(this, event);
  }

}
