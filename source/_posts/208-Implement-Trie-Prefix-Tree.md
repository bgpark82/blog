---
title: "208.\_Implement Trie (Prefix Tree)"
catalog: true
date: 2022-10-02 14:00:48
subtitle:
header-img:
tags:
---
## Conditions

---

- `word` and `prefix` consist only of lowercase English letters.

## Solve by hands first

---

This is basic Trie data structure

## Solution

---

```java
class Trie {
    
    Trie[] next;
    boolean end;

    public Trie() {
        this.next = new Trie[26];
        this.end = false;
    }
    
    public void insert(String word) {
        Trie trie = this;
        for(int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if(trie.next[c - 'a'] == null) trie.next[c - 'a'] = new Trie();
            trie = trie.next[c - 'a'];
        }
        trie.end = true;
    }
    
    public boolean search(String word) {
        Trie trie = this;
        for(int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            
            if(trie.next[c - 'a'] == null) return false; // letter is not stored

            trie = trie.next[c - 'a'];    
        }
        return trie.end;
    }
    
    public boolean startsWith(String prefix) {
        Trie trie = this;
        for(int i = 0; i < prefix.length(); i++) {
            char c = prefix.charAt(i);
            
            if(trie.next[c - 'a'] == null) return false;  // letter is not stored
            
            trie = trie.next[c - 'a'];    
        }
        return true;
    }
}
```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/5a1556a0-38ec-44c0-8c91-7b5cd2aa8239/Untitled.png)

## Lesson I learnt

---

- Trie is prefix tree
- Trie is for autocomplete and spellchecker
- we can only check if the letter is in Trie or not
    
    ```java
    if(trie.next[c - 'a'] == null) return false; // letter is not stored
    ```
