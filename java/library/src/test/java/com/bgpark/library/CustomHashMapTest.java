package com.bgpark.library;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class CustomHashMapTest {

    @Test
    void constructor1() {
        CustomHashMap map = new CustomHashMap();
        assertThat(map.loadFactor).isEqualTo(0.75f);
    }

    @Test
    void constructor2() {
        CustomHashMap map = new CustomHashMap(10, 1.0f);
        assertThat(map.loadFactor).isEqualTo(1.0f);
    }

    @Test
    void constructor3() {
        assertThatThrownBy(() -> new CustomHashMap(-1, 1.0f))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void constructor4() {
        assertThat(1 << 30).isLessThan(Integer.MAX_VALUE);
    }

    @Test
    void constructor5() {
        CustomHashMap map = new CustomHashMap(Integer.MAX_VALUE, 1.0f);
        assertThat(map.threshold).isEqualTo(1 << 30);
    }
}