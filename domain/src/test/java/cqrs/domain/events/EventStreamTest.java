package cqrs.domain.events;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public final class EventStreamTest {

  private static final long INITIAL_VERSION = 10;
  private static final UUID STREAM_ID = UUID.randomUUID();

  private EventStream cut;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Before
  public void setUp() {
    cut = new EventStream(STREAM_ID, INITIAL_VERSION);
  }

  @Test
  public void testConstructor() {
    EventStream list = new EventStream(STREAM_ID);
    assertThat(list.getStreamId(), is(equalTo(STREAM_ID)));
    assertThat(list.getVersion(), is(equalTo(AnEventBasedAggregateRoot.DEFAULT_VERSION)));
  }

  /*
   * @Test public void testDefaultVersionDoesNotThrow() {
   * cut.ensureVersion(AnEventBasedAggregateRoot.DEFAULT_VERSION); }
   *
   * @Test public void testCurrentVersionDoesNotThrow() { cut.ensureVersion(cut.getVersion()); }
   *
   * @Test public void testEnsureHigherVersionThrows() { thrown.expect(ConcurrencyException.class);
   * cut.ensureVersion(cut.getVersion() + 1L); }
   *
   * @Test public void testEnsureLowerVersionThrows() { thrown.expect(ConcurrencyException.class);
   * cut.ensureVersion(cut.getVersion() - 1L); }
   */
  @Test
  public void testAddEvent() {
    AnEvent event = mock(AnEvent.class);

    cut.add(event);

    long expectedVersion = INITIAL_VERSION + 1L;
    assertThat(cut.getVersion(), is(equalTo(expectedVersion)));
    assertThat(cut.getEventMetadata().size(), is(equalTo(1)));
    assertThat(cut.getEventMetadata().iterator().next().getEvent(), is(sameInstance(event)));
    assertThat(cut.getEventMetadata().iterator().next().getVersion(),
        is(equalTo(expectedVersion)));
  }

  @Test
  public void testAddAllEvents() {
    int numberOfEvents = 10;
    List<AnEvent> descriptors = new ArrayList<>();
    for (long i = 1; i <= numberOfEvents; i++) {
      descriptors.add(mock(AnEvent.class));
    }

    cut.addAll(descriptors);

    long expectedVersion = INITIAL_VERSION + numberOfEvents;
    assertThat(cut.getVersion(), is(equalTo(expectedVersion)));
    assertThat(cut.getEventMetadata().size(), is(equalTo(numberOfEvents)));
  }

  @Test
  public void testEqualsAndHashcode() {
    EqualsVerifier.forClass(EventStream.class).verify();
  }
}
