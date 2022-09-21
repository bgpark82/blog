---
title: "200.\_Number of Islands"
catalog: true
date: 2022-09-21 20:39:28
subtitle:
header-img:
tags:
---
## Conditions

---

- m == grid.length
- n == grid[i].length
- island is surrounded by water
- is formed by connecting adjacent lands horizontally or vertically

## Solve by hands first

---

we can start with bfs algorithm. since we have to check the adjacent cells at a time. if every cells is within boundary and there is no other land left in horizon and vertical direction. Count the number of island. Time complexity will be O(m*n)

**we also can use dfs algorithm!**

## Solution

---

### bfs

```java
class Solution {
    
    Queue<Point> q = new LinkedList();
    
    int count = 0;

    int[] dx = new int[]{1,-1,0,0};
    int[] dy = new int[]{0,0,1,-1};
    
    class Point {
        
        int x;
        int y;
        
        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    
    public int numIslands(char[][] grid) {
                
        for(int i = 0; i < grid.length; i++) {
            for(int j = 0; j < grid[0].length; j++) {
                if(grid[i][j] == '1') {
                    q.add(new Point(i, j));
                    bfs(grid, i, j);
                }
            }
        }
        
        return count;
    }
    
    private void bfs(char[][] grid, int i, int j) {
        while(!q.isEmpty()) {
            Point p = q.poll();
            
            for(int z = 0; z < 4; z++) {
                int nx = p.x + dx[z];
                int ny = p.y + dy[z];
                
                if(nx < 0 || ny < 0 || nx >= grid.length || ny >= grid[0].length || grid[nx][ny] == '0') continue;
                
                q.add(new Point(nx, ny));
                grid[nx][ny] = '0';
            }
        }
        count++;
    }
}
```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/7dd86d16-519e-4e61-875a-4ab995711e17/Untitled.png)

### bfs

```java
class Solution {
    
  public int numIslands(char[][] grid) {
      int count = 0;
      for(int i = 0; i < grid.length; i++) {
          for(int j = 0; j < grid[0].length; j++) {
              if(grid[i][j] == '1') {
                  dfs(grid, i, j);
                  count++;
              }
          }
      }
      return count;
  }
  
  private void dfs(char[][] grid, int i, int j) {
      if(i < 0 || j <i >= g                }
                
                for(int x = 0; x < 4; x++) {
                    int nx = p.x + dx[x];
                    int ny = p.y + dy[x];
                    
                    if(nx < 0 || ny < 0 || nx >= grid.length || ny >= grid[0].length || visited[nx][ny]) continue;
                    
                    if(grid[nx][ny] == '1') {
                        q.add(new Point(nx, ny)); // snapshot
                    }
                }
            }
            count++;
        }
    ```
    
2. stop double check uncessary condition
    
    ```java
    	private void bfs(char[][] grid, int i, int j) {
            Queue<Point> q = new ArrayDeque();
            
            q.add(new Point(i, j)); // snapshot
            while(!q.isEmpty()) {
                Point p = q.poll();
                
    						// uncessary condition
                if(grid[p.x][p.y] == '1' && !visited[p.x][p.y]) {
                    visited[p.x][p.y] = true;
                }
                
                for(int x = 0; x < 4; x++) {
                    int nx = p.x + dx[x];
                    int ny = p.y + dy[x];
                    
                    if(nx < 0 || ny < 0 || nx >= grid.length || ny >= grid[0].length || visited[nx][ny]) continue;
                    
    								// instead enable to check all at once
                    if(grid[nx][ny] == '1') {
                        q.add(new Point(nx, ny)); // snapshot
                    }
                }
            }
            count++;
        }
    ```
    
3. If we use the queue for bfs, it can cause memory limit when the size of queue become too large. so we need proper filter.
    
    ```java
    private void bfs(char[][] grid, int i, int j) {
        while(!q.isEmpty()) {
            Point p = q.poll();
            
            for(int z = 0; z < 4; z++) {
                int nx = p.x + dx[z];
                int ny = p.y + dy[z];
                
                if(nx < 0 || ny < 0 || nx >= grid.length || ny >= grid[0].length || grid[nx][ny] == '0') continue; // filter
                
                q.add(new Point(nx, ny));
                grid[nx][ny] = '0';
            }
        }
        count++;
    }
    ```
    
4. If we can replace value with other value, we don’t need to consume the space boolean[][] visited 
    
    ```java
    visited[nx][ny] = true // before
    grid[nx][ny] = '0'; // after
    ```
    
5. dfs in metrix can be simple!
    
    ```java
    private void dfs(char[][] grid, int i, int j) {
        if(i < 0 || j < 0 || i >= grid.length || j >= grid[0].length || grid[i][j] == '0') return;
        grid[i][j] = '0';
        dfs(grid, i-1, j);
        dfs(grid, i, j-1);
        dfs(grid, i+1, j);
        dfs(grid, i, j+1);
    }
    ```rid.length || j >= grid[0].length || grid[i][j] == '0') return;
      grid[i][j] = '0';
      dfs(grid, i-1, j);
      dfs(grid, i, j-1);
      dfs(grid, i+1, j);
      dfs(grid, i, j+1);
  }
}
```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/4a44a90b-42df-4611-bf56-884c4090b7a2/Untitled.png)

## Lesson I learnt

---

1. save the snapshot at each element
    
    ```java
    	private void bfs(char[][] grid, int i, int j) {
            Queue<Point> q = new ArrayDeque();
            
            q.add(new Point(i, j)); // snapshot
            while(!q.isEmpty()) {
                Point p = q.poll();
                
                if(grid[p.x][p.y] == '1' && !visited[p.x][p.y]) {
                    visited[p.x][p.y] = true;
 0 || 
