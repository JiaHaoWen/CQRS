package cqrs.demo.domain.commands;

import java.util.UUID;

import cqrs.domain.ACommand;

public class AddNewOrderItemCommand implements ACommand {

  private static final long serialVersionUID = 1L;

  private final UUID orderId;
  private final String ean;

  public AddNewOrderItemCommand(final UUID orderId, final String ean) {
    this.orderId = orderId;
    this.ean = ean;
  }

  public String getEan() {
    return ean;
  }

  public UUID getOrderId() {
    return orderId;
  }

}
