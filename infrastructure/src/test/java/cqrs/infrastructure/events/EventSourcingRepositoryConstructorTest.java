package cqrs.infrastructure.events;

import static org.mockito.Mockito.mock;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

import cqrs.domain.StoreAggregates;
import cqrs.domain.events.AnEventBasedAggregateRoot;
import cqrs.domain.events.EventBasedAggregateRoot;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

@RunWith(DataProviderRunner.class)
public class EventSourcingRepositoryConstructorTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @DataProvider
  public static Object[][] constructorParams() {
    return new Object[][] {
        { null, null },
        { mock(StoreEvents.class), null },
        { null, new EmptyRepository<EventBasedAggregateRoot>() },
    };
  }

  @Test
  @UseDataProvider("constructorParams")
  public void testInvalidConstructorParams(StoreEvents eventStore,
      StoreAggregates<AnEventBasedAggregateRoot> snapshotRepository) {
    thrown.expect(NullPointerException.class);
    new EventSourcingRepository<AnEventBasedAggregateRoot>(eventStore, snapshotRepository) {
    };
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testConstructorDoesNotThrow() {
    new EventSourcingRepository<EventBasedAggregateRoot>(mock(StoreEvents.class)) {
    };
    new EventSourcingRepository<EventBasedAggregateRoot>(mock(StoreEvents.class),
        mock(StoreAggregates.class)) {
    };
  }
}
