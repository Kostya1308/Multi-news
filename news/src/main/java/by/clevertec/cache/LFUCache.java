package by.clevertec.cache;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.*;
@Component
@ConditionalOnProperty(value = "cache.type", havingValue = "lfu")
public class LFUCache<K, V> implements Cache<K, V> {

    @Value("${cache.lfu.capacity}")
    private Integer capacity;
    private Integer min;
    private final Map<K, V> data;
    private final Map<K, Integer> counts;
    private final Map<Integer, LinkedHashSet<K>> lists;

    public LFUCache() {
        this.data = new HashMap<>();
        this.counts = new HashMap<>();
        this.lists = new HashMap<>();
        this.lists.put(1, new LinkedHashSet<>());
        this.min = -1;
    }

    @Override
    public boolean put(K key, V value) {
        if (capacity <= 0) {
            return false;
        }

        if (data.containsKey(key)) {
            data.put(key, value);
            get(key);
            return true;
        }

        if (data.size() >= capacity) {
            K removed = lists.get(min).iterator().next();
            lists.get(min).remove(removed);
            data.remove(removed);
            counts.remove(removed);
        }

        data.put(key, value);
        counts.put(key, 1);
        min = 1;
        lists.get(1).add(key);
        return true;
    }

    @Override
    public Optional<V> get(K key) {
        if (!data.containsKey(key)) {
            return Optional.empty();
        }

        Integer count = counts.get(key);
        counts.put(key, count + 1);
        lists.get(count).remove(key);

        if (count.equals(min) && lists.get(count).size() == 0) {
            min++;
        }
        if (!lists.containsKey(count + 1)) {
            lists.put(count + 1, new LinkedHashSet<>());
        }
        lists.get(count + 1).add(key);

        return Optional.of(data.get(key));
    }

    @Override
    public void remove(K key) {
        data.remove(key);
        Integer count = counts.remove(key);
        lists.get(count).remove(key);
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
        data.clear();
        counts.clear();
        lists.clear();
    }
}
