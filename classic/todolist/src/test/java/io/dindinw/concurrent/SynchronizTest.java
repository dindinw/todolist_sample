package io.dindinw.concurrent;

import org.junit.Test;

/**
 * <h3>
 * Synchronization:</h3>
 * <ol>
 * <li>Each object is associated with a monitor
 * <li>A thread can lock or unlock the monitor
 * <li>Only one thread at a time may hold a lock on a monitor.
 * <li>Any other threads attempting to lock that monitor are blocked until they
 * can obtain
 * </ol>
 * <b>Synchronized statement : synchronized(obj) </b>
 * <ol>
 * <li>computes a reference to an object
 * <li>try attempts to perform a lock action on that object's monitor
 * <li>blocked until the lock action has successfully completed
 * <li>if lock action performed, body statements will be executed
 * <li>the body completed, the unlock action is auto performed on that same
 * monitor.
 * </ol>
 * <b> Synchronized method : </b>
 * <ol>
 * <li>when it is invoked, automatically performs a lock action and blocked body
 * <li>if instance method, lock the monitor of instance for which is was
 * invoked. (lock this)
 * <li>if static method, lock the monitor of Class object of the method defined.
 * <li>if lock performed, method body will be executed
 * <li>when method completed normally or abruptly, unlock is auto performed.
 * </ol>
 * <p>
 * Note: 
 * <ul>
 * <li> 1. the Synchronization is just a way to access shared memory, but not
 * limited to.
 * <li> 2. Java synchronization is reentrant.
 * </ul>
 * <p>
 * @author yidwu
 * @see <a href=http://docs.oracle.com/javase/specs/jls/se7/html/jls-17.html>JLS
 *      Ch17: Threads and Locks</a>
 */
public class SynchronizTest {
    
    /**
     * Reentrancy means the lock is acquired per-thread instead of per-invocation
     */
    public static class ReentrancyBase {
        public synchronized void doSomething(){
            System.out.println("do Something in base");
        }
    }
    public static class ReentrancyDerived extends ReentrancyBase{
        @Override
        public synchronized void doSomething() {
            System.out.println("do Something in subclass");
            super.doSomething(); //not dead lock, same thread 
        }
    }
    @Test
    public void testReentrancy(){
        new ReentrancyDerived().doSomething();
    }
    
    
    /**
     * Although we call syn method of sb, but all reference of sb 
     * inside the method stack. so the sync is eliminated safely.
     */
    public static class SyncElim{
        public String contact(String s1, String s2){
            StringBuffer sb = new StringBuffer();
            sb.append(s1); //synchronized method, eliminated by JVM
            sb.append(s2); //synchronized method, eliminated by JVM
            return sb.toString();
        }
    }
    @Test
    public void testSyncElimination(){
        new SyncElim().contact("test", "syncElim");
    }

}
