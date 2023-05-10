package by.clevertec.cache;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests of the LFUCache")
class LFUCacheTest {

    private Integer capacity;
    private Cache<Integer, String> cache;

    private final String firstElement = "firstElement";
    private final String secondElement = "secondElement";
    private final String thirdElement = "thirdElement";

    @BeforeEach
    void setUp() {
        capacity = 3;
        cache = new LFUCache<>(capacity);
    }

    @AfterEach
    void tearDown() {
        cache = null;
    }

    @Test
    @DisplayName("If the cache capacity is less than 0, then throws IllegalArgumentException")
    void given_CapacityLessThanZero_when_BuildCache_then_ThrowIllegalArgumentException() {
        capacity = 0;
        assertThrows(IllegalArgumentException.class, () -> cache = new LRUCache<>(capacity));
    }

    @Test
    @DisplayName("After putting one item to an empty cache, its capacity is increased by 1")
    void given_EmptyCache_when_PutOneElement_then_ElementIsAddedAndCacheSizeIncreasesByOne() {
        int key = 1;
        int capacityBeforePut = cache.size();

        cache.put(key, firstElement);
        int capacityAfterPut = cache.size();

        assertAll(
                () -> assertFalse(cache.isEmpty()),
                () -> assertEquals(capacityBeforePut + 1, capacityAfterPut),
                () -> assertNotNull(cache.get(key))
        );
    }

    @Test
    @DisplayName("After putting one item to the full cache, least frequently used element removes from the cache")
    void given_FullCache_when_PutOneElement_then_LeastFrequentlyUsedElementIsNullAndCacheSizeIsEqualsToCapacity() {
        fillToCapacity();
        cache.put(4, "AnotherElement");

        assertAll(
                () -> assertTrue(cache.get(1).isEmpty()),
                () -> assertTrue(cache.get(4).isPresent()),
                () -> assertEquals(capacity, cache.size())
        );
    }

    @Test
    @DisplayName("After getting one item from the cache, we find the sought element")
    void given_FullCache_when_GetTheElement_then_TheResultingElementIsEquivalentToTheSoughtElement() {
        fillToCapacity();

        assertAll(
                () -> assertEquals(firstElement, cache.get(1).orElse("Not existed element")),
                () -> assertEquals(secondElement, cache.get(2).orElse("Not existed element")),
                () -> assertEquals(thirdElement, cache.get(3).orElse("Not existed element"))
        );
    }

    @Test
    @DisplayName("After removing one item from the full cache, its capacity is decreases by 1")
    void given_FullCache_when_RemoveOneElement_then_ElementIsRemovedAndCacheSizeDecreasesByOne() {
        int key = 1;
        fillToCapacity();
        int capacityBeforeRemove = cache.size();

        cache.remove(key);
        int capacityAfterRemove = cache.size();

        assertAll(
                () -> assertTrue(cache.get(key).isEmpty()),
                () -> assertEquals(capacityBeforeRemove - 1, capacityAfterRemove)
        );
    }

    @Test
    @DisplayName("After cleaning the full cache, the size is set to zero")
    void given_FullCache_when_Clear_then_CacheSizeIsZero() {
        fillToCapacity();
        cache.clear();
        assertEquals(0, cache.size());
    }

    private void fillToCapacity() {
        cache.put(1, firstElement);
        cache.put(2, secondElement);
        cache.put(3, thirdElement);
    }
}