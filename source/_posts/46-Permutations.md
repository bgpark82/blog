---
title: "46.\_Permutations"
catalog: true
date: 2022-10-31 21:47:32
subtitle:
header-img:
tags:
---
### **Requirements**

---

- Given array nums is distinct integers array
- return all the possible permutations in any order
- length of nums is less than equals to 6
- value of nums is greater than equals to -10 and less than equals to 10

### **Edge cases**

---

- [1,2,3] → [[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]]

### **Brute force approach (Discussion + Complexity)**

---

Start with Backtracking first. iterater each element in array. If current list contains value then skip. If current list contains any value in the array then move on. If the current list size is equals to length of nums, add list to answer list. when it returns remove the last value of current list.  TIme complexity is O(size of nums * size of nums * size of nums)

![24E71F87-DFDB-4826-B6D9-7D31B6A335FA.jpeg](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/2758459a-267d-48ee-8da0-3faa3cc71a80/24E71F87-DFDB-4826-B6D9-7D31B6A335FA.jpeg)

```java
List<List<Integer>> res = new ArrayList();
    
public List<List<Integer>> permute(int[] nums) {
    dfs(nums, 0, new ArrayList());
    return res;
}

private void dfs(int[] nums, int idx, List<Integer> list) {
		if(list.size() == nums.length) {
        res.add(new ArrayList(list));
        return;
    }
		
		for(int i = 0; i < nums.length; i++) {
      if(list.contains(nums[i])) continue;
			list.add(nums[i]);
			dfs(nums, i, list);
			list.remove(list.size()-1); // pop the last value
		}
}
```

### **Optimised approach (Discussion + Complexity) …X times**

---

```java
List<List<Integer>> res = new ArrayList();

public List<List<Integer>> permute(int[] nums) {
    dfs(nums, new ArrayList());
    return res;
}

private void dfs(int[] nums, List<Integer> list) {
    if(list.size() == nums.length) {
        res.add(new ArrayList(list));
        return;
    }

    for(int i = 0; i < nums.length; i++) {
        if(list.contains(nums[i])) continue;
        list.add(nums[i]);
        dfs(nums, list);
        list.remove(list.size()-1); // pop the last value
    }
}
```

### **Coding**

---

```java
class Solution {
    List<List<Integer>> res = new ArrayList();

    public List<List<Integer>> permute(int[] nums) {
        dfs(nums, new ArrayList());
        return res;
    }

    private void dfs(int[] nums, List<Integer> list) {
        if(list.size() == nums.length) {
            res.add(new ArrayList(list));
            return;
        }

        for(int i = 0; i < nums.length; i++) {
            if(list.contains(nums[i])) continue;
            list.add(nums[i]);
            dfs(nums, list);
            list.remove(list.size()-1); // pop the last value
        }
    }
}
```

### **Dry Run**

---

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/e149b8f7-c3ef-41ea-ade2-89b3ecad912d/Untitled.png)

### What I learned

---

1. Backtracking is a good approach for permutation
