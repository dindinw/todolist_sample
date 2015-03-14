package io.dindinw.cmdline.tool;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.dindinw.cmdline.CmdLine;
import io.dindinw.cmdline.Color;
import io.dindinw.cmdline.Option;
import io.dindinw.cmdline.Parser;

/**
 * A Java-version of unix tool 'grep'
 * Support options: -Hn --color
 * TODO:
 *   1. code need to refactor
 *   2. add more option
 * Created by alex on 3/13/15.
 */
public final class JGrep {

    private static boolean show_line_number = false;
    private static boolean show_file_name = false;
    private static boolean show_color = false;

    public static void main(String[] args) throws Exception{
        Parser parser = new Parser();
        parser.addOption(new Option("-n","--line-number","print the matched line number"));
        parser.addOption(new Option("-H","Always print filename headers with output lines."));
        parser.addOption(new Option(null,"--color","colorize the matching text"));
        CmdLine cmd = parser.parse(args);

        if (cmd.getArgs().length < 1) {
            System.err.println("Usage: JGrep pattern [file]|System.in "); System.exit(1);
            System.exit(1);
        }
        Pattern p= Pattern.compile(cmd.getArgs()[0]+"+");
        if (cmd.hasOption("-n")){
           show_line_number = true;
        }
        if (cmd.hasOption("-H")){
            show_file_name = true;
        }
        if (cmd.hasOption("--color")){
            show_color=true;
        }
        if (cmd.getArgs().length == 1) {
            process(p,"SYSTEM.IN");
        }else{
            for (int i=1; i<cmd.getArgs().length; i++)
                process(p, cmd.getArgs()[i]);
        }
    }

    static void process(Pattern pattern, String fileName) throws IOException {
        BufferedReader br = null;
        if ("SYSTEM.IN".equals(fileName)){
            br = new BufferedReader(new InputStreamReader(System.in));
        }else{
            br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
        }

        Matcher m = pattern.matcher("");
        String line = null;
        int lineNum = 0;
        String line_number = "";
        String file ="";
        if (show_file_name){
            file=fileName+":";
        }
        while ((line = br.readLine()) != null) {
            lineNum++;
            m.reset(line);
            StringBuffer colorized = new StringBuffer();
            boolean matched=false;
            while (m.find()) {
                matched = true;
                if (show_line_number) {
                    line_number = "" + lineNum+":";
                }
                if (show_color) {
                    m.appendReplacement(colorized, Color.ANSI_RED + m.group(0) + Color.ANSI_RESET);
                }
            }
            m.appendTail(colorized);

            if (matched) {
                String out;
                if (show_color){
                    out = colorized.toString();
                }else{
                    out = line;
                }
                System.out.printf("%s%s%s\n",file, line_number,out);
            }
        }

    }
}
