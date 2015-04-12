package test.io.dindinw.lang;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

/**
 * The test for Java Object serialization by using {@code Serializable} interface. Encoding
 * an object as a byte stream is known as serializing the object. the reverse operation is
 * de-serializing. Once an object has been serialized, it can be transmit to another JVM.
 * In this way. remote object can be called. it's the way how RMI pass objects between
 * JVMs. <p>
 *
 * It's required to implement {@code Serializable} interface to enable the serializability
 * of a class although the interface has no methods or fields. It serves only to identify
 * the semantics of which is being serializable. <p>
 *
 * http://docs.oracle.com/javase/8/docs/platform/serialization/spec/serial-arch.html#a4539
 * <p>
 * A Serializable class must do the following:
 *   Implement the java.io.Serializable interface
 *   Identify the fields that should be serializable
 *      - Use the serialPersistentFields member to explicitly declare them serializable
 *      - or use the transient keyword to denote nonserializable fields.
 *   Have access to the no-arg constructor of its first nonserializable superclass
 */
public class SerializeTest {


    /**
     * http://docs.oracle.com/javase/7/docs/platform/serialization/spec/serial-arch.html#4539
     * <p/>
     * A Serializable class must do the following
     * * Implement the java.io.Serializable interface
     *
     * @throws IOException
     */
    @Test
    public void testNotSerializableException() throws IOException {
        Person0 p = new Person0("alex", 35);
        FileOutputStream fos = new FileOutputStream("alex.out");
        try (ObjectOutputStream oos = new ObjectOutputStream(fos);) {
            oos.writeObject(p);
            fail();
        } catch (NotSerializableException e) {
            assertEquals("test.io.dindinw.lang.Person0", e.getMessage());
        }
    }

    /**
     * http://docs.oracle.com/javase/7/docs/platform/serialization/spec/serial-arch.html#4539
     * A Serializable class must do the following:
     * * Have access to the no-arg constructor of its first nonserializable superclass
     * It's ok to write. but not ok for read!
     */
    @Test
    public void testNotSerializableException2() throws IOException, ClassNotFoundException {
        PersonExt0 pExt0 = new PersonExt0("alex", 35);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("alex.out"));) {
            oos.writeObject(pExt0);
            oos.flush();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("alex.out"))) {
            PersonExt0 newExt0 = (PersonExt0) ois.readObject();
            fail();
        } catch (java.io.InvalidClassException e) {
            /**
             * the super class is not serializable, and the no-arg constructor is not provided
             */
            assertEquals("test.io.dindinw.lang.PersonExt0; no valid constructor",e.getMessage());
        }


        PersonExt1 pExt1 = new PersonExt1("alex", 35);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("alex.out"));) {
            oos.writeObject(pExt1);
            oos.flush();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("alex.out"))) {
            PersonExt1 newExt1 = (PersonExt1) ois.readObject();
            fail();
        } catch (java.io.InvalidClassException e) {
            /**
             * the super is noserializable, and no-arg constructor is private
             */
            assertEquals("test.io.dindinw.lang.PersonExt1; no valid constructor",e.getMessage());
        }


        PersonExt2 pExt2 = new PersonExt2("alex", 35);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("alex.out"));) {
            oos.writeObject(pExt2);
            oos.flush();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("alex.out"))) {
            PersonExt2 newExt2 = (PersonExt2) ois.readObject();
            /**
             * The no-serializable super is only initialized by no-arg constructor.
             * It makes sense. because if the class's state need to be serialized, it should be declared as
             * serializable. otherwise. the object is not required to be initialized by using deserialization
             */
            assertFalse("alex".equals(newExt2.name));
            assertFalse(35==newExt2.age);
            assertEquals("",newExt2.name);
            assertEquals(new Integer(0),newExt2.age);
        }
    }

    @Test
    public void testManSerialize() throws Exception {
        Man m = new Man("alex", 35);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("alex.out"))) {
            oos.writeObject(m);
            oos.flush();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader("alex.out"))) {
            String line = reader.readLine();
            while (line != null) {
                System.out.println(line);
                line = reader.readLine();
            }
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("alex.out"))){
            Man newMan = (Man)ois.readObject();
            assertEquals("alex",newMan.name);
            assertEquals(new Integer(35),newMan.age);
        }
    }

}


/**
 * Person class with no no-args constructor
 */
class Person0 {
    public final Integer age;
    public final String name;

    Person0(String name, Integer age) {
        this.name = name;
        this.age = age;
    }
}

/**
 * Person class with private no-args constructor
 */
class Person1 {
    public final Integer age;
    public final String name;

    Person1(String name, Integer age) {
        this.name = name;
        this.age = age;
    }
    private Person1() {
        this("", 0);
    }
}
/**
 * Person class with accessible no-args constructor
 */
class Person2 {
    public final Integer age;
    public final String name;

    Person2(String name, Integer age) {
        this.name = name;
        this.age = age;
    }
    Person2(){
        this("",0);
    }
}

class PersonExt0 extends Person0 implements Serializable {
    PersonExt0(String name, Integer age) {
        super(name, age);
    }
}

class PersonExt1 extends Person1 implements Serializable {
    PersonExt1(String name, Integer age) {
        super(name, age);
    }
}

class PersonExt2 extends Person2 implements Serializable {
    PersonExt2(String name, Integer age) {
        super(name, age);
    }
}
class Man implements Serializable {
    public final Integer age;
    public final String name;

    Man(String name, Integer age) {
        this.name = name;
        this.age = age;
    }
}
