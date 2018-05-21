package cqrs.infrastructure.events;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import cqrs.domain.events.AnEvent;
import cqrs.domain.events.EventMetadata;
import cqrs.domain.events.EventStream;

/**
 * 内存事件存储器
 */
public final class InMemoryEventStore extends EventStore implements StoreEvents {

  private final Map<UUID, EventStream> eventStreams;

  /**
   */
  public InMemoryEventStore() {
    this(null);
  }

  /**
   */
  public InMemoryEventStore(TransferEvents eventPublisher) {
    super(eventPublisher);
    eventStreams = new ConcurrentHashMap<>();
  }

  /**
   */
  @Override
  public EventStream getEventsForAggregate(UUID streamId, long lowerVersionExclusive) {
    EventStream eventDescriptorList =
        new EventStream(streamId, lowerVersionExclusive);
    eventDescriptorList
        .addAll(eventStreams.computeIfAbsent(streamId, (id) -> new EventStream(id))
            .getEventMetadata()
            .stream()
            .filter(ed -> ed.getVersion() > lowerVersionExclusive)
            .sorted(EventMetadata.BY_VERSION_COMPARATOR)
            .map(ed -> ed.getEvent())
            .collect(Collectors.toList()));
    return eventDescriptorList;
  }

  /**
   */
  @Override
  protected void save(UUID streamId, Collection<AnEvent> events) {
    EventStream eventDescriptorList = loadDescriptorsFromStream(streamId);
    eventDescriptorList.addAll(events);
  }

  /**
   */
  @Override
  protected long getCurrentStreamVersion(UUID streamId) {
    return loadDescriptorsFromStream(streamId).getVersion();
  }

  private synchronized EventStream loadDescriptorsFromStream(UUID streamId) {
    return eventStreams.computeIfAbsent(streamId, (id) -> new EventStream(id));
  }

}
