package test.io.dindinw.lang;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * Created by alex on 3/6/15.
 */
public class LangTest {

    // For Map Equals
    // @see java.util.AbstractMap
    @Test
    public void test_MapEquals(){
        Map<String,String> myMap = new HashMap<>();
        Map<String,String> myMap2 = new HashMap<>();
        assertNotNull("empty map is not null",myMap);
        assertNotNull("empty map is not null",myMap2);
        assertTrue("two empty maps should equal to each other", myMap.equals(myMap2));
        myMap.put("key","value");
        myMap2.put("key","value");
        assertTrue("two maps with same content should be equal",myMap.equals(myMap2));
        myMap2.put("key","value2");
        assertFalse("map 2 has been changed", myMap.equals(myMap2));
        myMap2.put("key","value");
        assertTrue("map 2 has been changed back", myMap.equals(myMap2));
    }
}
