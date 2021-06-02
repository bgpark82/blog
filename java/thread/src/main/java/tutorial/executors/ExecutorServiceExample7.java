package tutorial.executors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

public class ExecutorServiceExample7 {

    public static void main(String[] args) {

        ExecutorService executorService = Executors.newFixedThreadPool(3);

        List<Callable<String>> callables = new ArrayList<>();
        callables.add(newCallable("Task 1.1"));
        callables.add(newCallable("Task 1.2"));
        callables.add(newCallable("Task 1.3"));

        try {
            // Thread 중 execute이 가장 빨리된 하나를 반, 나머지는 취소된다
            List<Future<String>> results = executorService.invokeAll((Collection<Callable<String>>) callables);
            for (Future<String> result : results) {
                System.out.println(result.get());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }catch (ExecutionException e) {
            e.printStackTrace();
        }

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
