package com.bgpark.library;

import java.util.Objects;

public class CustomHashMap<K,V> {

    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    static final int MAXIMUM_CAPACITY = 1 << 30;

    transient Node<K,V>[] table;

    final float loadFactor;
    int threshold;

    public CustomHashMap() {
        this.loadFactor = DEFAULT_LOAD_FACTOR;
    }

    public CustomHashMap(int initialCapacity, float loadFactor) {
        if(initialCapacity < 0) {
            throw new IllegalArgumentException("음수 불가");
        }
        if (initialCapacity > MAXIMUM_CAPACITY) {
            initialCapacity = MAXIMUM_CAPACITY;
        }
        if (loadFactor < 0) {
            throw new IllegalArgumentException("음수 불가");
        }
        this.loadFactor = loadFactor;
        this.threshold = tableSizeFor(initialCapacity);
    }

    public CustomHashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    // 만약 capacity가 10이라면 10은 2^3과 2^4 사이의 값이므로 2^4 = 16을 반환한다
    static final int tableSizeFor(int capacity) {
        int n = capacity - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }

    static final int hash(Object key) {
        int h;
        return (key == null) ? 1 : (h = key.hashCode()) ^ (h >>> 16);
    }

    static Node newNode(int hash, String key, String value, Node next) {
        return new Node(hash, key, value, next);
    }

    final String putVal(int hash, String key, String value) {
        Node[] curTable;
        Node p;
        int length;
        int i;

        // 테이블이 null이거나 비어있는 경우
        if((curTable = table) == null || (length = curTable.length) == 0) {
            length = (curTable = resize()).length;
        }

        // 해당 키에 값이 없으면 새로운 노드 추가
        if((p = curTable[i = getKey(hash, length)]) == null) {
            curTable[i] = newNode(hash, key, value, null);
        }

        return null;
    }

    final Node[] resize() {
        Node[] oldTable = table;

        int oldCapacity = (oldTable == null) ? 0 : oldTable.length;
        int oldThreshold = threshold;
        int newCapacity = 0;
        int newThreshold = 0;


        newCapacity = DEFAULT_INITIAL_CAPACITY;
        newThreshold = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);

        Node[] newTable = new Node[newCapacity];
        threshold = newThreshold;
        table = newTable;

        return newTable;
    }

    public void put(String key, String value) {
        putVal(hash(key), key, value);
    }

    public V get(Object key) {
        Node<K,V> node;
        return (node = getNode(hash(key), key)) == null ? null : node.value;
    }

    final Node<K,V> getNode(int hash, Object key) {
        Node<K,V>[] curTable;
        Node<K,V> first;
        int length;
        K k;

        if((curTable = table) != null &&
            (length = table.length) > 0 &&
            (first = curTable[getKey(hash, length)]) != null) {

            if(first.hash == hash &&
                    ((k = first.key) == key || (key != null && key.equals(k)))) {
                return first;
            }
        }

        return null;
    }

    final int getKey(int hash, int length) {
        return (length - 1) & hash;
    }


    static class Node<K,V> {

        final int hash;
        final K key;
        V value;
        Node<K,V> next;

        public Node(int hash, K key, V value, Node next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return hash == node.hash && Objects.equals(key, node.key) && Objects.equals(value, node.value) && Objects.equals(next, node.next);
        }

        @Override
        public int hashCode() {
            return Objects.hash(hash, key, value, next);
        }
    }
}
