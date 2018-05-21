package cqrs.demo.readmodel;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import cqrs.demo.domain.events.OrderPlacedEvent;
import cqrs.domain.events.ConsumeEvents;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutableTriple;

public final class CustomerOverview implements ConsumeEvents {

  private final Map<String, Set<UUID>> ordersByCustomer = new ConcurrentHashMap<>();

  /**
   */
  public void consume(OrderPlacedEvent event) {
    ordersByCustomer.merge(event.getCustomerName(), Collections.singleton(event.getOrderId()),
        (associated, given) -> {
          Set<UUID> orderIds = new HashSet<>(associated);
          orderIds.addAll(given);
          return orderIds;
        });
  }

  /**
   */
  public void print() {
    System.out.println("Customer Overview:");
    System.out.println(StringUtils.rightPad("", 80, "-"));
    System.out.print("| ");
    System.out.print(StringUtils.rightPad("customer", 15));
    System.out.print(" | ");
    System.out.print(StringUtils.rightPad("#orders", 7));
    System.out.print(" | ");
    System.out.println("order ids");
    System.out.println(StringUtils.rightPad("", 80, "-"));

    ordersByCustomer.entrySet().stream().sequential()
        .map(e -> new ImmutableTriple<>(e.getKey(), e.getValue().size(),
            StringUtils.join(e.getValue(), ',')))
        .sorted((lhs, rhs) -> Long.compare(rhs.getMiddle(), lhs.getMiddle()))
        .forEachOrdered(e -> System.out
            .println(String.format("| %s | %s | %s", StringUtils.rightPad(e.getLeft(), 15),
                StringUtils.center(e.getMiddle().toString(), 7), e.getRight())));
    System.out.println();
  }
}
