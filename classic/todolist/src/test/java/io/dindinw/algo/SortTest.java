package io.dindinw.algo;

import static org.junit.Assert.assertArrayEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import org.junit.Test;
/**
 *  1. Big O Notation .
 *  O(n^2) means that if input is 2 then, algorithm takes 2^2 -> 4 times longer to run. so it 
 *  means a bad algorithm in most case.
 *  
 *  2. DualPivotQuicksort
 *  A Quicksort algorithm by Vladimir Yaroslavskiy, Jon Bentley, and Josh Bloch.
 *  @see
 *  http://permalink.gmane.org/gmane.comp.java.openjdk.core-libs.devel/2628
 *  http://programmingisterrible.com/post/41512566174/engineering-quicksort
 *  http://java.dzone.com/articles/algorithm-week-quicksort-three
 *  https://github.com/arunma/DataStructuresAlgorithms/tree/master/src/basics/sorting/quick
 *  
 * @author yidwu
 *
 */
public class SortTest {
    
    /**
     * in Arrays.sort() use DualPivotQuicksort
     */
    @Test
    public void sortInts() {
        final int[] numbers = {-3, -5, 1, 7, 4, -2};
        final int[] expected = {-5, -3, -2, 1, 4, 7};
        Arrays.sort(numbers);
        assertArrayEquals(expected, numbers);
    }
    /**
     * Inside the Collections.sort() method, also use Arrays.sort()
     *  Object[] a = list.toArray();
        Arrays.sort(a);
        ListIterator<T> i = list.listIterator();
        for (int j=0; j<a.length; j++) {
            i.next();
            i.set((T)a[j]);
        }
     */
    @Test
    public void sortIntList() {
        final String[] strings = {"z", "x", "y", "abc", "zzz", "zazzy"};
        final String[] expected = {"abc", "x", "y", "z", "zazzy", "zzz"};
        
        List<String> list = Arrays.asList(strings);
        Collections.sort(list);
        
        ListIterator<String> i = list.listIterator();
        for (int j=0; j<strings.length; j++){
            strings[j] = i.next();
        }
        
        assertArrayEquals(expected, strings);
    }
    
    /**
     * 
     */
    @Test
    public void sortObjects() {
        final String[] strings = {"z", "x", "y", "abc", "zzz", "zazzy"};
        final String[] expected = {"abc", "x", "y", "z", "zazzy", "zzz"};
        Arrays.sort(strings);
        assertArrayEquals(expected, strings);
    }
    

    
    

}
