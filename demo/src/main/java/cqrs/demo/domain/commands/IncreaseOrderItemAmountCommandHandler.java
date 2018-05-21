package cqrs.demo.domain.commands;

import cqrs.demo.domain.entities.Order;
import cqrs.demo.domain.entities.Orders;

import java.util.function.Consumer;

public class IncreaseOrderItemAmountCommandHandler
    implements Consumer<IncreaseOrderItemAmountCommand> {

  final Orders orders;

  public IncreaseOrderItemAmountCommandHandler(Orders orders) {
    this.orders = orders;
  }

  @Override
  public void accept(IncreaseOrderItemAmountCommand cmd) {
    Order order = orders.get(cmd.getOrderId());
    order.incrementOrderItem(cmd.getEan(), cmd.getIncrement());
    orders.update(order);
  }

}
