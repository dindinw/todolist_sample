package io.dindinw.cmdline.tool;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import io.dindinw.cmdline.CmdLine;
import io.dindinw.cmdline.Option;
import io.dindinw.cmdline.Parser;
import io.dindinw.util.SysUtil;

/**
 * A Command line tool to print date/time by using Java's DataFormat syntax
 * Note: The tool use the JAVA 8 new Date API
 * Created by alex on 3/15/15.
 */
public class JDate {
    private static String DEFAULT_DATE_FORMATTING = "yyyy/LL/dd";

    public static void main(String[] args) throws Exception {
        Parser parser = new Parser();
        parser.addOption(Option.argOption()
                .name("-F").longName("--format").withDesc("the format string")
                .build());
        parser.addOption(Option.argOption()
                .name("-L").longName("--local").withDesc("the local string")
                .build());
        CmdLine cmd = parser.parse(args);
        String format = null;
        String local = null;
        if (cmd.hasOption("-F")){
            format = cmd.optionValueOf("-F");
        }else{
            if (cmd.getArgs().length==0){
                format = DEFAULT_DATE_FORMATTING;
            }
            else if (cmd.getArgs().length==1){
                format = cmd.getArgs()[0];
            }else{
                System.out.printf("Usage: JDate [[-F|--format] <format-string>]");
                parser.printOptions(System.out);
                System.exit(1);
            }
        }

        DateTimeFormatter df = null;
        if (cmd.hasOption("-L")){
            local = cmd.optionValueOf("-L");
            String[] lang_Country = local.split("_");
            df = DateTimeFormatter.ofPattern(format,new Locale(lang_Country[0],lang_Country[1]));
        }else{
            df = DateTimeFormatter.ofPattern(format);

        }
        System.out.println(df.format(LocalDateTime.now()));
    }
}
