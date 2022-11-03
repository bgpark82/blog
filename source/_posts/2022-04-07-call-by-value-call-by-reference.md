---
title: call by value와 call by reference 차이점
catalog: true
date: 2022-11-02 19:08:54
subtitle:
header-img:
tags:
---

## call by value와 call by reference는 무엇인가

call by value와 call by reference는 **함수의 인자를 전달하는 방식**에 따라 나눠진다

## call by value와 call by reference의 차이점

call by value는 **값을 전달하는 방식**이다

함수의 인자로 **복사된 값을 전달**하여 값을 수정해도 **원본 데이터에 영향을 주지 않는 방식**이다

call by reference는 **주소를 전달하는 방식**이다

함수의 인자로 **복사된 주소값을 전달**하여 **실제 객체에 접근**하게된다

```java
class Person {
    
    private String name;
    
    public Person(String name) {
        this.name = name;
    }
    
    public String getName() {
      return name;
    }

    public void setName() {
      this.name = name;
    }
}

```
```java
public class CallByValueTest {

    @Test
    void callByReference() {
        int x = 1;
        int[] y = {1, 2, 3};
        Person person1 = new Person("개발자");
        Person person2 = new Person("기획자");

        process(x, y, person1, person2);

        assertThat(x).isEqualTo(1);
        assertThat(y[0]).isEqualTo(2);
        assertThat(person1.getName()).isEqualTo("개발자");
        assertThat(person2.getName()).isEqualTo("새로운 기획자");
    }

    private void process(int x, int[] y, Person person1, Person person2) {
        x++;
        y[0]++;
        person1 = new Person("새로운 개발자");
        person2.setName("새로운 기획자");
    }
}
```



### 0. 테스트 시작

테스트 시작 시점에는 Stack 영역에 x, y, person1, person2가 쌓이게 된다

이때, x는 원시값으로 Stack에 값 1을 저장하고

y는 Heap 영역에 생성된 int 배열의 주소를 저장한다

person1과 person2는 각각 Heap 영역에 생성된 String 객체를 가르킨다

![image-20220407235150969](https://raw.githubusercontent.com/bgpark82/image/master/images/image-20220407235150969.png)

### 1. process 함수에 전달된 x

process 함수가 실행되면 Stack 영역에 새로운 공간을 생성한다

이때, 전달받은 x 값의 복사본을 Stack에 저장한다

그리고 x++ 연산에 의해 전달받은 값 1은 2가 되게 된다

하지만 복사된 값이므로 process가 종료되면 Stack 영역에서 사라지며 원본값에 변경을 주지 않는다

![image-20220407235605229](https://raw.githubusercontent.com/bgpark82/image/master/images/image-20220407235605229.png)

### 2. process 함수에 전달된 y

y는 Heap 영역의 int 배열의 주소의 복사본을 Stack 영역에 저장한다

주소의 복사본을 통해 Heap 영역의 int 배열에 접근이 가능하다

y[0]++ 연산을 통해 Heap 영역의 첫번째 인덱스의 값을 2로 증가시킨다

![image-20220407235851249](https://raw.githubusercontent.com/bgpark82/image/master/images/image-20220407235851249.png)

### 3. process 함수에 전달된 person1

![image-20220408001450586](https://raw.githubusercontent.com/bgpark82/image/master/images/image-20220408001450586.png)

![image-20220408001557420](https://raw.githubusercontent.com/bgpark82/image/master/images/image-20220408001557420.png)

![image-20220408001706383](https://raw.githubusercontent.com/bgpark82/image/master/images/image-20220408001706383.png)



## Call By Value

## Call By Reference



## 자바는 어느 방식을 사용할까?

이 중 자바는 ***call by value***를 사용한다

[Java Language Specification](https://docs.oracle.com/javase/specs/jls/se9/html/jls-4.html#jls-4.3)에서 명시해놓았다

![image-20220407232209070](https://raw.githubusercontent.com/bgpark82/image/master/images/image-20220407232209070.png)

## 왜 자바가 call by reference로 생각될까?

주소의 복사본을 통해 Heap에 접근하여 원본 객체를 변경 할 수 있기 때문에 call by reference라고 생각하게 되기 때문이다

call by reference는 원본 데이터를 변경한다

예를들어 C에서는 함수의 매개변수를 포인터로 선언하여 원본 데이터에 직접 접근이 가능하다



자바에서 객체를 전달하지 않고 **객체의 주소를 복사한 값을 전달**한다

전달받은 주소값으로 Heap 영역의 객체에 접근하여 수정하는 것이 가능하다

함수 내부에서 실제 객체 자체를 변경할 수는 없다



즉, 원래의 값은 변하지 않는다

기본 자료형은 무조건 call by value로 동작한다



call by reference : 호출한 데이터에 영향을 준다

정확하게는 값이 아니라 객체의 참조를 호출한다

참조 자료형은 call by reference로 동작한다



https://mangkyu.tistory.com/107

https://softwareengineering.stackexchange.com/questions/263945/where-is-it-specified-that-java-is-call-by-value
