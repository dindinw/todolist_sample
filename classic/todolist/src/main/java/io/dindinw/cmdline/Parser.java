package io.dindinw.cmdline;

import static io.dindinw.lang.Check.checkArg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by alex on 3/10/15.
 */
public final class Parser {


    List<Option> optionList = new ArrayList<>();

    public CmdLine parse(String[] args) throws Exception{
        CmdLine cmd = new CmdLine(optionList);
        List<String[]> argsList = argsToFlatteningList(args);
        /*try{*/
        for( String[] f_args : argsList){
            // like [--o,foo1,foo2]
            if (f_args[0].startsWith("--")){
                //f_args[0] = f_args[0].replaceFirst("--","");
                parseOption(f_args, cmd);
            }
            // like [-option,foo1,foo2]
            else if (f_args[0].startsWith("-")){
                //f_args[0] = f_args[0].replaceFirst("-","");
                parseOption(f_args, cmd);
            }
            // like [foo]
            else{
                checkArg(f_args.length != 1, "fattening array size should always equal 1 when work as normal argument");
                cmd.addArg(f_args[0]);
            }
        }
        /*}catch(Exception e){
            throw new Exception(String.format(
                    "Parse Error for command line args %s. %s",Arrays.asList(args),e.getMessage()));
        }
        */
        return cmd;
    }

    private List<String[]> argsToFlatteningList(String[] args) {
        List<String[]> flatteningList = new ArrayList<>();
        for (int i = 0; i < args.length; i++) {
            if (_isOption(args[i])){
                boolean lastOption=true;
                for (int endIndex = i; endIndex<args.length-1; endIndex++){
                   if (_isOption(args[endIndex+1])){
                       flatteningList.add(Arrays.copyOfRange(args,i,endIndex+1));
                       lastOption=false;
                       i=endIndex;
                       break;
                   }
                }
                if (lastOption){
                    flatteningList.add(new String[]{args[i]});
                }
            }else{
                flatteningList.add(new String[]{args[i]});
            }
        }
        return flatteningList;
    }
    private boolean _isOption(String arg){
       return arg.startsWith("--") || arg.startsWith("-");
    }
    /*
        fattening args like:
        [o,foo1,foo2]
        [option,foo1,foo2]
     */
    private void parseOption(String[] args,CmdLine cmd) throws Exception {
        Option o = findOptionByName(this.optionList,args[0]);
        if (o != null){
            if (args.length>1&&o.optionType== Option.OptionType.SimpleOption){
                throw new Exception(String.format(
                        "SimpleOption: %s can't take values %s ",o,Arrays.asList(args)));
            }
            for (int i=1; i<args.length; i++) {
                cmd.addOptionValue(args[0], args[i]);
            }
        }else{
            throw new Exception(String.format(
                    "option '%s' not defined.",args[0]));
        }
    }

    public void addOption(Option option){
        Option optByName = findOptionByName(this.optionList,option.name);
        Option optByLongName = findOptionByName(this.optionList,option.longName);
        checkArg(optByName!=null,"Option : '%s' already exists in %s ",option.name,optByName);
        checkArg(optByLongName!=null,"Option : '%s' already exists in %s",option.longName,optByLongName);
        optionList.add(option);
    }

    static Option findOptionByName(List<Option> optionList, String optionName) {
        for (Option o : optionList){
            if (optionName.equals(o.name) || optionName.equals(o.longName)){
                return o;
            }
        }
        return null;
    }
}


