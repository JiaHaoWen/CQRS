package cqrs.infrastructure.events;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import cqrs.domain.events.EventMockA;
import cqrs.domain.events.EventMockB;
import cqrs.domain.events.AnEvent;
import cqrs.domain.events.MockEvent;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@RunWith(MockitoJUnitRunner.class)
public class InMemoryEventBusTest {

  private InMemoryEventBus cut;

  @Before
  public void setUp() {
    cut = new InMemoryEventBus();
  }

  @Test
  public void testHandlerShouldBeCalledIfUnspecificEvent() {
    final MutableBoolean handled = new MutableBoolean(false);
    Consumer<AnEvent> handler = new Consumer<AnEvent>() {
      @Override
      public void accept(AnEvent event) {
        handled.setTrue();
      }
    };
    cut.registerHandler(handler);
    cut.send(new MockEvent());
    assertThat(handled.getValue(), is(true));
  }

  @Test
  public void test() {

    final List<String> calledHandlers = new ArrayList<String>();

    final Consumer<EventMockA> ca = new Consumer<EventMockA>() {
      @Override
      public void accept(final EventMockA event) {
        calledHandlers.add("A");
      }
    };

    final Consumer<EventMockB> cb = new Consumer<EventMockB>() {
      @Override
      public void accept(final EventMockB event) {
        calledHandlers.add("B");
      }
    };

    cut.registerHandler(ca);
    cut.registerHandler(cb);

    cut.send((AnEvent) new EventMockA());
    assertTrue(calledHandlers.contains("A"));
    assertFalse(calledHandlers.contains("B"));

    calledHandlers.clear();

    cut.send(new EventMockA());
    assertTrue(calledHandlers.contains("A"));
    assertFalse(calledHandlers.contains("B"));

    calledHandlers.clear();

    cut.send(new EventMockB());
    assertFalse(calledHandlers.contains("A"));
    assertTrue(calledHandlers.contains("B"));
  }

}
