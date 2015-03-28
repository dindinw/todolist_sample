package io.dindinw.cmdline;

import static io.dindinw.lang.Check.checkNotNull;
import static io.dindinw.lang.Check.checkState;

import java.util.ArrayList;
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
     */
    CmdLine(){
        this.optionList = new ArrayList<>();
        this.optionValues = new HashMap<>();
    }
    /*
     use only by Parser in package level
     add option, if input token is a valid option recognized by parser.
     */
    void addOption(Option o){
        checkNotNull(o);
        optionList.add(o);
        //also need to initialize value list if null
        if (o.hasArg&&null==optionValues.get(o.name)){
            optionValues.put(o.name, new ArrayList<String>());
        }

    }
    /*
     use only by Parser in package level
     add as argument, if input token is recognized as a argument by parser.
    */
    void addArg(String arg){
        argList.add(arg);
    }
    /*
     use only by Parser in package level
     add as a value of a option, if a input token is recognized by parser.
    */
    void addOptionValue(String optionName,String optionValue){
        Option o = _findOptionByName(optionName);
        if (o!=null&&o.hasArg){
             optionValues.get(o.name).add(optionValue);
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
    public String[] optionValuesOf(String optionName) {
        Option o = _findOptionByName(optionName);
        if (o!=null){
            if (optionValues.containsKey(o.name)){
                List<String> optValues = optionValues.get(o.name);
                return optValues.toArray(new String[optValues.size()]);
            }
        }
        return new String[]{};
    }

    /**
     * Get Java Properties by a Java Property Option like (-Dkey=value)
     * @param optionName
     * @return
     */
    public Properties optionPropertiesOf(String optionName) {
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
     * The easier version of {@link #optionValuesOf(String)}
     * <p>
     * The method will return the first element in the option's value list.
     * </p>
     * @param optionName
     * @return the first value of existed, otherwise null
     */
    public String optionValueOf(String optionName) {
        String[] values = optionValuesOf(optionName);
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

    public Option[] getOptions(){
        return optionList.toArray(new Option[optionList.size()]);
    }

}
