package cqrs.infrastructure.events;

import cqrs.infrastructure.TransferMessages;
import cqrs.domain.events.AnEvent;

/**
 * 事件处理总线
 */
public interface TransferEvents extends TransferMessages<AnEvent> {
}
