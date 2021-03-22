package tutorial.executors;

import java.util.concurrent.*;

public class ExecutorServiceExample2 {

    public static void main(String[] args) {

        int corePoolSize = 10;  // 기본 스레드 개수
        int maxPoolSize = 20;   // 만약 스레드가 10개보다 많아지면 20개의 스레드까지 생성할 수 있다
        long keepAliveTime = 3000; // 추가로 생성된 10개의 스레드가 살아있는 시간

        // 1. Task를 대한 빨리 반환한다
        ExecutorService threadPoolExecutor = new ThreadPoolExecutor(
                corePoolSize,
                maxPoolSize,
                keepAliveTime,
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue(128));  // Task가 ThreadPool에 들어가기 전 저장해 있는 Queue

        ExecutorService executorService =
                (ThreadPoolExecutor) Executors.newFixedThreadPool(3);  // core, max 사이즈가 3개로 고정

        // 2. Task의 실행 시점을 스케쥴링 할 수 있다..
        ExecutorService scheduleExecutorService = new ScheduledThreadPoolExecutor(corePoolSize);

        executorService = Executors.newFixedThreadPool(10);
    }
}
