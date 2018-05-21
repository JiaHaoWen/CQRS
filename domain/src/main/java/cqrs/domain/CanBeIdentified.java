package cqrs.domain;

import java.util.UUID;

/**
 * 接口标记通用接口,给出唯一id
 */
public interface CanBeIdentified {
  /**
   */
  UUID getId();
}
