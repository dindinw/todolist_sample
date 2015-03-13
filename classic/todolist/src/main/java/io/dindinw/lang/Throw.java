package io.dindinw.lang;

import static io.dindinw.lang.Check.checkNotContainEmptyElement;
import static io.dindinw.lang.Check.checkState;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

/**
 * Created by alex on 3/13/15.
 */
public class Throw {
    public static void throwException(boolean exp, String errMsgTemplate, Object... errMsgArgs) throws Exception{
       if(exp){
           throwException(errMsgTemplate,errMsgArgs);
       }
    }
    public static <T extends Throwable> void throwMe(Class<T> clazz,boolean exp, String errMsgTemplate, Object... errMsgArgs) throws T{
        if(exp){
            T throwable = newT(clazz,errMsgTemplate,errMsgArgs);
            checkState(throwable == null, "failed to initialize %s",clazz.getName());
            throw throwable;
        }
    }
    public static void throwException(String errMsgTemplate, Object... errMsgArgs) throws Exception{
        throw newException(errMsgTemplate,errMsgArgs);
    }

    public static Exception newException(String errMsgTemplate, Object... errMsgArgs) throws Exception{
        Exception e = newT(Exception.class,errMsgTemplate,errMsgArgs);
        return e;
    }
    //TODO,
    // 1.) not properly handling for the case of private inner class
    // 2.) how to handle the default constructor if no constructor is defined to take string parameter.
    private static <T extends Throwable> T newT(Class<T> clazz, String errMsgTemplate, Object... errMsgArgs){
        T t = null;
        Constructor<T> constructor;
        try {
            constructor = clazz.getDeclaredConstructor(String.class);
        } catch (NoSuchMethodException e) {
            //not found
            return null;
        }
        if (constructor!=null){
            try {
                t  = constructor.newInstance(String.format(errMsgTemplate,errMsgArgs));
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return t;
    }
}
