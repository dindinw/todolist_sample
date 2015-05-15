package test.io.dindinw.util;

import io.dindinw.util.StrUtil;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 *  Unit Test for {@link io.dindinw.util.StrUtil}.
 */
public class StrUtilTest {
    @Test
    public void testEmpty() {
        assertFalse(StrUtil.CommonLang3.isEmpty("test"));
        assertFalse(StrUtil.Guava.isEmpty("test"));
        assertFalse(StrUtil.Mine.isEmpty("test"));

        for (StrUtil u : StrUtil.values()){
            assertFalse(u.isEmpty("test"));
        }
        for (StrUtil u : StrUtil.values()){
            assertTrue("Execute " + u.name() + " Error", u.isEmpty(""));
        }
    }
}
