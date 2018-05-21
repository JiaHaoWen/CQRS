package cqrs.demo.domain.commands;

import cqrs.demo.domain.entities.Order;
import cqrs.demo.domain.entities.Orders;

import java.util.function.Consumer;

public class AddNewOrderItemCommandHandler implements Consumer<AddNewOrderItemCommand> {

  final Orders orders;

  public AddNewOrderItemCommandHandler(Orders orders) {
    this.orders = orders;
  }

  @Override
  public void accept(AddNewOrderItemCommand cmd) {
    Order order = orders.get(cmd.getOrderId());
    order.addNewOrderItem(cmd.getEan());
    orders.update(order);
  }

}
