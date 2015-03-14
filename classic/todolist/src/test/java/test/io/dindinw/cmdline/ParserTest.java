package test.io.dindinw.cmdline;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Properties;

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
        assertArrayEquals(new String[]{"input","input2"},cmd.optionValuesOf("-i"));
        assertEquals("input", cmd.optionValueOf("-i"));
        assertArrayEquals(new String[]{"output"},cmd.optionValuesOf("-o"));
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
        assertArrayEquals(new String[]{"skipTests","true","build","myProfile"},cmd.optionValuesOf("-D"));
        Properties properties = cmd.optionPropertiesOf("-D");
        assertEquals(2, properties.stringPropertyNames().size());
        assertArrayEquals(new String[]{"skipTests","build"}, properties.stringPropertyNames().toArray(new String[2]));
    }

    @Test
    public void testArgumentOptionWithNumberOfArgs() throws Exception {
        Parser parser = new Parser();
        parser.addOption(Option.argOption().name("-i").setNumberOfArgs(2).build());
        CmdLine cmd = parser.parse(new String[]{"-i","input1","input2"});
        cmd.hasOption("-i");
        assertArrayEquals(new String[]{"input1","input2"},cmd.optionValuesOf("-i"));
    }

    @Test
    public void testOptionWithRequired() throws Exception {
        Parser parser = new Parser();
        Option input = Option.argOption().name("-i").required(true).build();
        Option output = Option.argOption().name("-o").build();
        parser.addOption(input);
        parser.addOption(output);

        assertTrue(input.isRequired);   //required need to be set
        assertFalse(output.isRequired); //default
        CmdLine cmd = parser.parse(new String[]{"-i","input","-o","output"});
        assertTrue(cmd.hasOption("-i"));
        assertTrue(cmd.hasOption("-o"));

        assertArrayEquals(new String[]{"input"}, cmd.optionValuesOf("-i"));
        assertArrayEquals(new String[]{"output"},cmd.optionValuesOf("-o"));

        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("required option [-i,");
        cmd = parser.parse(new String[]{"-o","output"});
    }

    @Test
    public void testPrintHelp() throws Exception {
        Parser parser = new Parser();
        Option input = Option.argOption()
                .name("-i").longName("--input").withDesc("input file")
                .required(true)
                .build();
        Option output = Option.argOption()
                .name("-o").longName("--output").withDesc("out file")
                .build();
        parser.addOption(input);
        parser.addOption(output);
        try {
            CmdLine cmd = parser.parse(new String[]{"-o", "output"});
        }catch(IllegalStateException e){
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            PrintStream testOut = new PrintStream(outContent);
            parser.printOptions(testOut);
            assertEquals("-i, --input \t input file\n-o, --output \t out file\n",outContent.toString());
        }
    }

    /**
     * For simple option(boolean option) should support clustering. ( multiple short-opt in a single argument)
     * prerequisites:
     * 1. input should only in short-opt names combination. long-opt names cannot be clustered
     * 2. only simple option,
     * @throws Exception
     */
    @Test
    public void testClusteredOptions() throws Exception{

        Parser parser = new Parser();
        Option a = new Option("-a", "--text","Treat all files as ASCII text.");
        Option n = new Option("-n", "--line-number","Show line number");
        Option H = new Option("-H","Always print filename headers with output lines.");
        Option color= new Option(null,"--color","colorize the matching text");
        parser.addOption(a);
        parser.addOption(n);
        parser.addOption(H);
        parser.addOption(color);

        CmdLine cmd = parser.parse(new String[]{"-aHn", "--color", "foo"});

        assertTrue(cmd.hasOption("-a"));
        assertTrue(cmd.hasOption("-H"));
        assertTrue(cmd.hasOption("-n"));
        assertTrue(cmd.hasOption("--color"));
        assertArrayEquals(new String[]{"foo"},cmd.getArgs());
        //Unrecognized option -aHnW
        thrown.expect(Exception.class);
        cmd = parser.parse(new String[]{"-aHnW", "--color", "foo"});

    }
}
