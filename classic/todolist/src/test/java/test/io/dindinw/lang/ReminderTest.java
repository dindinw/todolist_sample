package test.io.dindinw.lang;

import org.junit.Test;

import static org.junit.Assert.fail;

/**
 * Test for Java reminder operator (%)
 * https://docs.oracle.com/javase/specs/jls/se8/html/jls-15.html#jls-15.17.3
 *
 */
public class ReminderTest {
    /**
     * If the value of the divisor for an integer remainder operator is 0,
     * then an ArithmeticException is thrown.
     */
    @Test
    public void testZeroDivisor(){
        try {
            int i = 1 % 0; // dividend is 1 and divisor is 0
            fail();
        }catch (java.lang.ArithmeticException e){
           //ArithmeticException: division by zero.
        }
    }


}
