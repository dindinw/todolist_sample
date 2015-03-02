package io.dindinw.lang;

import java.util.Iterator;
import java.util.Objects;

/**
 * Utility class for input check.
 * <p> Inspired by Google guava's Precondition class. But instead, use the 
 * standard JDK 7 methods. 
 * 
 * @author alex
 * @see also http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/src-html/com/google/common/base/Preconditions.html
 */
public final class Check {
    private Check() {}
    
    public static void checkArg(boolean exp,String errMsg){
        checkArg(exp,errMsg,"");
    }
    public static void checkArg(boolean exp,String errMsgTemplate, Object ... errMsgArgs){
        if (!exp){
            throw new IllegalArgumentException(String.format(errMsgTemplate, errMsgArgs));
        }
    }
    public static <T> void checkNotNull(T input) {
        checkNotNull(input,"","");
    }
    public static <T> void checkNotNull(T input, String errMsg) {
        checkNotNull(input,errMsg,"");
    }
    public static <T> void checkNotNull(T input, String errMsgTemplate, Object ... errMsgArgs) {
        Objects.requireNonNull(input, String.format(errMsgTemplate, errMsgArgs));
    }
    
    public static <T> void checkNotEmpty(T[] array){
        checkNotEmpty(array,"empty %s",array.getClass().getSimpleName());
    }
    
    public static <T> void checkNotEmpty(T[] array,String errMsg){
        checkNotEmpty(array,errMsg,"");
    }
    
    public static <T> void checkNotEmpty(T[] array, String errMsgTemplate, Object ... errMsgArgs) {
        checkArg(array.length > 0,errMsgTemplate,errMsgArgs);
    }
    
    public static <T extends Iterable<?>> void checkNotEmpty(T iterable ){
        checkNotEmpty(iterable, "empty %s",iterable.getClass().getSimpleName());
    }
    
    public static <T extends Iterable<?>> void checkNotEmpty(T iterable ,String errMsg) {
        checkNotEmpty(iterable,errMsg,"");
    }
    
    public static <T extends Iterable<?>> void checkNotEmpty(T iterable ,String errMsgTemplate, Object ... errMsgArgs) {
        checkNotNull(iterable);
        Iterator<?> iter = iterable.iterator();
        checkArg(iter.hasNext() == true, errMsgTemplate,errMsgArgs);
    }
    
    public static void checkNotEmpty(String input){
        checkNotEmpty(input,"","");
    }
    public static void checkNotEmpty(String input, String errMsg){
        checkNotEmpty(input,errMsg,"");
    }
    public static void checkNotEmpty(String input, String errMsgTemplate, Object ... errMsgArgs) {
        checkNotNull(input,errMsgTemplate, errMsgArgs);
        if (input.equals("")) {
            throw new IllegalArgumentException(String.format(errMsgTemplate, errMsgArgs));
        }
    }
    
    /**
     * 
     * @see also http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/src-html/com/google/common/collect/ObjectArrays.html?r=release
     */
    public static <T> T[] checkElementsNotNull(T... elements){
        checkNotEmpty(elements);
        for (int i = 0 ; i < elements.length; i++){
            _checkElementNotNull(elements,i);
        }
        return elements;
    }
    
    public static <T extends Iterable<U>, U> T checkElementsNotNull(T elements){
        checkNotNull(elements);
        Iterator<U> iter = elements.iterator();
        while(iter.hasNext()){
            U element = iter.next();
            checkNotNull(element,"%s %s contains null",elements.getClass().getSimpleName(), elements);
        }
        return elements;
    }
    /**
     * Check if the element of array[index] is null, return element if not null
     * otherwise NPE thrown
     * @param array
     * @param index
     * @return
     */
    public static <T> T checkElementNotNull(T[] array, int index){
        checkIndexInArray(array,index);
        _checkElementNotNull(array,index);
        return array[index];
    }
    
    public static <T> T[] checkIndexInArray(T[] array, int index){
        checkNotNull(array);
        checkArg(index>=0,"input index [%s] should not be negative.",index);
        checkArg(index<array.length,"input index [%s] should not be bigger than array's length [%s].",index,array.length);
        return array;
    }
    
    
    public static String[] checkElementsNotEmpty(String... elements){
        checkNotEmpty(elements);
        for (int i = 0 ; i < elements.length; i++){
            _checkElementNotNull(elements,i);
            _checkElementNotEmpty(elements,i);
        }
        return elements;
    }
    
    public static String checkElementNotEmpty(String[] array, int index){
        checkIndexInArray(array,index);
        _checkElementNotNull(array,index);
        _checkElementNotEmpty(array,index);
        return array[index];
    }
    
    private static <T> void _checkElementNotNull(T[] array, int index){
        checkNotNull(array[index], "at index [%s]", index);
    }
    private static <T> void _checkElementNotEmpty(String[] stringArray, int index){
        checkNotEmpty(stringArray[index], "empty string at index [%s]", index);
    }
}
