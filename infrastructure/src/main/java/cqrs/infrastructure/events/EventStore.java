package cqrs.infrastructure.events;

import java.util.Collection;
import java.util.UUID;

import cqrs.domain.events.AnEvent;
import cqrs.domain.events.AnEventBasedAggregateRoot;
import cqrs.domain.events.ConcurrencyException;

/**
 * 事件存储基类
 */
public abstract class EventStore implements StoreEvents {

  private final TransferEvents eventPublisher;

  /**
   */
  public EventStore() {
    this(null);
  }

  /**
   */
  public EventStore(final TransferEvents eventPublisher) {
    if (eventPublisher == null) {
      this.eventPublisher = new TransferEvents() {
        @Override
        public void send(AnEvent msg) {
        }
      };
    } else {
      this.eventPublisher = eventPublisher;
    }
  }

  /**
   */
  @Override
  public final synchronized void save(UUID streamId, Collection<AnEvent> events,
      long expectedVersion) {

    ensureVersion(getCurrentStreamVersion(streamId), expectedVersion);
    save(streamId, events);
    events.forEach(event -> {
      eventPublisher.send(event);
    });
  }

  /**
   */
  protected abstract void save(UUID streamId, Collection<AnEvent> events);

  /**
   */
  protected abstract long getCurrentStreamVersion(UUID streamId);

  private static void ensureVersion(long currentVersion, long expectedVersion)
      throws ConcurrencyException {

    if (expectedVersion == AnEventBasedAggregateRoot.DEFAULT_VERSION) {
      return;
    }

    if (currentVersion != expectedVersion) {
      throw new ConcurrencyException(currentVersion, expectedVersion);
    }
  }

}
