package br.com.eduardothomaz.algorithms;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/** Hash table using the division method and separate chaining. */
public final class DivisionHashTable<K, V> {
    private static final int DEFAULT_CAPACITY = 8;
    private static final double MAX_LOAD_FACTOR = 0.75;

    private List<Entry<K, V>>[] buckets;
    private int size;

    public DivisionHashTable() {
        this(DEFAULT_CAPACITY);
    }

    @SuppressWarnings("unchecked")
    public DivisionHashTable(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity must be positive");
        }
        buckets = (List<Entry<K, V>>[]) new List<?>[capacity];
        for (int index = 0; index < capacity; index++) {
            buckets[index] = new LinkedList<Entry<K, V>>();
        }
    }

    public int size() {
        return size;
    }

    public int capacity() {
        return buckets.length;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    /** Returns hash(key) mod m, normalized to the interval [0, m - 1]. */
    public int bucketIndex(K key) {
        Objects.requireNonNull(key, "key");
        return Math.floorMod(key.hashCode(), buckets.length);
    }

    public V put(K key, V value) {
        Objects.requireNonNull(key, "key");
        int index = bucketIndex(key);
        for (Entry<K, V> entry : buckets[index]) {
            if (entry.key.equals(key)) {
                V previous = entry.value;
                entry.value = value;
                return previous;
            }
        }

        buckets[index].add(0, new Entry<K, V>(key, value));
        size++;
        if ((double) size / buckets.length > MAX_LOAD_FACTOR) {
            resize(buckets.length * 2);
        }
        return null;
    }

    public V get(K key) {
        Objects.requireNonNull(key, "key");
        for (Entry<K, V> entry : buckets[bucketIndex(key)]) {
            if (entry.key.equals(key)) {
                return entry.value;
            }
        }
        return null;
    }

    public boolean containsKey(K key) {
        Objects.requireNonNull(key, "key");
        for (Entry<K, V> entry : buckets[bucketIndex(key)]) {
            if (entry.key.equals(key)) {
                return true;
            }
        }
        return false;
    }

    public V remove(K key) {
        Objects.requireNonNull(key, "key");
        List<Entry<K, V>> bucket = buckets[bucketIndex(key)];
        for (int index = 0; index < bucket.size(); index++) {
            Entry<K, V> entry = bucket.get(index);
            if (entry.key.equals(key)) {
                bucket.remove(index);
                size--;
                return entry.value;
            }
        }
        return null;
    }

    public List<K> keys() {
        List<K> result = new ArrayList<K>(size);
        for (List<Entry<K, V>> bucket : buckets) {
            for (Entry<K, V> entry : bucket) {
                result.add(entry.key);
            }
        }
        return result;
    }

    private void resize(int newCapacity) {
        DivisionHashTable<K, V> replacement = new DivisionHashTable<K, V>(newCapacity);
        for (List<Entry<K, V>> bucket : buckets) {
            for (Entry<K, V> entry : bucket) {
                replacement.put(entry.key, entry.value);
            }
        }
        this.buckets = replacement.buckets;
        this.size = replacement.size;
    }

    private static final class Entry<K, V> {
        private final K key;
        private V value;

        private Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
