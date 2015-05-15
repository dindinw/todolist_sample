package io.dindinw.util;

import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;

/**
 * StringUtil has been included in
 * Apache Common
 * Google Guava
 *
 */
interface Operation {
    boolean isEmpty(String str);
    boolean isBlank(String str);

}
public enum StrUtil implements Operation {
    CommonLang3 {
        @Override
        public boolean isEmpty(String str) {
            // apache common
            return StringUtils.isEmpty(str);
        }

        @Override
        public boolean isBlank(String str) {
            return StringUtils.isBlank(str);
        }

    },
    Guava {
        @Override
        public boolean isEmpty(String str) {
            // guava Strings
            return Strings.isNullOrEmpty(str);
        }

        @Override
        public boolean isBlank(String str) {
           return str==null||Strings.isNullOrEmpty(str.trim());
        }
    },
    Mine {
        @Override
        public boolean isEmpty(String str) {
            return str==null||str.isEmpty();
        }

        @Override
        public boolean isBlank(String str) {
            int strLen;
            if (str == null || (strLen = str.length()) == 0) {
                return true;
            }
            for (int i = 0; i < strLen; i++) {
                if (!Character.isWhitespace(str.charAt(i))) {
                    return false;
                }
            }
            return true;
        }
    },
}

