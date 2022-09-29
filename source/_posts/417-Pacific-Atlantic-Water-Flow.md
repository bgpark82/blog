---
title: "417.\_Pacific Atlantic Water Flow"
catalog: true
date: 2022-09-29 10:30:47
subtitle:
header-img:
tags:
---
## Conditions

---

- The **Pacific Ocean** touches the island's left and top edges
- the **Atlantic Ocean** touches the island's right and bottom edges.
- `heights[r][c]` represents the **height above sea level**
- if the neighboring cell's height is **less than or equal to** the current cell's height
- Return *a **2D list** of grid coordinates* `result` *where* `result[i] = [ri, ci]` *denotes that rain water can flow from cell* `(ri,ci)` *to **both** **the Pacific and Atlantic oceans***
.

## Solve by hands first

---

we can start with DFS with every cells. constraint is that water only can run to **both the Pacific and Atlantic [oceans**.](http://oceans.so) so we have check water can reach to top left or bottom right. 

how to compare the prev node value and current node value in DFS

- we can just pass the current height as method parameter

we can have two visitor metrix to check which ocean the water flows to. pacific and atlantic. we don’t actually start with every single cells, instead we can travel back from the each ocean and check the cell visited if the water can flows from the visited cell in reverse way.

there are 4 possibilities

1. [0,0] → [0, n] : cell enable to flows to pacific ocean
2. [0,0] → [n, 0] : cell enable to flows to pacific ocean
3. [n, n] → [0, n] : cell enable to flows to atlantic ocean
4. [n, n] → [n, 0] : cell enable to flows to atlantic ocean

create two boolean metrix to mark as visited depends on where the water flows from. if the water comes from Pacific ocean side, set true in pacific ocean boolean metric. after all traversal, we can simple iterate through each metrix and check both marked visited in pacific and atlantic metrix. store the coordination at both of metrix marked visited.

![4BA68C9B-A2E0-496D-9250-508BC74BAC89.jpeg](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/89a61c15-7689-4b25-b6b9-7990af240092/4BA68C9B-A2E0-496D-9250-508BC74BAC89.jpeg)

## Solution

---

```java
class Solution {
    
    boolean[][] pm, am;
    
    public List<List<Integer>> pacificAtlantic(int[][] heights) {
        List<List<Integer>> ans = new ArrayList();
        
        pm = new boolean[heights.length][heights[0].length];
        am = new boolean[heights.length][heights[0].length];
        
        int row = heights.length;
        int col = heights[0].length;
        
        for(int r = 0; r < row; r++) {
            dfs(heights, r, 0, heights[r][0], pm);
            dfs(heights, r, col-1, heights[r][col-1], am);
        }
        
        for(int c = 0; c < col; c++) {
            dfs(heights, 0, c, heights[0][c], pm);
            dfs(heights, row-1, c, heights[row-1][c], am);
        }

        for(int i = 0; i < row; i++) {
            for(int j = 0; j < col; j++) {
                if(am[i][j] && pm[i][j]) ans.add(Arrays.asList(i,j));
            }
        }
        return ans;
    }
    
    
    private void dfs(int[][] heights, int i, int j, int prev, boolean[][] m) {
        if(i < 0 || j < 0 || i >= heights.length || j >= heights[0].length || m[i][j]|| prev > heights[i][j]) return;
    
        m[i][j] = true;
        dfs(heights, i+1, j, heights[i][j], m);
        dfs(heights, i, j+1, heights[i][j], m);
        dfs(heights, i-1, j, heights[i][j], m);
        dfs(heights, i, j-1, heights[i][j], m);
    }
}
```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/bc41c7ae-36fa-4523-9682-bc7ee9f1b396/Untitled.png)

## Lesson I learnt

---

1. thinking reverse way
2. we can just visit every node with dfs and **mark visited in somewhere else (like cache)**.  in this case mostly we don’t need return value for dfs method.
