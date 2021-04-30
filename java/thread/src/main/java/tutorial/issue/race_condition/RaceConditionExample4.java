package tutorial.issue.race_condition;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RaceConditionExample4 {

    public static void main(String[] args) {
        Map<String, String> sharedMap = new ConcurrentHashMap<>();

        Thread thread1 = new Thread(() -> getSharedMap(sharedMap));
        Thread thread2 = new Thread(() -> getSharedMap(sharedMap));

        thread1.start();
        thread2.start();
    }

    private static void getSharedMap(Map<String, String> sharedMap) {
        for(int i = 0; i < 1000000; i++) {
            synchronized (sharedMap) {
                // 메모리에 sharedMap이 key를 가지고 동시에 두개의 CPU 스레드가 key를 지우면
                if (sharedMap.containsKey("key")) {
                    String val = sharedMap.remove("key");
                    // 나중에 지워진 스레드는 null을 가지게 된다
                    if (val == null) {
                        System.out.println("Iteration " + i + ": Value for 'key' was null");
                    }
                } else {
                    sharedMap.put("key", "value");
                }

            }
        }
    }
}
