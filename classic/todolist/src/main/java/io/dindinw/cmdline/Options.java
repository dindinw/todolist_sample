package io.dindinw.cmdline;

import static io.dindinw.lang.Check.checkNotEmpty;

import io.dindinw.lang.Check;

/**
 * The Utility Class for handing CommandLine Option.
 * <p>
 *     <ul>
 *         <li>
 *             fooTool -O --Option arg1 arg2
 *         </li>
 *     </ul>
 * </p>
 * Created by alex on 3/7/15.
 */
public final class Options {
    private Options() {};
    public Option create(){
       return new Option();
    }
}
class Option{
    private String _longOpt;
    private String _shortOpt;

    public Option longOpt(String longOpt){
        checkNotEmpty(longOpt, "longOpt should not be empty");
        this._longOpt = longOpt;
        return this;
    }

    public Option shortOpt(String shortOpt){
        this._shortOpt = shortOpt;
        return this;
    }
}
