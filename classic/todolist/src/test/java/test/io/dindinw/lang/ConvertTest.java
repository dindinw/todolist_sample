package test.io.dindinw.lang;

import org.junit.Test;

import static io.dindinw.lang.Convert.convertArray;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit test for {@link io.dindinw.lang.Convert}
 */
public class ConvertTest {
    @Test
    public void testConvertArrayInteger2String(){
        // Integer -> String
        Integer[] iArr = new Integer[]{1,2,3};
        String[] sArr = convertArray(iArr, String::valueOf, String[]::new);
        assertEquals(3,sArr.length);
    }

    @Test
    public void testConvertArrayString2Integer(){
        // String -> Integer
        String[] sArr = new String[]{"1","2","3"};
        Integer[] iArr = convertArray(sArr, Integer::valueOf,Integer[]::new);
        assertEquals(3,sArr.length);

        sArr = new String[]{"A","B","C"};
        try {
            iArr = convertArray(sArr, Integer::valueOf, Integer[]::new);
        }catch(Exception e){
            //java.lang.NumberFormatException: For input string: "A"
            // The utility used with danger for user's case to mark sure
            // the input value is valid to convert.
            assertTrue(e instanceof NumberFormatException);
        }
    }

    @Test
    public void testConvertArray_Int2String(){
        // Int -> String
        int[] iArr = new int[]{1,2,3};
        String[] sArr = convertArray(iArr,String::valueOf,String[]::new);
        assertEquals(3,sArr.length);
    }

}
