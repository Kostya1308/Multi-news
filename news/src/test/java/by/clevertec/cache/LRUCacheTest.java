package by.clevertec.cache;

import org.junit.jupiter.api.*;

import java.lang.reflect.Field;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class LRUCacheTest {

    private LRUCache<Integer, String> cache;

    @BeforeEach
    void initCache() {
        int capacity = 2;
        cache = new LRUCache<>();
    }

    @AfterEach
    void clearCache() {
        cache.clear();
    }


    @Test
    void putThreeElementsAndCacheContainsLastTwoElementsTest() {
        fillCacheByTreeElements();

        assertTrue(cache.get(2).isPresent());
        assertTrue(cache.get(3).isPresent());
        assertFalse(cache.get(1).isPresent());
    }

    @Test
    void clearCacheAndCacheIsEmptyTest() {
        fillCacheByTreeElements();

        cache.clear();
        assertTrue(cache.isEmpty());
    }

    @RepeatedTest(10000)
    @Disabled
    void afterWorkingOfThreeTreadsCapacityIsAsSpecified() throws InterruptedException, IllegalAccessException,
            NoSuchFieldException {
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                cache.put(1, "USA");
            }
        });
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                cache.put(2, "Spain");
            }
        });
        Thread thread3 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                cache.put(3, "Italy");
            }
        });

        thread1.start();
        thread2.start();
        thread3.start();

        thread1.join();
        thread2.join();
        thread3.join();

        Field data = cache.getClass().getDeclaredField("order");
        data.setAccessible(true);
        LinkedList<Integer> linkedList = (LinkedList<Integer>) data.get(cache);

        assertTrue(linkedList.size() <= cache.capacity());
    }

    private void fillCacheByTreeElements() {
        cache.put(1, "Green");
        cache.put(2, "Blue");
        cache.put(3, "Black");
    }
}