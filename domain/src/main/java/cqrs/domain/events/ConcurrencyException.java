package cqrs.domain.events;

/**
 * 并发异常
 */
public class ConcurrencyException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  /**
   */
  public ConcurrencyException(final long currentVersion, final long expectedVersion) {
    super(String.format("当前版本 %d 与期望版本 %d 不相等.",
        currentVersion, expectedVersion));
  }
}
