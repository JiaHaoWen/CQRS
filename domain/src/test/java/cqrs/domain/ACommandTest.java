package cqrs.domain;

import java.util.function.Consumer;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public final class ACommandTest {

  @Test
  public void testDispatch() {
    MutableBoolean dispatched = new MutableBoolean(false);
    Consumer<ACommand> handler = (command) -> dispatched.setTrue();
    ACommand command = new ACommand() {
    };
    command.dispatch(handler);
    assertThat(dispatched.getValue(), is(true));
  }

}
