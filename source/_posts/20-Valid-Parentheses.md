---
title: "20.\_Valid Parentheses"
catalog: true
date: 2022-09-13 19:27:35
subtitle:
header-img:
tags:
---
## Conditions

---

- Open brackets must be closed by the same type of brackets.
- Open brackets must be closed in the correct order.
- Every close bracket has a corresponding open bracket of the same type.

## Solve by hands first

---

Valid parentheses questions can be solve by using **Stack**. Store the pair of parenthese as key and value of the map which can have an advantages on time complexity (O(1)). Total time complexity is O(n) and space complexity is O(6)

## Solution

---

```java
public boolean isValid(String s) {
        
			// store the parentheses
      Map<Character, Character> sm = new HashMap();
      sm.put('[', ']');
      sm.put('{', '}');
      sm.put('(', ')');
      
      Stack<Character> cs = new Stack();

			// O(n)      
      for(int i = 0; i < s.length(); i++) {            
          char c = s.charAt(i);
          
          if(cs.isEmpty()) cs.push(c);
          else if(!cs.isEmpty() && sm.get(cs.peek()) != null && sm.get(cs.peek()) == c) cs.pop();
          else cs.push(c);
      }        
      
      return cs.size() == 0;
  }
```

## Lesson I learnt

---

1. `peek()` of stack can be useful when validate the value before poping.
2. Store the pair of parentheses as key and value in map
