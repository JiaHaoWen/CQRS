package cqrs.domain.events;

import java.util.UUID;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

@RunWith(DataProviderRunner.class)
public final class ByVersionComparatorTest {

  @DataProvider
  public static Object[][] testcases() {
    EventMetadata lowVersion =
        new EventMetadata(UUID.randomUUID(), 10L, mock(AnEvent.class));
    EventMetadata highVersion =
        new EventMetadata(UUID.randomUUID(), 99L, mock(AnEvent.class));
    return new Object[][] {
        { lowVersion, highVersion, -1 },
        { highVersion, lowVersion, 1 },
        { lowVersion, lowVersion, 0 },
    };
  }

  @Test
  @UseDataProvider("testcases")
  public void test(EventMetadata lhs, EventMetadata rhs, int expectedResult) {
    assertThat(EventMetadata.BY_VERSION_COMPARATOR.compare(lhs, rhs),
        is(equalTo(expectedResult)));
  }

}
