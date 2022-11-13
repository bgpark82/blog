---
title: Runnable-Callable
catalog: true
date: 2022-11-13 15:56:57
subtitle:
header-img:
tags:
---
## Runnable

Runnable은 **멀티 스레드의 작업**을 뜻한다

Runnable은 **Thread**와 **ThreadPool**에서 사용가능하다

Runnalbe은 자바 1.0부터 소개되었으며 `java.lang` 패키지에 존재한다

![image-20220422220225279](https://raw.githubusercontent.com/bgpark82/image/master/images/image-20220422220225279.png)

Runnable은 `run()` 메소드 하나만 가진 Functional Interface이다. 파라미터나 반환값을 가지지 않는다

Runnable을 상속받아 run() 메소드를 오버라이딩하여 사용하거나 최근에는 람다 표현식으로도 사용가능하다

```java
@Test
void run() {
  Thread thread1 = new Thread(() -> System.out.println("hello"));
  Thread thread2 = new Thread(new Runnable() {
    @Override
    public void run() {
      System.out.println("hello");
    }
  });
  thread1.start();
  thread2.start();
}
```

Runnable은 메소드에 **`throws` 키워드가 존재하지 않아 예외 처리를 할 수 없다** 는 단점이 있다



## Callable

Callable은 **멀티 스레드의 작업**을 뜻한다

Callable은 **ThreadPool**에서 사용가능하다

Callable은 자바 1.5에서 처음 소개 되었으며 `java.util.concurrent` 패키지에 존재한다

![image-20220422221444858](https://raw.githubusercontent.com/bgpark82/image/master/images/image-20220422221444858.png)

Callable은 `run()` 메소드 하나만 가진 Functional Interface이다. 

Callable과의 차이점은 **Generic 타입의 Future를 반환한다**

```java
class Task implements Callable<String> {

  private String message;

  public Task(String message) {
    this.message = message;
  }

  @Override
  public String call() throws Exception {
    if (!message.equals("hello")) {
      throw new RuntimeException(message + " is invalid");
    }
    return message;
  }
}
```

```java
@Test
void callable() throws ExecutionException, InterruptedException {
  ExecutorService executor = Executors.newSingleThreadExecutor();
  Future<String> future = executor.submit(new Task("hello"));
  assertThat(future.get()).isEqualTo("hello");
}
```

![image-20220422222028015](https://raw.githubusercontent.com/bgpark82/image/master/images/image-20220422222028015.png)



Callable은 메소드에 **`throws Exception`이 존재하므로 예외처리를 할 수 있다**

```java
@Test
void exception() {
  ExecutorService executor = Executors.newSingleThreadExecutor();
  Future<String> future = executor.submit(new Task("world"));
  assertThatThrownBy(() -> future.get()).isInstanceOf(ExecutionException.class);
}
```

Callable은 `call()` 메소드 실행도중 에러가 발생하면 `ExecutionException`을 발생시킨다



## Future

Future는 **비동기 작업의 결과**를 나타낸다

비동기 작업이 완료되면 Future 객체를 통해 결과에 접근할 수 있다

Future는 ExecutorService에 Callable을 submit함으로서 얻을 수 있다

Future 인터페이스는 아래와 같은 메소드들을 제공한다

```java
public interface Future<V> {

    boolean cancel(boolean mayInterruptIfRunning);

    boolean isCancelled();

    boolean isDone();

    V get();

    V get(long timeout, TimeUnit unit);
}


```

### 1. `get()`

`get()` 메소드는 **비동기 작업의 결과를 반환**한다

만약 비동기 작업이 끝나지 않았다**면 작업을 마칠 때까지 block 상태가** **된다**

### 2. `get(int timeout, TimeUnit unit)`

```java
@Test
void future() {
  ExecutorService executor = Executors.newSingleThreadExecutor();
  Future<String> future = executor.submit(new Callable<String>() {
    @Override
    public String call() throws Exception {
      Thread.sleep(1500);
      return "hello";
    }
  });
  assertThatThrownBy(() -> future.get(1, TimeUnit.SECONDS)).isInstanceOf(TimeoutException.class);
}
```

`get()` 메소드의 파라미터로 timeout과 단위를 넘기면 **해당 시간동안 작업이 마치지 않으면 `TimeoutException`을 발생**시킨다

### 3. `cancel()`

`cancel()` 메소드는 비동기 작업을 취소시킨다

하지만 이미 완료된 비동기 작업은 취소할 수 없다

### 4. `isDone()`, `isCancelled()`

비동기 작업이 완료되었는지 취소되었는지 확인하는 메소드이다



## 정리

| Runnable           | Callable                               |
| ------------------ | -------------------------------------- |
| Java 1.0           | java 1.5                               |
| java.lang          | java.util.concurrent                   |
| Thread, ThreadPool | ThreadPool                             |
| 반환값 미존재      | Generic 타입의 작업 완료된 반환값 존재 |
| 예외 처리 불가     | 예외 처리 가능                         |




## 참고

https://jenkov.com/tutorials/java-util-concurrent/java-future.html#:~:text=Future%20%2C%20represents%20the%20result%20of,result%20of%20the%20asynchronous%20task.
