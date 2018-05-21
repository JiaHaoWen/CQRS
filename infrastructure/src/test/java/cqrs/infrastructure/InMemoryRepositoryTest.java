package cqrs.infrastructure;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;

import cqrs.domain.AggregateRoot;
import cqrs.domain.AnAggregateRoot;

import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

public final class InMemoryRepositoryTest {

  private InMemoryRepository<AnAggregateRoot> cut;

  @Before
  public void setUp() {
    cut = new InMemoryRepository<>();
  }

  @Test
  public void testStoreAndRetrieve() {
    UUID id = UUID.randomUUID();
    AnAggregateRoot root = new AggregateRoot(id) {
    };

    cut.store(root);

    assertThat(cut.retrieveWithId(id), is(sameInstance(root)));

  }

  @Test
  public void testUpdate() {
    UUID id = UUID.randomUUID();
    AnAggregateRoot root = new AggregateRoot(id) {
    };
    AnAggregateRoot root2 = new AggregateRoot(id) {
    };

    cut.store(root);
    cut.store(root2);
    assertThat(cut.retrieveWithId(id), is(sameInstance(root2)));
  }

}
