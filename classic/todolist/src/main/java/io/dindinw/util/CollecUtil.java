package io.dindinw.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import static io.dindinw.lang.Check.checkElementsNotNull;

/**
 * My Util additional to 
 *   - Collections
 *   - Arrays
 * 
 * @author alex
 *
 */
public class CollecUtil {

    /**
     * like guava : Lists.newArrayList("alpha", "beta", "gamma");
     * @return
     * @see also http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/src-html/com/google/common/collect/Lists.html?r=release
     */
    public static <T> ArrayList<T> newArrayList(T... elements){
        checkElementsNotNull(elements);
        ArrayList<T> list = new ArrayList<T>(elements.length);
        Collections.addAll(list,elements);
        return list;
    }
    
    /**
     * 
     * @param comparable
     * @return immutable List from Arrays.asList()
     */
    @SafeVarargs
    public static <T extends Comparable<T>> List<T> immutableSortedList(T... comparableElements){
            Arrays.sort(comparableElements);
            return Arrays.asList(comparableElements);
    }
    
    
    @SafeVarargs
    public static <T extends Comparable<T>> List<T> newSortedList(T... comparableElements){
            Arrays.sort(comparableElements);
            List<T> myList = new ArrayList<>(comparableElements.length);
            for (T u : comparableElements){
                myList.add(u);
            }
            return myList;
    }
    
    @SafeVarargs
    public static <T extends Iterable<U>, U extends Comparable<U>> List<U> newSortedList(T... iterable){
        List<U> myList = new ArrayList<>(32*iterable.length);
        for (Iterable<U> t : iterable){
            Iterator<U> iter = t.iterator();
            while(iter.hasNext()){
                myList.add(iter.next());
            }
        }
        Object[] myArray = myList.toArray();
        Arrays.sort(myArray);
        ListIterator<U> i = myList.listIterator();
        for (int j=0; j<myArray.length; j++) {
            i.next();
            i.set((U)myArray[j]);
        }
        return myList;
    }
    
    public static <T> List<T> toSortedList(Set<T> inputSet){
        T[] myArray = (T[])inputSet.toArray();
        Arrays.sort(myArray);
        return Arrays.asList(myArray);
    }
    
    public static <T> List<T> toSortedList2(Set<T> inputSet){
        T[] myArray = (T[]) new Object[inputSet.size()];
        inputSet.toArray(myArray);
        Arrays.sort(myArray);
        return Arrays.asList(myArray);
    }
    
    public static <T> List<T> toSortedList3(T... input){
        T[] myArray =  (T[]) Array.newInstance(input.getClass(), input.length);
        int i = 0; 
        for (T t : input){
            myArray[i]=t;
            i++;
        }
        Arrays.sort(myArray);
        return Arrays.asList(myArray);
    }
    
    public static <T> List<T> toSortedList4(Set<T> inputSet){
        Object[] myArray = inputSet.toArray();
        Arrays.sort(myArray);
        List<T> result = new ArrayList<>(myArray.length);
        ListIterator<T> i = result.listIterator();
        for (int j=0; j<myArray.length; j++) {
            i.next();
            i.set((T)myArray[j]);
        }
        return result;
    }
    

}
