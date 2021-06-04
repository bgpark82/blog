package com.bgpark.library;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.IntStream;

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
    void constructor10() {
        CustomHashMap map = new CustomHashMap(10);
        assertThat(map.loadFactor).isEqualTo(0.75f);
        assertThat(map.threshold).isEqualTo(1 << 4);
    }

    @Test
    void tableSizeFor1() {
        int result1 = CustomHashMap.tableSizeFor(3); // 2^1 ~ 2^2
        assertThat(result1).isEqualTo((int)Math.pow(2, 2));

        int result2 = CustomHashMap.tableSizeFor(10); // 2^3 ~ 2^4
        assertThat(result2).isEqualTo((int)Math.pow(2, 4));

        int result3 = CustomHashMap.tableSizeFor(1<<5); // 2^3 ~ 2^4
        assertThat(result3).isEqualTo((int)Math.pow(2, 5));
    }

    @Test
    void tableSizeFor2() {
        int result1 = CustomHashMap.tableSizeFor(-10); // 2^1 ~ 2^2
        assertThat(result1).isEqualTo(1);
    }

//        https://mincong.io/2018/04/08/learning-hashmap/
//        https://www.zhihu.com/question/20733617
//        https://d2.naver.com/helloworld/831311
//        https://coderoad.ru/45125497/%D0%97%D0%B0%D1%87%D0%B5%D0%BC-%D0%B2%D0%BE%D0%B7%D0%B2%D1%80%D0%B0%D1%89%D0%B0%D1%82%D1%8C-h-key-hashCode-h-16-%D0%BA%D1%80%D0%BE%D0%BC%D0%B5-key-hashcode
//        hash ^ (hash >> 16)
//        hash                = 11111111 11111111 11110000 11101010
//        hash >> 16          = 00000000 00000000 11111111 11111111
//        hash ^ hash >> 16   = 11111111 11111111 00001111 00010101
//        ^ : 50/50
//        | : 75/25
//        & : 25/75
    @Test
    void put() {
        int h = "key-value".hashCode();
        System.out.println(Integer.toBinaryString((h)));
        System.out.println(Integer.toBinaryString(h >>> 16));
        System.out.println(Integer.toBinaryString(h ^ (h >>> 16)));
        System.out.println(h >>> 16);
        System.out.println(h ^ (h >>> 16));
    }

    @Test
    void put2() {
        CustomHashMap map = new CustomHashMap();
        map.put("key", "value");
        assertThat(map.table.length).isEqualTo(16);
        assertThat(map.table).contains(map.newNode(CustomHashMap.hash("key"), "key", "value", null));
        assertThat(map.threshold).isEqualTo(12);
    }

    @Test
    void put3() {
        CustomHashMap<String, String> map = new CustomHashMap();
        map.put("key","value");

        String result = map.put("key", "value1");

        assertThat(result).isEqualTo("value");
        assertThat(map.get("key")).isEqualTo("value1");
        assertThat(map.size).isEqualTo(1);
    }

    @Test
    void put4() {
        CustomHashMap<String, String> map = new CustomHashMap();
        map.put("key","value");
        assertThat(map.table.length).isEqualTo(16);
        assertThat(map.threshold).isEqualTo(12);
        assertThat(map.size).isEqualTo(1);

        IntStream.range(0,12).forEach(i -> map.put("key" +i, "value" + i));

        assertThat(map.table.length).isEqualTo(32);
        assertThat(map.threshold).isEqualTo(24);
        assertThat(map.size).isEqualTo(13);

        IntStream.range(0,24).forEach(i -> map.put("key" +i, "value" + i));

        assertThat(map.table.length).isEqualTo(64);
        assertThat(map.threshold).isEqualTo(48);
        assertThat(map.size).isEqualTo(25);

        IntStream.range(0,48).forEach(i -> map.put("key" +i, "value" + i));

        assertThat(map.table.length).isEqualTo(128);
        assertThat(map.threshold).isEqualTo(96);
        assertThat(map.size).isEqualTo(52);         // 49가 나와야 하는데 52가 나왔다

        IntStream.range(0,96).forEach(i -> map.put("key" +i, "value" + i));

        assertThat(map.table.length).isEqualTo(256);
        assertThat(map.threshold).isEqualTo(192);
        assertThat(map.size).isEqualTo(112);         // 97이 나와야 하는데 112가 나왔다
    }

    @Test
    void put5() {
        CustomHashMap<String, String> map = new CustomHashMap<>();
        map.put("1", "2");

        int n = map.table.length;
        int hash1 = CustomHashMap.hash("1");
        int hash2 = CustomHashMap.hash("key25");
        assertThat((n -1) & hash1).isEqualTo((n-1) & hash2);
    }

    @Test
    void put6() {
        CustomHashMap<String, String> map = new CustomHashMap<>();
        map.put("1", "2");
        map.put("key25", "2");

        int hash1 = CustomHashMap.hash("1");
        int index1 = map.getIndex(hash1, 16);

        int hash2 = CustomHashMap.hash("key25");
        int index2 = map.getIndex(hash2, 16);

        assertThat(index1).isEqualTo(index2);
        assertThat(map.table[index1].key).isEqualTo("1");
        assertThat(map.table[index1].next.key).isEqualTo("key25");
    }

    @Test
    void getIndex() {
        CustomHashMap<String, String> map = new CustomHashMap<>();
        map.put("1", "2");
        map.put("key25", "2");
        map.put("php13", "2");
        map.put("java5", "2");
        map.put("python3", "2");
        map.put("javascript4", "2");
        map.put("a1", "2");


    }

    @Test
    void treeify() {
        CustomHashMap<String, String> map = new CustomHashMap<>();
        CustomHashMap.Node<String, String> node6 = map.newNode(6, "6", "6", null);
        CustomHashMap.Node<String, String> node5 = map.newNode(5, "5", "5", node6);
        CustomHashMap.Node<String, String> node4 = map.newNode(4, "4", "4", node5);
        CustomHashMap.Node<String, String> node3 = map.newNode(3, "3", "3", node4);
        CustomHashMap.Node<String, String> node2 = map.newNode(2, "2", "2", node3);
        CustomHashMap.Node<String, String> node1 = map.newNode(1, "1", "1", node2);

        map.treeify(node1);
    }

    @Test
    void hash() {
        int hash = CustomHashMap.hash("key");

        assertThat(hash).isEqualTo(106078);
        assertThat(Integer.toBinaryString("key".hashCode())).isEqualTo("11001111001011111");
        assertThat(Integer.toBinaryString("key".hashCode() >> 16)).isEqualTo("1");
        assertThat(Integer.toBinaryString(hash)).isEqualTo("11001111001011110");
    }

    @Test
    void hash2() {
        int hash = CustomHashMap.hash(null);

        assertThat(hash).isEqualTo(1);
    }

    @Test
    void putVal() {
        int hash = CustomHashMap.hash("key");
        CustomHashMap map = new CustomHashMap();

        map.putVal(hash, "key", "value", false);

        assertThat(map.table.length).isEqualTo(16);
        assertThat(map.table).contains(map.newNode(hash, "key", "value", null));
    }

    @Test
    void newNode() {
        int hash = CustomHashMap.hash("key");
        CustomHashMap map = new CustomHashMap();

        CustomHashMap.Node node = map.newNode(hash, "key", "value", null);

        assertThat(node.hash).isEqualTo(hash);
        assertThat(node.key).isEqualTo("key");
        assertThat(node.value).isEqualTo("value");
        assertThat(node.next).isEqualTo(null);
    }

    @Test
    void resize1() {
        float DEFAULT_LOAD_FACTOR = 0.75f;
        int DEFAULT_INITIAL_CAPACITY = 1 << 4;

        assertThat(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY).isEqualTo(12);
    }

    @Test
    void resize2() {
        CustomHashMap map = new CustomHashMap();

        CustomHashMap.Node[] table = map.resize();

        assertThat(table.length).isEqualTo(16);
        assertThat(map.threshold).isEqualTo(12);
    }

    @Test
    void get() {
        CustomHashMap<String, String> map = new CustomHashMap();

        String value = map.get("key");

        assertThat(value).isNull();
    }

    @Test
    void get2() {
        CustomHashMap<String, String> map = new CustomHashMap();
        map.put("key","value");

        String value = map.get("key");

        assertThat(value).isEqualTo("value");
    }

    @Test
    void getNode() {
        int hash = CustomHashMap.hash("key");
        CustomHashMap<String, String> map = new CustomHashMap();
        map.put("key","value");

        CustomHashMap.Node<String, String> node = map.getNode(hash, "key");

        assertThat(node.key).isEqualTo("key");
        assertThat(node.value).isEqualTo("value");
        assertThat(node.hash).isEqualTo(hash);
        assertThat(node.next).isNull();
    }
}