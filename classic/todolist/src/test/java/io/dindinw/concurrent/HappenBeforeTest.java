package io.dindinw.concurrent;

import org.junit.Test;



/**
 * http://docs.oracle.com/javase/specs/jls/se7/html/jls-17.html
 * 
 * Rules about:
 * Which values may be seen by a read of shared memory that is updated by multiple threads. 
 * 
 * Synchronization:
 * ----------------
 *
 *  1. Each object is associated with a monitor
 *  2. A thread can lock or unlock the monitor
 *  3. Only one thread at a time may hold a lock on a monitor.
 *  4. Any other threads attempting to lock that monitor are blocked until they can obtain
 * 
 *  Synchronized statement : synchronized(obj)
 *   - 1. computes a reference to an object 
 *   - 2. try attempts to perform a lock action on that object's monitor
 *   - 3. blocked until the lock action has successfully completed
 *   - 4. if lock action performed, body statements will be executed
 *   - 5. the body completed, the unlock action is auto performed on that same monitor. 
 *  Synchronized method : 
 *   - 1. when it is invoked, automatically performs a lock action and blocked body
 *   - 2. if instance method, lock the monitor of instance for which is was invoked. (lock this)
 *   - 3. if static method, lock the monitor of Class object of the method defined.
 *   - 4. if lock performed, method body will be executed
 *   - 5. when method completed normally or abruptly, unlock is auto performed.
 *   
 *  Note: the Synchronization is just a way to access shared memory, but not limited to.
 * 
 * 
 * -------------
 * 
 * The Rule of Happen-before:
 *  1. in same thread, in the program order
 *  2. passed, hb(x,y), hb(y,z) -> hb(x,z)
 * @author yidwu
 *
 */
public class HappenBeforeTest {

    @Test
    public void testIlleaglMonitorException() throws InterruptedException {
        Object o = new Object();
        o.wait(); // a java.lang.IllegalMonitorStateException will throw
        
    }
}
