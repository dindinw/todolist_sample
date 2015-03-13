package test.io.dindinw.lang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import io.dindinw.lang.Throw;
import org.junit.Test;

/**
 * Created by alex on 3/13/15.
 */
public class ThrowTest {
    @Test
    public void testThrowNewException(){
        try {
            Throw.throwException(true, "new %s's exception", "alex");
        }catch (Exception e) {
            assertNotNull(e);
            assertEquals("new alex's exception", e.getMessage());
        }
    }

    public static class MyException extends Exception {
        public MyException(String msg){
            super(msg);
        }
    }
    @Test
    public void testThrowExp(){

        try {
            Throw.throwMe(MyException.class,true,"I am %s",MyException.class.getSimpleName());
        } catch (MyException e) {
            assertNotNull(e);
            assertEquals("I am MyException",e.getMessage());
        }
    }

}
