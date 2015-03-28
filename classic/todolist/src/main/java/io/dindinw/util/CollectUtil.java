package io.dindinw.util;

import static io.dindinw.lang.Check.checkNotContainNullElement;
import static io.dindinw.lang.Check.checkNotNull;

import java.util.*;

import io.dindinw.lang.Check;

/**
 * My Collection Utility in additional to {@link java.util.Collections}
 * and {@link java.util.Arrays}.
 *
 * <p> The methods of this class provide language sugar to create {@link java.util.List}
 * <ul>
 * <li> newArrayList
 * <li> newSortedList
 * <li> asImmutableList
 * <li> asImmutableStoredList
 * </ul>
 *
 * <p>See also <a href="http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/src-html/com/google/common/collect/Lists.html?r=release">
 *     google guava Lists</a>
 *
 * @author Alex Wu
 * @version 0.1
 *
 * @see java.util.Arrays
 * @see java.util.Collections
 *
 *
 */
public class CollectUtil {

   /**
     * Get an new ArrayList from the given a array of elements
     * <p>
     * Usage just like guava lists : Lists.newArrayList("alpha", "beta", "gamma");
     * @param elements
     * @param <T>
     * @return
     */
    @SafeVarargs
    public static <T> ArrayList<T> newArrayList(T... elements) {
        checkNotNull(elements, "input elements array can't be null");
        ArrayList<T> list = new ArrayList<T>(elements.length);
        Collections.addAll(list, elements);
        return list;
    }

    /**
     * <p>
     * return the a stored list wrapper a array of elements.
     * The elements should be comparable, and the return list is just a wrapper of the array.
     * so that the size of list is fixed, you can't add/remove element from the list.
     * WARNING: if you sort the list, the array referenced insider will also sorted.
     * java.lang.UnsupportedOperationException will thrown if try to add/remove element the return list.
     * </p>
     *
     * @param comparableElements
     * @throws ClassCastException if the array contains elements that are not
     *         <i>mutually comparable</i> (for example, strings and integers)
     * @return a wrapper List from Arrays.asList()
     * @deprecated the method is nothing good that use Arrays.sort directly
     */
    @SafeVarargs
    @Deprecated
    public static <T extends Comparable<?>> List<T> asSortedList(T... comparableElements) {
        checkNotNull(comparableElements, "comparableElements can't be null.");
        checkNotContainNullElement(comparableElements);
        Arrays.sort(comparableElements);
        return Arrays.asList(comparableElements);
    }

    /**
     * get a stored list from a set of elements
     * @param inputSet
     * @throws ClassCastException if the set contains elements that are not
     *         <i>mutually comparable</i> (for example, strings and integers)
     * @return wrapper List from Arrays.asList()
     * @deprecated this method is nothing good that use Arrays.sort directly
     */
    @Deprecated
    public static <T> List<T> asSortedList(Set<T> inputSet) {
        checkNotNull(inputSet, "inputSet can't be null.");
        checkNotContainNullElement(inputSet);
        T[] myArray = (T[]) inputSet.toArray();
        Arrays.sort(myArray);
        return Arrays.asList(myArray);
    }

    /**
     * <p>
     * create new List from a array of elements. the elements are sorted.
     * </p>
     *
     * @param elements
     * @throws ClassCastException if the array contains elements that are not
     *         <i>mutually comparable</i> (for example, strings and integers)
     * @return an new ArrayList of element
     */
    @SafeVarargs
    public static <T> List<T> newSortedList(T... elements) {
        Arrays.sort(elements);
        return newArrayList(elements);
    }

    /**
     * <p>
     *     create new list of a set of elements. the elements are stored.
     * </p>
     * @param inputSet
     * @throws ClassCastException if the array contains elements that are not
     *         <i>mutually comparable</i> (for example, strings and integers)
     * @return an new ArrayList of element
     */
    public static <T> List<T> newSortedList(Set<T> inputSet) {
        T[] myArray = (T[])inputSet.toArray();
        Arrays.sort(myArray);        
        return newArrayList(myArray);
    }

    /**
     * <p>
     * create new list of by combining multiple iterable (a list for example), all elements are sorted.
     * </p>
     * @throws ClassCastException if any iterable contains elements that are not
     *         <i>mutually comparable</i> (for example, strings and integers)
      *
     * @param collections array of Iterable
     * @return an new ArrayList
     */
    @SafeVarargs
    public static <ITERABLE extends Iterable<T>, T> List<T> newSortedList(ITERABLE... collections) {
        checkNotNull(collections);
        checkNotContainNullElement(collections);
        int sizeOfList = 0;
        for (Iterable<T> collection : collections){
            if (collection instanceof List){
                sizeOfList += ((List<T>) collection).size();
            }else if (collection instanceof Set){
                sizeOfList += ((Set<T>) collection).size();
            }else if (collection instanceof Queue){
                sizeOfList += ((Queue<T>) collection).size();
            }else{
               Check.checkArg(false, "%s[%s] is not supported.", collection.getClass().getSimpleName(), collection);
            }
        }
        List<T> myList = new ArrayList<>(sizeOfList);
        for (Iterable<T> collection : collections) {
            for (T aT : collection) {
                myList.add(aT);
            }
        }
        T[] myArray = (T[])myList.toArray();
        Arrays.sort(myArray);
        return newArrayList(myArray);
    }
}

