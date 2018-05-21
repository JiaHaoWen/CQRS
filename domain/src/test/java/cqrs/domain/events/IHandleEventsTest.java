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
public final class IHandleEventsTest {

  @Mock
  private HandleEvents cut;

  @Test
  public void testAccept() {
    AnEvent event = mock(AnEvent.class);
    doCallRealMethod().when(cut).accept(event);
    cut.accept(event);
    verify(cut).apply(cut, event);
  }

  @Test
  public void testApply() {
    AnEvent event = mock(AnEvent.class);
    doCallRealMethod().when(cut).apply(cut, event);
    cut.apply(cut, event);
    verify(cut).handle(event);
  }

  @Test
  public void testAcceptConcreteEvent() {
    MockEvent event = new MockEvent();
    MockEventConsumer handler = new MockEventConsumer();
    handler.accept(event);
    assertThat(handler.consumed, is(true));
  }

  private static final class MockEventConsumer implements HandleEvents {
    public boolean consumed = false;

    public void handle(MockEvent event) {
      consumed = true;
    }
  }

}
