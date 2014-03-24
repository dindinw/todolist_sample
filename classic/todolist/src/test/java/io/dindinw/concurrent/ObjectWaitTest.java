package io.dindinw.concurrent;

import static io.dindinw.concurrent.TestHelper.LOGGER;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.concurrent.TimeUnit;

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
            LOGGER.info("SUCCESS!"); // excepted
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
            LOGGER.info("SUCCESS!"); // excepted
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
        Thread t = new Thread("testObjectWaitInterrupted_t") {
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
                    LOGGER.info("SUCCESS!"); // excepted interrupt
                    assertFalse(isInterrupted());
                    
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
        assertTrue((end - start) >= 100); // elapsed time may 100ms or 101ms (depends on OS system)
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
     * <li>1. Thread t add to Object o's wait set and unlock
     * </ul>
     * <p>
     * how to remove : By using notify() method the code is dead lock p
     * 
     * @throws InterruptedException
     */
    @Test
    public void testObjectWaitNotify() throws InterruptedException {
        final Object o = new Object();
        Thread t1 = new Thread("testObjectWaitNotify_t1"){
            @Override
            public void run() {
                synchronized (o) {
                    try {
                        LOGGER.info("t1 is waiting for ...");
                        o.wait();
                        //Upon waking, it re-acquires the lock before returning. it's atomatic
                        LOGGER.info("t1 is notified by t2");
                        o.notify();
                        LOGGER.info("t1 notfiy t2");
                    } catch (InterruptedException e) {
                        fail(); // not go here
                    }
                }
                LOGGER.info("t1 fininshed!");
            }

        };

        Thread t2 = new Thread("testObjectWaitNotify_t2"){
            @Override
            public void run() {
                synchronized (o) {
                    try {
                        o.notify();
                        LOGGER.info("t2 notify t1");
                        LOGGER.info("t2 is waiting for ...");
                        o.wait();
                        LOGGER.info("t2 is notified by t1");
                    } catch (Exception e) {
                        fail(); // not go here
                    }
                }
                LOGGER.info("t2 fininshed!");
            }

        };
        
        t1.start();
        t2.start();
        t1.join(100);
        t2.join(100);
        assertFalse(t1.isAlive());
        assertFalse(t2.isAlive());
    }

    static class FooServer {
        private enum state {STOPPED,RUNNING,FAILED};
        private state _state = state.STOPPED;
        private static final Object _lock = new Object();
        private static final Object _joinlock = new Object();
        public void join() throws InterruptedException{
            synchronized(_joinlock){
                while (isRunning()){
                    _joinlock.wait();
                }
            }
        }
        public boolean isRunning () {
            return  _state == state.RUNNING ;
        }
        public boolean isStopped () {
            return _state == state.STOPPED;
        }
        public boolean isFailed () {
            return _state == state.FAILED;
        }
        public void start() throws Exception{
            synchronized (_lock){
                if (isRunning()) return;
                try {
                    doStart();
                    _state = state.RUNNING;
                    LOGGER.info("Foo Server started!");
                } catch (Exception e) {
                    _state = state.FAILED;
                    throw e;
                }
            }
        }
        public void stop() throws Exception{
            synchronized (_lock){
                if (!isRunning()) return;
                try {
                    doStop();
                    _state = state.STOPPED;
                    LOGGER.info("Foo Server stopped!");
                    synchronized(_joinlock){
                        _joinlock.notifyAll(); 
                    //Here notifyAll to mark sure all threads that are waiting 
                    //on this object's monitor are notified
                    }
                } catch (Exception e) {
                    _state = state.FAILED;
                    throw e;
                } 
            }
        }
        protected void doStop() throws Exception{}  // time-consume
        protected void doStart() throws Exception{} // time-consume
    }
    @Test
    public void testObjectWaitNotifyAll() throws Exception {
        final FooServer server = new FooServer(){
            @Override
            protected void doStart() throws Exception {
                TimeUnit.MILLISECONDS.sleep(100);
            }
            @Override
            protected void doStop() throws Exception {
                TimeUnit.MILLISECONDS.sleep(200);
            }
        };
        Thread t1 = new Thread("FooServer_start"){
            @Override
            public void run() {
                try {
                    server.start();
                } catch (Exception e) {
                    fail(); //not go there
                }
            }
        };
        Thread t2 = new Thread("FooServer_stop"){
            @Override
            public void run() {
                try {
                    TimeUnit.MILLISECONDS.sleep(1500); //mock some time consume
                    server.stop();
                } catch (Exception e) {
                    fail(); //not go there
                }
            }
        };
        
        Thread t3 = new Thread("FooServer_join1"){
            @Override
            public void run() {
                try {
                    server.join();
                } catch (Exception e) {
                    fail(); //not go there
                }
            }
        };
        Thread t4 = new Thread("FooServer_join2"){
            @Override
            public void run() {
                try {
                    server.join();
                } catch (Exception e) {
                    fail(); //not go there
                }
            }
        };
        
        assertTrue(server.isStopped()); 
        assertFalse(server.isRunning());
        server.join(); //not wait, because server is not started
        
        assertTrue(server.isStopped()); //before t1 start, server not started
        t1.start();
        t1.join();  //after t1 done
        assertTrue(server.isRunning()); //server started ok
        
        t3.start();
        t4.start();
        
        t2.start(); //try to do stop job
        /**
         * Here if we change the notifyAll to notify, t3 and t4 may not return
         * Because, We have 3 thread in the wait set. t3,t4 and main. when stop
         * send a notify, only one guarantee wake up.
         */
        t3.join();  //make sure join return ok
        t4.join();  //make sure join return ok
        server.join(); 
        
        assertFalse(server.isRunning());  //verify the server is not running
        assertTrue(server.isStopped());
    }


}
