---
title: "55.\_Jump Game"
catalog: true
date: 2022-09-16 21:31:49
subtitle:
header-img:
tags:
---
## Conditions

---

- You are initially positioned at the array's first index
- 1 <= nums.length <= 10^4
- 0 <= nums[i] <= 10^5

## Solve by hands first

---

This is typical dynamic programing question. but the problem is 

## Solution

---

1. **dynamic programming**

```java
class Solution {
    
    public boolean canJump(int[] nums) {
        return dfs(nums, 0);
    }
    
    private boolean dfs(int[] nums, int idx) {
        if(idx == nums.length - 1) return true;
        if(nums[idx] == 0) return false;
        
        boolean t = false;
        for(int i = 1; i <= nums[idx]; i++) {
            t |= dfs(nums, idx + i);
        }
        
        return t;
    }  
}
```

![79E23087-0712-4445-8EC9-DC6C3ECBBACC.jpeg](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/a03d7e11-37f5-494c-a2fd-1bb5307d27e2/79E23087-0712-4445-8EC9-DC6C3ECBBACC.jpeg)

problem of this solution was it gonna run throught the same decision tree again which will end up cause the time limit. The time complexity will be O(m^n) (m: the largest element in array, n: length of array)

![0F8571B2-E301-4976-BF77-C5F862F4C946.jpeg](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/580f2ae7-af19-448b-9476-b5be187beb42/0F8571B2-E301-4976-BF77-C5F862F4C946.jpeg)

we can handled the issue by using memoization. Time complexity will be O(n) with more space

1. **greedy**

```java
class Solution {
    
    public boolean canJump(int[] nums) {
        int max = 0;
        for(int i = 0; i < nums.length; i++) {
            if(max < i) return false;
            max = Math.max(max, nums[i] + i);
        }
        return true;
    }
}
```

we can come up with greedy solution. so we don’t need either dynamic programing or cahce memory for this. Time complexity will be O(n) in this case. **The key point is that the farest index on each element is less than any index before last index which means we can’t reach to the last index**.

![B8991A57-0642-490D-A2BC-8C8B57797066.jpeg](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/5421fb21-5f0d-4ba8-95c4-8bf1341e544c/B8991A57-0642-490D-A2BC-8C8B57797066.jpeg)

## Lesson I learnt

---

1. Time complexity of dynamic programming is **O(height of tree*max value among elements)**
2. **I should’ve compared at lease 2 different cases (true and false cases)**
3. **greedy can be a good alternative on dynamic programming**
