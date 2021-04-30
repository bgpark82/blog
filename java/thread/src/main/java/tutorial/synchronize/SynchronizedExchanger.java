package tutorial.synchronize;

public class SynchronizedExchanger {

    protected static Object object = null;

    public static  synchronized void setObject(Object o) {
        object = o;
    }

    public  static  synchronized Object getObject() {
        return object;
    }

    public  static  void setObj(Object o) {
        synchronized (StaticSynchronizedExchanger.class) {
            object = o;
        }
    }

    public  static  Object getObj() {
        synchronized (StaticSynchronizedExchanger.class) {
            return object;
        }
    }
}
