package io.dindinw.cmdline;

import static io.dindinw.lang.Check.checkArg;
import static io.dindinw.lang.Check.checkState;
import static io.dindinw.lang.Throw.newException;
import static io.dindinw.lang.Throw.throwException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.dindinw.lang.Throw;

/**
 * Created by alex on 3/10/15.
 */
public final class Parser {


    List<Option> optionList = new ArrayList<>();
    /*
     Note: Argument String in the format like :
       -o
       --option
       -Dkey=value
       --CONFIGkey=value
     */
    private Option _findOptionByArgument(String argument){
        Option o = null;
        // if it is a property argument
        if (argument.matches(".*=.*")) {
            for(Option pOption :
                    filterOptionByType(this.optionList, Option.OptionType.PropertyOption)) {
                if (argument.matches(pOption.name + ".*=.*")
                        || argument.matches(pOption.longName + ".*=.*")){
                    o = pOption;
                    break;
                }
            }
        }else{
            o = findOptionByName(optionList,argument);
        }
        return o;
    }
    public CmdLine parse(String[] args) throws Exception{
        CmdLine cmd = new CmdLine();
        for (int index = 0 ; index < args.length; index++){
            if (_isOption(args[index])){

                // check if the Option has been defined
                Option o = _findOptionByArgument(args[index]);
                throwException(o == null, "Parse Error: Unknown Option : [%s]", args[index]);

                // only property option allow to be seen multiple times in args[]
                if (!Option.OptionType.PropertyOption.equals(o.optionType) && cmd.hasOption(args[index])){
                    throwException("Parse Error: Duplicated Option : [%s] in %s",args[index],Arrays.asList(args));
                }
                cmd.addOption(o); //the command line has the option

                // Parse option in case of its type
                switch (o.optionType) {
                    case SimpleOption:
                        //no more handing need, break directly
                        break;
                    case ArgumentOption:
                        //need to parse the value
                        for (int i=index+1; i<=index+o.numberOfArgs; i++ ){
                            //OutOfIndex
                            checkState(i>=args.length,
                                    "Parse error: Incorrect numberOfArgs: [%s]. Option : [%s], index=%s in %s.",
                                    o.numberOfArgs,args[index],index,Arrays.asList(args));
                            //NotValue
                            checkState(_isOption(args[i]),
                                    "Parse error: Incorrect value : [%s]. Option : [%s], index=%s in %s",
                                    args[i],args[index],index,Arrays.asList(args));
                            cmd.addOptionValue(o.name,args[i]);
                        }
                        //need to change lastIndex
                        index = index+o.numberOfArgs;
                        break;
                    case PropertyOption:
                        //need to parse Property
                        String[] pair  = args[index].split("=");
                        checkState(pair.length!=2,
                                "Parse error : Incorrect property option argument [%s] ",args[index]);
                        String optName = o.name;
                        if(args[index].startsWith("--")){
                            optName = o.longName;
                        }
                        String key = pair[0].replaceFirst(optName,"");
                        String value = pair [1];
                        cmd.addOptionValue(optName,key);
                        cmd.addOptionValue(optName,value);
                        break;
                }
            }else{
                //double check it, the Option parse might change the index value.
                checkState(_isOption(args[index]),"The [%s] in %s should not a Option",args[index],Arrays.asList(args));
                cmd.addArg(args[index]);
            }
        }
        return cmd;
    }

    private boolean _isOption(String arg){
       return arg.startsWith("-");
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


