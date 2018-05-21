package cqrs.demo.domain.events;

import java.util.UUID;

import cqrs.domain.events.Event;

public class OrderItemAmountIncreasedEvent extends Event implements HasOrderId {

  private static final long serialVersionUID = 1L;

  private final String ean;
  private final UUID orderId;
  private final long increment;

  /**
   */
  public OrderItemAmountIncreasedEvent(final UUID orderId, final String ean, final long increment) {
    super();
    this.orderId = orderId;
    this.ean = ean;
    this.increment = increment;
  }

  public String getEan() {
    return ean;
  }

  @Override
  public UUID getOrderId() {
    return orderId;
  }

  public long getIncrement() {
    return increment;
  }

}
