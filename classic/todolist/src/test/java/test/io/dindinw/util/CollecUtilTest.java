package test.io.dindinw.util;

import static io.dindinw.util.CollecUtil.newSortedList;
import static io.dindinw.util.CollecUtil.unmutableSortedList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CollecUtilTest {
    
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    
    @Test
    public void test_unmutableSortedList_throw_UnsupportedOperationException(){
        List<String> list1 = unmutableSortedList("alex","wu","1_text");
        List<String> list2 = unmutableSortedList("alex","wu","1_text");
        list1.equals(list2);
        assertFalse(list1==list2); //two objects
        assertEquals(list1,list2); //two lists are equals because the strings which the list contain are same.
        
        thrown.expect(UnsupportedOperationException.class); 
        list1.add("6"); //can't add, since the list is unmodified, exception thrown

    }
    @Test
    public void test_newSortedList() {
        assertEquals("1_text",newSortedList("alex","wu","1_text").get(0)); //[1_text, alex, wu]
        assertTrue(3 == newSortedList(1,2,3).get(2)); // [1,2,3]
        List<String> list1 = Arrays.asList("one", "two", "three");
        List<String> list2 = Arrays.asList("4", "5");

        assertEquals("one",newSortedList(list1).get(0)); //[one, three, two]
        assertEquals("4",newSortedList(list1, list2).get(0)); //[4, 5, one, three, two] 
        
        Set<String> mySet = new HashSet<>();
        mySet.addAll(Arrays.asList("W","Y","D"));
        assertEquals("D",newSortedList(mySet).get(0)); //[D,W,Y]
    }
    

    @Test
    public void test_newSortedList_throw_CastingException(){
        List myList = new ArrayList<>();
        myList.add("2");
        myList.add(1);
        thrown.expect(ClassCastException.class);
        thrown.expectMessage("java.lang.String cannot be cast to java.lang.Integer");
        newSortedList(myList);
    }
    /**
     * From Javadoc of Arrays.sort(): 
     * all elements in the array must be mutually comparable 
     * (that is, e1.compareTo(e2) must not throw a ClassCastException 
     * for any elements e1 and e2 in the array).
     */
    @Test
    public void test_newSortedList_throw_CastingException_InArraysSort() {
        Object[] array = new Object[]{"3",1,2};
        thrown.expect(ClassCastException.class);
        thrown.expectMessage("java.lang.String cannot be cast to java.lang.Integer");
        Arrays.sort(array); //cast exception
    }

}
