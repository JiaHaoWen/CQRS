package cqrs.infrastructure;

import cqrs.domain.AMessage;

/**
 * 消息总线顶级接口
 */
public interface TransferMessages<TMessage extends AMessage> {

  /**
   * 将指定的消息发送到对应的消息总线中
   */
  <T extends TMessage> void send(T msg);
}
