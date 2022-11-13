---
title: Volatile-Synchronized-Atomic
catalog: true
date: 2022-11-13 15:55:19
subtitle:
header-img:
tags:
---
## volatile

Volatile은 **모든 Thread가 CPU의 Cache 영역의 데이터가 아닌 메인 메모리에서 최신 데이터를 참조**하도록 한다. 즉, 동일한 시점에서 모든 스레드가 동일한 값을 참조하도록 동기화 한다 (메모리 동기화 라고도 부른다). **Locking 메커니즘은 사용되지 않아 모든 Thread가 volatile 변수에 접근 가능**하다 

volatile은 Thread의 **메모리 가시성 문제를 해결**할 수 있다

![image-20220424140812644](https://raw.githubusercontent.com/bgpark82/image/master/images/image-20220424140812644.png)

각 Thread는 각각의 CPU에서 연산을 하고 CPU의 Cache 영역에 변수를 저장한다. Cache의 데이터를 메인 메모리에 반영하여 데이터를 동기화 처리를 한다. 하지만 메인 메모리에 반영되지 않으면 다른 Thread는 예전 데이터를 메인 메모리에서 참조하여 사용하게 된다. 메모리 가시성은 **Thread 쓰기 작업이 메인 메모리에 반영되지 않아 다른 Thread가 상태변경을 볼 수 없는 상황**을 말한다. volatile은 메인 메모리에 쓰기 작업을 바로하여 **CPU의 Cache 영역이 아닌 메인 메모리에 바로 상태 변경**을 한다

> CPU Cache를 바로 flush하는 방식인가?

>  또한 volatile은 **compiler나 JVM이 코드의 재정렬을 막는다**

```java
private static volatile boolean stop = false; // volatile variable로 선언

@Test
void test2() throws InterruptedException {
    ExecutorService executorService = Executors.newFixedThreadPool(10);
    for (int i = 0; i < 10; i++) {
      executorService.submit(() -> {
        while (!stop) {    }
      });
    }
    Thread.sleep(1000);
    stop = true;
}
```

volatile은 **하나의 Thread가 쓰기 연산을 하고 다른 하나의 Thread가 읽기 연산을 할 때만 가능**하다. boolean 타입의 stop이라는 변수를 `volatile`로 선언하고 메인 메모리에 false가 저장된다. stop이라는 공유 자원을 10개의 작업을 통해 stop이 true가 될때까지 실행하는 코드이다. 오직 main Thread만이 stop을 true로 변경하면 메인 메모리의 true가 반영되고 모든 Thread들이 메인 메모리를 참조하여 while 문을 빠져나오게 된다

```java
private static volatile boolean stop = false;
private static volatile int count = 50;

@Test
void test3() throws InterruptedException {
    ExecutorService executorService = Executors.newFixedThreadPool(50);
    for (int i = 0; i < 50; i++) {
      executorService.submit(() -> {
        while (!stop) {}
        System.out.println("count=" + --count);
      });
    }
    Thread.sleep(1000);
    stop = true;
    Thread.sleep(1000);
    assertThat(count).isEqualTo(0);
}
```

이번에는 while문을 빠져나오는 순간 count라는 공유 변수를 각 Thread들이 내부에서 변경하는 코드이다. 즉, count는 여러 Thread가 동시에 값을 변경 시키려 할 것이다. 50개의 Thread가 각각 50의 값을 1씩 감소시키면 결과는 0으로 기대가 된다

![image-20220425001651162](https://raw.githubusercontent.com/bgpark82/image/master/images/image-20220425001651162.png)

하지만 **1을 반환한다**. **대부분의 경우는 0이 반환되지만 Multi Thread가 쓰기 작업을 하면 Race Condition으로 다른 값을 반환할 수도 있다**. 이때는  `volatile` 이외에 다른 처리를 해야한다



## synchronized (Blocking, Critical Section)

Race Condition을 제거하기 위해 `synchronized` 블록을 사용할 수 있다. synchronized는 블록 혹은 메소드 레벨에서 하나의 Thread만 Critical Section에서 Lock을 획득하도록 보장한다. 오직 **Lock을 획득한 Thread만 Critical Section에 들어갈 수 있고 다른 Thread는 대기**하여야 한다. 이처럼 synchronized는 **Lock을 이용한 Blocking 방식**으로 사용한다

![image-20220424222524965](https://raw.githubusercontent.com/bgpark82/image/master/images/image-20220424222524965.png)

하지만, Blocking Thread는 매우 비싸다. 하나의 Thread가 Lock을 획득하는 순간 다른 모든 Thread들이 기다려야 한다. Thread가 작업을 끝내고 Lock을 반환해도 다른 Thread들이 바로 자원을 획득할 수 없다. 왜냐하면 **Thread의 상태를 변경하는 것은 OS의 스케쥴러가 결정**하기 때문이다



## Atomic (Non Blocking)

Atomic은 **더이상 쪼개질 수 없는 성질**을 뜻한다. Atomic 연산은 **더 이상 쪼개질 수 없는 연산**을 뜻한다. **멀티 스레드에서 Atomic 하지 않은 연산은 동시성 문제**를 일으킨다

![image-20220425230938506](https://raw.githubusercontent.com/bgpark82/image/master/images/image-20220425230938506.png)

예를들어 단항 연산 `i++`는 보기에는 1을 증가시키는 역할만하는 Atomic 연산이지만 기계어로 번역되었을 때는 그렇지 않다. `i`를 메인 메모리로 부터 읽어 CPU Cache에 옮기고 Cache에서 1을 더한 뒤 Cache의 값을 메모리에 반영한다. 이때, Race Condition이 발생되면 Cache에서 메인 메모리로 반영되지 않은 값을 다른 스레드들이 참조하게 된다. 즉, **자바코드가 기계어로 번역되면서 Atomic하지 않은 코드가 생성되어 스레드가 동일한 값을 참조하지 못하게 된다** 



## CAS 알고리즘

멀티 스레드 환경에서 Race Condition을 피하기 위해서는 Atomic한 방법을 사용할 수 있다. Atomic의 핵심은 **CAS(Compare and Swap) 알고리즘**이다. CAS는 **기대값과 실제값이 일치할 때까지 Compare과정을 반복하고 일치하면 Swap하는 알고리즘**이다. volatile이 메모리의 가시성 문제를 해결하면서 모든 Thread들이 메인 메모리에서 최신의 데이터를 CAS로 일치할 수 있다. 그래서 Race Condition에서도 Thread가 Blocking 되어 OS의 스케쥴러를 사용하는게 아닌 **CPU 연산으로 Compare 과정을 통해 모든 Thread가 데이터를 체크**한다

```java
private volatile int value;

public final int incrementAndGet() {
	  return unsafe.getAndAddInt(this, valueOffset, 1) + 1;
}

public final int getAndAddInt(Object o, long offset, int delta) {
    int v;
    do {
      v = getIntVolatile(o, offset); 												// 1. 메인 메모리 값
    } while (!compareAndSwapInt(o, offset, v, v + delta));  // 2. 실패하면 계속 반복
    return v;
}
```

AtomicInteger의 `incrementAndGet()` 메소드를 보면 CAS 알고리즘을 사용하는 것을 확인할 수 있다. `getIntVolatile()` 은 메인 메모리의 값을 반환한다. 이후 `compareAndSwapInt()`로 메인 메모리의 값과 객체의 값을 비교하여 동일하면 Swap하고 다르면 같을 때까지 계속 loop를 돌게된다

예를들어 Thread A가 계산을 하고 메인 메모리에 반영하기 직전에 Thread B가 계산을 하여 메인 메모리에 반영한 경우이다. 이때, Thread A가 변경값을 메모리에 반영하면 Thread B의 기대값이 달라져 버린다. 그래서 false를 반환하면 무한 루프에서 다른 Thread에 의해 변경된 메모리 값을 읽고 같은 시도를 반복한다

> 단 하나의 스레드만 변경을 어떻게 하는거지?

```java
private static int count = 0;
private static AtomicInteger atomicCount = new AtomicInteger(0);

@Test
void integer() throws InterruptedException {
  ExecutorService executorService = Executors.newFixedThreadPool(10);
  for (int i = 0; i < 10; i++) {
    executorService.submit(() -> {
      for (int j = 0; j < 1000; j++) {
        count++;
        atomicCount.incrementAndGet();
      }
    });
  }
  Thread.sleep(1000);
  System.out.println("atomic 결과 : " + atomicCount.get());
  System.out.println("int 결과 : " + count);
}
```

![image-20220424233022391](https://raw.githubusercontent.com/bgpark82/image/master/images/image-20220424233022391.png)

![image-20220424222907008](https://raw.githubusercontent.com/bgpark82/image/master/images/image-20220424222907008.png)

CAS는 **Lock 기반의 멀티 스레딩 방식보다 훨씬 빠르다**. `synchronized`는 하나의 스레드가 Lock을 획득하여 연산하는 동안 다른 스레드들은 대기해야 한다. 또한 연산을 마친 스레드가 Lock을 해제하더라도 **OS의 스케줄러에 의해 스레드들이 Lock을 획득하기 때문에 언제 획득할지 모른다**. 

반면 CAS는 **모든 스레드가 메인 메모리의 데이터와 기대값이 일치할 때까지 loop를 돌게 된다**. 기대값과 일치하면 모든 스레드가 한번에 데이터를 변경할 수 있어 **Lock 기반보다 더 빠르다**.



## 참조

https://stackoverflow.com/questions/3519664/difference-between-volatile-and-synchronized-in-java#:~:text=Marking%20a%20variable%20as%20volatile,when%20they%20exit%20the%20block.

https://stackoverflow.com/questions/9749746/what-is-the-difference-between-atomic-volatile-synchronized

https://javarevisited.blogspot.com/2011/06/volatile-keyword-java-example-tutorial.html#axzz7RLhJEZ00

https://jronin.tistory.com/110

https://steady-coding.tistory.com/568

http://www.cs.umd.edu/~pugh/java/memoryModel/jsr-133-faq.html

http://www.dre.vanderbilt.edu/~schmidt/cs891s/2020-PDFs/3.4.3-atomic-operations-and-classes-pt3-implementing-java-atomiclong-atomicboolean.pdf

https://readystory.tistory.com/53

https://docs.oracle.com/javase/tutorial/essential/concurrency/atomicvars.html

https://junhyunny.github.io/information/java/java-atomic/
