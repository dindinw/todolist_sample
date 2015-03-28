package io.dindinw.util;

import java.io.File;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * The utility class for system
 * 
 * @author alex
 *
 */
public class SysUtil {

    public static List<String> getSysPathList() {
        String path = getSysPath();
        if (path != null) {
            return Collections.unmodifiableList(Arrays.asList(path
                    .split(File.pathSeparator)));
        } else {
            return Collections.emptyList();
        }
    }
    
    public static String getSysPath(){
        return System.getenv("PATH");
    }
    
    public static Map<String,String> getSysProps(){
        Map<String,String> propMap = new HashMap<>();
        Properties props = System.getProperties();      
        for (Enumeration<Object> e = props.keys() ; e.hasMoreElements() ;) {
            String key = (String)e.nextElement();
            propMap.put(key, (String)props.get(key));
        }
        return Collections.unmodifiableMap(propMap);
    }
    
    public static void dump(PrintStream out){
        //System.getProperties().list(out); not sorted!
        out.println("-- SYSTEM Properties --");
        Map<String,String> propMap = getSysProps();
        for (String key : CollectUtil.asSortedList(propMap.keySet())){
            out.println(key + "=" + propMap.get(key));
        }
        out.println("-- ALL ENV --");
        Set<String> keySet = System.getenv().keySet();
        for (String key : CollectUtil.asSortedList(keySet)){
            out.println(key + "=" + System.getenv(key));
        }
        out.println("-- PATH --");
        out.println(getSysPath());
    }

    public static final void err(String errMsgTemplate, Object ... errMsgArgs){
        System.err.println(String.format(errMsgTemplate, errMsgArgs));
    }
    public static final void echo(String msgTemplate, Object ... msgArgs){
        System.out.println(String.format(msgTemplate,msgArgs));
    }
    public static void main(String[] args) {
        dump(System.out);
    }
}
