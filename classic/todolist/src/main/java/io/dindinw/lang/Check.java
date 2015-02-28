package io.dindinw.lang;

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
    
    public static <T> void checkNotNullOrEmpty(T input){
        checkNotNullOrEmpty(input,"","");
    }
    public static <T> void checkNotNullOrEmpty(T input, String errMsg){
        checkNotNullOrEmpty(input,errMsg,"");
    }
    public static <T> void checkNotNullOrEmpty(T input, String errMsgTemplate, Object ... errMsgArgs) {
        if (input instanceof String){
            if (input.equals("")) {
                throw new IllegalArgumentException(String.format(errMsgTemplate, errMsgArgs));
            }
        }
        checkNotNull(input,errMsgTemplate, errMsgArgs);
    }
}
