package test.io.dindinw.util;

import static io.dindinw.util.CollectUtil.asSortedList;
import static io.dindinw.util.CollectUtil.newArrayList;
import static io.dindinw.util.CollectUtil.newSortedList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
    public void test_asSortedList_nullSet(){
        thrown.expect(IllegalArgumentException.class);
        Set<String> nullSet = null;
        asSortedList(nullSet);
    }

    @Test
    public void test_asSortedList_setWithNullElements(){
        thrown.expect(IllegalArgumentException.class);
        Set<String> nullSet = new HashSet<>();
        nullSet.add(null);
        asSortedList(nullSet);
    }

    @Test
    public void test_asSortedList_setContainsNullElements(){
        thrown.expect(IllegalArgumentException.class);
        Set<String> nullSet = new HashSet<>();
        nullSet.add("first");
        nullSet.add("2nd");
        nullSet.add(null);
        nullSet.add("Last");
        asSortedList(nullSet);
    }
    @Test
    public void test_asSortedList_nullArray(){
        thrown.expect(IllegalArgumentException.class);
        String[] nullArray = null;
        asSortedList(nullArray);
    }
    @Test
    public void test_asImmutableSortedList_arrayWithNullElements(){
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("at index [0]");
        asSortedList(null, null);
    }

    @Test
    public void test_asImmutableSortedList_arrayContainNull(){
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("at index [1]");
        asSortedList("not_null", null);
    }

    @Test
    public void test_asImmutableSortedList_arrayNotComparable(){
        thrown.expect(ClassCastException.class);
        asSortedList("string", 2); //cannot compare String with Integer
    }

    @Test
    public void test_asSortedList_formArray(){
        String[] foo = new String[]{"1", "2", "3"};
        List<String> why = Arrays.asList(foo);

        why.set(0,"One");
        try {
            why.add("Four");
            fail();
            //can't add, Arrays.asList() gotcha, the list's size is fixed.
            // the list is the array. and the array is the list.
        }catch (UnsupportedOperationException e) { }

        assertEquals("One", foo[0]); // foo == why, they point to the same array.
                                    // the 'why' list is just a wrapper for the foo
        assertEquals("One",why.get(0));
        assertEquals(why.get(0),foo[0]);
        System.out.println(why);
        Arrays.sort(foo);
        assertEquals(why.get(0),foo[0]);
        assertEquals("2",foo[0]);
        System.out.println("why="+why);    //it is also changed after Arrays.sort(), because they are just same
        List<String> why2 = asSortedList(foo); //so that why2=why=foo
        assertEquals(why2.get(0),foo[0]);
        assertEquals(why2.get(0),why.get(0));
        assertEquals(why2.get(0),why.get(0));
        System.out.println("why2=" + why2);
        why2.set(2,"1");
        Collections.sort(why2);  // Collections.sort also affect the referenced insider array
        assertEquals("1", foo[0]);
        assertEquals("1",why.get(0));
        System.out.println("why=" + why);
        System.out.println("why2=" + why2);
        try {
            why2.remove(2);  // you can't remove, the list is just a wrapper for insider array
            fail();
        }catch(UnsupportedOperationException e){};
    }

    @Test
    public void test_asSortedList_formSet(){
        String[] foo = new String[] {"1","2","3"};
        Set<String> fooSet = new HashSet<>();
        fooSet.addAll(Arrays.asList(foo));
        foo[0] = "one";
        assertFalse(fooSet.contains("one")); // foo[] is nothing to do with fooSet
        assertTrue(fooSet.contains("1"));

        List<String> fooList = asSortedList(fooSet);
        assertEquals("1",fooList.get(0));
        fooList.set(0,"one");
        assertFalse(fooSet.contains("one")); //fooList is nothing to do with fooSet

        try {
            fooList.add("Four"); //can't add new, the size of fooList is fixed, because it's wrapper of insider array
            fail();
        }catch (UnsupportedOperationException e){};
        try {
            fooList.remove(0); //also true for removing
            fail();
        }catch(UnsupportedOperationException e){};

        fooSet.add("Four");
        fooList.contains("Four"); //nothing related each other between fooSet and fooList
        // because fooSet is convert into a new array. and fooList is only related to the insider array.
        // because there is no reference from the outsider, so that it's safe for fooList.
        // but the fooList is just array. so it's can't
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
        thrown.expect(IllegalArgumentException.class);
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
