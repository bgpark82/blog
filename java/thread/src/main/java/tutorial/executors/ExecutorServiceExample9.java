package tutorial.executors;

import java.util.List;
import java.util.concurrent.*;

public class ExecutorServiceExample9 {

    public static void main(String[] args) {

        ExecutorService executorService = Executors.newFixedThreadPool(1);

        Future future = executorService.submit(newCallable("Task 1.1"));

        // 보통 지금시점에서는 false
        System.out.println(future.isDone());

        boolean mayInterrupt = true;
        // 진행중인 테스크를 취소, 취소 유무를 반환
        boolean wasCancelled = future.cancel(mayInterrupt);
        System.out.println(wasCancelled);

        try {
            // 취소되었다면 결과를 얻지 못한다
            String result = (String) future.get();
            System.out.println(result);
        } catch (InterruptedException e) {
        } catch (ExecutionException e) {
        } catch (CancellationException e) {
            // 취소되었다면 결과를 얻지못하고 CancellationException을 발생시킨
            System.out.println("Can not call Future.get() since task was cancelled");
        }

        System.out.println(future.isDone());
        System.out.println(future.isCancelled());

        executorService.shutdown();

    }

    private static Callable newCallable(String msg) {
        return new Callable() {

            @Override
            public Object call() throws Exception {
                String completeMsg = Thread.currentThread().getName() + " : " + msg;
                return completeMsg;
            }
        };
    }
}
