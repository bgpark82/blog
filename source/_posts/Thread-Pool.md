---
title: Thread Pool
catalog: true
date: 2022-11-13 15:25:26
subtitle:
header-img:
tags:
---

## Thread의 문제

Thread는 작업을 병렬적으로 처리하도록 돕는다. 하지만 병렬작업이 많이 처리되면 스레드의 개수가 증가하게 된다. **스레드 생성과 스케쥴링은 CPU와 메모리 리소스를 많이 사용하게 된다**. 또한 생성되는 스레드의 라이프사이클을 관리하기 쉽지 않게된다. 결국, 어플리케이션 성능이나 관리 측면에서 개별적으로 Thread를 생성하는 것은 좋지 않다.

> 특히 WAS 서버는 병렬처리를 위해 요청마다 Thread를 생성하게 된다. 트래픽이 많은 서비스는 매우 많은 요청이 들어어고 그만큼 Thread가 필요하다.



## ThreadPool

Thread Pool은 일정 개수의 미리 생성된 스레드를 어플리케이션 시작 시점에 저장하는 곳이다. 작업을 실행하면 Thread Pool에서 미리 생성된 스레드를 사용하고 작업이 끝나면 해당 스레드를 다시 Thread Pool로 반납한다

![image-20220419012004916](https://raw.githubusercontent.com/bgpark82/image/master/images/image-20220419012004916.png)

ThreadPool은 병렬처리에 사용되는 **Thread를 제한된 개수만큼 미리 생성**한다. Queue에 들어오는 작업을 미리 생성된 Thread가 처리한다. 작업이 끝난 Thread는 결과를 어플리케이션에 반환하고, Thread는 Queue로 들어와 재활용된다

Thread Pool의 장점 다음과 같다

1. 스레드의 개수를 한정할 수 있다
2. 스레드의 라이프사이클을 관리할 수 있다

Thread Pool의 단점은 다음과 같다

1. Thread Pool에 **필요 이상의 스레드를 생성한다면 메모리 낭비**가 발생할 수 있다. 그래서 스레드 사용량을 계산하여 적절한 스레드를 산정하는 것이 좋다

>  작업이란 Runnable이나 Callable 인터페이스를 상속받은 클래스를 말한다

> ThreadPool은 **데몬 스레드가 아니다**. 즉, main 스레드가 종료되어도 ThreadPool의 Thread는 작업을 처리해야 하므로 어플리케이션을 종료하지 않는다



## Java에서 ThreadPool

## Excutors

ThreadPool은 **Executors라는 Java 1.5 버전에 추가된  `java.concurrent` 패키지의 클래스를 사용하여 생성**한다. Executors는 **정적 메소드**인  `newCachedThreadPool`과 `newFixedThreadPool`를 제공한다. 해당 메소드들은 **ExecutorService의 구현체**를 반환한다. (개인적으로 ExecutorService와 ThreadPool이라는 개념이 연결되지 않았는데 구현체의 이름을 보면 좀 더 쉽게 이해할 수 있다)

![image-20220419013202061](https://raw.githubusercontent.com/bgpark82/image/master/images/image-20220419013202061.png)

`execute`

- **작업 중에 예외가 발생하면 해당 스레드가 종료**되고 Pool에서 제거된 뒤, 새로운 스레드를 생성하여 다른 작업을 처리한다
- 처리결과를 반환하지는 않는다

`submit`

- 작업 처리 중에 예외가 발생하더라도 스레드가 종료되지 않고 다음 작업에 사용된다
- 처리 결과를 `Future`로 반환한다



### 3-1. newCachedThreadPool

![image-20220419014414284](https://raw.githubusercontent.com/bgpark82/image/master/images/image-20220419014414284.png)

```java
ExecutorService executorService = Executors.newCachedThreadPool();
```

CachedThreadPool은  **필요할 때마다 스레드를 생성**하는 Thread Pool이다. 하지만 **미리 생성된 스레드들은 사용가능하면 재사용**된다. 스레드가 **60초 이상 사용되지 않으면(idle) 종료되는 동시에 캐시에서 제거**된다. 그래서 Pool에서 idle되는 스레드들을 제거하여 리소스를 아낄 수 있다. 그래서 해당 Thread Pool은 **짧은 비동기 작업이 많을 경우 성능 향상**을 보인다. (Cached라는 이름이 조금 헷깔렸는데 아마 이미 생성된 Thread에 대해서는 ThreadPool 안에서 Cache 된다는 의미로 보인다)

`corePoolSize`

- 초기 생성할 스레드 개수
- CachedThreadPool에서는 **처음에는 스레드가 존재하지 않는다**. **필요할 때마다 스레드를 생성한 뒤 재사용**한다. **스레드가 60초 동안 idle이 발생하면 Pool에서 제거**하는 방식으로 사용된다

`maximumPoolSize`

- Thread Pool에 생성가능한 최대 스레드 개수
- CachedThreadPool에서는 Integer.MAX_VALUE만큼 스레드 생성이 가능하다 (물론 OS 상황마다 다르다)

`keepAliveTime`

- 스레드 유지 기간
- CachedThreadPool에서는 스레드가 60초 동안 idle되면 terminate되는 동시에 Pool에서 제거된다.

> SynchronousQueue 알아보기


### 3-2. newFixedThreadPool

![image-20220419014909183](https://raw.githubusercontent.com/bgpark82/image/master/images/image-20220419014909183.png)

```java
ExecutorService executorService = Executors.newFixedThreadPool(nThread);
```

FixedThreadPool은 **초기 생성된 스레드 개수를 유지**하는 Thread Pool이다. Fixed라는 이름처럼 **주어진 Thread 개수만큼 ThreadPool에 항상 남아있고 그 이상 Thread가 필요해도 더 이상 생성하지 않고 재활용해서 사용한다**. 그래서 **모든 스레드가 작업 중일 때 추가 작업이 요청되면 작업이 끝난 스레드가 나올때 까지 Queue에서 기다리게 된다**. 만약 쓰레드가 어떤 이유에서 terminate된다면 새로운 스레드가 추가된다. 존재하는 모든 스레드는 shutdown 할 때까지 Pool에 개수가 유지된다.

`corePoolSize`

- 초기 생성할 스레드 개수
- FixedThreadPool에서는 **초기 스레드 개수가 고정된다**

`maximumPoolSize`

- Thread Pool에 생성가능한 최대 스레드 개수
- FixedThreadPool에서는 **최대 스레드 개수 또한 고정된다**. 그래서 고정된 개수 이상의 작업이 요청된다면 작업을 처리할 수 있는 스레드가 나올때까지 Queue에서 대기하게 된다.

`keepAliveTime`

- 스레드 유지 기간
- FixedThreadPool에서는 고정된 스레드가 존재하므로 **스레드를 제거할 필요가 없다**

### 3-3. newScheduledThreadPool

```java
ExecutorService executorService = Executors.newFixedThreadPool(nThread);
```

newScheduledThreadPool는 일정 시간이 흐루고 난 뒤 실행하는 스케줄링 스레드이다. 

> `Executors.newScheduledThreadPool(0)` 을 하더라도 실행에는 문제가 없어 보인다. 다만 JDK 8 버전 이하에서 발견된 [버그](https://bugs.openjdk.java.net/browse/JDK-8129861)로 단일 코어 가상 머신에서 CPU 를 100% 사용하는 버그가 있기 때문에 파라미터로 1 이상으로 설정한다.




### newCachedThreadPool vs newFixedThreadPool

두 메소드를 정리해서 비교하면 다음과 같다

| 메소드명                        | 초기 스레드 수 | 코어 스레드 수 | 최대 스레드 수    |
| ------------------------------- | -------------- | -------------- | ----------------- |
| newCachedThreadPool()           | 0              | 0              | Integer.MAX_VALUE |
| newFixedThreadPool(int nThread) | 0              | nThread        | nThread           |

(코어 스레드는 **ThreadPool을 유지하는 최소한의 Thread 개수**를 뜻한다). newCachedThreadPool은 **필요할 때마다 Thread를 생성**하고, newFixedThreadPool은 **초기 설정 개수만큼만 Thread를 생성**한다
