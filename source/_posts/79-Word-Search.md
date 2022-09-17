---
title: "79.\_Word Search"
catalog: true
date: 2022-09-17 18:10:53
subtitle:
header-img:
tags:
---
## Conditions

---

- `board` and `word` consists of only lowercase and uppercase English letters.
- 1 <= m, n <= 6 (max = 6*6 = 36)
- 1 <= word.length <= 15 (max = 15)

## Solve by hands first

---

I can start with Brute Force way but its Time complexity will be O(m*n*word.length). Instead I can use Trie data structure to store the initial input string and loop through the board[][] then its time compexity will be O(m*n)

## Solution

---

I tried to use Trie data structure to save time. Time complexity is O(m*n) and space complexity is O(127 * n)

```java
class Solution {
    
    class Trie {
        Trie[] next = new Trie[127];
        String word = "";
    }
    
    int[] dx = new int[] {1, -1, 0, 0};
    int[] dy = new int[] {0, 0, 1, -1};
    
    boolean[][] visited;
    
    public boolean exist(char[][] board, String word) {
        visited = new boolean[board.length][board[0].length];
        
        Trie trie = new Trie();
        Trie tmp = trie;
        for(int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            
            if(trie.next[c] == null) {
                trie.next[c] = new Trie();
            }
            trie = trie.next[c];
        }
        trie.word = word;
        
        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board[0].length; j++) {
                
                char s = board[i][j];
                
                if(tmp.next[s] == null || visited[i][j]) continue;
                
                tmp = tmp.next[s];
                visited[i][j] = true;
                if(!tmp.word.equals("")) return true;
                
                
                for(int z = 0; z < 4; z++) {
                    int nx = i + dx[z];
                    int ny = j + dy[z];
                    
                    if(nx < 0 || ny < 0 || nx >= board.length || ny >= board[0].length || visited[nx][ny]) continue;
                    
                    char nc = board[nx][ny];    
                    
                    if(tmp.next[nc] == null || visited[nx][ny]) continue;

                    tmp = tmp.next[nc];
                    visited[nx][ny] = true;
                    
                    if(!tmp.word.equals("")) return true;
                }    
            }
        }   
        return false;
    }   
}
```

Problem of this approach is that I loop through the adjacent node vertically and horizontally. **when it reaches to last index, there is no way travel back to previous index**. so we need to use different way. Dynamic programming

```java
class Solution {
    
    class Trie {
        Trie[] next = new Trie[127];
        String word = "";
    }
    
    int[] dx = new int[] {1, -1, 0, 0};
    int[] dy = new int[] {0, 0, 1, -1};
    
    boolean[][] visited;
    
    public boolean exist(char[][] board, String word) {
        visited = new boolean[board.length][board[0].length];
        
        Trie trie = new Trie();
        Trie tmp = trie;
        for(int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            
            if(trie.next[c] == null) {
                trie.next[c] = new Trie();
            }
            trie = trie.next[c];
        }
        trie.word = word;
        
        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board[0].length; j++) {
                if(dfs(tmp, board, i, j)) return true;
            }
        }
        
        return false;
    }
    
    private boolean dfs(Trie tmp, char[][] board, int i, int j) {
        
        if(i < 0 || j < 0 || i >= board.length || j >= board[0].length || visited[i][j]) return false;

        char s = board[i][j];
                
        if(tmp.next[s] == null || visited[i][j]) return false;

        tmp = tmp.next[s];
        visited[i][j] = true;
        if(!tmp.word.equals("")) return true;
        
        boolean ans = dfs(tmp, board, i+1, j) || dfs(tmp, board, i, j+1) || dfs(tmp, board, i-1, j) || dfs(tmp, board, i, j-1);
        
        visited[i][j] = false; // if the character is not in the trie, then check unvisited
        
        return ans;
    }
}
```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/6a8535de-dbc5-4673-843e-353e98a876f6/Untitled.png)

I used the backtracking instead of dynamic programing **because when the current character is not in the trie, then it checks unvisited so that those index can be visited again later in time**.

![83102DB4-5B57-473B-9248-DA95360DB983.jpeg](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/8b0abaf9-239f-411e-9311-d16f3f87597e/83102DB4-5B57-473B-9248-DA95360DB983.jpeg)

## Lesson I learnt

---

1. **we can contain 127 ascii characters in trie** (when we don’t really want to care about anything else!)

```java
class Trie {
    Trie[] next = new Trie[127];
    String word = "";
}

Trie trie = new Trie();
Trie tmp = trie; // cache the pointer to the head node of trie
for(int i = 0; i < word.length(); i++) {
    char c = word.charAt(i);
    
    if(trie.next[c] == null) {
        trie.next[c] = new Trie();
    }
    trie = trie.next[c];
}
trie.word = word;
```

1. **cache the pointer to the head node of trie**

```java
Trie trie = new Trie();
Trie tmp = trie; // cache the pointer to the head node of trie
```

1. **loop and dynamic programming is different!**
    - the mistake I made on this question is that loop was enough. but loop doesn’t chain the next node. so if the loop reaches to the last index, there is no way it can traverse to the other nodes
    - **if we need to traverse other node again! then we have to use dynamic programming**

```java
// before
for(int i = 0; i < board.length; i++) {
    for(int j = 0; j < board[0].length; j++) {
        
        char s = board[i][j];
        
        if(tmp.next[s] == null || visited[i][j]) continue;
        
        tmp = tmp.next[s];
        visited[i][j] = true;
        if(!tmp.word.equals("")) return true;
        
        
        for(int z = 0; z < 4; z++) {
            int nx = i + dx[z];
            int ny = j + dy[z];
            
            if(nx < 0 || ny < 0 || nx >= board.length || ny >= board[0].length || visited[nx][ny]) continue;
            
            char nc = board[nx][ny];    
            
            if(tmp.next[nc] == null || visited[nx][ny]) continue;

            tmp = tmp.next[nc];
            visited[nx][ny] = true;
            
            if(!tmp.word.equals("")) return true;
        }    
    }
}   
```

```java
// after
for(int i = 0; i < board.length; i++) {
    for(int j = 0; j < board[0].length; j++) {
        if(dfs(tmp, board, i, j)) return true;
    }
}

 private boolean dfs(Trie tmp, char[][] board, int i, int j) {
        
    if(i < 0 || j < 0 || i >= board.length || j >= board[0].length || visited[i][j]) return false;

    char s = board[i][j];
            
    if(tmp.next[s] == null || visited[i][j]) return false;

    tmp = tmp.next[s];
    visited[i][j] = true;
    if(!tmp.word.equals("")) return true;
    
    boolean ans = dfs(tmp, board, i+1, j) || dfs(tmp, board, i, j+1) || dfs(tmp, board, i-1, j) || dfs(tmp, board, i, j-1);
    
    visited[i][j] = false; // if the character is not in the trie, then check unvisited
    
    return ans;
}
```

1. use **the backtracking to initialize the condition in matrix problem**
