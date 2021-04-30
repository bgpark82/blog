package tutorial.synchronize;

public class MixedSynchronization {

    // static
    public static Object staticObj = null;

    public static synchronized void setStaticObj(Object obj) {
        staticObj = obj;
    }

    // instance
    public Object instanceObj = null;

    public synchronized void setInstanceObj(Object instanceObj) {
        this.instanceObj = instanceObj;
    }
}
