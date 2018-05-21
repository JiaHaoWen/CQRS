package cqrs.demo.domain.commands;

import java.util.UUID;

import cqrs.domain.ACommand;

public class IncreaseOrderItemAmountCommand implements ACommand {

  private static final long serialVersionUID = 1L;

  private final UUID orderId;
  private final String ean;
  private final long increment;

  /**
   * 构造函数
   */
  public IncreaseOrderItemAmountCommand(final UUID orderId, final String ean,
      final long increment) {
    this.orderId = orderId;
    this.ean = ean;
    this.increment = increment;
  }

  public String getEan() {
    return ean;
  }

  public UUID getOrderId() {
    return orderId;
  }

  public long getIncrement() {
    return increment;
  }

}
