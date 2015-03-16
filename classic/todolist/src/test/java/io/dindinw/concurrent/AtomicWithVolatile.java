package io.dindinw.concurrent;

/**
 * The Test to answer the question Why prefer using the AtomicXXX Class in JAVA 5
 * over the 'volatile + synchronized block'
 * <p>
 *     In General (Copy from Brian Goetz's Book)
 * <p>
 *     AtomicXXX classes
 *    <ul>
 *      <li>provide Non-blocking Compare-And-Swap implementation
 *      <li>Takes advantage of the support provide by hardware
 *          (the CMPXCHG instruction on Intel) When lots of threads are running through your code that uses these atomic concurrency API, they will scale much better than code which uses Object level monitors/synchronization. Since, Java's synchronization mechanisms makes code wait, when there are lots of threads running through your critical sections, a substantial amount of CPU time is spent in managing the synchronization mechanism itself (waiting, notifying, etc). Since the new API uses hardware level constructs (atomic variables) and wait and lock free algorithms to implement thread-safety, a lot more of CPU time is spent "doing stuff" rather than in managing synchronization.
 *      <li>not only offer better throughput, but they also provide greater resistance to liveness problems such as deadlock and priority inversion *     </p>
 *    </ul>
 * <p>
 *     The AtomicXXX's is no-block (which block in Hardware level), but {@code synchronized} statement alwasy means the calling thread need suspend.
 *     So that In the most cases, the {@code AtomicXXX} should better perform than 'volatile + synchronized'
 *
 * <p>Created by alex on 3/16/15.
 *
 * @see <a href="http://docs.oracle.com/javase/1.5.0/docs/api/java/util/concurrent/atomic/package-summary.html">
 *     The Summary of atomiac package in JAVA 1.5</a>
 * @see <a href="http://grepcode.com/file/repository.grepcode.com/java/root/jdk/openjdk/8-b132/java/util/concurrent/atomic/AtomicInteger.java?av=f">
 *     Source of AtomicInteger</a>
 * @see <a href="http://www.pwendell.com/2012/08/13/java-lock-free-deepdive.html">
 *     Some explain for sun.misc.Unsafe's compareAndSwapInt and 'cmpxchg' instrunction of CPU</a>
 */
public class AtomicWithVolatile {
}
