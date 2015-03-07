package io.dindinw.lang;

import java.util.Iterator;
import java.util.Objects;

/**
 * Utility class for input check.
 * <p>
 *     Inspired by Google guava's Precondition class. But instead, use the standard JDK 7 methods.
 *
 * @see @linktourl http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/src-html/com/google/common/base/Preconditions.html
 * @author alex
 */
public final class Check {
    private Check() {}

    public static void checkArg(boolean exp,String errMsgTemplate, Object... errMsgArgs) throws IllegalArgumentException{
        _checkArg(exp,errMsgTemplate,errMsgArgs);

    }
    private static void _checkArg(boolean exp,String errMsgTemplate, Object... errMsgArgs) throws IllegalArgumentException{
        errMsgTemplate = String.valueOf(errMsgTemplate); //in case null->"null"
        if (!exp){
            throw new IllegalArgumentException(String.format(errMsgTemplate, errMsgArgs));
        }
    }

    public static <T> void checkNotNull(T input) throws NullPointerException {
        _checkNotNull(input, "");
    }
    public static <T> void checkNotNull(T input, String errMsgTemplate, Object ... errMsgArgs) throws NullPointerException {
        _checkNotNull(input,errMsgTemplate,errMsgArgs);
    }
    private static <T> void _checkNotNull(T input, String errMsgTemplate, Object ... errMsgArgs) throws NullPointerException {
        errMsgTemplate = String.valueOf(errMsgTemplate); // in case null -> "null"
        Objects.requireNonNull(input, String.format(errMsgTemplate, errMsgArgs));
    }
    
    public static <T> void checkNotEmpty(T[] array) throws NullPointerException,IllegalArgumentException{
        _checkNotNull(array,"input array should not be null");
        _checkNotEmpty(array, "empty %s", array.getClass().getSimpleName());
    }
    public static <T> void checkNotEmpty(T[] array,String errMsgTemplate, Object ... errMsgArgs) throws NullPointerException, IllegalArgumentException{
        _checkNotNull(array,errMsgTemplate,errMsgArgs);
        _checkNotEmpty(array,errMsgTemplate,errMsgArgs);
    }
    private static <T> void _checkNotEmpty(T[] array, String errMsgTemplate, Object... errMsgArgs) throws NullPointerException,IllegalArgumentException{
        _checkArg(array.length > 0, errMsgTemplate, errMsgArgs);
    }
    
    public static <T extends Iterable<?>> void checkNotEmpty(T iterable ){
        _checkNotNull(iterable, "input iterable should not be null");
        _checkNotEmpty(iterable, "empty %s",iterable.getClass().getSimpleName());
    }
    public static <T extends Iterable<?>> void checkNotEmpty(T iterable ,String errMsgTemplate, Object ... errMsgArgs) {
        _checkNotNull(iterable, errMsgTemplate,errMsgArgs);
        _checkNotEmpty(iterable,errMsgTemplate,errMsgArgs);
    }
    private static <T extends Iterable<?>> void _checkNotEmpty(T iterable ,String errMsgTemplate, Object ... errMsgArgs) throws NullPointerException, IllegalArgumentException {
        Iterator<?> iterator = iterable.iterator();
        _checkArg(iterator.hasNext(), errMsgTemplate, errMsgArgs);
    }

    public static void checkNotEmpty(String input){
        _checkNotNull(input, "input string should not be null");
        _checkNotEmpty(input, "input string should not be empty");
    }
    public static void checkNotEmpty(String input, String errMsgTemplate, Object ... errMsgArgs) {
        _checkNotNull(input,errMsgTemplate, errMsgArgs);
        _checkNotEmpty(input,errMsgTemplate,errMsgArgs);
    }
    private static void _checkNotEmpty(String input, String errMsgTemplate, Object ... errMsgArgs) {
        if (input.equals("")) {
            throw new IllegalArgumentException(String.format(errMsgTemplate, errMsgArgs));
        }
    }

    
    /**
     * 
     * @see @linktourl http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/src-html/com/google/common/collect/ObjectArrays.html?r=release
     */
    public static <T> T[] checkNotContainNullElement(T[] elements){
        _checkNotNull(elements, "input array elements should not be null");
        for (int i = 0 ; i < elements.length; i++){
            _checkElementNotNull(elements,i);
        }
        return elements;
    }
    /**
     * Check if the element of array[index] is null, return element if not null
     * otherwise NPE thrown
     * @param array input array
     * @param index the index of the array
     * @return the element aka. array[index]
     */
    public static <T> T checkNotContainNullElement(T[] array, int index){
        _checkNotNull(array, "input array should not be null");
        _checkIndexInArray(array, index);
        _checkElementNotNull(array,index);
        return array[index];
    }
    
    public static <T extends Iterable<U>, U> T checkNotContainNullElement(T elements){
        _checkNotNull(elements, "input T elements should not be null");
        for (U element : elements) {
            _checkNotNull(element, "%s %s contains null", elements.getClass().getSimpleName(), elements);
        }
        return elements;
    }
    
    
    public static String[] checkNotContainEmptyElement(String[] elements){
        _checkNotNull(elements, "input String[] should not be null");
        _checkNotEmpty(elements, "input String[] should not be empty");
        for (int i = 0 ; i < elements.length; i++){
            _checkElementNotNull(elements,i);
            _checkElementNotEmpty(elements,i);
        }
        return elements;
    }
    public static String checkNotContainEmptyElement(String[] array, int index){
        _checkNotNull(array, "input String[] array should not be null");
        _checkIndexInArray(array, index);
        _checkElementNotNull(array,index);
        _checkElementNotEmpty(array,index);
        return array[index];
    }

    private static <T> void _checkIndexInArray(T[] array, int index){
        _checkArg(index >= 0, "input index [%s] should not be negative.", index);
        _checkArg(index < array.length, "input index [%s] should not be bigger than array's length [%s].", index, array.length);
    }

    private static <T> void _checkElementNotNull(T[] array, int index){
        _checkNotNull(array[index], "at index [%s]", index);
    }
    private static <T> void _checkElementNotEmpty(String[] stringArray, int index){
        _checkNotEmpty(stringArray[index], "empty string at index [%s]", index);
    }

    public static <T extends Checker> T getChecker(Class<T> checkerClass) {
        return (T)Checkers.valueOf(checkerClass.getSimpleName()).get();
    }

    private enum Checkers{
        StringChecker(_stringChecker),
        NumberChecker(_numberChecker);
        private Checker _checker;
        Checkers(Checker checker) {
            this._checker=checker;
        }
        Checker get() {
            return _checker;
        }
    }
    private static StringChecker _stringChecker = new StringChecker();
    private static NumberChecker _numberChecker = new NumberChecker();
    private interface Checker {};
    public static final class StringChecker implements Checker{
        private StringChecker() {};

        public static boolean isSingleChar(String input){
            return (input!=null&&!input.isEmpty()&&input.length()==1) ? true:false;
        }
        public static boolean isLetter(String input){
            if (isSingleChar(input)){
                return Character.isLetter(input.charAt(0));
            }
            return false;
        }
    }
    public static final class NumberChecker implements Checker{
        private NumberChecker() {};
    }

}


