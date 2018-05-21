package cqrs.domain.events;

import java.util.UUID;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public final class EventTest {

  @Test
  public void testDefaultConstructor() {
    Event event = new Event() {
    };
    assertThat(event.getId(), is(not(nullValue())));
  }

  @Test
  public void testIdConstructor() {
    UUID id = UUID.randomUUID();
    Event event = new Event(id) {
    };
    assertThat(event.getId(), is(equalTo(id)));
  }

  @Test(expected = NullPointerException.class)
  public void testIdIsMandatory() {
    new Event(null) {
    };
  }

  @Test
  public void testEqualsAndHashcode() {
    EqualsVerifier.forClass(Event.class).withRedefinedSubclass(MyEvent.class).verify();
  }

  public static final class MyEvent extends Event {
    private static final long serialVersionUID = 1L;

    @Override
    public boolean canEqual(Object other) {
      return other instanceof MyEvent;
    }
  }

}
