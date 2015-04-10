package test.io.dindinw.lang;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

/**
 * Unit Test for Java 8 stream
 * <p/>
 * Ref :
 * http://www.drdobbs.com/jvm/lambdas-and-streams-in-java-8-libraries/240166818?pgno=1
 */
public class StreamTest {

    @Ignore("Time consuming for demonstrate wrong use")
    @Test
    public void testOnClose() {
        try (IntStream s = Arrays.stream(new int[]{1, 2, 3})) {
            s.onClose(() -> System.out.println("I am first."))
                    .onClose(() -> System.out.println("I am second"))
                    .onClose(() -> evilThings())
                    .onClose(StreamTest::evilThings);
        }
    }
    // when a lambda is use to call a existed method. we can use method reference instead of lambda
    // 4 kinds of method refs :
    // Kind                                                                         Example
    // ----------------------------------                                           ------------------------------------
    // Reference to a static method                                                 ContainingClass::staticMethodName
    // Reference to an instance method of a particular object                       containingObject::instanceMethodName
    // Reference to an instance method of an arbitrary object of a particular type  ContainingType::methodName
    // Reference to a constructor                                                   ClassName::new

    /**
     * If we set a onClose() Runnable never(almost) stopped
     * The try-close is vulnerable to run
     */
    private static void evilThings() {
        try {
            TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("I am evil.");
    }

    @Test
    public void testParallel() {
        final int[] intArr = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        convertArray(intArr);
        int[] array2 = new int[20];
        Arrays.fill(array2,0);
        convertArray(array2);
    }

    private void convertArray(int[] intArr) {
        try (IntStream s = Arrays.stream(intArr)) {
            assertFalse(s.isParallel());
            final long startSeq = System.currentTimeMillis();
            s.mapToObj(StreamTest::timeConsume).toArray();
            printDuration(s.isParallel(),intArr.length,System.currentTimeMillis()-startSeq);
        }
        try (IntStream s = Arrays.stream(intArr)) {
            s.parallel(); //use parallel for a time-consumed task, if the task can be executed in parallel
            final long startPal = System.currentTimeMillis();
            s.mapToObj(StreamTest::timeConsume).toArray();
            printDuration(s.isParallel(),intArr.length,System.currentTimeMillis()-startPal);
        }
    }


    private static void printDuration(boolean parallel,int size,long duration) {
        System.out.printf("Parallel=%s\tSize=%s \tDuration: %s ms  \n",parallel,size,duration);
    }

    private static String timeConsume(int i) {
        String convertTo = Integer.toBinaryString(i);
        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return convertTo;
    }
}
