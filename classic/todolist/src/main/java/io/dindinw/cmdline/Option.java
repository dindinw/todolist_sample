package io.dindinw.cmdline;

import static io.dindinw.lang.Check.checkArg;
import static io.dindinw.lang.Check.checkNotEmpty;
import static io.dindinw.lang.Check.getChecker;

import io.dindinw.lang.Check;

/**
 * Created by alex on 3/9/15.
 */

public class Option {

    /** after initialized they are not changeable */
    public final int numberOfArgs;
    public final boolean hasArg;
    public final boolean isRequired;
    public final OptionType optionType;
    public final String name;
    public final String longName;
    public final String description;

    /**
     * Create "-debug" option
     * Option option = new Option("debug","print debug information")
     *
     * @param name
     * @param description
     */
    public Option(String name, String description){
        this(name,null,description);
    }
    public Option(String name, String longName, String description) {
        this(name,longName,description,OptionType.BooleanOption,true,false,0);
    }
    private Option(String name,String longName,String description,OptionType type,boolean isRequired, boolean hasArg, int numberOfArgs){
        //TODO Check Args
        this.name=name;
        this.longName=longName;
        this.description=description;
        this.optionType=type;
        this.hasArg=hasArg;
        this.isRequired=isRequired;
        this.numberOfArgs=numberOfArgs;
    }
    private Option(final ArgumentOptionBuilder builder){
        this(builder._name,builder._longName,builder._desc,OptionType.ArgumentOption,builder._isRequired,true, builder._numberOfArgs);
    }
    private Option(final PropertyOptionBuilder builder){
        this(builder._name,builder._longName,builder._desc,OptionType.PropertyOption,builder._isRequired,true, 1);
    }
    private Option(final BooleanOptionBuilder builder) {
        this(builder._name,builder._longName);
    }

    public static ArgumentOptionBuilder argOption(){
        return new ArgumentOptionBuilder();
    }
    public static PropertyOptionBuilder propertyOption(){
        return new PropertyOptionBuilder();
    }

    public static abstract class OptionBuilder {
        protected String _name;
        protected String _longName;
        protected String _desc;
        public OptionBuilder longName(String longName){
            checkNotEmpty(longName, "Option long name should not be empty");
            this._longName = longName;
            return this;
        }
        public OptionBuilder withDesc(String description){
            this._desc = description;
            return this;
        }
        public OptionBuilder name(String name) {
            checkNotEmpty(name, "Option name should not be empty");
            //checkArg(getChecker(Check.StringChecker.class, name).isLetter(),"Option name should be a letter char");
            this._name=name;
            return this;
        }
        abstract public Option build();
    }
    public static class ArgumentOptionBuilder extends OptionBuilder {
        public boolean _isRequired;
        public int _numberOfArgs;
        @Override
        public Option build() {
            return new Option(this);
        }
    }
    public static class PropertyOptionBuilder extends OptionBuilder {
        public boolean _isRequired;
        @Override
        public Option build() {
            return new Option(this);
        }
    }
    public static class BooleanOptionBuilder extends OptionBuilder {
        @Override
        public Option build() {
            return new Option(this);
        }
    }
    enum OptionType{
       BooleanOption,
       ArgumentOption,
       PropertyOption
    }
}


