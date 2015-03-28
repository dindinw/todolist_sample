package io.dindinw.cmdline.tool;

import static io.dindinw.util.SysUtil.err;

import io.dindinw.cmdline.CmdLine;
import io.dindinw.cmdline.Option;
import io.dindinw.cmdline.Parser;
import io.dindinw.lang.Check;

/**
 * The command line tool to concept with unix services tool
 * but work for java thread in commandline.
 * so that i can start/stop a service
 * Created by alex on 3/21/15.
 */
public class JServices {
    private enum Command{
        start("start","start service container"),
        shutdown("shutdown","stop service container");
        Command(String name,String desc){
            this.name =name;
            this.desc = desc;
        };
        public String name;
        public String desc;
    }
    public static void main(String[] args) throws Exception{
        Parser parser = new Parser();
        parser.addOption(new Option("-s","--start","start service container"));
        parser.addOption(new Option("-d","--shutdown","start service container"));
        parser.addOption(new Option("-l","--list","list all running service in the container"));
        parser.addOption(
                Option.argOption().name("-r").longName("--run").withDesc("run a service").build());
        parser.addOption(
                Option.argOption().name("-k").longName("--kill").withDesc("kill a service").build());
        CmdLine cmd = parser.parse(args);

        parser.printOptions(System.out);
        if (cmd.getArgs().length==1&&cmd.getOptions().length == 0){
            String command = cmd.getArgs()[0];
            if ("start".equals(command)){
                doStart();
            }else if ("shutdown".equals(command)){
                doShutdown();
            }else{
                //error
                err("Unknown Command: %s",command);
                parser.printOptions(System.out);
            }
        }
        else if (cmd.getArgs().length==0&&cmd.getOptions().length==1){
           if (cmd.hasOption("-l")) {
               //do list
           }else if (cmd.hasOption("-r")){
               //do run
               String serviceName = cmd.optionValueOf("-r");
           }else if (cmd.hasOption("-k")){
               //do kill
               String serviceName = cmd.optionValueOf("-k");
           }else{
               //error
           }
        }else{
            //error
        }
    }

    private static void doShutdown() {
        System.out.println("Stop Container");
    }

    private static void doStart() {
        System.out.println("Start Container");

    }

    private void doListService(String serviceName){
        System.out.println("Services :");
    }
    private void doStartService(String serviceName){

    }
    private void doKillService(String serviceName){

    }
}
