package by.clevertec.cache;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LRUCacheTest {

    private  static Cache<Integer, String> cache;


    @BeforeAll
    static void init(){
        cache = new LRUCache<Integer, String>();
        int capacity = cache.capacity();
    }
    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void put() {
    }

    @Test
    void get() {
    }

    @Test
    void remove() {
    }

    @Test
    void clear() {
    }
}