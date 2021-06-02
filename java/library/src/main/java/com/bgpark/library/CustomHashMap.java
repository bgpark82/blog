package com.bgpark.library;

public class CustomHashMap {

    static final float  DEFAULT_LOAD_FACTOR = 0.75f;

    final float loadFactor;
     int threshold;

    public CustomHashMap() {
        this.loadFactor = DEFAULT_LOAD_FACTOR;
    }

    public CustomHashMap(int initialCapacity, float loadFactor) {
        if(initialCapacity < 0) {
            throw new IllegalArgumentException("음수 불가");
        }
        if (initialCapacity > 1 << 30) {
            initialCapacity = 1 << 30;
        }
        this.loadFactor = loadFactor;
        this.threshold = initialCapacity;
    }
}
