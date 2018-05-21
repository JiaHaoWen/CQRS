package cqrs.demo;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import cqrs.demo.domain.commands.AddNewOrderItemCommand;
import cqrs.demo.domain.commands.AddNewOrderItemCommandHandler;
import cqrs.demo.domain.commands.IncreaseOrderItemAmountCommand;
import cqrs.demo.domain.commands.IncreaseOrderItemAmountCommandHandler;
import cqrs.demo.domain.commands.PlaceOrderCommand;
import cqrs.demo.domain.commands.PlaceOrderCommandHandler;
import cqrs.demo.domain.entities.Orders;
import cqrs.demo.infrastructure.OrderRepository;
import cqrs.demo.readmodel.CustomerOverview;
import cqrs.demo.readmodel.ItemPopularity;
import cqrs.infrastructure.InMemoryCommandBus;
import cqrs.infrastructure.TransferCommands;
import cqrs.infrastructure.events.InMemoryEventBus;
import cqrs.infrastructure.events.InMemoryEventStore;
import cqrs.infrastructure.events.StoreEvents;
import org.apache.commons.lang3.RandomUtils;

public class Main {

  private static final List<String> CUSTOMERS = Arrays.asList("Ray", "Maurice", "Jen");
  private static final List<String> PRODUCTS = Arrays.asList(
      "The Internet", "TFM", "I Screw Robots sticker", "Music Elitism Venn Diagram tee");

  private TransferCommands commandBus;
  private ItemPopularity itemPopularity;
  private CustomerOverview customerOverview;

  /**
   * Run
   */
  public void run() {

    setUpInfrastructure();

    // interact with the write model (command)
    for (String customer : CUSTOMERS) {
      placeSomeOrders(customer);
    }

    // interact with read model (query)
    itemPopularity.print();
    customerOverview.print();
  }

  private void placeSomeOrders(String customer) {
    int numberOfOrders = RandomUtils.nextInt(2, 5);
    for (int i = 0; i < numberOfOrders; i++) {
      PlaceOrderCommand createOrderCommand = new PlaceOrderCommand(customer);
      commandBus.send(createOrderCommand);
      addSomeOrderItems(customer, createOrderCommand.getOrderId());
    }
  }

  private void addSomeOrderItems(String customer, UUID orderId) {
    int numberOfItems = RandomUtils.nextInt(2, 15);
    for (int i = 0; i < numberOfItems; i++) {
      String product = PRODUCTS.get(RandomUtils.nextInt(0, PRODUCTS.size()));
      commandBus.send(new AddNewOrderItemCommand(orderId, product));
      commandBus
          .send(new IncreaseOrderItemAmountCommand(orderId, product, RandomUtils.nextLong(0, 5)));
    }
  }

  private void setUpInfrastructure() {
    // set up event bus to notify read model about updates
    InMemoryEventBus eventBus = new InMemoryEventBus();

    itemPopularity = new ItemPopularity();
    eventBus.registerHandler(itemPopularity);
    customerOverview = new CustomerOverview();
    eventBus.registerHandler(customerOverview);

    // set up event store which stores event streams
    // publishes events via eventbus
    StoreEvents eventStore = new InMemoryEventStore(eventBus);

    // set up a repository which stores all orders as event streams
    Orders orders = new OrderRepository(eventStore);

    // set up a command bus to interact with the domain model
    InMemoryCommandBus commandBus = new InMemoryCommandBus();

    // register command handlers
    commandBus.registerHandler(new PlaceOrderCommandHandler(orders));
    commandBus.registerHandler(new AddNewOrderItemCommandHandler(orders));
    commandBus.registerHandler(new IncreaseOrderItemAmountCommandHandler(orders));
    this.commandBus = commandBus;
  }

  public static void main(String[] args) {
    new Main().run();
  }

}
