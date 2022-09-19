---
title: "212.\_Word Search II"
catalog: true
date: 2022-09-19 23:39:54
subtitle:
header-img:
tags:
---
## Conditions

---

- all lowercase english letter
- all the strings of words are unique
- word is constructed from letters of sequentially adjacent cells which neiboring horizontally or vertically
- the same cells are not used more than once in a word

## Solve by hands first

---

We can start with Brute Force way. Iterate though every input `words` (length ≤ 10) and matches the each letters (length ≤ 3 * 10^4) in `words`. with `board` array (length ≤ 12 ^ 2). so Time complexity will be approximately O(w*l*m*n) = O(n^4). It will takes around 10 x 3 x 10^3 x 12 x 12 = 4,320,000 second which is not efficient

We can also come up with Trie data structure. Create Trie with input `words` and iterate `board` through adjacent cells horizontally and vertically. if node in Trie has the letter in cell then check the adjacent cell if it matches next node in Trie. Since we are only enable to check adjacent cells, use BFS to check the adjacent cell around the target letter.

**So, this question is combination of Trie + BFS + Backtracking algorithm**

## Solution

---

```java
class Solution {
    
    class Trie {
        Trie[] next = new Trie[26];
        String word;
    }
    
    int[] dx = new int[] {1,-1,0,0};
    int[] dy = new int[] {0,0,1,-1};
    
    class Cell {
        int x;
        int y;
        
        Cell(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    
    public List<String> findWords(char[][] board, String[] words) {
        
        List<String> ans = new ArrayList();
        Stack<Cell> q = new Stack();
        
        Trie trie = new Trie();
        for(int i = 0; i < words.length; i++) {
            build(words[i], trie);
        }
        
        Trie root = trie;
        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board[i].length; j++) {
            
                // initialze the trie
                trie = root;
                q.push(new Cell(i, j));
                
                while(!q.isEmpty()) {
                    Cell cc = q.pop();
                    char c = board[cc.x][cc.y];
                    
                    int idx = c - 'a';
                    
                    // if there is no letter in trie then skip
                    if(trie.next[idx] == null) continue;
                    
                    // move to next node
                    trie = trie.next[idx];
                        
                    // add if it's last
                    if(trie.word != null) {
                        ans.add(trie.word);
                        trie.word = null; // initialize the last word otherwise it will keep iterate
                    }
                                        
                    for(int z = 0; z < 4; z++) {
                        int cx = cc.x + dx[z];
                        int cy = cc.y + dy[z];

                        if(cx < 0 || cy < 0 || cx >= board.length || cy >= board[i].length) continue;
                        
                        q.push(new Cell(cx, cy));
                    }
                }
            }
        }
        
        return ans;
    }
    
    private void build(String word, Trie trie) {
        
        // 1. create Trie data structure
        for(int i = 0; i < word.length(); i++) {    
            char c = word.charAt(i);
            int idx = c - 'a';
            
            if(trie.next[idx] == null) {
                trie.next[idx] = new Trie();
            }
            
            trie = trie.next[idx];
        }
        
        // 2. caches the words at the last trie 
        trie.word = word;
    }
}
```

1. create **Trie** with input words
2. visit every cell and find the word in Trie by **DFS**
3. switch visited cell to temp word and recover it by **Backtracking**

**The mistake that I made was that I use BFS not DFS**. I can switch Queue to Stack but still needed backtracking but it seems impossible to backtrack with Stack.

More than one word in Trie can be started from the same cell. For instance words “hklf” and “hf” is from the same cell “h”. In “hf”, f is already visited so “hklf” can’t access to f

```java

      l   pop  
h pop f        f
```

## Lesson I learnt

---

1. How to build trie
    
    **In Trie, each index represent the alphat letter.** If the value in index of array is null, it means there is no next letter in Trie.
    
    ```java
    class Trie {
    
    	Trie[] next = new Trie[26];
    	String word;
    }
    
    void build() {
    	Trie trie = new Trie();
    
    	for(int i = 0; i < word.length(); i++) {    
    		// 1. create Trie
    		char c = word.charAt(i);
    		int idx = c - 'a';
    		
    		if(trie.next[idx] == null) {
    			trie.next[idx] = new Trie();
    		}
    		
    		trie = trie.next[idx];
    	}
    
    	  // 2. caches the words at the last trie 
    	trie.word = word;
    }
    ```
    
    Trie data structure to make word “bb”
    
    ![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/9b2855d5-cb2b-4c95-b90d-f3fac6bf3ba7/Untitled.png)
    
2. **initialize the trie node when other words start** 
    
    ```java
    Trie root = trie;
    for(int i = 0; i < board.length; i++) {
        for(int j = 0; j < board[i].length; j++) {
        
            // initialze the trie
            trie = root;
    	}
    }
    ```
    
3. **intialize the trie.word to skip the duplicated word**
    
    oath, ath, th, h end with h which Trie contains word = oath. so in order to ignore the imperfect word, initialize the trie.word to null
    
    ```java
    if(trie.word != null) {
        ans.add(trie.word);
        trie.word = null; // to avoid checking similiar words
    }
    ```
    
4. **How can I check if the word is already used?**
    
    ```java
    board[y][x] = '_';                     // temp word
    find(board, x + 1, y, trie, result);
    find(board, x, y + 1, trie, result);
    find(board, x - 1, y, trie, result);
    find(board, x, y - 1, trie, result);
    board[y][x] = c;                       // recover
    ```
    
    If we use the Backtracking, we can switch the value temporarly and recover it. Threre are two ways to implement backtracking
    
    1. **Stack**
    2. **Memory Stack**
    
    Problem of using Stack data structure, we have to cache the important to data to recover. so it is way easier to use Memory Stack for backtracking
    
5. **Trie can also check if the character is in the next Trie array**
    
    ```java
    class Trie {
        Trie[] next = new Trie[26];
        String word;
    
    		boolean isContain(char c) {
    			return next[c - 'a'] != null;
    		}
    }
    ```
