package io.dindinw.test;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Test;


public class StaticFactoryTest {
    public static class Foo {
        public final CountDownLatch latch = new CountDownLatch(1);
        private int foo_id;
        Foo(){
            new Thread(){
                public void run(){
                    //I am Evil.
                    try {
                        TimeUnit.MILLISECONDS.sleep(100);
                        foo_id=47;
                        latch.countDown();
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }.start();
            try {
                latch.await(50, TimeUnit.MILLISECONDS);
                foo_id=31;
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        Foo(int id){
            this.foo_id=id;
        }
    }
    
    public static synchronized Foo createFoo(){
        final Foo foo = new Foo();
        assertEquals(foo.foo_id,31);
        try {
            foo.latch.await();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        assertEquals(foo.foo_id,47);
        return foo;
    }
    
    public static Foo createFoo2(){
        final Foo foo = new Foo();
        assertEquals(foo.foo_id,31);
        return foo;
    }
    
    public static Foo createFoo3(){
        final Foo foo = new Foo(47);
        assertEquals(foo.foo_id,47);
        return foo;
    }
    
    public static synchronized Foo createFoo4(){
        final Foo foo = new Foo();
        assertEquals(foo.foo_id,31);
        return foo;
    }
    
    
    @Test
    public void test1(){
        StaticFactoryTest.createFoo();
    }
    
    @Test
    public void test2(){
        StaticFactoryTest.createFoo2();
    }
    
    @Test
    public void test3(){
        StaticFactoryTest.createFoo3();
    }
    
    @Test
    public void test4(){
        Foo foo2 = StaticFactoryTest.createFoo2();
        Foo foo4 = null ;
        try {
            foo4 = StaticFactoryTest.createFoo4();
        } finally {
            
        }
    }

}
