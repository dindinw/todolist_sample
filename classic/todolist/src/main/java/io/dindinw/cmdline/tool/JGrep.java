package io.dindinw.cmdline.tool;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.dindinw.cmdline.CmdLine;
import io.dindinw.cmdline.Option;
import io.dindinw.cmdline.Parser;

/**
 * A Java-version of unix tool 'grep'
 * TODO:
 *   1. code need to refactor
 *   2. only support the -n option
 *   3. my option parser don't handle GNU getOpt() standard, so that I can't support the option like '-aHn'
 * Created by alex on 3/13/15.
 */
public final class JGrep {
    public static boolean show_line_number = false;
    public static void main(String[] args) throws Exception{
        Parser parser = new Parser();
        parser.addOption(new Option("-n","--line-number","print the matched line number"));
        CmdLine cmd = parser.parse(args);

        if (cmd.getArgs().length < 1) {
            System.err.println("Usage: JGrep pattern [file]|System.in "); System.exit(1);
            System.exit(1);
        }
        Pattern p= Pattern.compile(cmd.getArgs()[0]);
        if (cmd.hasOption("-n")){
            //System.out.printf("show line number \n");
           show_line_number = true;
        }
        if (cmd.getArgs().length == 1) {
            process(p,System.in);
        }else{
            for (int i=1; i<cmd.getArgs().length; i++)
                process(p, cmd.getArgs()[i]);
        }

    }
    static void process(Pattern pattern, InputStream input) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(input));

        Matcher m = pattern.matcher("");
        String line = null;
        int lineNum = 0;
        while ((line = br.readLine()) !=null){
            lineNum++;
            m.reset(line);
            if (m.find()){
                String line_number = "";
                if (show_line_number){
                    line_number = ""+lineNum+":";
                }
                System.out.printf("%s%s\n",line_number,line);
            }
        }
    }

    static void process(Pattern pattern, String fileName) throws IOException {
        // Get a FileChannel from the given file.
        process(pattern, new FileInputStream(fileName)); // Map the file's content

    }
}
