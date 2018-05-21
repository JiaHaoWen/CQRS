package cqrs.domain.events;

import cqrs.domain.AMessage;
import cqrs.domain.CanBeIdentified;

/**
 * event统一接口
 */
public interface AnEvent extends AMessage, CanBeIdentified {
}
