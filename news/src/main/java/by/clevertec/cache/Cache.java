package by.clevertec.cache;

import java.util.Optional;

public interface Cache<K, V> {
    boolean put(K key, V value);

    Optional<V> get(K key);

    void remove(K key);

    int capacity();

    boolean isEmpty();

    void clear();

}
