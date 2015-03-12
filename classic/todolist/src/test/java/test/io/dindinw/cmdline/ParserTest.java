package test.io.dindinw.cmdline;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.prefs.Preferences;

import io.dindinw.cmdline.CmdLine;
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
                Option.argOption().name("-a").longName("--all").withDesc("print all information").build()
        );
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("'-a' already exists in Option");
        parser.addOption(
            Option.argOption().name("-a").longName("--print-all").withDesc("print all information").build()
        );
    }

    @Test
    public void testParser() throws Exception {
        Parser parser = new Parser();
        parser.addOption(new Option("-a", "--all", "print all file"));
        parser.addOption(new Option("-d", "--Debug", "debug option"));
        CmdLine cmd = parser.parse(new String[]{"clean", "install", "-a", "--Debug", "foo1"});
        assertArrayEquals(new String[]{"clean", "install", "foo1"}, cmd.getArgs());
        assertTrue(cmd.hasOption("-a"));
        assertTrue(cmd.hasOption("--Debug"));

        parser = new Parser();
        parser.addOption(Option.argOption()
                .name("-i")
                .longName("--input")
                .withDesc("input file")
                .setNumberOfArgs(1)
                .build());
        parser.addOption(
                Option.argOption().name("-o")
                        .build());
        parser.addOption(Option.simpleOption().name("-d").longName("--Debug").build());

        cmd = parser.parse(new String[]{"-i","input","-o","output","--Debug"});
        assertArrayEquals(new String[]{}, cmd.getArgs());
        assertArrayEquals(new String[]{"input"},cmd.getOptionValues("-i"));
        assertEquals("input",cmd.getOptionValue("-i"));
    }
}
