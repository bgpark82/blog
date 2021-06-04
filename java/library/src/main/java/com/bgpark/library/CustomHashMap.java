package com.bgpark.library;

import java.util.Objects;

public class CustomHashMap<K,V> {

    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    static final int MAXIMUM_CAPACITY = 1 << 30;
    static final int TREEIFY_THRESHOLD = 3;         // origin: 8
    static final int MIN_TREEIFY_CAPACITY = 64;

    transient Node<K,V>[] table;

    // threshold 만들 때 사용하는 값
    final float loadFactor;
    // 배열에 실제 들어있는 값의 개수(size)가 threshold를 넘어가면 resize
    int threshold;
    // 배열에 실제 들어있는 값의 개수
    int size;

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

    Node<K,V> newNode(int hash, K key, V value, Node<K,V> next) {
        return new Node(hash, key, value, next);
    }

    final V putVal(int hash, K key, V value, boolean onlyIfAbsent) {
        Node<K,V>[] curTable;
        Node<K,V> node;
        int length;
        int i;

        // 1. 테이블이 비어있는 경우 초기화
        if((curTable = table) == null || (length = curTable.length) == 0) {
            length = (curTable = resize()).length;
        }
        System.out.println("===>> index : " + getIndex(hash, length) + ", key : " + key + ", hash : " + hash+ ", size : " + size+ ", length : " + length);

        // 2. 해당 index 에 값이 없으면 새로운 노드 추가
        if((node = curTable[i = getIndex(hash, length)]) == null) {
            curTable[i] = newNode(hash, key, value, null);

        // 3. 해당 index 에 값이 있는 경우
        } else {
            Node<K,V> tmpNode = null;
            K k;

            // 같은 hash에 같은 key이면, 해당 index에 새로운 값으로 교체
            if(node.hash == hash && ((k = node.key) == key || (key != null && key.equals(k)))) {
                tmpNode = node;
            // 다른 hash에 다른 key이면, 해당 index에 값에 노드 연결
            } else {
                for(int binCount = 0; ; ++binCount) {
                    // LinkedList로 연결한다
                    if((tmpNode = node.next) == null) {
                        System.out.println("index : " + getIndex(hash, length) + ", key : " + key + ", hash : " + hash+ ", size : " + size+ ", length : " + length);

                        node.next = newNode(hash, key, value, null);

                        // node가 길어지면 tree 형태로 변환한다
                        if(binCount >= TREEIFY_THRESHOLD - 1) {
                            treeifyBin(table, hash);
                        }
                        break;
                    }
                    // 다음 노드
                    node = tmpNode;
                }
            }

            if(tmpNode != null) {
                V oldValue = tmpNode.value;
                if(!onlyIfAbsent || oldValue == null) {
                    tmpNode.value = value;
                }
                return oldValue;
            }
        }

        // size가 threshold보다 크면 size 키운다 (12, 24, 48...)
        if(++size > threshold) {
            resize();
        }

        return null;
    }

    private void treeifyBin(Node<K,V>[] table, int hash) {
        int length;
        int index;
        Node<K,V> node;
        if(table == null || (length = table.length) < MIN_TREEIFY_CAPACITY) {
            resize();

        // 테이블의 길이가 64이상이면 TreeNode로 변환
        } else if ((node = table[getIndex(hash, length)]) != null) {
            treeify(node);
        }
    }

    void treeify(Node<K, V> node) {
        TreeNode<K,V> root = null;
        TreeNode<K,V> leaf = null;
        do {
            TreeNode<K, V> treeNode = replacementTreeNode(node, null);
            if(leaf == null) {
                root = treeNode;
            } else {
                treeNode.prev = leaf;
                leaf.next = treeNode;
            }
            leaf = treeNode;
            System.out.println("root : " + root +", leaf : " + leaf +", treeNode : " + treeNode);

        } while((node = node.next) != null);
    }

    private TreeNode<K,V> replacementTreeNode(Node<K,V> node, Node<K,V> next) {
        return new TreeNode<>(node.hash, node.key, node.value, next);
    }

    static class TreeNode<K,V> extends Node {
        TreeNode<K,V> parent;
        TreeNode<K,V> left;
        TreeNode<K,V> right;
        TreeNode<K,V> prev;
        boolean red;

        TreeNode(int hash, K key, V value, Node<K,V> next) {
            super(hash, key, value, next);
        }
    }

    /**
     * resize 할 때마다 size, threshold 두배씩 증가
     * size = 16, 32, 64, 128, 256..
     * threshold = 12, 24, 48, 96, 192...
     */
    final Node[] resize() {
        Node[] oldTable = table;

        int oldCapacity = (oldTable == null) ? 0 : oldTable.length;
        int oldThreshold = threshold;
        int newCapacity = 0;
        int newThreshold = 0;

        // 기존 값이 있는 경우
        if(oldCapacity > 0) {
            if((newCapacity = oldCapacity << 1) < MAXIMUM_CAPACITY && oldCapacity >= DEFAULT_INITIAL_CAPACITY) {
                newThreshold = oldThreshold << 1;
            }
        // 초기화
        } else {
            newCapacity = DEFAULT_INITIAL_CAPACITY;
            newThreshold = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
        }

        Node[] newTable = new Node[newCapacity];
        threshold = newThreshold;
        table = newTable;
        if(oldTable != null) {
            for(int j = 0; j < oldCapacity; ++j) {
                Node<K,V> e;
                if((e = oldTable[j]) != null) {
                    oldTable[j] = null;
                    // 새로운 table에 기존 node 할당
                    if(e.next == null) {
                        newTable[e.hash & (newCapacity - 1)] = e;
                    }
                }
            }
        }

        return newTable;
    }

    public V put(K key, V value) {
        return putVal(hash(key), key, value, false);
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
            (first = curTable[getIndex(hash, length)]) != null) {

            if(first.hash == hash &&
                    ((k = first.key) == key || (key != null && key.equals(k)))) {
                return first;
            }
        }

        return null;
    }

    final int getIndex(int hash, int length) {
        return (length - 1) & hash;
    }


    static class Node<K,V> {

        final int hash;
        final K key;
        V value;
        Node<K,V> next;

        Node(int hash, K key, V value, Node<K,V> next) {
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

        @Override
        public String toString() {
            return "Node{" +
                    "hash=" + hash +
                    ", key=" + key +
                    ", value=" + value +
                    ", next=" + next +
                    '}';
        }
    }
}
