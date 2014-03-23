package io.dindinw.concurrent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * 
 * <ul>
 * <li>1. add caller thread to object o's wait set.
 * <li>2. and try to perform unlock action
 * <li>3. caller thread blocked until it removed from wait set.
 * <p>
 * Although, the unlock is performed. but no way to remove thread from wait-set.
 * so block until the max time elapsed.(100ms in the case)
 * </ul>
 * 
 * From 14.1.3 of JCIP by Brain Goetz
 * <p>
 * Just as each Java object can act as a lock, each object can also act as a
 * condition queue, and the {@code wait}, {@code notify},and {@code notifyAll}
 * methods in Object constitute the API for intrinsic condition queues.
 * <p>
 * An object’s intrinsic lock and its intrinsic condition queue are related: in
 * order to call any of the condition queue methods on object X , you must hold
 * the lock on X.
 * <p>
 * Object.wait atomically releases the lock and asks the OS to suspend the
 * current thread, allowing other threads to acquire the lock and therefore
 * modify the object state. Upon waking, it re-acquires the lock before
 * returning.
 * <p>
 * Intuitively, It means “I want to goto sleep, but wake me when something
 * interesting happens”, and calling the notification methods means “something
 * interesting appended ”.
 * <p>
 * After the thread wakes up, wait re-acquires the lock before returning. A
 * thread waking up from wait gets no special priority in re-acquiring the lock;
 * it contends for the lock just like any other thread attempting to enter a
 * synchronized block.
 * 
 * @author yidwu
 * @see http://docs.oracle.com/javase/specs/jls/se7/html/jls-17.html
 */
public class ObjectWaitTest {
    /**
     * When Thread t execute {@code wait()} on object o, if there is zero of
     * number of lock actions by Thread t on object o. then a
     * {@code IllegalMonitorStateException} thrown.
     * 
     */
    @Test
    public void testObjectWaitIlleaglMonitorException() {
        Object o = new Object();
        try {
            o.wait(); // a java.lang.IllegalMonitorStateException will throw
            fail(); // not here
        } catch (IllegalMonitorStateException success) {
            System.out.println("SUCCESS!"); // excepted
        } catch (Exception e) {
            fail(); // should not here
        }
    }

    /**
     * If this is a timed wait and the nanosecs argument is not in the range of
     * 0-999999 or the millisecs argument is negative, then an
     * IllegalArgumentException is thrown.
     */
    @Test
    public void testObjectWaitIllegalArg() {
        final Object o = new Object();
        try {
            synchronized (o) {
                o.wait(-1); // wrong argument
                fail(); // no way to here
            }
        } catch (IllegalArgumentException e) {
            System.out.println("SUCCESS!"); // excepted
        } catch (Exception e) {
            fail(); // no way to here.
        }
    }

    /**
     * if thread t is interrupted, then an InterruptedException is thrown and
     * t's interruption status is set to false.
     * 
     * @see http://docs.oracle.com/javase/specs/jls/se7/html/jls-17.html
     */
    @Test
    public void testObjectWaitInterrupted() {
        final Object o = new Object();
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    synchronized (o) {
                        o.wait(); // t is waiting for notify, in the case means
                                  // forever
                        fail(); // no way to here
                    }
                    fail(); // no way to here
                } catch (InterruptedException success) { // throw in wait()
                    // success.printStackTrace();
                    assertFalse(isInterrupted());
                    System.out.println("SUCCESS!"); // excepted interrupt
                }
            }
        };
        try {
            t.start();
            t.interrupt();
            t.join();
            assertFalse(t.isAlive()); // verify the join is returned
                                      // successfully.
        } catch (Exception unexpected) {
            fail(); // if we go here, it's error
        }
    }

    /**
     * Normal wait for millsecs
     * 
     * @throws InterruptedException
     */
    @Test
    public void testObjectWaitMills() throws InterruptedException {
        Object o = new Object();
        final long start = System.currentTimeMillis();
        synchronized (o) {
            o.wait(100); // blocked 100ms
        }
        final long end = System.currentTimeMillis();
        assertEquals(100, end - start);
    }

    /**
     * Wait Set
     * <ul>
     * <li>Every object, has an associated wait set. A wait set is a set of threads.
     * <li>When a object is created, its wait set is empty. 
     * <li>Actions to add/remove threads to/from wait sets are atomic. 
     * <li>Wait sets are manipulated only through Object.wait/notify/notifyAll.
     * </ul>
     * wait()
     * <ul>
     * <li>1. Thread t add to Object o's wait set.
     * <li>2. 
     * </ul>
     * <p>
     * how to remove : By using notify() method the code is dead lock p
     * 
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
                    try {
                        o.notify();
                        System.out.println("done notify in t2");
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
    public void testObjectWaitSetRemovalByNotifyAll() {

    }
}
