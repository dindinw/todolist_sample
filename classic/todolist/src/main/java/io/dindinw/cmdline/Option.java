package io.dindinw.cmdline;

import static io.dindinw.lang.Check.checkArg;
import static io.dindinw.lang.Check.checkNotEmpty;
import static io.dindinw.lang.Check.checkNotNull;
import static io.dindinw.lang.Check.getChecker;

import io.dindinw.lang.Check;

/**
 * <p>
       <li>arguments. like 'mvn clean install', the 'clean' and 'install' is not the option, it just the arguments</li>
 *     <li>options </li>
 *     <ul>
 *         <li>simple Option  like -debug                 print debugging information</li>
 *         <li>properties option like -D<property>=<value>   use value for given property</li>
 *         <li>argument Option. like -logfile <file>        use given file for log</li>
 *     </ul>
 * </p>
 * <p>
 *    Inspired by Apache Common-CLI @{link http://commons.apache.org/proper/commons-cli/}
 * </p>
 * Created by alex on 3/9/15.
 */

public class Option implements Comparable<Option> {

    /** after initialized they are not changeable */
    public final int numberOfArgs;
    public final boolean hasArg;
    public final boolean isRequired;
    public final OptionType optionType;
    public final String name;
    public final String longName;
    public final String description;

    @Override
    public String toString() {
        return "Option{" +
                " name='" + name + '\'' +
                ", longName='" + longName + '\'' +
                ", description='" + description + '\'' +
                ", optionType=" + optionType +
                ", numberOfArgs=" + numberOfArgs +
                ", hasArg=" + hasArg +
                ", isRequired=" + isRequired +
                '}';
    }


    /**
     * Create "-debug" option
     * Option option = new Option("-debug","print debug information")
     *
     * @param name starts with '-'
     * @param description
     */
    public Option(String name, String description){
       this(name, "-"+name, description);;
    }

    /**
     * Create "-d --debug" option
     *
     * Option option = new Option("-d","--debug","print debug information")
     * @param name starts with '-'
     * @param longName starts with '--'
     * @param description
     */
    public Option(String name, String longName, String description) {
       this(name,longName,description,OptionType.SimpleOption,false,false,0);
    }

    private Option(String name,String longName,String description,OptionType type,boolean isRequired, boolean hasArg, int numberOfArgs){
        // both null
        checkArg(name==null&&longName==null,"'name' and 'longName' both null");
        // name is null,
        if(name==null) name=longName.replaceFirst("--","-");
        // longName is null
        if(longName==null) longName=name.replaceFirst("-","--");

        checkNotEmpty(name, "'name' should not be empty");
        checkNotEmpty(longName, "'longName' should not be empty");

        checkArg(!name.startsWith("-")
                        ||name.startsWith("--")
                        ||!longName.startsWith("--"),
                "Failed to create Option, 'name' should start with '-' and 'longName' should start with '--'. [name=%s,longName=%s]",name,longName);

        this.name=name;
        this.longName=longName;
        this.description=description;
        this.optionType=type;
        this.hasArg=hasArg;
        this.isRequired=isRequired;
        this.numberOfArgs = numberOfArgs;
    }

    private Option(final ArgumentOptionBuilder builder) {
        this(builder._name, builder._longName,builder._desc,OptionType.ArgumentOption,builder._isRequired,true, builder._numberOfArgs);
    }
    private Option(final PropertyOptionBuilder builder){
        this(builder._name,builder._longName,builder._desc,OptionType.PropertyOption,builder._isRequired,true, 1);
    }
    private Option(final SimpleOptionBuilder builder) {
        this(builder._name,builder._longName,builder._desc);
    }

    public static ArgumentOptionBuilder argOption(){
        return new ArgumentOptionBuilder();
    }
    public static PropertyOptionBuilder propertyOption(){
        return new PropertyOptionBuilder();
    }
    public static SimpleOptionBuilder simpleOption(){
        return new SimpleOptionBuilder();
    }

    @Override
    public int compareTo(Option o) {
        int result = this.name.compareTo(o.name);
        return result == 0 ? this.longName.compareTo(o.longName):result;
    }


    public static abstract class OptionBuilder {
        protected String _name;
        protected String _longName;
        protected String _desc;
        protected boolean _isRequired;
        /** default is 0 */
        protected final int DEFAULT_NUMBER_OF_ARGS=0;
        protected int _numberOfArgs=DEFAULT_NUMBER_OF_ARGS;
        public OptionBuilder longName(String longName){
            this._longName = longName;
            return this;
        }
        public OptionBuilder withDesc(String description){
            this._desc = description;
            return this;
        }
        public OptionBuilder name(String name) {
            this._name=name;
            return this;
        }
        public OptionBuilder required(boolean isRequired){
            this._isRequired=isRequired;
            return this;
        }
        abstract public OptionBuilder setNumberOfArgs(int numberOfArgs);
        abstract public Option build() throws IllegalArgumentException;
    }
    public static class ArgumentOptionBuilder extends OptionBuilder {
        @Override
        public Option build() {
            if (_numberOfArgs==DEFAULT_NUMBER_OF_ARGS) _numberOfArgs=1;
            return new Option(this);
        }
        @Override
        public OptionBuilder setNumberOfArgs(int numberOfArgs){
            checkArg(numberOfArgs<=0,"numberOfArgs at least one");
            this._numberOfArgs=numberOfArgs;
            return this;
        }
    }
    public static class PropertyOptionBuilder extends OptionBuilder {
        @Override
        public OptionBuilder setNumberOfArgs(int numberOfArgs) {
            throw new UnsupportedOperationException("Option: 'numberOfArgs' is fixed to 1 for PropertyOption");
        }
        @Override
        public Option build() {
            return new Option(this);
        }
    }
    public static class SimpleOptionBuilder extends OptionBuilder {
        @Override
        public OptionBuilder setNumberOfArgs(int numberOfArgs) {
            throw new UnsupportedOperationException("Option: 'numberOfArgs' is fixed to 0 for SimpleOption");
        }
        @Override
        public Option build() { return new Option(this); }
    }
    public enum OptionType{
       SimpleOption,
       ArgumentOption,
       PropertyOption
    }
}


