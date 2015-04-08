package io.dindinw.lang;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.IntUnaryOperator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by alex on 4/8/15.
 */
public final class Convert {
    private Convert() {
    }

    ;

    /**
     * [1,2,3] -> ["1","2","3"]
     *   convertArray(new Integer[]{1,2,3},String::valueOf,String[]:new)
     *
     * Note: the func can't convert primitive
     *
     * @param array
     * @param func
     * @param generator
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> R[] convertArray(T[] array, Function<T, R> func, IntFunction<R[]> generator) {
        return Arrays.stream(array).parallel().map(func).toArray(generator);
    }

    // What about primitive conversion?
    // Generic is not for primitive

    public static <R> R[] convertArray(int[] array, IntFunction<R> func, IntFunction<R[]> generator) {
        return Arrays.stream(array).mapToObj(func).toArray(generator);
    }



}
