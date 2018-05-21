package cqrs.infrastructure.events;

/**
 * 聚合根实例化异常
 */
final class AggregateRootInstantiationException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  protected AggregateRootInstantiationException(String message, Throwable cause) {
    super(message, cause);
  }

}
