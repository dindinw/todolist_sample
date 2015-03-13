package io.dindinw.cmdline;

import static io.dindinw.lang.Check.checkArg;
import static io.dindinw.lang.Check.checkNotNull;
import static io.dindinw.lang.Check.checkState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * All get methods return array since the results are immutable by nature
 */
public class CmdLine {

    /** empty list of arguments when created */
    private ArrayList<String> argList = new ArrayList<>();

    /** the option list load when CmdLine is created" */
    private final List<Option> optionList;

    /** optionName -> optionValues */
    private final Map<String,List<String>> optionValues;

    /**
     * only at package level, should not created by end-user
     * @param options
     */
    CmdLine(List<Option> options){
        this.optionList = options;
        this.optionValues = new HashMap<>();
        for (Option o : options){
            if (o.hasArg){
                optionValues.put(o.name,new ArrayList<String>());
            }
        }
    }

    /**
     * If the option can be found by using the optionName, the method will try to
     * check both of 'name' and 'longName' of the Option
     * @param optionName
     * @return
     */
    public boolean hasOption(String optionName) {
        checkNotNull(optionName);
        return (_findOptionByName(optionName)==null) ? false : true ;
    }
    private Option _findOptionByName(String optionName) {
        return Parser.findOptionByName(optionList,optionName);
    }

    /**
     * Get option values by option name, the input can be 'name' or 'longName' of the Option class.
     * @param optionName
     * @return
     */
    public String[] getOptionValues(String optionName) {
        Option o = _findOptionByName(optionName);
        if (o!=null){
            if (optionValues.containsKey(o.name)){
                List<String> optValues = optionValues.get(o.name);
                return optValues.toArray(new String[optValues.size()]);
            }
        }
        return new String[]{};
    }

    public Properties getOptionProperties(String optionName) {
        Properties properties = new Properties();
        Option o = _findOptionByName(optionName);
        if (o!=null&&o.optionType == Option.OptionType.PropertyOption){
            List<String> values = optionValues.get(o.name);
            // the option values array should always be even for property option
            checkState(values.size()%2==1,"properties array contains odd values %s. a Parser error is not found.",values);
            // add values to property pair by pair
            for (int i = 0; i<= values.size()/2; i= i+2  ) {
                properties.setProperty(values.get(i), values.get(i + 1));
            }
        }
        return properties;
    }
    /**
     * The easier version of {@link #getOptionValues(String)}
     * <p>
     * The method will return the first element in the option's value list.
     * </p>
     * @param optionName
     * @return the first value of existed, otherwise null
     */
    public String getOptionValue(String optionName) {
        String[] values = getOptionValues(optionName);
        return (values.length == 0) ? null : values[0];
    }

    /**
     * The args list of the command line:
     * mvn clean install means arg[1]=clean and arg[2]=install
     * @return
     */
    public String[] getArgs(){
        return argList.toArray(new String[argList.size()]);
    }

    void addArg(String arg){
        argList.add(arg);
    }

    void addOptionValue(String optionName,String optionValue){
        Option o = _findOptionByName(optionName);
        if (o!=null&&o.hasArg){
             optionValues.get(o.name).add(optionValue);
        }
    }


    /*might not need
    public Option[] getOptions(){
        return optionList.toArray(new Option[optionList.size()]);
    }
    */

}
