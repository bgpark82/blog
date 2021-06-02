package com.bgpark.library;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

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

    @Test
    void constructor6() {
        assertThatThrownBy(() -> new CustomHashMap(10, -1.0f))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void constructor7() {
        assertThatThrownBy(() -> new Float(0 / 0).isNaN())
                .isInstanceOf(ArithmeticException.class);
    }

    @Test
    void constructor8() {
        //https://zapiro.tistory.com/entry/%EC%BB%B4%ED%93%A8%ED%84%B0%EC%9D%98-%EC%9D%8C%EC%88%98-%ED%91%9C%ED%98%84%EB%B2%95%EB%B3%B4%EC%88%98%EB%B2%95
        int a = 8;
        int b = -8;
        assertThat(Integer.toBinaryString(a)).isEqualTo("1000");
        assertThat(Integer.toBinaryString(b)).isEqualTo("11111111111111111111111111111000"); // 8의 보수 + 1
        assertThat(Integer.toBinaryString(a >> 2)).isEqualTo("10");
        assertThat(Integer.toBinaryString(a >>> 2)).isEqualTo("10");
        assertThat(Integer.toBinaryString(b >> 2)).isEqualTo("11111111111111111111111111111110"); // 왼쪽에서 11 추가
        assertThat(Integer.toBinaryString(b >>> 2)).isEqualTo("111111111111111111111111111110"); // 왼쪽에서 00 추가
    }

    //    cap           = 0000 1000 0000 0000 0000 0000 0000 0001 = 2^7+1
    //    n = cap - 1   = 0000 1000 0000 0000 0000 0000 0000 0000
    //    n |= n >>> 1  = 0000 1100 0000 0000 0000 0000 0000 0000
    //    n |= n >>> 2  = 0000 1111 0000 0000 0000 0000 0000 0000
    //    n |= n >>> 4  = 0000 1111 1111 0000 0000 0000 0000 0000
    //    n |= n >>> 8  = 0000 1111 1111 1111 1111 0000 0000 0000
    //    n |= n >>> 16 = 0000 1111 1111 1111 1111 1111 1111 1111
    //    return n + 1  = 0001 0000 0000 0000 0000 0000 0000 0000 = 2^8
    @Test
    void constructor9() {
        // https://stackoverflow.com/questions/51118300/hashmap-tablesizefor-how-does-this-code-round-up-to-the-next-power-of-2
        // https://www.programmersought.com/article/46661105678/
        int b = 129;
        assertThat(129).isLessThan((int) Math.pow(2, 8));
        assertThat(Integer.toBinaryString(b)).isEqualTo("10000001");
        b |= b >>> 1;
        assertThat(Integer.toBinaryString(b)).isEqualTo("11000001");
        b |= b >>> 2;
        assertThat(Integer.toBinaryString(b)).isEqualTo("11110001");
        b |= b >>> 4;
        assertThat(Integer.toBinaryString(b)).isEqualTo("11111111");
        b |= b >>> 8;
        assertThat(Integer.toBinaryString(b)).isEqualTo("11111111");
        b |= b >>> 16;
        assertThat(Integer.toBinaryString(b)).isEqualTo("11111111");
        assertThat(Integer.toBinaryString(b+1)).isEqualTo("100000000");
    }

    @Test
    void tableSizeFor() {
        int result1 = CustomHashMap.tableSizeFor(3); // 2^1 ~ 2^2
        assertThat(result1).isEqualTo((int)Math.pow(2, 2));
        int result2 = CustomHashMap.tableSizeFor(10); // 2^3 ~ 2^4
        assertThat(result2).isEqualTo((int)Math.pow(2, 4));
    }
}