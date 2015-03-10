package io.dindinw.cmdline;

import static io.dindinw.lang.Check.checkArg;
import static io.dindinw.lang.Check.checkNotEmpty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.dindinw.lang.Check;

/**
 * The Utility Class for handing CommandLine Option.
 * <p>
 *     <ul>
 *         <li>boolean Option  like -debug                 print debugging information</li>
 *         <li>properties option like -D<property>=<value>   use value for given property</li>
 *         <li>argument Option. like -logfile <file>        use given file for log</li>
 *     </ul>
 * </p>
 * <p>
 *    Inspired by Apache Common-CLI @{link http://commons.apache.org/proper/commons-cli/}
 * </p>
 * Created by alex on 3/7/15.
 */
public final class Options {
    /** Option Name -> Option  */
    Map<String,Option> options = new HashMap<>();
    public Options() {};
    public void add(Option option){
        checkArg(options.containsKey(option.name),"Option : '%s' already exists",option.name);
        options.put(option.name, option);
    }
}

