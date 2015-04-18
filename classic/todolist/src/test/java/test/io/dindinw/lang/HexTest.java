package test.io.dindinw.lang;

import java.util.Arrays;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Test for extension for Integer.toBinaryString
 */
public class HexTest {

    @Test
    public void testInteger() {

        System.out.println("Integer");
        System.out.println("-----------------------------------------------");

        // integer sizeof is 32
        System.out.printf ("size : %d \n",Integer.SIZE);
        assertEquals(32, Integer.toBinaryString(0x80000000).length());
        assertEquals(32, Integer.SIZE);

        // int.min is 0x80000000 -> 10000000000000000000000000000000 -> -2147483648
        System.out.printf("   The minimum value : %d \n", 0x80000000); //-2147483648
        System.out.printf("   The binary string : %s \n", Integer.toBinaryString(0x80000000));


        assertEquals('1', Integer.toBinaryString(0x80000000).charAt(0));
        assertEquals(0x80000000, Integer.MIN_VALUE);
        // 0 -> int.max+1

        showBinaryString(-2);
        // -1 ->
        showBinaryString(-1);
        showBinaryString(1);
        showBinaryString(2);
        showBinaryString(3);
        showBinaryString(~3 + 1);
        showBinaryString((3 ^ 0xffffffff) + 1); // same to ~3
        // int.max 2147483647

        System.out.printf("   The maximum value : %d \n",0x7fffffff);
        System.out.printf("   The binary string : %s \n", Integer.toBinaryString(0x7fffffff));

        assertEquals(-2147483648,Integer.MIN_VALUE);
        // 2147483648 is a wrong Integer literal, the max allow value is 2147483647
        assertEquals(2147483647,Integer.MAX_VALUE);
        // so you need to use long for 2147483648L
        assertEquals(2147483648L,(long)Integer.MAX_VALUE+1);  // Dangerous! Integer.MAX_VALUE+1 is
                                                              // overflow if no force conversion.
        // be careful! here is a overflow
        assertEquals(-2147483648,Integer.MAX_VALUE+1);

    }

    @Test
    public void testByte(){
        System.out.println("Byte");
        System.out.println("-----------------------------------------------");
        //unsigned byte
        for (int i = 0; i<= 0xff ; i++){
            System.out.printf("%15d : %s \n",i,Integer.toBinaryString(i));
        }
        Byte[] bytes = new Byte[1 << Byte.SIZE]; // 1<<8, 256
        byte b = Byte.MIN_VALUE;
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = b;
            b++;
        }
        assertSame(Byte.MAX_VALUE, bytes[bytes.length - 1]);
        System.out.println(Arrays.asList(bytes));
//        Arrays.stream(bytes).forEach(this::showPadString);

        int[] unsignedInts;
        System.out.println(Integer.toBinaryString(Integer.MAX_VALUE));
        System.out.println(Integer.toBinaryString(1));    //1
        System.out.println(Integer.toBinaryString(1<<1)); //10 -> 2^1
        System.out.println(Integer.toBinaryString(1<<2)); //100 -> 2^2

        System.out.println(Integer.toBinaryString(1073741823));
        assertEquals(Integer.MAX_VALUE - (1 << 30),0x3fffffff);
        assertEquals((1 << 30)+0x3fffffff, Integer.MAX_VALUE); //1<<31 is 2^31
        System.out.println(Integer.MAX_VALUE);
        System.out.println(Long.MAX_VALUE);
        System.out.println((1L<<62)+0x3fffffff); // 1L<<63 is overflow
        System.out.println(Integer.toUnsignedString((1 << 30)+0x3fffffff - 2)); //1<<31 is overflow

    }
    private void showBinaryString(int i){
        System.out.printf("%15d : %s \n",i,Integer.toBinaryString(i));
    }
    private void showPadString(int value) {
        System.out.println("--------------");
        System.out.println("Try byte :" + value);
        System.out.println("--------------");
        System.out.println("padBin1 : " + padBinaryString1(value));
        System.out.println("padBin2 : " + padBinaryString2(value));
        System.out.println("padBin3 : " + padBinaryString3(value));
        System.out.println("I.toBin : " + Integer.toBinaryString(value));
        System.out.println("I.toHex : " + Integer.toHexString(value));
    }

    private static String padBinaryString1(int value) {
        /* 0x100 is 256, or 0b100000000 */
        return Integer.toBinaryString((value & 0xFF) + 0x100).substring(1);
    }

    private static String padBinaryString2(int value) {
        return String.format("%8s", Integer.toBinaryString(value & 0xFF)).replace(' ', '0');
    }

    /**
     * Problem :
     * 0 -> 0
     * 1 -> 1, not pad
     */
    private static String padBinaryString3(int value) {
        /* 0xFF -> 0xFF is 255, or 11111111 (max value for an unsigned byte) */
        /* &0xFF will excluded leading 24 bit  */
        return Integer.toBinaryString((value & 0xFF));
    }
}
