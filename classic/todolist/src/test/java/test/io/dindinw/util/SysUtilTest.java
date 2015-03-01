package test.io.dindinw.util;

import static org.hamcrest.CoreMatchers.both;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertThat;
import io.dindinw.util.SysUtil;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.junit.Test;

/**
 * Use new hamcrest syntax to get more readable assertion of test
 * @author alex
 *
 */
public class SysUtilTest {

    @Test
    public void testGetSystemPath() {
        String path = SysUtil.getSysPath();
        assertThat("System Path Should be seperated by " + File.pathSeparator,
                path, containsString(File.pathSeparator));
    }
    
    @Test
    public void testGetSystemPathList() {
        List<String> pathList = SysUtil.getSysPathList();
        assertThat("Every Path Should be seperated by " + File.separator,
                pathList, everyItem(containsString(File.separator)));
    }
    
    @Test
    public void testGetSysProps(){
       Map<String, String> propsMap = SysUtil.getSysProps();
       assertThat("Should contains property key like java.xxx. . ",
               propsMap.keySet(), hasItem(
                       both(startsWith("java")).and(containsString("."))));
    }

}
