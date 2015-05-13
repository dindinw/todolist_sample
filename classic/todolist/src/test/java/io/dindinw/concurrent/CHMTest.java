package io.dindinw.concurrent;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Function;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


/**
 * The class is created for the Testing of ConcurrentHashMap impl
 *
 * ---------------------------------------------------------------------------------------------------------------------
 * JDK 6 :
 *     The concurrencyLevel will control the segment creation. the default value for concurrencyLevel is 16
 *     a lot of ConcurrentHashMap$Segment, ConcurrentHashMap$HashEntry[] and ReentrantLock$NonfairSync
 *     will result a memory footprint if u try to construct a map (even empty) but with  a large size.
 * http://hg.openjdk.java.net/jdk6/jdk6/jdk/file/39e8fe7a0af1/src/share/classes/java/util/concurrent/ConcurrentHashMap.java
 * see line 640: initialize all 16
 *     for (int i = 0; i < this.segments.length; ++i)
 *           this.segments[i] = new Segment<K,V>(cap, loadFactor);
 * But, it's already fixed by 7036559: ConcurrentHashMap footprint and contention improvements (Mon, 18 Apr 2011)
 * http://hg.openjdk.java.net/jdk6/jdk6/jdk/file/c4ed64237075/src/share/classes/java/util/concurrent/ConcurrentHashMap.java
 * see line 755: lazy initialize only first one
 *     Segment<K,V> s0 =  new Segment<K,V>(loadFactor, (int)(cap * loadFactor), (HashEntry<K,V>[])new HashEntry[cap]);
 *     Segment<K,V>[] ss = (Segment<K,V>[])new Segment[ssize];
 *     UNSAFE.putOrderedObject(ss, SBASE, s0); // ordered write of segments[0]
 *     this.segments = ss;
 * But I think the fix only in OpenJDK, the (OpenJDK 6 is from jdk7-b23 (2009.1.31))
 * See file changes log at :
 * http://hg.openjdk.java.net/jdk6/jdk6/jdk/log/c4ed64237075/src/share/classes/java/util/concurrent/ConcurrentHashMap.java
 * 2011-04-18	dl	7036559: ConcurrentHashMap footprint and contention improvements
 * See also :
 *    https://ria101.wordpress.com/2011/12/12/concurrenthashmap-avoid-a-common-misuse/
 *
 * ---------------------------------------------------------------------------------------------------------------------
 *
 * JDK 7 :
 *    lazy segment initialization added to impl.
 *
 * OpenJDK (use lazy segment)
 *   http://hg.openjdk.java.net/jdk7/jdk7/jdk/file/9b8c96f96a0f/src/share/classes/java/util/concurrent/ConcurrentHashMap.java
 *   Log here:
 *   http://hg.openjdk.java.net/jdk7/jdk7/jdk/log/9b8c96f96a0f/src/share/classes/java/util/concurrent/ConcurrentHashMap.java
 *   see 2011-04-18 dl 7036559: ConcurrentHashMap footprint and contention improvements
 *   Note:
 *   see 759, only first one is created by constructor
 *   see ensureSegment() 671
 *
 * ---------------------------------------------------------------------------------------------------------------------
 * JDK 8 :
 *    don't use lock.
 *
 * OpenJDK Latest:
 *  http://hg.openjdk.java.net/jdk8/jdk8/jdk/file/tip/src/share/classes/java/util/concurrent/ConcurrentHashMap.java
 *  http://hg.openjdk.java.net/jdk8/jdk8/jdk/file/dc2f0c40399a/src/share/classes/java/util/concurrent/ConcurrentHashMap.java (05 Dec 2013)
 * See Log here:
 *  http://hg.openjdk.java.net/jdk8/jdk8/jdk/log/687fd7c7986d/src/share/classes/java/util/concurrent/ConcurrentHashMap.java
 *
 * Latest JDK7 impl by Doug Lea: (no segment usage anymore)
 *   http://gee.cs.oswego.edu/cgi-bin/viewcvs.cgi/jsr166/src/jdk7/java/util/concurrent/ConcurrentHashMap.java?view=markup
 * Latest JAVA 8 impl by Doug Lea: (a lot of java.util.function usage)
 *   http://gee.cs.oswego.edu/cgi-bin/viewcvs.cgi/jsr166/src/main/java/util/concurrent/ConcurrentHashMap.java?view=markup
 * ConcurrentHashMapV8 Java 8 backport (can compiled with JDK 6)
 *   http://gee.cs.oswego.edu/cgi-bin/viewcvs.cgi/jsr166/src/jsr166e/ConcurrentHashMapV8.java?view=markup
 *
 *
 *
 * ---------------------------------------------------------------------------------------------------------------------
 * Doug Lea's site: http://gee.cs.oswego.edu/dl/concurrency-interest/
 *
 */
public class CHMTest {

    @Test
    public void testConstructors(){
        // test initialCapacity
        for (int cap=0 ; cap<0x100; cap++) {
            ConcurrentHashMap chm = new ConcurrentHashMap(cap);
            // 2->4
            // 3,4,5->8
            // 6,7,8,9,10->16
            // 11~21 -> 32
            // 22~42 -> 64
            // 43~85 -> 128
            // 86~169? -> 256
            // 170,? -> 512
        }


        // using factor 0.75
        for (int cap=0 ; cap<0x100; cap++) {
            ConcurrentHashMap chm = new ConcurrentHashMap(cap,0.75f);
            // 0,1, -> 2 -> 2
            // 2    -> 3 -> 4
            // 3    -> 5 -> 8
            // 4    -> 6 -> 8
            // 5    -> 7 -> 8
            // 6    -> 9 -> 16
            // 7    -> 10 -> 16
            // 8    -> 11 -> 16
            // 9    -> 13 -> 16
            // 10   -> 14 -> 16
            // 11   -> 15 -> 16
            // 12   -> 17 -> 32
            // ..
            // 24   -> 33 -> 64
            // 25   -> 34 -> 64
            // 48   -> 65 -> 128
        }

        // use factor 0.5
        for (int i = 0 ; i<0x100; i++){
            ConcurrentHashMap n = new ConcurrentHashMap(i,0.5f);
            // 1         3 -> 4
            // 2         5 -> 8
            // 3         7 -> 8
            // 4         9  -> 16
            // 5         11 -> 16
            // 6         13 -> 16
            // 7         15 -> 16
            // 8         17 -> 32
            // 9         19 -> 32
            // 10        21 -> 32
            // ...
            // 16        33 -> 64
            // ...
            // 32        65 -> 128
            n.newKeySet();

        }




    }

    /**
     * Java 8 feature
     */
    @Test
    public void testLongAdder(){

        //Java 8
        ConcurrentHashMap<String,LongAdder> feq = new ConcurrentHashMap<>();
        feq.putIfAbsent("foo", new LongAdder());
        feq.get("foo").increment();
        assertEquals(1,feq.get("foo").intValue());

        //pre Java 8
        ConcurrentHashMap<String,AtomicLong> feq17 = new ConcurrentHashMap<>();
        feq17.putIfAbsent("foo", new AtomicLong(0L));
        feq17.get("foo").incrementAndGet();


        // So the question is , Why need LongAdder ?
        // By JavaDoc
        // LongAdder is usually preferable to AtomicLong when multiple threads
        // update a common sum that is used for purposes such as collecting statistics,
        // not for fine-grained synchronization control. Under low update contention,
        // the two classes have similar characteristics.
        // But under high contention, expected throughput of this class is
        // significantly higher, at the expense of higher space consumption.
    }

    @Test
    public void testCompute(){
        // Java 8 feature
        ConcurrentHashMap<String,Long> feq = new ConcurrentHashMap<>();
        feq.put("foo", 0L);
        feq.compute("foo", (k, v) -> v == 0 ? 1 : v + 1);
        assertEquals(new Long(1L), feq.get("foo"));
        feq.put("foo", 1L);
        feq.compute("foo", (k, v) -> v == 0 ? 1 : v + 1);
        assertEquals(2, feq.get("foo").intValue());
        feq.compute("foo", (k, v) -> v == 0 ? 1 : v + 1);
        assertEquals(3, feq.get("foo").intValue());

        // Java 8 feature by using computerIfAbsent
        ConcurrentHashMap<String,LongAdder> feq2 = new ConcurrentHashMap<>();
        feq2.computeIfAbsent("foo", k->new LongAdder()).increment();
        feq2.computeIfAbsent("foo", k->new LongAdder()).increment(); //the current (existing or computed) value
        // so that it's the existing one. not an new one
        assertEquals(2, feq2.get("foo").intValue());
        feq2.get("foo").increment();
        assertEquals(3, feq2.get("foo").intValue());

        feq2.compute("foo",(k,v)->new LongAdder()).increment(); //replace with new one this time
        assertEquals(1,feq2.get("foo").intValue());
        feq2.get("foo").increment();
        assertEquals(2,feq2.get("foo").intValue());

        // Using merge
        ConcurrentHashMap<String,Long> feq3 = new ConcurrentHashMap<>();
        feq3.merge("foo", 1L, Long::sum);
        assertEquals(1,feq3.get("foo").intValue());
        assertEquals(1, feq3.get("foo").intValue());
    }

}
