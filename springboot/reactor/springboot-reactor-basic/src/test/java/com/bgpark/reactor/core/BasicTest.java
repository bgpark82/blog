package com.bgpark.reactor.core;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Processor;
import org.reactivestreams.Subscription;
import reactor.core.Disposable;
import reactor.core.Disposables;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@DisplayName("Flux와 Mono 관련 테스트")
public class BasicTest {

    @DisplayName("Flux와 Mono를 생성한다")
    @Test
    void create() {
        Flux<String> flux1 = Flux.just("foo", "bar", "foobar");
        Flux<String> flux2 = Flux.fromIterable(Arrays.asList("foo", "bar", "foobar"));
        Flux<Integer> flux3 = Flux.range(5, 3); // 5, 6, 7

        Mono<Object> mono1 = Mono.empty(); // empty여도 타입을 가진다
        Mono<String> mono2 = Mono.just("foo");
    }

    /**
     * lambda interface (consumer)를 사용하여 subscriber를 subscribe한다
     */
    @DisplayName("Subscriber를 subscribe한다")
    @Test
    void subscribe() {
        Disposable disposable = Flux.just("foo").subscribe();   // subscribe하고 reactive 흐름을 시작한다
        // disposable은 subscription을 참조하여 data가 더 이상 생성 안되도록 cancel 할 수 있다, 생성된 데이터를 clean up한다

        Flux.just("foo").subscribe(
                a -> System.out.println("consumer"));           // 각 생성된 데이터로 특정일을 시킨다

        Flux.just("foo").subscribe(
                a -> System.out.println("consumer"),            // consumer
                e -> new RuntimeException(e));                  // onError : exception

        Flux.just("foo").subscribe(
                a -> System.out.println("consumer"),            // consumer
                e -> new RuntimeException(e),                   // onError : exeception
                new Runnable() {                                // onComplete : 흐름이 잘 끝나면 특정일을 시킨다
                    @Override
                    public void run() {
                        System.out.println("complete consumer");
                    }
                }
        );
        // 3.5에서 삭제 -> subscribeWith(subscriber) 사용, 사람들이 request 호출하는 것을 까먹는다고 지웠다고 함ㅎㅎ
        Flux.just("foo").subscribe(
                a -> System.out.println("consumer"),            // consumer
                e -> new RuntimeException(e),                   // onError : exeception
                () -> System.out.println("complete consumer"),  // onComplete : 흐름이 잘 끝나면 특정일을 시킨다
                s -> System.out.println("subscription consumer")// subscribe 시점에서 subscription에게 특정 콜
        );
    }

    @DisplayName("subscribe 예제")
    @Test
    void subscribeExamples() {
        Flux<Integer> flux1 = Flux.range(1, 3);
        flux1.subscribe();

        Flux<Integer> flux2 = Flux.range(1, 3);
        flux2.subscribe(i -> System.out.println(i));

        Flux<Integer> flux3 = Flux.range(1, 4)
                .map(i -> {
                    if (i <= 3) {
                        return i;
                    }
                    throw new RuntimeException("Got to 4");
                });
        flux3.subscribe(
                i -> System.out.println(i),
                error -> System.out.println("Error: " + error));

        Flux<Integer> flux4 = Flux.range(1, 4);
        flux4.subscribe(
                i -> System.out.println(i),
                error -> System.out.println("Error: " + error), // onError : 에러로 흐름 종료
                () -> System.out.println("Done"));              // onComplete : 성공적으로 흐름 종료 == Runabble이다 (Supplier)

        Flux<Integer> flux5 = Flux.range(1, 15);
        flux5.subscribe(
                i -> System.out.println(i),
                error -> System.out.println("Error: " + error), // onError : 에러로 흐름 종료
                () -> System.out.println("Done"),               // onComplete : 성공적으로 흐름 종료 == Runabble이다 (Supplier)
                subscription -> subscription.request(10));   // subscription에게 10개의 데이터를 요청한다
    }

    @DisplayName("Dispoable로 요청을 cancel한다")
    @Test
    void cancel() {
        Flux<Integer> flux1 = Flux.range(1, 3);
        Disposable disposable = flux1.subscribe();
        disposable.dispose(); // subscription에게 요청 취소, Publisher에게 더 이상 데이터를 생성하지 말라고 요청한다, 하지만 취소가 바로될지는 모른다, cancel 시그널이 발생하더라도 빠르게 생성된 데이터는 cancel 완료 시그널을 받기 전에 생성완료가 될 수 있다.

        Disposable.Swap swap = Disposables.swap();
        swap.replace(disposable);   // 개별적으로 cancel할 때 사용

        Disposable.Composite composite = Disposables.composite(disposable);
        composite.dispose();    // composite에 포함된 모든 disposable이 바로 취소된다
    }

    @DisplayName("lambda 대용으로 BaseSubscriber 사용")
    @Test
    void baseSubscriber() {
        Flux.range(1, 4)
                .subscribe(new BaseSubscriber<Integer>() {
                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        System.out.println("Subscribed");
                        request(1);
                    }

                    @Override
                    protected void hookOnNext(Integer value) {
                        System.out.println(value);
                        request(1);
                    }
                });
    }

    @DisplayName("Backpressure 테스트")
    @Test
    void backpressure() {
        Flux.range(1, 10)
                .doOnRequest(r -> System.out.println("request of " + r))
                .subscribe(new BaseSubscriber<Integer>() {
                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        request(1);
                    }

                    @Override
                    protected void hookOnNext(Integer value) {
                        System.out.println("Cancelling after having received " + value);
                        cancel();
                    }
                });
    }

    @DisplayName("downstream에서 demand를 변경하는 operators")
    @Test
    void operators() {
        Flux.range(1, 10)
                .buffer(2)
                .doOnSubscribe(s -> s.request(2))
                .subscribe(s -> System.out.println(s));
    }

    @DisplayName("generate로 동기적으로 Flux를 생성한다")
    @Test
    void generate() {
        Flux<String> flux = Flux.generate(
                () -> 0,                                                    // 초기 state = 0
                (state, sink) -> {
                    sink.next("3 x " + state + " = " + 3 * state);       // 다음으로 무엇을 emit할 지 결정
                    if (state == 10) sink.complete();                       // 언제 멈출지 결정 (종료)
                    return state + 1;                                       // 다음 호출에 사용할 새로운 state를 반환
                });

        flux.subscribe(System.out::println);
    }

    @DisplayName("generate로 동기적으로 Flux를 생성한다")
    @Test
    void generate2() {
        Flux<String> flux = Flux.generate(
                AtomicLong::new,                                                    // 초기 state = 0
                (state, sink) -> {
                    long i = state.getAndIncrement();
                    sink.next("3 x " + i + " = " + 3 * i);       // 다음으로 무엇을 emit할 지 결정
                    if (i == 10) sink.complete();                       // 언제 멈출지 결정 (종료)
                    return state;                                       // 다음 호출에 사용할 새로운 state를 반환
                });

        flux.subscribe(System.out::println);
    }

    @DisplayName("generate로 동기적으로 Flux를 Consumer와 함께 생성한다")
    @Test
    void generate3() {
        Flux<String> flux = Flux.generate(
                AtomicLong::new,                                                    // 초기 state = 0
                (state, sink) -> {
                    long i = state.getAndIncrement();
                    sink.next("3 x " + i + " = " + 3 * i);       // 다음으로 무엇을 emit할 지 결정
                    if (i == 10) sink.complete();                       // 언제 멈출지 결정 (종료)
                    return state;                                       // 다음 호출에 사용할 새로운 state를 반환
                }, (state) -> System.out.println("state" + state));
    }

    @DisplayName("create로 비동기, 멀티 스레드 방식으로 Flux를 생성한다")
    @Test
    void create1() {

    }

    @DisplayName("push로 비동기, 싱글 스레드 방식으로 Flux를 생성한다")
    @Test
    void push() {
        Flux<String> flux = Flux.push(sink -> {

            new SingleThreadEventListener<String>() {
                @Override
                public void onDataChunk(List<String> chunk) {       // event가 싱글 listenr 스레드로 부터 next 메소드를 통해 sink로 push
                    for (String s : chunk) {
                        sink.next(s);
                    }
                }

                @Override
                public void processComplete() {                     // 같은 listener 스레드를 통해 complete 이벤트 생성
                    sink.complete();
                }

                @Override
                public void processError(Throwable e) {             // 같은 listener 스레드를 통해 error 이벤트 생성
                    sink.error(e);
                }
            };
        });

        flux.subscribe(System.out::println);
    }

    interface SingleThreadEventListener<T> {
        public void onDataChunk(List<T> chunk) ;
        public void processComplete() ;
        public void processError(Throwable e) ;
    }

    @Test
    void newThread() throws InterruptedException {

        final Mono<String> mono = Mono.just("hello");  // main 메소드에서 Mono가 생성되었다.

        Thread thread = new Thread(() -> mono
                .map(msg -> msg + " thread ")
                .subscribe(v -> System.out.println(v + Thread.currentThread().getName()))); // 하지만 Thread-0에 mono가 subscribe되었다.

        thread.start();
        thread.join();
    }
}
