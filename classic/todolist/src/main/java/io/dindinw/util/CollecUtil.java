package io.dindinw.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

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
     * 
     * @param comparable
     * @return unmutable List from Arrays.asList()
     */
    @SafeVarargs
    public static <U extends Comparable<U>> List<U> unmutableSortedList(U... comparable){
            Arrays.sort(comparable);
            return Arrays.asList(comparable);
    }
    
    @SafeVarargs
    public static <U extends Comparable<U>> List<U> newSortedList(U... comparable){
            Arrays.sort(comparable);
            List<U> myList = new ArrayList<>(comparable.length);
            for (U u : comparable){
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
