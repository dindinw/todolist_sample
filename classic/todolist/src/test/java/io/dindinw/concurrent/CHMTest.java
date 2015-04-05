package io.dindinw.concurrent;

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
}
