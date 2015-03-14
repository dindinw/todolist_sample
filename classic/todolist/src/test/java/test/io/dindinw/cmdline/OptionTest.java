package test.io.dindinw.cmdline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import io.dindinw.cmdline.Option;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Created by alex on 3/9/15.
 */
public class OptionTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testSimpleOption(){
        Option debug = new Option("-Debug","print debug information");
        assertEquals("-Debug",debug.name);
        assertEquals("--Debug",debug.longName);
        assertEquals(0,debug.numberOfArgs);
        assertEquals(false,debug.hasArg);
        assertEquals(false,debug.isRequired); //by default, a option should be not required
        assertEquals(Option.OptionType.SimpleOption,debug.optionType);
    }

    @Test
    public void testSimpleOption_illegalArgs_emptyName(){
        thrown.expect(IllegalArgumentException.class);
        Option debug = new Option("","print debug information");
    }
    @Test
    public void testSimpleOption_illegalArgs_nullName(){
        thrown.expect(IllegalArgumentException.class);
        Option debug = new Option(null,"print debug information");
    }
    @Test
    public void testSimpleOption_notValidName(){
        thrown.expect(IllegalArgumentException.class);
        Option foo = new Option("foo","foo information");
    }
    @Test
    public void testSimpleOption_notValidName2(){
        thrown.expect(IllegalArgumentException.class);
        Option foo = new Option("--foo","foo information");
    }
    @Test
    public void testSimpleOption_notValidName3(){
        thrown.expect(IllegalArgumentException.class);
        Option foo = new Option("-f","-foo","foo information");
    }
    @Test
    public void testSimpleOptionBuilder_illegalArgs_nullNull(){
        thrown.expect(IllegalArgumentException.class);
        // null, null
        Option nullNameAndLongName = Option.simpleOption().build();
    }
    @Test
    public void testSimpleOptionBuilder_illegalArgs_emptyNull(){
        thrown.expect(IllegalArgumentException.class);
        // empty,null
        Option emptyName = Option.simpleOption().name("").build();
    }
    @Test
    public void testSimpleOptionBuilder_illegalArgs_nullEmpty(){
        thrown.expect(IllegalArgumentException.class);
       // null,empty
        Option emptyLongName = Option.simpleOption().longName("").build();
    }
    @Test
    public void testSimpleOptionBuilder_illegalArgs_okEmpty(){
        thrown.expect(IllegalArgumentException.class);
       // ok, empty
        Option some = Option.simpleOption().name("-some").longName("").build();
    }
    @Test
    public void testSimpleOptionBuilder_setNumberOfArgs(){
        //can't set args for simple option
        thrown.expect(UnsupportedOperationException.class);
        Option.simpleOption().name("simple").setNumberOfArgs(1);
    }
    @Test
    public void testArgumentOptionBuilder(){
        assertTrue(Option.argOption() instanceof Option.OptionBuilder);
        assertTrue(Option.argOption() instanceof Option.ArgumentOptionBuilder);
        /** -a,-all          print all information*/
        Option all = Option.argOption().name("-a").longName("--all").withDesc("print all").build();
        assertEquals("-a",all.name);
        assertEquals("--all",all.longName);
        assertEquals("print all",all.description);
        assertEquals(Option.OptionType.ArgumentOption,all.optionType);
        assertEquals(true,all.hasArg);
        assertEquals(1, all.numberOfArgs);
    }

    @Test
    public void testArgumentOptionBuilder_setNumberOfArgs(){
        Option F = Option.argOption().setNumberOfArgs(2).name("-F").longName("--file").withDesc("print all").build();
        assertEquals(2,F.numberOfArgs);
        thrown.expect(IllegalArgumentException.class);
        Option.argOption().setNumberOfArgs(0).name("F").longName("file").withDesc("print all").build();
    }

    @Test
    public void testPropertyOptionBuilder_setNumberOfArgs(){
        Option D = Option.propertyOption().name("-D").build();
        //can't set args for property option
        thrown.expect(UnsupportedOperationException.class);
        Option.propertyOption().name("E").setNumberOfArgs(1);
    }

}
