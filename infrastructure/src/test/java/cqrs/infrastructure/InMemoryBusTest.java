package cqrs.infrastructure;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import cqrs.domain.AMessage;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collection;
import java.util.function.Consumer;

@RunWith(MockitoJUnitRunner.class)
public final class InMemoryBusTest {

  @Spy
  private InMemoryBus<AMessage> cut = new InMemoryBus<AMessage>() {
    @Override
    protected void handle(AMessage msg, Collection<Consumer<AMessage>> handlers) {
    }
  };

  @Test
  public void testSend() {
    AMessage msg = mock(AMessage.class);
    @SuppressWarnings("unchecked")
    ArgumentCaptor<Collection<Consumer<AMessage>>> captor =
        ArgumentCaptor.forClass(Collection.class);
    cut.send(msg);

    verify(cut).handle(eq(msg), captor.capture());
    assertThat(captor.getValue(), is(empty()));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testHandlerResolution() {

    Consumer<BaseMsg> baseHandler = (msg) -> {
    };
    Consumer<DerivedMsg> derivedHandler = (msg) -> {
    };
    Consumer<OtherMsg> otherHandler = (msg) -> {
    };
    Consumer<DerivedMsg> anotherDerivedMsgHander = (msg) -> {
    };

    cut.registerHandler(baseHandler);
    cut.registerHandler(derivedHandler);
    cut.registerHandler(otherHandler);
    cut.registerHandler(anotherDerivedMsgHander);

    AMessage msg = new DerivedMsg();
    cut.send(msg);

    ArgumentCaptor<Collection<Consumer<AMessage>>> captor =
        ArgumentCaptor.forClass(Collection.class);
    verify(cut).handle(eq(msg), captor.capture());
    assertThat(captor.getValue(),
        containsInAnyOrder(baseHandler, derivedHandler, anotherDerivedMsgHander));

  }

  private static class BaseMsg implements AMessage {
    private static final long serialVersionUID = 1L;
  }

  private static final class DerivedMsg extends BaseMsg {
    private static final long serialVersionUID = 1L;
  }

  private static final class OtherMsg extends BaseMsg {
    private static final long serialVersionUID = 1L;
  }
}
