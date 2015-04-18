package test.io.dindinw.lang;

import java.math.BigInteger;
import java.util.Arrays;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Usage of bit Operands such as : | & ~ >> >>> <<
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
        assertEquals(2*2,1<<2);
        Arrays.stream(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 10, 11, 12, 13,14,15,16}).map((x) -> {
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


    @Test
    public void testRightShift(){
        // rightShift 1 -> integer divided by 2
        for (int shift = 1; shift<(1<<5); shift=shift<<1) {
            System.out.printf("shift = %s \n", shift);
            for (int i = 0; i < 0x100; i++) {
                final int _rightShift = i >>> shift;
                System.out.printf("%s>>>%s = %s, %s -> %s \n", i, shift, _rightShift, Integer.toBinaryString(i), Integer.toBinaryString(_rightShift));
            }

        }
    }

    private String padByte(byte b){
        return Integer.toBinaryString((b& 0xF)+0x10).substring(1);
    }
    private String padInt(int b){
        return Integer.toBinaryString((b& 0xFF)+0x100).substring(1);
    }

    @Test
    public void testBitwise(){
        // Bitwise inclusive OR (|) will compare each bit of 1st operand to the corresponding bit of 2nd operand.
        //    if either bit is 1, then the result bit is 1. otherwise, result bit is 0.

        // The bitwise OR can be used to set the selected bits to 1  (It also answer the question why true is 1,
        // FALSE OR TRUE is TRUE, since 0 | 1 is 1.)
        // This technique is an efficient way to store a number of Boolean values using as little memory as possible.

        final byte ENABLE_BIT_1 = 0b1000;
        final byte ENABLE_BIT_2 = 0b0100;
        final byte ENABLE_BIT_3 = 0b0010;
        final byte ENABLE_BIT_4 = 0b0001;

        assertEquals(0b1100,ENABLE_BIT_1|ENABLE_BIT_2);
        assertEquals(0b0110,ENABLE_BIT_2|ENABLE_BIT_3);
        assertEquals(0b0111,ENABLE_BIT_2|ENABLE_BIT_3|ENABLE_BIT_4);

        byte config = 0b0000 ; //nothing enabled
        config |= ENABLE_BIT_1;  // 1st is enabled
        assertEquals(0b1000,config);
        // now enable 3 and 4
        config |= ENABLE_BIT_3|ENABLE_BIT_4;
        assertEquals(0b1011,config);

        // how to disable (clear) : bitwise AND (&) with the bitwise NOT (~) of the bit
        // Bitwise And (&) will compare each bit of op1 and op2, if both of bit is 1, then result 1. otherwise result 0
        // Bitwise Not (~) will toggle the each bit of a operand.
        config &= ~ENABLE_BIT_1 ; //clear 1
        assertEquals(0b0011,config);
        config &= ~(ENABLE_BIT_2|ENABLE_BIT_3); // clear 2 and 3
        assertEquals(0b0001,config); // only 4 remains

        // Ok, How to check a bit if enabled
        assertEquals(1,config&ENABLE_BIT_4);
        assertEquals(0, config & ENABLE_BIT_2);

    }

    @Test
    public void testEq(){
        for (byte n = 0; n<0x40; n++) {
            int result;
            result = n | (n >>> 1); // | is  Bitwise inclusive OR
            // same to : x = x | (x >>> 1)
            // See Sec 3-2 : Rounding Up/Down to the Next Power of 2
            // in Henry S. Warren. “Hacker’s Delight, Second Edition.”
            System.out.printf("%d (%s) = %d (%s) \n",n,padInt(n),result,padInt(result));
        }
    }

    @Test
    public void testRightMostBit(){
        // Integer literal may be expressed in :
        //    decimal (base 10), hexadecimal (base 16), octal (base 8), or binary (base 2).
        // octal is base 8, Java use digit 0 following by 0 ~ 7 as octal literal
        //    Q: Why use leading '0' as for octal ?
        //    A: It's a legacy issue from Java -> C -> B -> BCPL
        //       see : http://stackoverflow.com/questions/11483216/why-are-leading-zeroes-used-to-represent-octal-numbers
        // hex use 0x
        // binary use 0b
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

        assertEquals(10,0b1010);
        assertEquals(10,012);
        assertEquals(10,0x0a);


        // 0744, 0661

        assertEquals(0774,0b111111100);
        assertEquals(0774,508);
        assertEquals(0774,0x1FC);
        assertEquals(0661,0b110110001);
        assertEquals(0661,433);
        assertEquals(0661,0x1B1);

        assertEquals(15, 0b1111);
        assertEquals(15, 0xf);
        assertEquals(16, 0x10);
        assertEquals(63, 0x3f);
        assertEquals(64, 0x40);
        assertEquals(127,0x7f);
        assertEquals(128,0x80);
        assertEquals(255,0xff);
        assertEquals(256,0x100);

        // byte is 8-bit (-0b10000000 ~ 0b1111111)
        assertEquals(127,Byte.MAX_VALUE);
        assertEquals(0b1111111,Byte.MAX_VALUE);
        assertEquals(0x7F,Byte.MAX_VALUE);

        assertEquals(-128,Byte.MIN_VALUE);
        assertEquals(-0b10000000,Byte.MIN_VALUE);
        assertEquals(-0x80,Byte.MIN_VALUE);

        // short is 16-bit
        // integer is 32-bit
        // long is 64-bit

        assertEquals(16, Long.bitCount(1));

    }

    private boolean isPower2(int i){
        return true;
    }
}
