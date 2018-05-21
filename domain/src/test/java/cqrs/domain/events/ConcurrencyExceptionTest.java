package cqrs.domain.events;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public final class ConcurrencyExceptionTest {

  @Test
  public void testConstructor() {
    assertThat(new ConcurrencyException(1L, 2L).getMessage(),
        is(equalTo("版本不相等")));
  }

}
