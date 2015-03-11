package io.dindinw.cmdline;

import static io.dindinw.lang.Check.checkArg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by alex on 3/10/15.
 */
public final class Parser {

    Map<String,Option> optionNameToOptionMap = new HashMap<>();

    public CmdLine parse(String[] args) throws Exception{
        CmdLine cmd = new CmdLine(Arrays.asList(
                optionNameToOptionMap.values().toArray(new Option[optionNameToOptionMap.size()])));
        List<String[]> argsList = argsToFlatteningList(args);
        for( String[] f_args : argsList){
            // like [-o,foo1,foo2]
            if (f_args[0].startsWith("-")){
                f_args[0].replaceFirst("-","");
                parseOption(f_args, cmd);
            }
            // like [--option,foo1,foo2]
            else if (f_args[0].startsWith("--")){
                f_args[0].replaceFirst("--","");
                parseOption(f_args, cmd);
            }
            // like [foo]
            else{
                checkArg(f_args.length != 1, "fattening array size should alway =1 when work as normal argument");
                cmd.addArg(f_args[0]);
            }
        }
        return cmd;
    }

    private List<String[]> argsToFlatteningList(String[] args) {
        List<String[]> flatteningList = new ArrayList();
        for (int i = 0; i < args.length; i++) {
            if (_isOption(args[i])){
                for (int endIndex = i; endIndex<args.length-1; endIndex++){
                   if (_isOption(args[endIndex+1])){
                        flatteningList.add(i,Arrays.copyOfRange(args,i,endIndex));
                   }
                }
            }else{
                flatteningList.add(i,new String[]{args[i]});
            }
        }
        return flatteningList;
    }
    private boolean _isOption(String arg){
       return arg.startsWith("-") || arg.startsWith("--");
    }
    /*
        fattening args like:
        [o,foo1,foo2]
        [option,foo1,foo2]
     */
    private void parseOption(String[] args,CmdLine cmd) throws Exception {
        if (optionNameToOptionMap.containsKey(args[0])){
            for (int i=1; i<args.length; i++) {
                cmd.addOptionValue(args[0], args[i]);
            }
        }
    }

    public void addOption(Option option){
        checkArg(optionNameToOptionMap.containsKey(option.name),"Option : '%s' already exists",option.name);
        optionNameToOptionMap.put(option.name, option);
    }
}


