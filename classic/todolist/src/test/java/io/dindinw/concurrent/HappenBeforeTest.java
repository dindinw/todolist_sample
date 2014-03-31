package io.dindinw.concurrent;

import org.junit.Test;

/** 
 * <h3>
 * The Rule of Happen-before:</h3>
 * we have two actions x and y, we write hb(x, y) to indicate that x happens-before y
 * <ul>
 * <li>1. in same thread, in the program order
 * <li>2. hb is transitive, if hb(x,y), hb(y,z) then hb(x,z)
 * <li>3. 
 * </ul>
 * 
 * @see <a href=http://docs.oracle.com/javase/specs/jls/se7/html/jls-17.html>JLS
 *      Ch17: Threads and Locks</a>
 * @see 17.4.5. Happens-before Order 
 * @author yidwu
 */
public class HappenBeforeTest {

    /**
     * There is a happens-before edge from the end of a constructor of an object 
     * to the start of a finalizer (§12.6) for that object. 
     * @throws InterruptedException 
     * @see java.lang.ref.Finalizer
     */
    @Test public void testBeforeFinalize() throws InterruptedException{
        java.lang.ref.WeakReference<String> wf ;
        Object o = new Object(){
            @Override
            protected void finalize() throws Throwable {
                System.out.println("CALLED?");
            }
        };
        o = null;
        System.gc();
        Thread.sleep(1000);
    }
    /**
     * 1. A weak reference, is a reference that isn't strong enough to force an 
     * object to remain in memory. Weak references allow you to leverage the garbage
     *  collector's ability to determine reach-ability for you, so you don't have 
     *  to do it yourself. 
     * 2. 
     */
}
