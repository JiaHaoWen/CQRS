package cqrs.infrastructure.events;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import cqrs.domain.events.ConcurrencyException;
import cqrs.domain.events.EventStream;
import cqrs.domain.events.AnEvent;
import cqrs.domain.events.AnEventBasedAggregateRoot;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

@RunWith(MockitoJUnitRunner.class)
public final class EventStoreTest {

  private static final long CURRENT_STREAM_VERSION = 0L;

  @Mock
  private TransferEvents eventPublisher;

  @Spy
  private EventStore cut;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    cut = new EventStore(eventPublisher) {

      @Override
      public EventStream getEventsForAggregate(UUID streamId, long lowerVersionExclusive) {
        return null;
      }

      @Override
      protected void save(UUID streamId, Collection<AnEvent> events) {
      }

      @Override
      protected long getCurrentStreamVersion(UUID streamId) {
        return CURRENT_STREAM_VERSION;
      }
    };
  }

  @Test
  public void testEventPublishing() {
    AnEvent event = mock(AnEvent.class);
    UUID streamId = UUID.randomUUID();
    cut.save(streamId, Collections.singleton(event), AnEventBasedAggregateRoot.DEFAULT_VERSION);
    verify(eventPublisher).send(event);
  }

  @Test
  public void testUnexpectedVersion() {
    AnEvent event = mock(AnEvent.class);
    thrown.expect(ConcurrencyException.class);
    cut.save(UUID.randomUUID(), Collections.singleton(event), CURRENT_STREAM_VERSION + 1L);
  }

  @Test
  public void testExpectedVersion() {
    AnEvent event = mock(AnEvent.class);
    cut.save(UUID.randomUUID(), Collections.singleton(event), CURRENT_STREAM_VERSION);
  }

}
