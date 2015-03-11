package test.io.dindinw.cmdline;

import io.dindinw.cmdline.Option;
import io.dindinw.cmdline.Parser;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Created by alex on 3/10/15.
 */
public class ParserTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testErrorWhenAddSameNameOption(){
        Parser parser = new Parser();

        parser.addOption(
                Option.argOption().name("a").longName("all").withDesc("print all information").build()
        );
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Option : 'a' already exists");
        parser.addOption(
            Option.argOption().name("a").longName("print-all").withDesc("print all information").build()
        );
    }
}
