package test.io.dindinw.cmdline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import io.dindinw.cmdline.Option;
import io.dindinw.cmdline.Options;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Created by alex on 3/9/15.
 */
public class OptionTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    public void testSimpleOption(){
        Option debug = new Option("Debug","print debug information");
        assertEquals("the default option is simple option",debug.optionType, Option.OptionType.SimpleOption);
    }

    @Test
    public void testSimpleOptionBuilder(){
        thrown.expect(IllegalArgumentException.class);
        Option empty = Option.simpleOption().build();
    }

    @Test
    public void testArgumentOptionBuilder(){
        assertTrue(Option.argOption() instanceof Option.OptionBuilder);
        assertTrue(Option.argOption() instanceof Option.ArgumentOptionBuilder);
        /** -a,-all          print all information*/
        Option all = Option.argOption().name("a").longName("all").withDesc("print all").build();
        assertEquals("a",all.name);
        assertEquals("all",all.longName);
        assertEquals("print all",all.description);
        assertTrue("Argument Option always has arg", all.hasArg);
        assertEquals("The default number of args is 1", 1, all.numberOfArgs);
    }
}
