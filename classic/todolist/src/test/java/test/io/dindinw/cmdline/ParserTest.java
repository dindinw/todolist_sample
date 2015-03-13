package test.io.dindinw.cmdline;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Properties;
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
                .setNumberOfArgs(2)
                .build());
        parser.addOption(
                Option.argOption().name("-o")
                        .longName("--output")
                        .build());
        parser.addOption(Option.simpleOption().name("-d").longName("--Debug").build());

        cmd = parser.parse(new String[]{"foo","foo2","-i","input","input2","-o","output","--Debug"});
        assertArrayEquals(new String[]{"foo","foo2"}, cmd.getArgs());
        assertArrayEquals(new String[]{"input","input2"},cmd.getOptionValues("-i"));
        assertEquals("input", cmd.getOptionValue("-i"));
        assertArrayEquals(new String[]{"output"},cmd.getOptionValues("-o"));
    }

    @Test
    public void testParseProperties() throws Exception{
        Parser parser = new Parser();
        parser = new Parser();
        parser.addOption(Option.propertyOption()
                .name("-D")
                .withDesc("Java properties option like -Dkey=value")
                .build());
        CmdLine cmd = parser.parse(new String[]{"mvn","clean","install","-DskipTests=true","-Dbuild=myProfile"});
        assertArrayEquals(new String[]{"mvn","clean","install"}, cmd.getArgs());
        assertArrayEquals(new String[]{"skipTests","true","build","myProfile"},cmd.getOptionValues("-D"));
        Properties properties = cmd.getOptionProperties("-D");
        assertEquals(2, properties.stringPropertyNames().size());
        assertArrayEquals(new String[]{"skipTests","build"}, properties.stringPropertyNames().toArray(new String[2]));
    }

    @Test
    public void testArgumentOptionWithNumberOfArgs() throws Exception {
        Parser parser = new Parser();
        parser.addOption(Option.argOption().name("-i").setNumberOfArgs(2).build());
        CmdLine cmd = parser.parse(new String[]{"-i","input1","input2"});
        cmd.hasOption("-i");
        assertArrayEquals(new String[]{"input1","input2"},cmd.getOptionValues("-i"));
    }

    @Test
    public void testOptionWithRequired() throws Exception {
        Parser parser = new Parser();
        Option input = Option.argOption().name("-i").build();
        Option output = Option.argOption().name("-o").required(false).build();
        parser.addOption(input);
        parser.addOption(output);

        assertTrue(input.isRequired);
        assertFalse(output.isRequired);
        CmdLine cmd = parser.parse(new String[]{"-i","input","-o","output"});
        assertTrue(cmd.hasOption("-i"));
        assertTrue(cmd.hasOption("-o"));

        assertArrayEquals(new String[]{"input"}, cmd.getOptionValues("-i"));
        assertArrayEquals(new String[]{"output"},cmd.getOptionValues("-o"));

        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("required option [-i,");
        cmd = parser.parse(new String[]{"-o","output"});

    }
}
