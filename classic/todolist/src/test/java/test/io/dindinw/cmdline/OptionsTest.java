package test.io.dindinw.cmdline;

import io.dindinw.cmdline.Option;
import io.dindinw.cmdline.Options;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Created by alex on 3/10/15.
 */
public class OptionsTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testErrorWhenAddSameNameOption(){
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
