package cqrs.infrastructure.events;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import cqrs.domain.AggregateRoot;
import cqrs.domain.AnAggregateRoot;

import org.junit.Test;

public final class EmptyRepositoryTest {

  @Test
  public void testAlwaysReturnsNull() {
    EmptyRepository<AnAggregateRoot> emptySnaphotRepository =
        new EmptyRepository<>();
    AggregateRoot root = new AggregateRoot() {
    };
    emptySnaphotRepository.store(root);
    assertThat(emptySnaphotRepository.retrieveWithId(root.getId()), is(nullValue()));
  }

}
