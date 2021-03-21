package book.threaddump;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MakeThreads {

    public static void main(String[] args) {

        for (int loop = 0; loop < 3; loop++) {
            LoopingThread thread = new LoopingThread();
            thread.start();
        }
        System.out.println("Started looping threads... you must stop this process after test");
    }

    static class LoopingThread extends Thread {
        @Override
        public void run() {
            int runCount = 100;
            while(true) {
                String string = new String("AAA");
                List<String> list = new ArrayList<>(runCount);
                for (int loop = 0; loop < runCount; loop++) {
                    list.add(string);
                }
                HashMap<String, Integer> hashMap = new HashMap<>(runCount);
                for (int loop = 0; loop < runCount; loop++) {
                    hashMap.put(string + loop, loop);
                }
            }
        }
    }
}
