package cqrs.demo.readmodel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cqrs.demo.domain.events.NewOrderItemAddedEvent;
import cqrs.demo.domain.events.OrderItemAmountIncreasedEvent;
import cqrs.domain.events.ConsumeEvents;
import org.apache.commons.lang3.StringUtils;

public final class ItemPopularity implements ConsumeEvents {

  private final Map<String, Long> popularityByEan = new ConcurrentHashMap<>();

  public void consume(NewOrderItemAddedEvent event) {
    popularityByEan.merge(event.getEan(), 1L, (associated, given) -> associated + given);
  }

  public void consume(OrderItemAmountIncreasedEvent event) {
    popularityByEan.merge(event.getEan(), event.getIncrement(),
        (associated, given) -> associated + given);
  }

  /**
   */
  public void print() {
    System.out.println("Item popularity:");
    System.out.println(StringUtils.rightPad("", 80, "-"));
    System.out.print("| ");
    System.out.print(StringUtils.rightPad("product", 30));
    System.out.print(" | ");
    System.out.print(StringUtils.rightPad("amount", 6));
    System.out.println(" |");
    System.out.println(StringUtils.rightPad("", 80, "-"));
    popularityByEan.entrySet().stream()
        .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
        .forEachOrdered(e -> System.out.println(
            String.format("| %s | %s |", StringUtils.rightPad(e.getKey(), 30),
                StringUtils.center(e.getValue().toString(), 6))));
    System.out.println();
  }

}
