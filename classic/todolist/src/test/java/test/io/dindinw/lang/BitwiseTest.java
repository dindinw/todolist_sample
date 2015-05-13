package test.io.dindinw.lang;

import java.math.BigInteger;
import java.util.Arrays;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Usage of bit Operands such as : | & ~ >> >>> <<
 *
 * The Bible for those algorithm is Henry S. Warren's "Hacker’s Delight"
 * http://www.hackersdelight.org
 *
 */
public class BitwiseTest {
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
        assertEquals(2 * 2, 1 << 2);
        Arrays.stream(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 10, 11, 12, 13, 14, 15, 16}).map((x) -> {
            int base2pow = base2pow(x);
            assertEquals(BigInteger.valueOf(2).pow(x).intValue(), base2pow);
            System.out.printf("1<<%s\t= 2^%s\t= %s\n", x, x, base2pow);
            return base2pow;
        }).toArray();
    }

    /**
     * a power is represented with a base number and an exponent.
     * The base number tells what number is being multiplied.
     * The exponent, a small number written above and to the
     * right of the base number, tells how many times the base
     * number is being multiplied.
     *
     * @param exponent the times of 2 is being multiplied
     * @return 2^exp
     */
    private static int base2pow(int exponent) {
        return 1 << exponent;
    }

    /**
     * @param n should be 2, 4, 8, 16 ...
     * @return 1, 2, 3, 4
     */
    private static int base2log(int n) {
        if (n <= 0) throw new IllegalArgumentException();
        return 31 - Integer.numberOfLeadingZeros(n);
    }

    @Test
    public void testLog() {
        assertEquals(3, base2log(8));
    }


    @Test
    public void testRightShift() {
        // rightShift 1 -> integer divided by 2
        for (int shift = 1; shift < (1 << 5); shift = shift << 1) {
            System.out.printf("shift = %s \n", shift);
            for (int i = 0; i < 0x100; i++) {
                final int _rightShift = i >>> shift;
                System.out.printf("%s>>>%s = %s, %s -> %s \n", i, shift, _rightShift, Integer.toBinaryString(i), Integer.toBinaryString(_rightShift));
            }

        }
    }


    private String padByte(int b) {
        if (b > 0xFF) throw new IllegalArgumentException(b+" is large than 255");
        return Integer.toBinaryString((b & 0xFF) + 0x100).substring(1);
    }

    private String padInteger(int b) {
        return String.format("%32s", Integer.toBinaryString(b)).replace(' ', '0');
    }

    private String padShort(int b) {
        return String.format("%16s", Integer.toBinaryString(b)).replace(' ', '0');
    }

    private String padByte2(int b) {
        if (b > 0xFF) throw new IllegalArgumentException("b large than 255");
        return String.format("%8s", Integer.toBinaryString(b)).replace(' ', '0');
    }

    private String padLong(long b) {
        return String.format("%64s", Long.toBinaryString(b)).replace(' ', '0');
    }

    @Test
    public void testASCIIChar(){
        char A = 'A';
        //System.out.printf("%d %s %s %s \n",(byte)a,a,Integer.toBinaryString(a),Integer.toHexString(a));
        assertEquals(0x41,65);
        assertEquals(65,(byte)A);

        char a = 'a';
        assertEquals(0x61,97);
        assertEquals(97,(byte)a);
    }


    @Test
    public void testBitwise() {
        // Bitwise inclusive OR (|) will compare each bit of 1st operand to the corresponding bit of 2nd operand.
        //    if either bit is 1, then the result bit is 1. otherwise, result bit is 0.

        // The bitwise OR can be used to set the selected bits to 1  (It also answer the question why true is 1,
        // FALSE OR TRUE is TRUE, since 0 | 1 is 1.)
        // This technique is an efficient way to store a number of Boolean values using as little memory as possible.

        final byte ENABLE_BIT_1 = 0b1000;
        final byte ENABLE_BIT_2 = 0b0100;
        final byte ENABLE_BIT_3 = 0b0010;
        final byte ENABLE_BIT_4 = 0b0001;

        assertEquals(0b1100, ENABLE_BIT_1 | ENABLE_BIT_2);
        assertEquals(0b0110, ENABLE_BIT_2 | ENABLE_BIT_3);
        assertEquals(0b0111, ENABLE_BIT_2 | ENABLE_BIT_3 | ENABLE_BIT_4);

        //-----------------------------------
        // How to enable a bit
        //-----------------------------------
        byte config = 0b0000; //nothing enabled
        config |= ENABLE_BIT_1;  // 1st is enabled
        assertEquals(0b1000, config);
        // now enable 3 and 4
        config |= ENABLE_BIT_3 | ENABLE_BIT_4;
        assertEquals(0b1011, config);

        // ---------------------------------
        // how to disable (clear) :
        // ---------------------------------
        // bitwise AND (&) with the bitwise NOT (~) of the bit
        // Bitwise And (&) will compare each bit of op1 and op2, if both of bit is 1, then result 1. otherwise result 0
        // Bitwise Not (~) will toggle the each bit of a operand.
        config &= ~ENABLE_BIT_1; //clear 1
        assertEquals(0b0011, config);
        config &= ~(ENABLE_BIT_2 | ENABLE_BIT_3); // clear 2 and 3
        assertEquals(0b0001, config); // only 4 remains

        // ---------------------------------
        // Check a bit if enabled
        // ---------------------------------
        // way-1: first, shift the bit to the right-most,
        //       then bitwise AND with 1, will result the bit
        assertEquals(1, config >> 0 & 1);
        assertEquals(0, config >> 2 & 1);
        config |= ENABLE_BIT_2;
        assertEquals("00000101",padByte(config)); // original is 2 and 4 enabled
        assertEquals("00000100", padByte(config & ENABLE_BIT_2)); // clear all others bits except 2
        assertEquals("00000001", padByte(config >> 2)); // if we right shift 2 steps, the bit-2 will go to the right-most
        assertEquals(1, config >> 2 & 1); //we bitwise And with 1, we get 1, we now know the bit-2 is 1

        // way-2: bitwise And with the flag, so that we will clear all other bits expects the bit which the flog point to
        //        then we compare the result with the flag. if same, the bit is 1, otherwise the bit is 0
        assertEquals(0b0101, config);
        assertEquals(config, ENABLE_BIT_2 | ENABLE_BIT_4);
        assertTrue((config & ENABLE_BIT_2) == ENABLE_BIT_2);
        assertTrue((config & ENABLE_BIT_4) == ENABLE_BIT_4);
        assertFalse((config & ENABLE_BIT_3) == ENABLE_BIT_3);

    }

    @Test
    public void testEq() {
        for (byte n = 0; n < 0x40; n++) {
            int result;
            result = n | (n >>> 1); // | is  Bitwise inclusive OR
            // same to : x = x | (x >>> 1)
            // See Sec 3-2 : Rounding Up/Down to the Next Power of 2
            // in Henry S. Warren. “Hacker’s Delight, Second Edition.”
            System.out.printf("%d (%s) = %d (%s) \n", n, padByte(n), result, padByte(result));
        }
    }

    @Test
    public void testRightMostBit() {
        // Integer literal may be expressed in :
        //    decimal (base 10), hexadecimal (base 16), octal (base 8), or binary (base 2).
        // octal is base 8, Java use digit 0 following by 0 ~ 7 as octal literal
        //    Q: Why use leading '0' as for octal ?
        //    A: It's a legacy issue from Java -> C -> B -> BCPL
        //       see : http://stackoverflow.com/questions/11483216/why-are-leading-zeroes-used-to-represent-octal-numbers
        // hex use 0x
        // binary use 0b

        assertEquals(8, Byte.SIZE);

        assertEquals(0, 0b000);
        assertEquals(1, 0b001);
        assertEquals(2, 0b010);
        assertEquals(3, 0b011);
        assertEquals(4, 0b100);
        assertEquals(5, 0b101);
        assertEquals(6, 0b110);
        assertEquals(7, 0b111);

        assertEquals(8, 0b1000);
        assertEquals(8, 010);

        assertEquals(9, 0b1001);
        assertEquals(9, 011);

        assertEquals(10, 0b1010);
        assertEquals(10, 012);
        assertEquals(10, 0x0a);

        assertEquals(11, 0b1011);
        assertEquals(11, 013);
        assertEquals(11, 0xb);
        assertEquals(12, 0b1100);
        assertEquals(12, 014);
        assertEquals(12, 0xc);
        assertEquals(13, 0b1101);
        assertEquals(13, 015);
        assertEquals(13, 0xd);
        assertEquals(14, 0b1110);
        assertEquals(14, 016);
        assertEquals(14, 0xe);
        assertEquals(15, 0b1111);
        assertEquals(15, 017);
        assertEquals(15, 0xf);

        // 0744, 0661

        assertEquals(0774, 0b111111100);
        assertEquals(0774, 508);
        assertEquals(0774, 0x1FC);
        assertEquals(0661, 0b110110001);
        assertEquals(0661, 433);
        assertEquals(0661, 0x1B1);

        assertEquals(16, 0b10000);
        assertEquals(16, 020);
        assertEquals(16, 0x10);

        assertEquals(63, 0b111111);
        assertEquals(63, 0x3f);

        assertEquals(64, 0b01000000);
        assertEquals(64, 0x40);

        assertEquals(127, 0b01111111);
        assertEquals(127, 0x7f);

        assertEquals(128, 0b10000000); // overflow of a byte
        assertEquals(-128, (byte) 128); //-128 is the min of byte
        assertEquals(-127, (byte) 129);
        assertEquals(-2, (byte) 254);
        assertEquals(-1, (byte) 255);
        assertEquals(0, (byte) 256);
        assertEquals(1, (byte) 257); // look the overflow!

        //because byte b is always <= it's MAX_VALUE, the condition is always true.
        // when it overflow, the loop will never stopped.
        for (byte b = 0; b <= Byte.MAX_VALUE; b++) {
            break;
        }
        // also always true condition.
        // when it overflow, the loop will never stopped.
        for (byte b = 0; b < Byte.MAX_VALUE + 1; b++) {
            break;
        }


        assertEquals(128, 0x80);

        assertEquals(255, 0xff);
        assertEquals(256, 0x100);

        // byte is 8-bit (-0b10000000 ~ 0b1111111)
        assertEquals(127, Byte.MAX_VALUE);
        assertEquals(0b1111111, Byte.MAX_VALUE);
        assertEquals(0x7F, Byte.MAX_VALUE);

        assertEquals(-128, Byte.MIN_VALUE);
        assertEquals(-0b10000000, Byte.MIN_VALUE);
        assertEquals(-0x80, Byte.MIN_VALUE);

        // short is 16-bit
        // integer is 32-bit
        // long is 64-bit

        assertEquals(16, Short.SIZE);
        assertEquals(2, Short.BYTES);

        for (byte i = 0; i != 0x7f; i++) {
            System.out.printf("right-most 1->0 %d (%s) -> %d (%s) \n", i, padByte(i), turnOffRightMost1Bit(i), padByte(turnOffRightMost1Bit(i)));
            System.out.printf("right-most 0->1 %d (%s) -> %d (%s) \n", i, padByte(i), turnOnRightMost0Bit(i), padByte(turnOnRightMost0Bit(i)));
            if (turnOffRightMost1Bit(i) == 0) {
                System.out.printf("  %d is power-2 if (right-most 1->0)==0 \n", i);
            }
        }
        assertTrue(isPower2(1 << 2));
        assertTrue(isPower2(1 << 3));
        assertTrue(isPower2(1 << 4));
        assertTrue(isPower2(1 << 5));
        assertTrue(isPower2(1 << 30));

        int i = 1;
        System.out.println(i);
        i <<= 30;
        System.out.printf("%d (%s) \n", i, padInteger(i));
        System.out.printf("%d (%s) (%s) \n", Integer.MAX_VALUE, padInteger(Integer.MAX_VALUE), Integer.toHexString(Integer.MAX_VALUE));
        System.out.println(Long.toBinaryString(1 << 31)); //overflow
        System.out.println(Long.toBinaryString(1L << 31)); //ok
        System.out.println(padLong(1 << 31)); //overflow, it actually -2147483648
        System.out.println(padLong(1L << 31)); //ok, it's 2147483648L now
        assertNotEquals(1 << 31, 1L << 31);

        for (int v = 0; v < 0x100; v++) {
            // v<<31 is overflow, it 0 or -2147483648 , which depends on the right-most bit, if 1, then -2147483648, if 0, then 0
            if ((v & 1) == 1) assertEquals(Integer.MIN_VALUE, v << 31);
            if ((v & 1) == 0) assertEquals(0, v << 31);
            //System.out.printf("%d(%s) << 31 -> %d(%s), %d(%s) \n", v, padInteger(v), v << 31, padInteger(v << 31), (long) v << 31, padLong((long) v << 31));
        }

    }

    /**
     * turn off the rightmost 1-bit in a word, producing 0 if none
     * From Sec 2-1, Henry S. Warren. “Hacker’s Delight, Second Edition.”
     *
     * @param i
     * @return
     */
    private int turnOffRightMost1Bit(int i) {
        return i & (i - 1);
    }

    /**
     * used turn-off rightmost-1-bit method to determine if an unsigned integer is a power of 2 or is 0
     * because a power of 2 integer only has single 1 bit. turn it off will result a zero.
     *
     * @param i
     * @return
     */
    private boolean isPower2(int i) {
        if (i <= 0) throw new IllegalArgumentException("input should > 0");
        return turnOffRightMost1Bit(i) == 0;
    }

    /**
     * Sec 2.1 HD
     *
     * @param i
     * @return
     */
    private int turnOnRightMost0Bit(int i) {
        return i | (i + 1);
    }

    /**
     * return the next ceiling of closet power-of-2,
     * see HD sec-3.2
     * http://www.hackersdelight.org/hdcodetxt/clp2.c.txt
     * @param n
     * @return
     */
    private int roundUpNextPower2(int n) {
        n = n - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return n + 1;
    }

    private int roundDownNextPower2(int n){
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return n - (n>>>1);
    }



    @Test
    public void testRoundUp(){
        for (int b=0; b<0x100; b++ ){
            final int result =  roundUpNextPower2(b); //result will overflow the byte range since 129 (results 256)
            System.out.printf("%d(%s) -> %d(%s)\n",b,padByte(b),result,padInteger(result));
        }
    }

    @Test
    public void testRoundDown(){
        for (int b=0;b<0x100; b++) {
            final int result = roundDownNextPower2(b); //result will never overflow a byte, the largest value is 128
            System.out.printf("%d(%s) -> %d(%s)\n",b,padByte(b),result,padByte(result));
        }
    }


    /**
     * It's a copy for CHM initialization logic
     * Why CHM using logic in addOnInitial() to shift the initialCapacity value before round up to a power-of-2 value ?
     * In general, What CHM do is just try to double the size of input initialCapacity. The size need to be round so
     * the round algorithm is fit in. The additional adjustment of the initialCapacity will shift the range, so that the
     * round is more comfortable for memory usage.
     * Ex. :
     * # Without adjustment:
     * ------------------------- --------
     * The initial capacity      result
     * ------------------------- --------
     *   5 ~ 8                     8
     *   9 ~ 16                   16
     *   17 ~ 32                  32
     *   33 ~ 64                  64
     *
     * # After add adjustment :
     * ------------------------- --------
     * The initial capacity      result
     * ------------------------- --------
     *   3 ~  5                    8
     *   6 ~ 10                   16
     *   11 ~ 21                  32
     *   22 ~ 42                  64
     *
     *  so that 9 will not result 16, and it's too memory consume
     *  in the other hand, the 6~8 will result 16 instead of 8. because 8 is not enough
     */
    @Test
    public void testAdjustSize(){
        for (int b=0; b<0x100; b++){
            final int result = addOnInitial(b);
            final int size = roundUpNextPower2(result);
            System.out.printf("%d(%s) -> %d(%s) -> %d(%s)\n", b,padByte(b),result,padInteger(result),
                    size,padInteger(size));
        }
    }
    private int addOnInitial(int i){
        return i + (i >>> 1) + 1 ;
    }




}
