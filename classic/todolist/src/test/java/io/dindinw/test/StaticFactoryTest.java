package io.dindinw.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.After;
import org.junit.Test;

/**
 * To show how static factory need a protection with synchronized if any non-threadsafe 
 * field is involved.
 * @author yidwu
 *
 */
public class StaticFactoryTest {
    private static final int[] WAIT_TIMES = {0,10};
    private static final AtomicInteger nextInc = new AtomicInteger(0);
    private static final ThreadLocal<Foo> THREAD_LOCAL_FOO_CONTEXT = new ThreadLocal<Foo>();
    private static final List<Foo> NOMAL_LOCAL_FOO_CONTEXT = new ArrayList<Foo>();
    
    public static class Foo {
        private final int foo_id;
        private final int wait_time;
        private final long create_time;
        Foo(){
            create_time = System.currentTimeMillis();
            wait_time =WAIT_TIMES[new Random().nextInt(2)];
            try {
                TimeUnit.MILLISECONDS.sleep(wait_time);
            } catch (InterruptedException e) {   
                e.printStackTrace();
            }
            foo_id = nextInc.incrementAndGet();
        }

        public String toString(){
            return foo_id+"_"+wait_time+'_'+create_time+'_'+super.toString();
        }
    }
    
    public static synchronized Foo createFoo(){
        return createFoo2();
    }
    /**
     * Without the synchronized protection, the line :
     * NOMAL_LOCAL_FOO_CONTEXT.add(foo); it's not thread safe.
     * we can't guarantee that NOMAL_LOCAL_FOO_CONTEXT is ready to get() after add().
     * @return
     * @throws InterruptedException 
     */
    public static Foo createFoo2(){
        final Foo foo = new Foo();
        THREAD_LOCAL_FOO_CONTEXT.set(foo);
        if (foo.wait_time==0){
            // here , if we don't waste time on get id, now need to wait to add to List
            // to do this will increase the chance that id_1 -> list[1] and id_2 -> list[0]
            try {
                TimeUnit.MILLISECONDS.sleep(50);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        NOMAL_LOCAL_FOO_CONTEXT.add(foo);
        return foo;
    }
    
    @After
    public void clean(){
        nextInc.set(0);
        THREAD_LOCAL_FOO_CONTEXT.set(null);
        NOMAL_LOCAL_FOO_CONTEXT.clear();
    }
    
    @Test
    public void test1() throws InterruptedException{
        Thread t1 = new Thread(){
            public void run(){
                Foo foo = StaticFactoryTest.createFoo();
                System.out.println("test1:thrd:"+foo);
                assertNotNull(foo);
                switch (foo.foo_id) {
                case 1:
                    assertEquals(foo,NOMAL_LOCAL_FOO_CONTEXT.get(0));
                    break;
                case 2:
                    assertEquals(foo,NOMAL_LOCAL_FOO_CONTEXT.get(1));
                    break;
                default:
                    break;
                }
            }
        };
        t1.setName("test1_thread");
        t1.start();
        if (WAIT_TIMES[new Random().nextInt(2)]> 0){ 
            //thread first if executed, or main first without executed
            TimeUnit.MILLISECONDS.sleep(1); 
        }
        Foo foo = StaticFactoryTest.createFoo();
        System.out.println("test1:main:"+foo);
        assertNotNull(foo);
        assertEquals(foo,THREAD_LOCAL_FOO_CONTEXT.get());
        
        t1.join(); //t1 return
        
        switch (foo.foo_id) {
        case 1:
            assertEquals(foo,NOMAL_LOCAL_FOO_CONTEXT.get(0)); // still break, the unproected list may return a null
            break;
        case 2:
            assertEquals(foo,NOMAL_LOCAL_FOO_CONTEXT.get(1));
            break;
        default:
            break;
        }

        System.out.println("test1:main:"+NOMAL_LOCAL_FOO_CONTEXT.get(0));
        System.out.println("test1:main:"+NOMAL_LOCAL_FOO_CONTEXT.get(1));

    }
    
    @Test
    public void test2() throws InterruptedException{
        Thread t2 = new Thread(){
            public void run(){
                Foo foo = StaticFactoryTest.createFoo2();
                System.out.println("test2:thrd:"+foo);
                assertNotNull(foo);
                switch (foo.foo_id) {
                case 1:
                    assertEquals(foo,NOMAL_LOCAL_FOO_CONTEXT.get(0)); //broken!
                    break;
                case 2:
                    assertEquals(foo,NOMAL_LOCAL_FOO_CONTEXT.get(1));
                    break;
                default:
                    break;
                }
            }
        };
        t2.setName("test2_thread");
        t2.start();
        if (WAIT_TIMES[new Random().nextInt(2)]> 0){ 
            //thread first if executed, or main first without executed
            TimeUnit.MILLISECONDS.sleep(100); 
        }
        Foo foo2 = StaticFactoryTest.createFoo2();
        System.out.println("test2:main:"+foo2);
        assertNotNull(foo2);
        assertEquals(foo2,THREAD_LOCAL_FOO_CONTEXT.get());
        
        t2.join(); //t1 return
        assertEquals(2,NOMAL_LOCAL_FOO_CONTEXT.size()); //we CANNOT make sure that!
        
        switch (foo2.foo_id) {
        case 1:
            assertEquals(foo2,NOMAL_LOCAL_FOO_CONTEXT.get(0)); // still break, the unprotected list may return a null
            break;
        case 2:
            assertEquals(foo2,NOMAL_LOCAL_FOO_CONTEXT.get(1));
            break;
        default:
            break;
        }

        System.out.println("test2:main:"+NOMAL_LOCAL_FOO_CONTEXT.get(0));
        System.out.println("test2:main:"+NOMAL_LOCAL_FOO_CONTEXT.get(1));
    }
}
