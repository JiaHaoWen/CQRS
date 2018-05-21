package cqrs.infrastructure.events;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

import cqrs.domain.StoreAggregates;
import cqrs.domain.events.AnEventBasedAggregateRoot;
import cqrs.domain.events.EventStream;

/**
 * ES存储器
 */
public class EventSourcingRepository<RootT extends AnEventBasedAggregateRoot>
    implements StoreAggregates<RootT> {

  private static final long DEFAULT_SNAPSHOT_THRESHOLD = 10L;

  private final Class<RootT> aggregateRootType;
  private final StoreEvents eventStore;
  private final StoreAggregates<RootT> snapshotRepository;
  private final long snapshotThreshold;

  /**
   */
  public EventSourcingRepository(StoreEvents eventStore) {
    this(eventStore, new EmptyRepository<>());
  }

  /**
   */
  public EventSourcingRepository(StoreEvents eventStore,
      StoreAggregates<RootT> snapshotRepository) {
    this(eventStore, snapshotRepository, DEFAULT_SNAPSHOT_THRESHOLD);
  }

  /**
   */
  public EventSourcingRepository(StoreEvents eventStore,
                                 StoreAggregates<RootT> snapshotRepository, long snapshotThreshold) {
    Objects.requireNonNull(eventStore);
    Objects.requireNonNull(snapshotRepository);
    this.eventStore = eventStore;
    this.snapshotRepository = snapshotRepository;
    this.snapshotThreshold = snapshotThreshold;
    this.aggregateRootType = determineAggregateRootType();
  }

  @SuppressWarnings("unchecked")
  private Class<RootT> determineAggregateRootType() {
    return (Class<RootT>) Arrays.asList(getClass().getGenericSuperclass()).stream()
        .map(clazz -> ((ParameterizedType) clazz))
        .filter(pt -> pt.getRawType().equals(EventSourcingRepository.class))
        .map(pt -> pt.getActualTypeArguments()[0])
        .findFirst().get();
  }

  /**
   */
  @Override
  public final void store(RootT root) {
    Objects.requireNonNull(root);
    eventStore.save(root.getId(), root.getUncommittedEvents(), root.getVersion());
    root.markEventsAsCommitted();
  }

  /**
   */
  @Override
  public final RootT retrieveWithId(UUID aggregateRootId) {

    RootT root = snapshotRepository.retrieveWithId(aggregateRootId);
    if (root == null) {
      root = createAggregateRoot(aggregateRootId);
    }
    long baseVersion = root.getVersion();
    EventStream history =
        eventStore.getEventsForAggregate(aggregateRootId, root.getVersion());
    root.rebuildFromHistory(history);
    saveSnapshot(root, baseVersion);
    return root;
  }

  private void saveSnapshot(RootT root, long baseVersion) {
    if (root.getVersion() - baseVersion >= snapshotThreshold) {
      snapshotRepository.store(root);
    }
  }

  private RootT createAggregateRoot(UUID id) {
    try {
      Constructor<RootT> constructor = aggregateRootType.getDeclaredConstructor(UUID.class);
      constructor.setAccessible(true);
      return constructor.newInstance(id);
    } catch (NoSuchMethodException | SecurityException | InstantiationException
        | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
      throw new AggregateRootInstantiationException(String.format(
          "对类型 '%s' 创建聚合根失败. 需要保证 '%s' 为具备至少一个参数类型 '%s' (聚合根id) 构造函数的非抽象类",
          aggregateRootType.getSimpleName(),
          aggregateRootType.getName(),
          UUID.class.getName()), e);
    }
  }
}
