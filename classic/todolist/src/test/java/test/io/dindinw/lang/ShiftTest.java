package test.io.dindinw.lang;

import java.math.BigInteger;
import java.util.Arrays;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Usage of >> >>> <<
 */
public class ShiftTest {

    @Test
    public void testDouble() {
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            assertEquals(i * 2, Double(i));
            assertEquals(-i * 2, Double(-i));
        }
    }

    /**
     * @param i integer
     * @return i*2
     */
    private static int Double(int i) {
        return i << 1;
    }

    @Test
    public void testBase2power() {
        Arrays.stream(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 10, 11, 12, 13}).map((x) -> {
            int base2pow = base2pow(x);
            assertEquals(BigInteger.valueOf(2).pow(x).intValue(), base2pow);
            System.out.printf("1<<%s\t= 2^%s\t= %s\n",x,x,base2pow);
            return base2pow;
        }).toArray();
    }

    /**
     * a power is represented with a base number and an exponent.
     * The base number tells what number is being multiplied.
     * The exponent, a small number written above and to the
     * right of the base number, tells how many times the base
     * number is being multiplied.
     * @param exponent the times of 2 is being multiplied
     * @return 2^exp
     */
    private static int base2pow(int exponent) {
        return 1 << exponent;
    }

    /**
     *
     * @param n should be 2, 4, 8, 16 ...
     * @return 1,2,3,4
     */
    private static int base2log(int n){
        if(n <= 0) throw new IllegalArgumentException();
        return 31 - Integer.numberOfLeadingZeros(n);
    }

    @Test
    public void testLog(){
        assertEquals(3,base2log(8));
    }

}
