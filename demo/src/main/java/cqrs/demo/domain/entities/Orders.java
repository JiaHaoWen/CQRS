package cqrs.demo.domain.entities;

import java.util.UUID;

public interface Orders {
  Order get(UUID orderId);

  void update(Order order);
}
