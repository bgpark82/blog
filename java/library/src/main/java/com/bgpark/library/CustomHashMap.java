package com.bgpark.library;

public class CustomHashMap {

    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final int MAXIMUM_CAPACITY = 1 << 30;

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
        this.threshold = initialCapacity;
    }

    // 만약 capacity가 10이라면 10은 2^3과 2^4 사이의 값이므로 2^4 = 16을 반환한다
    static final int tableSizeFor(int capacity) {
        int n = capacity - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return n + 1;
    }
}
