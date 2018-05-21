package cqrs.infrastructure;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

import cqrs.domain.ACommand;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@RunWith(MockitoJUnitRunner.class)
public final class InMemoryCommandBusTest {

  private InMemoryCommandBus cut = new InMemoryCommandBus();

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void testOnlyOneHandlerPerCommand() {

    thrown.expect(IllegalStateException.class);

    cut.registerHandler((A command) -> {
    });
    cut.registerHandler((A command) -> {
    });

    cut.send(new A());
  }

  @Test
  public void testCallHandler() {

    final List<Consumer<? extends ACommand>> calledHandlers = new ArrayList<>();

    Consumer<A> handlerA = new Consumer<A>() {
      @Override
      public void accept(A msg) {
        calledHandlers.add(this);
      }
    };
    Consumer<B> handlerB = new Consumer<B>() {
      @Override
      public void accept(B msg) {
        calledHandlers.add(this);
      }
    };
    cut.registerHandler(handlerA);
    cut.registerHandler(handlerB);

    cut.send((ACommand) new A());

    assertThat(calledHandlers, containsInAnyOrder(handlerA));

    calledHandlers.clear();

    cut.send(new A());
    assertThat(calledHandlers, containsInAnyOrder(handlerA));

    calledHandlers.clear();

    cut.send(new B());
    assertThat(calledHandlers, containsInAnyOrder(handlerB));
  }

  private static final class A implements ACommand {
    private static final long serialVersionUID = 1L;
  }

  private static final class B implements ACommand {
    private static final long serialVersionUID = 1L;
  }

}
