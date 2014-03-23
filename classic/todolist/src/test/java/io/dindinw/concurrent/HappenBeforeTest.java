package io.dindinw.concurrent;

import static org.junit.Assert.assertTrue;

import org.junit.Test;


/** 
 * <h3>
 * The Rule of Happen-before:</h3>
 * <ul>
 * <li>1. in same thread, in the program order
 * <li>2. passed, hb(x,y), hb(y,z) -> hb(x,z)
 * </ul>
 * 
 * @see <a href=http://docs.oracle.com/javase/specs/jls/se7/html/jls-17.html>JLS
 *      Ch17: Threads and Locks</a>
 * @author yidwu
 */
public class HappenBeforeTest {

    /**
     * When Thread t execute {@code wait()} on object o, if there is zero of
     * number of lock actions by Thread t on oject o. then a
     * {@code IllegalMonitorStateException} thrown.
     * 
     */
    @Test
    public void testIlleaglMonitorException() {
        Object o = new Object();
        try {
            o.wait(); // a java.lang.IllegalMonitorStateException will throw
        } catch (Exception e) {
            assertTrue(e instanceof java.lang.IllegalMonitorStateException);
        }
    }

    /**
     * <ul>
     * <li>1. add caller thread to object o's wait set.
     * <li>2. and try to perform unlock action
     * <li>3. caller thread blocked until it removed from wait set.
     * <p>
     * Although, the unlock is performed. but no way to remove thread from wait-set.
     * so block until the max time elapsed.(100ms in the case)
     * </ul>

     * 
     * 
     * @throws InterruptedException
     */
    @Test
    public void testObjectWait() throws InterruptedException {
        Object o = new Object();
        synchronized (o) {
            o.wait(100); // blocked 100ms
        }
    }

    /**
     ** how to remove : By using notify() method
     * @throws InterruptedException
     */
    @Test
    public void testObjectWaitSetRemovalByNotify() throws InterruptedException {
        final Object o = new Object();
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (o) {
                    try {
                        o.wait();
                        System.out.println("recoveryed from wait() in t1");
                        o.notify();
                        System.out.println("done notify in t1");
                        System.out.println("t1 fininshed");
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }

        });
        t1.start();
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (o) {
                    o.notify();
                    System.out.println("done notify in t2");
                    try {
                        o.wait();
                        System.out.println("recovery from wait() in t2");
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    System.out.println("t2 fininshed!");
                }
            }

        });
        t2.start();

        Thread t3 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (o) {
                    try {
                        o.wait();
                        System.out.println("recovery from wait() in t3");
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    System.out.println("t3 fininished!");
                }
            }
        });
        t3.start();

        Thread t4 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (o) {
                    o.notify();
                    System.out.println("done notify() in t4");
                    System.out.println("t4 fininished!");
                }
            }
        });
        t4.start();

        t1.join();
        t2.join();
        t3.join();
        t4.join();
    }
    @Test
    public void testObjectWaitSetRemovalByNotifyAll(){
        
    }
}
