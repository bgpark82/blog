package benchmarking;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        NumberChecker nc = new NumberChecker();

        System.out.println("start warm up until native compile finished");
        for(int i = 1; i < 10000; i++) {
            nc.isPrime(i);
        }

        Thread.sleep(2000);

        System.out.println("war mup finished, now measuring");

        long start = System.currentTimeMillis();

        for (int i = 1; i < 50000; i++) {
            nc.isPrime2(i);
        }


        long end = System.currentTimeMillis();
        System.out.println("Time taken " + (end - start) + "ms") ;
    }
}
