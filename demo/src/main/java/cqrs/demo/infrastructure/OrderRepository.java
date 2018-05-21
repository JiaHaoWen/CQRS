package cqrs.demo.infrastructure;

import cqrs.demo.domain.entities.Order;
import cqrs.demo.domain.entities.Orders;
import cqrs.infrastructure.InMemoryRepository;
import cqrs.infrastructure.events.EventSourcingRepository;
import cqrs.infrastructure.events.StoreEvents;

import java.util.UUID;

public class OrderRepository extends EventSourcingRepository<Order> implements Orders {

  public OrderRepository(final StoreEvents eventStore) {
    super(eventStore, new InMemoryRepository<>());
  }

  @Override
  public Order get(UUID orderId) {
    return retrieveWithId(orderId);
  }

  @Override
  public void update(Order order) {
    store(order);
  }

}
