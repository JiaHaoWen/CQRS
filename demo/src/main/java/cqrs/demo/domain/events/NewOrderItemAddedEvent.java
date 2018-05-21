package cqrs.demo.domain.events;

import java.util.UUID;

import cqrs.domain.events.Event;

public class NewOrderItemAddedEvent extends Event implements HasOrderId {

  private static final long serialVersionUID = 1L;

  private final String ean;
  private final UUID orderId;

  /**
   */
  public NewOrderItemAddedEvent(final UUID orderId, final String ean) {
    super();
    this.orderId = orderId;
    this.ean = ean;
  }

  public String getEan() {
    return ean;
  }

  @Override
  public UUID getOrderId() {
    return orderId;
  }

}
