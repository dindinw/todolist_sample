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
            // like [-option,foo1,foo2]
            if (_isOption(f_args[0])){
                parseOption(f_args, cmd);
            }
            // like [foo], it just a argument
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
       return arg.startsWith("-");
    }

    /*
    private boolean _isArgOption(String arg){
       return _isOption(arg)
               &&findOptionByOptionTypeAndName(this.optionList, Option.OptionType.ArgumentOption,arg)==null;
    }
    private boolean _isPropertiesOption(String arg){
       return _isOption(arg)
               &&findOptionByOptionTypeAndName(this.optionList, Option.OptionType.PropertyOption,arg)==null;
    }
    */
    /*
        fattening args like:
        [-o,foo1,foo2]
        [--option,foo1,foo2]
        [-Dkey1=value1] : how to parse this?
           1. check if contains '='
           2. check if OptionList has properties Option.
           3. check if match pattern like -<optionName>.*=.* or --<optionLongName>.*=.*
           4. then parse the string token
     */
    private void parseOption(String[] args,CmdLine cmd) throws Exception {
        //when args[0] like "-Dkey1=value1", go to parse propertyOption
        if (args.length == 1 && args[0].matches(".*=.*")) {
            parsePropertyOption(args[0], cmd);
        }
        //Parse ArgumentOption
        else {
            Option o = findOptionByName(this.optionList, args[0]);
            if (o != null) {
                // we got multiple option values (args.length > 1) and we find a defined option.
                // but we don't define it as a option to take values, instead, it is a simpleOption
                // which should take no value.
                if (args.length > 1 && o.optionType == Option.OptionType.SimpleOption) {
                    throw new Exception(String.format(
                            "SimpleOption: %s can't take values %s ", o, Arrays.asList(args)));
                }
                for (int i = 1; i < args.length; i++) {
                    cmd.addOptionValue(args[0], args[i]);
                }
            } else {
                throw new Exception(String.format(
                        "option '%s' not defined.", args[0]));
            }
        }
    }

    private void parsePropertyOption(String arg, CmdLine cmd){
        for(Option o : filterOptionByType(this.optionList, Option.OptionType.PropertyOption)){
            String opt = null;
            if(arg.matches(o.name+".*=.*")){ //-Dkey1=value1
               opt = o.name;
            }else if (arg.matches(o.longName+".*=.*")){ //--Configkey1=value2
               opt = o.longName;
            }
            //matched
            if (opt!=null){
                String[] pair  = arg.split("=");
                String key = pair[0].replaceFirst(opt,"");
                String value = pair [1];
                cmd.addOptionValue(opt,key);
                cmd.addOptionValue(opt,value);
                //checkArg(true,"go to parse property [%s] -> [%s],[%s] ",arg,key,value);
            }
        }
    }

    public void addOption(Option option){
        Option optByName = findOptionByName(this.optionList,option.name);
        Option optByLongName = findOptionByName(this.optionList,option.longName);
        checkArg(optByName!=null,"Option : '%s' already exists in %s ",option.name,optByName);
        checkArg(optByLongName!=null,"Option : '%s' already exists in %s",option.longName,optByLongName);
        optionList.add(option);
    }

    /*
       The internal logic of OptionList need to might sure one and only one can be found,
       otherwise return null;
     */
    static Option findOptionByName(List<Option> optionList, String optionName) {
        for (Option o : optionList){
            if (optionName.equals(o.name) || optionName.equals(o.longName)){
                return o;
            }
        }
        return null;
    }
    static List<Option> filterOptionByType(List<Option> optionList, Option.OptionType type){
        List<Option> newList = new ArrayList<>(optionList.size());
        for (Option o : optionList){
            if (o.optionType == type) newList.add(o);
        }
        return newList;
    }
    /*
    static Option findOptionByOptionTypeAndName(List<Option> optionList,Option.OptionType optType, String optionName){
        Option o = findOptionByName(optionList,optionName);
        return (o!=null&&o.optionType==optType) ? o : null;
    }
    */
}


