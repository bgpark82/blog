---
title: "211.\_Design Add and Search Words Data Structure"
catalog: true
date: 2022-09-26 21:21:26
subtitle:
header-img:
tags:
---
## Conditions

---

- data structure adding new words
- data structure finding if a string matches any previous added string

## Solve by hands first

---

This is typical Trie problem I would say. since we will keep adding up the string into Trie, we need to create the root Trie node as instance variable. we will keep tracking of base on root node. 

for addWord method, create local variable with root node. add the each letters into Trie.

for search method, iterate throught each character in string. if the letter matches Trie then move the pointer to next Trie. 

## Solution

---

```java
class WordDictionary {
    
    Trie root = new Trie();
    
    class Trie {
        Trie[] next = new Trie[26];
        boolean isLast = false;   
    }
    
    public void addWord(String word) {
        Trie cur = root;
        
        for(char c : word.toCharArray()) {
            if(cur.next[c - 'a'] == null) {
                cur.next[c - 'a'] = new Trie();
            }
            cur = cur.next[c - 'a'];
        }
        cur.isLast = true;
    }
    
    public boolean search(String word) {
        return dfs(root, word, 0);
    }
    
    private boolean dfs(Trie node, String word, int idx) {
        if(word.length() == idx) return node.isLast;

        char c = word.charAt(idx);
        if(c == '.') {
           for(Trie tc : node.next) {
                if(tc != null && dfs(tc, word, idx + 1)) return true;
            }
        } else if(node.next[c - 'a'] != null){
           return dfs(node.next[c - 'a'], word, idx + 1);
        }
        return false;
    }
    
}

/**
 * Your WordDictionary object will be instantiated and called as such:
 * WordDictionary obj = new WordDictionary();
 * obj.addWord(word);
 * boolean param_2 = obj.search(word);
 */
```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/3480f671-625a-4b8c-a932-e225085ee284/Untitled.png)

there are two conditions

1. If Trie has the letter, move on to next Trie node at character and next charatecr
2. if the letter is `.` , it can match any characters in Trie but when it’s not null. but it can only be true when it matches the word.

**then what is the valid last node?** 

when we reaches to the length of word and its index is equals to that length, If Trie is the complete word. then it is the word that we search for. 

**question is why the current index is equals to length of word not length - 1?** 

because last Trie node always has the empty array and marker of last node which in this case isLast = true

## Lesson I learnt

---

1. when creating Trie, don’t forget to check null at each letter
2. last node of Trie has alway with **empty array** and **isLast = true**
    
    ```java
     [b,   d,   m]   0
     /     |     \
    [a]   [a]   [a]  1
     |     |     |
    [d]   [d]   [d]  2
     |     |     |
    [ ]   [ ]   [ ]  3
    true  true  true
    ```
    
3. DFS in Tree
    
    ```java
    private void dfs() {
    	// 1. return at the end of node
    
    	// 2. conditions to the next node
    
    	// 3. (optional) backtracking if you need
    }
    ```
