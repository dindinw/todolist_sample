package io.dindinw.cmdline;

import static io.dindinw.lang.Check.checkArg;
import static io.dindinw.lang.Check.checkState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by alex on 3/10/15.
 */
public final class Parser {


    List<Option> optionList = new ArrayList<>();

    /**
     * Parse the input arguments array.
     * @param args
     * @return
     * @throws Exception
     */
    public CmdLine parse(String[] args) throws Exception{
        CmdLine cmd = new CmdLine(optionList);
        List<String[]> argsList = _argsToFlatteningList(args);
        for( String[] f_args : argsList){
            // like [--o,foo1,foo2]
            // like [-option,foo1,foo2]
            if (_isOption(f_args[0])){
                _parseOption(f_args, cmd);
            }
            // like [foo], it just a argument
            else{
                checkArg(f_args.length != 1, "fattening array size should always equal 1 when work as normal argument");
                cmd.addArg(f_args[0]);
            }
        }
        return cmd;
    }

    private boolean _isOption(String arg){
       return arg.startsWith("-");
    }

    /*
      The method convert the input args[] into the flatten list of args.
      Ex:
      [-i,inputfile,-o,outputfile,--debug] -> {[-i,inputfile],[-o,outputfile],[--debug]}
     */
    private List<String[]> _argsToFlatteningList(String[] args) {
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

    /*
        input fattening args like:
        [-o,foo1,foo2]
        [--option,foo1,foo2]
        [-Dkey1=value1]
     */
    private void _parseOption(String[] args, CmdLine cmd) throws Exception {
        //when args[0] like "-Dkey1=value1", go to parse propertyOption
        if (args.length == 1 && args[0].matches(".*=.*")) {
            _parsePropertyOption(args[0], cmd);
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
    /*
       [-Dkey1=value1] : parse property option
           1. check if contains '='
           2. check if OptionList has properties Option.
           3. check if match pattern like -<optionName>.*=.* or --<optionLongName>.*=.*
           4. then parse the string token
    */
    private void _parsePropertyOption(String arg, CmdLine cmd){
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
                checkState(pair.length!=2,"The property option [%s] can't parse into key->value pair correctly",arg);
                String key = pair[0].replaceFirst(opt,"");
                String value = pair [1];
                cmd.addOptionValue(opt,key);
                cmd.addOptionValue(opt,value);
                //TODO, do we need to do some logging ?
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

}


