package cqrs.infrastructure.events;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;

import cqrs.domain.events.AnEventBasedAggregateRoot;
import cqrs.domain.events.EventMockA;
import cqrs.domain.events.EventMockB;
import cqrs.domain.events.EventStream;
import cqrs.domain.events.AnEvent;
import cqrs.domain.events.MockEvent;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.UUID;

public class InMemoryEventStoreTest {

  private static final AnEvent[] EVENTS = new AnEvent[] {
      new EventMockB(), new EventMockA(), new EventMockB(), new MockEvent()
  };
  private static final UUID STREAM_ID = UUID.randomUUID();

  private InMemoryEventStore cut;

  @Before
  public void setUp() {
    cut = new InMemoryEventStore();
  }

  @Test
  public void testGetEventsForAggregateWithDefaultVersion() {
    EventStream edl = new EventStream(STREAM_ID);
    edl.addAll(Arrays.asList(EVENTS));

    cut.save(STREAM_ID, Arrays.asList(EVENTS), AnEventBasedAggregateRoot.DEFAULT_VERSION);

    assertThat(cut.getEventsForAggregate(STREAM_ID), is(equalTo((edl))));
  }

  @Test
  public void testGetEventsForAggregateWithNonDefaultVersion() {
    final long version = 1L;

    EventStream edl = new EventStream(STREAM_ID);
    edl.addAll(Arrays.asList(EVENTS));

    cut.save(STREAM_ID, Arrays.asList(EVENTS), AnEventBasedAggregateRoot.DEFAULT_VERSION);

    EventStream versionedList = new EventStream(STREAM_ID, version);
    versionedList.addAll(Arrays.asList(Arrays.copyOfRange(EVENTS, 2, 4)));

    assertThat(cut.getEventsForAggregate(STREAM_ID, version),
        is(equalTo(versionedList)));
  }

  @Test
  public void testUnknownStreamId() {
    UUID streamId = UUID.randomUUID();
    EventStream edl = cut.getEventsForAggregate(streamId);
    assertThat(edl, is(notNullValue()));
    assertThat(edl.getEventMetadata(), is(empty()));
    assertThat(edl.getStreamId(), is(equalTo(streamId)));
    assertThat(edl.getVersion(), is(equalTo(AnEventBasedAggregateRoot.DEFAULT_VERSION)));
  }

}
