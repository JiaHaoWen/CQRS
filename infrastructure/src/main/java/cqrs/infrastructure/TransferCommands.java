package cqrs.infrastructure;

import cqrs.domain.ACommand;

/**
 * 命令消息总线接口
 */
public interface TransferCommands extends TransferMessages<ACommand> {
}
