package by.clevertec.cache;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Component
@ConditionalOnProperty(value = "cache.type", havingValue = "lru")
public class LRUCache<K, V> implements Cache<K, V> {

    @Value("${cache.lru.capacity}")
    private Integer capacity;
    private final Map<K, V> data;
    private final LinkedList<K> order;
    private final ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();

    public LRUCache() {
        this.data = new ConcurrentHashMap<>();
        this.order = new LinkedList<>();
    }

    @Override
    public boolean put(K key, V value) {
        this.reentrantReadWriteLock.writeLock().lock();
        try {
            if (order.size() >= this.capacity) {
                K keyRemoved = order.removeLast();
                data.remove(keyRemoved);
            }
            order.addFirst(key);
            data.put(key, value);
            return true;

        } finally {
            this.reentrantReadWriteLock.writeLock().unlock();
        }
    }

    @Override
    public Optional<V> get(K key) {
        this.reentrantReadWriteLock.readLock().lock();
        try {
            Optional<V> value = Optional.ofNullable(data.get(key));
            value.ifPresent((itemValue) -> {
                order.remove(key);
                order.addFirst(key);
            });
            return value;
        } finally {
            this.reentrantReadWriteLock.readLock().unlock();
        }
    }

    @Override
    public void remove(K key) {
        this.reentrantReadWriteLock.readLock().lock();
        try {
            data.remove(key);
            order.remove(key);
        } finally {
            this.reentrantReadWriteLock.readLock().unlock();
        }
    }

    @Override
    public int capacity() {
        return this.capacity;
    }

    @Override
    public boolean isEmpty() {
        return this.data.isEmpty();
    }

    @Override
    public void clear() {
        this.reentrantReadWriteLock.writeLock().lock();
        try {
            data.clear();
            order.clear();
        } finally {
            this.reentrantReadWriteLock.writeLock().unlock();
        }
    }
}
