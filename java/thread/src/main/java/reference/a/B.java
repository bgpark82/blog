package reference.a;

public class B {

    public static void main(String[] args) {
        B b = new B();
        b.c();
    }

    private void c() {
        try {
            while(true) {
                Thread.sleep(1000);
                d();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void d() {
        try {
            Thread.sleep(1000);
            Z z = new Z();
            z.calculate(System.currentTimeMillis());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
