package test.io.dindinw.lang;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.Vector;
import java.util.function.Consumer;
import java.util.stream.Stream;

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




    /*
        For testing the new forEach() in Java 8
        Also revisit the ways to do iteration in Java language since 1.0 to 8
     */
    @Test
    public void testForEach() throws Exception{
        ArrayList<String> names = new ArrayList<>();
        names.add("alex");
        names.add("ALEX");

        // JAVA 1  C style for loop
        for (int i = 0; i<names.size(); i++){
            assertEquals("alex",names.get(i).toLowerCase());
        }
        // JAVA 1  Enumeration
        Vector<String> nameVector = new Vector<>();
        nameVector.addAll(names);
        for (Enumeration<String> e = nameVector.elements(); e.hasMoreElements();)
            assertEquals("alex", e.nextElement().toLowerCase());

        // JAVA 2  java.util.Iterator
        // while loop by iterator
        Iterator<String> iterator = names.iterator();
        while(iterator.hasNext()){
            assertEquals("alex",iterator.next().toLowerCase());
        }
        // for loop by iterator
        iterator = names.iterator();
        for(int i= 0;iterator.hasNext();i++){
            assertEquals("alex",iterator.next().toLowerCase());
            if (!iterator.hasNext()){
                assertEquals(1,i);
            }
        }

        // JAVA 5  "foreach" loop

        for (String name: names){
            assertEquals("alex",name.toLowerCase());
        }

        // JAVA 8  language sugar -> functional programming

        // 1. -> to method
        // $$lambda$1 class will be created under your class internally
        // LangTest$$Lambda$1/2066940133.accept(
        names.forEach(s -> assertEquals("alex",s.toLowerCase()));

        // 2. -> {} block
        // LangTest$$Lambda$2/274064559.accept(
        // TODO: I want to know why a magic number like '274064559' as the tail of the lambda class
        names.forEach(s -> {
            assertEquals("alex",s.toLowerCase());
            // I can do want
        });

        // Internal Implementation, native Java inner class
        // LangTest$1.accept(), no any $$Lambda class is created
        Consumer<? super String> what = new Consumer<String>() {
            @Override
            public void accept(String s) {
                assertEquals("alex",s.toLowerCase());
            }
        };
        names.forEach(what);

        // link a lambada to a interface
        // LangTest$$Lambda$3/1018081122.accept
        Consumer<String> handleNames = (String s) -> {
            assertEquals("alex",s.toLowerCase());
        };
        names.forEach(handleNames);

        // the type of argument (String) is not required,
        // because the generic type has the exactly type.
        Consumer<String> handleNames2 = (s) -> {
            assertEquals("alex",s.toLowerCase());
        };
        names.forEach(handleNames2);

        // What exactly lambada is, it's just a inner class for an SINGLE method interface, with language sugar.
        // $$Lambda$5/1433867275.testSame
        Same weAreSame = (s1, s2 ) -> {
            assertNotEquals(s1, s2);
        };
        callSame(weAreSame,names.get(0),names.get(1));

        // so that the lambada is a language sugar for implementing an SINGLE method interface
        String s1 = "foo1";
        String s2 = "foo2";
        callSame((str1,str2) ->{
            assertNotEquals(str1,str2);
        },s1,s2);

        // so that I can create a runnable like this . ()-> for no parameter
        Thread t = new Thread(()->System.out.println("Running"));
        t.start();
        t.join();

        // so that I can create a Comparator like this (String x,String y)->{return Integer.compare(x.length(),y.length())}
        // sort by string length.
        // and in short-hand version :
        // (String x,String y)->Integer.compare(x.length(),y.length())
        // so that we know the {} block is just a type. same with the return type of the interface.
        // since the type of String can be inferred by names.toArray(T[]) method.
        // so that we can simplify to :
        // (x,y)->Integer.compare(x.length(),y.length())

        names.add("AleY");
        assertArrayEquals(new String[]{"alex","ALEX","AleY"},names.toArray(new String[names.size()]));
        String[] sortedName = new String[names.size()];
        Arrays.sort(
                names.toArray(sortedName),
                (x, y) -> Character.compare(x.charAt(x.length() - 1), y.charAt(y.length() - 1))
        );
        assertArrayEquals(new String[]{"ALEX", "AleY", "alex"}, sortedName);

        //'this' in a lambda expression.
        //There is no way to reference to the lambda expression from inside the lambda.
        //@see http://docs.oracle.com/javase/specs/jls/se8/html/jls-15.html#jls-15.27.2
        //If it is necessary for a lambda expression to refer to itself
        //(as if via this), a method reference or an anonymous inner class should be used instead.

        //Method reference

        // 1.) Class::staticMethod  or
        //     object::instanceMethod
        //  the parameters is exactly same from lambda to method reference
        names.forEach((name)->{System.out.println(name);});
        // I can remove the block {} when the code in a single expression
        names.forEach((name)->System.out.println(name));
        // When single expression && If the parameter in () is just the the method input parameter
        // => so that, the lambda expression works as a reference to method.
        // we can omit parameter
        // System.out::println is equivalent to x -> System.out.println(x).
        names.forEach(System.out::println);

        // 2.) Class::instanceMethod
        // the first parameter becomes the target of the method.
        // For example, String::compareToIgnoreCase is the same with (x,y)->x.compareToIgnoreCase(y)
        Arrays.sort(sortedName,(x,y)->x.compareToIgnoreCase(y));
        Arrays.sort(sortedName,String::compareToIgnoreCase);

        //Constructor Ref
        Stream<String> newNames = names.stream();
        String[] a1 = newNames.toArray(x->new String[x]);
        // x-> new String[x] same with String[]::new

        // need to do again
        // you can consume streams only once. otherwise:
        // java.lang.IllegalStateException: stream has already been operated upon or closed
        newNames = names.stream();
        String[] a2 = newNames.toArray(String[]::new);


    }
    private <T> void callSame(Same same,String s1,String s2){
        same.compare(s1, s2);
    }
    interface Same<T> {
        void compare(String s1, String s2);
        // Default Method
        // the interface methods with implementations called default methods.
        // Those methods can be safely added to existing interfaces.
        // For example, the forEach() is a default method
        // which added to the Iterable interface in Java 8
        default void add(String s1, String s2){
            System.out.println("I am JAVA 8 Interface");
        }
        default void add2(String s1, String s2){
            System.out.println("I am JAVA 8 Interface");
        }

        //In Java 8, interface is allowed to add static methods.
        // So that, we don't need List/Lists, Collection/Collections
        static void log(){
            System.out.println("Called!");
        }
    }

    /*
        For Testing the Collections.sort() and Arrays.sort()
    */
    @Test
    public void testSort(){
        List<String> abc = Arrays.asList("C","B","A");
        //before sort
        assertArrayEquals(new String[]{"C", "B", "A"}, abc.toArray());
        Collections.sort(abc);
        //after sort
        assertArrayEquals(new String[]{"A", "B", "C"}, abc.toArray());

        // JAVA 5/6/7
        // convert to Array then, delegate to Arrays.sort
        /*
         * public static <T extends Comparable<? super T>> void sort(List<T> list) {
         *    Object[] a = list.toArray();
         *    Arrays.sort(a);
         *    ListIterator<T> i = list.listIterator();
         *    for (int j=0; j<a.length; j++) {
         *        i.next();
         *        i.set((T)a[j]);
         *    }
         * }
         */

        // JAVA 8
        // 8-b132 (2014/03/18, GA)
        // http://hg.openjdk.java.net/jdk8/jdk8/jdk/file/687fd7c7986d/src/share/classes/java/util/Collections.java
        // same with 7
        /*
            public static <T extends Comparable<? super T>> void sort(List<T> list) {
            Object[] a = list.toArray();
                Arrays.sort(a);
                ListIterator<T> i = list.listIterator();
                for (int j=0; j<a.length; j++) {
                    i.next();
                    i.set((T)a[j]);
                }
            }
         */

        // 8u20 (19th August 2014 8u20 GA)
        // http://hg.openjdk.java.net/jdk8u/jdk8u20/jdk/file/f5d77a430a29/src/share/classes/java/util/Collections.java
        // delegate to List.sort, a default method added into the List interface
        /*
         *   public static <T extends Comparable<? super T>> void sort(List<T> list) {
         *       list.sort(null);
         *   }
         */
        //  The default method in List interface. also delegate to Arrays.sort
        /*
         *  default void sort(Comparator<? super E> c) {
         *      Object[] a = this.toArray();
         *      Arrays.sort(a, (Comparator) c);
         *      ListIterator<E> i = this.listIterator();
         *      for (Object e : a) {
         *          i.next();
         *          i.set((E) e);
         *      }
         *   }
         */

        // 8u40 (3rd March 2015 8u40 GA)
        // http://hg.openjdk.java.net/jdk8u/jdk8u40/jdk/file/c7bbaa04eaa8/src/share/classes/java/util/Collections.java
        // same with 8u20

        List<String> xyz = Arrays.asList("Z","X","Y");
        assertArrayEquals(new String[]{"Z","X","Y"},xyz.toArray());
        xyz.sort(null);
        assertArrayEquals(new String[]{"X","Y","Z"},xyz.toArray());


        List<String> withCase = Arrays.asList("b01","A02","a01");
        //before sort
        assertArrayEquals(new String[]{"b01","A02","a01"},withCase.toArray());
        withCase.sort(null); //default sort
        //after sored by default , A is before a
        assertArrayEquals(new String[]{"A02","a01","b01"},withCase.toArray());
        withCase.sort(String::compareToIgnoreCase); //I love Java 8
        //after sored ignoring case
        assertArrayEquals(new String[]{"a01","A02","b01"},withCase.toArray());

    }

    /*
        Testing for TreeSet/TreeMap, which is ordered (key is ordered for map) in nature
     */
    @Test
    public void testTreeSet(){
        TreeSet<String> orderedSet = new TreeSet();
        orderedSet.add("A01");
        orderedSet.add("A10");
        orderedSet.add("A11");
        assertEquals("A01", orderedSet.first());
        assertEquals("A11",orderedSet.last());
        //Now comparing with HashSet
        HashSet<String> unorderedSet = new HashSet<>();
        unorderedSet.add("A01");
        unorderedSet.add("A10");
        unorderedSet.add("A11");
        ArrayList<String> list = new ArrayList<>(3);
        unorderedSet.forEach(list::add);
        assertEquals("A10",list.get(0));
        assertEquals("A11",list.get(1));
        assertEquals("A01",list.get(2));
    }

    /*
        Find element from Collection
     */
    @Test
    public void testFind(){
        // -----------------------------------------------------------------------------
        // Array
        // -----------------------------------------------------------------------------
        String[] names = new String[]{"Brian","David","Eva","Alex","Christ"};
        assertEquals("Alex",names[3]); //before sorted [Brian,David,Alex,Christ]

        // Gotcha ! the Array need to be sored before using binarySearch
        int found = Arrays.binarySearch(names,"Alex");
        assertEquals(-1,found);
        //why not found
        // the 'Alex' is set to the position bigger than middle value (Eva), so that
        // only [0,1] aka Brian and David will be compared, so that Alex will not be found

        Arrays.sort(names);
        assertEquals("Alex",names[0]); //after sorted  [Alex,Brian,Christ,David,Eva]
        // now find again
        found = Arrays.binarySearch(names,"Alex");
        // this time, binarySearch works
        assertEquals(0,found);
        assertEquals("Alex",names[found]);

        int unfounded = Arrays.binarySearch(names,"alex");
        assertEquals(-6,unfounded); //Why -6
        assertEquals(-30,'C'-'a');  //first compare with mid-value Christ, <0, not found, go to higher one
        assertEquals(-29,'D'-'a');  //then David,  <0, not this one, go to higher
        assertEquals(-28,'E'-'a');  //then Eva, <0, still not found, i am last one now
        //since the last one is still not found, return -(low+1) -> -(5+1) -> -6
        //in the case low is the last one
        //Q: Why return -(insertion point)-1  ?
        //'insertion point' is :
        //    the index of the first element greater than the key,
        //    or the array's length if all elements in the array are less than the key.
    }
}
