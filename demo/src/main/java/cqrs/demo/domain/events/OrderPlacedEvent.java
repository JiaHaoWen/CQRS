package cqrs.demo.domain.events;

import java.util.UUID;

import cqrs.domain.events.Event;

public final class OrderPlacedEvent extends Event implements HasOrderId {

  private static final long serialVersionUID = 1L;

  private final String customerName;
  private final UUID orderId;

  /**
   */
  public OrderPlacedEvent(UUID orderId, String customerName) {
    super();
    this.customerName = customerName;
    this.orderId = orderId;
  }

  public String getCustomerName() {
    return customerName;
  }

  @Override
  public UUID getOrderId() {
    return orderId;
  }

}
