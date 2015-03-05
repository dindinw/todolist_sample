package test.io.dindinw.util;

import static io.dindinw.util.CollectUtil.asImmutableSortedList;
import static io.dindinw.util.CollectUtil.newArrayList;
import static io.dindinw.util.CollectUtil.newSortedList;
import static org.junit.Assert.assertEquals;
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
public class CollectUtilTest {
    
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    @Test
    public void test_asImmutableSortedList_nullSet(){
        thrown.expect(NullPointerException.class);
        Set<String> nullSet = null;
        asImmutableSortedList(nullSet);
    }

    @Test
    public void test_asImmutableSortedList_setWithNullElements(){
        thrown.expect(NullPointerException.class);
        Set<String> nullSet = new HashSet<>();
        nullSet.add(null);
        asImmutableSortedList(nullSet);
    }

    @Test
    public void test_asImmutableSortedList_setContainsNullElements(){
        thrown.expect(NullPointerException.class);
        Set<String> nullSet = new HashSet<>();
        nullSet.add("first");
        nullSet.add("2nd");
        nullSet.add(null);
        nullSet.add("Last");
        asImmutableSortedList(nullSet);
    }
    @Test
    public void test_asImmutableSortedList_nullArray(){
        thrown.expect(NullPointerException.class);
        String[] nullArray = null;
        asImmutableSortedList(nullArray);
    }
    @Test
    public void test_asImmutableSortedList_arrayWithNullElements(){
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("at index [0]");
        asImmutableSortedList(null, null);
    }

    @Test
    public void test_asImmutableSortedList_arrayContainNull(){
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("at index [1]");
        asImmutableSortedList("not_null",null);
    }

    @Test
    public void test_asImmutableSortedList_arrayNotComparable(){
        thrown.expect(ClassCastException.class);
        asImmutableSortedList("string",2); //cannot compare String with Integer
    }

    @Test
    public void test_newSortedList() {
        assertEquals("1_text",newSortedList("alex","wu","1_text").get(0)); //[1_text, alex, wu]
        assertTrue(3 == newSortedList(1,2,3).get(2)); // [1,2,3]

        Set<String> mySet = new HashSet<>();
        mySet.addAll(Arrays.asList("W","Y","D"));
        assertEquals("D",newSortedList(mySet).get(0)); //[D,W,Y]

        List<String> list1 = Arrays.asList("one", "two", "three");
        List<String> list2 = newArrayList("4", "5");

        assertEquals("one",newSortedList(list1).get(0)); //[one, three, two]
        assertEquals("the first one should be 4","4",newSortedList(list1, list2).get(0)); //[4, 5, one, three, two]
        assertEquals("the size should be 5",list1.size()+list2.size(), newSortedList(list1,list2).size());

   }
    

    @Test
    public void test_newSortedList_throw_CastingException(){
        List<Object> myList = new ArrayList<>();
        myList.add("2");
        myList.add(1);
        /**
         * From Javadoc of Arrays.sort():
         * all elements in the array must be mutually comparable
         * (that is, e1.compareTo(e2) must not throw a ClassCastException
         * for any elements e1 and e2 in the array).
         */
        thrown.expect(ClassCastException.class);
        thrown.expectMessage("java.lang.String cannot be cast to java.lang.Integer");
        newSortedList(myList);
    }

    @Test
    public void test_newStoredList_twoList_CastingException(){
       List<Object> list3 = newArrayList(new Object[] {4, "5"});
        assertEquals(4, list3.get(0));
        assertEquals("5",list3.get(1));
        List<Object> list4 = newArrayList(new Object[] {});
        thrown.expect(ClassCastException.class); //can't compare integer with string
        newSortedList(list3,list4);

    }

    @Test
    public void test_newArrayList_null(){
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("input elements array can't be null");
        newArrayList((Object[]) null);
    }

    @Test
    public void test_newArrayList_nullNull(){
        //allow to get a list with null elements
        List<Object> nullNullList = newArrayList(null, null);
        assertEquals(2,nullNullList.size());
        assertEquals(null,nullNullList.get(0));
        assertEquals(null,nullNullList.get(1));
    }

    @Test
    public void test_newArrayList_2(){
       assertEquals(2,newArrayList("test","test2").size());
    }
    
    @Test
    public void test_newArrayList_fromArray(){
       assertEquals(2,newArrayList(new String[]{"test","test2"}).size());
       assertEquals(2,newArrayList(new Object[]{1,"test2"}).size());
       ArrayList<Object> list = newArrayList(new Object[]{1,"test2"});
       assertEquals(1,list.get(0));
       assertEquals("test2",list.get(1));
    }

}
