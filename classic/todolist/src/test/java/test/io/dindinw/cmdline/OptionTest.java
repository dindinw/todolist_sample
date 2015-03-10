package test.io.dindinw.cmdline;

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

    @Test
    public void testSimpleOption(){
        Option opt = new Option("Debug","print debug information");
    }

    @Test
    public void testOptionBuilder(){
        assertTrue(Option.argOption() instanceof Option.OptionBuilder);
        assertTrue(Option.argOption() instanceof Option.ArgumentOptionBuilder);
        /** -a,-all          print all information*/
        Options options = new Options();

        options.add(
                Option.argOption().name("a").longName("all").withDesc("print all information").build()
        );
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Option : 'a' already exists");
        options.add(
            Option.argOption().name("a").longName("print-all").withDesc("print all information").build()
        );
    }
}
