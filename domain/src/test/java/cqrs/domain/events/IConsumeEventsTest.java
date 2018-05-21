package cqrs.domain.events;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public final class IConsumeEventsTest {

  @Mock
  private ConsumeEvents cut;

  @Test
  public void testAccept() {
    AnEvent event = mock(AnEvent.class);
    doCallRealMethod().when(cut).accept(event);
    cut.accept(event);
    verify(cut).consume(event);
  }

  @Test
  public void testAcceptConcreteEvent() {
    MockEvent event = new MockEvent();
    MockEventConsumer consumer = new MockEventConsumer();
    consumer.accept(event);
    assertThat(consumer.consumed, is(true));
  }

  private static final class MockEventConsumer implements ConsumeEvents {
    public boolean consumed = false;

    public void consume(MockEvent event) {
      consumed = true;
    }
  }

}
