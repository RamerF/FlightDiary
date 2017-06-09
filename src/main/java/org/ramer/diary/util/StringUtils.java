package org.ramer.diary.util;

import lombok.extern.slf4j.Slf4j;

/**
 * 字符串工具类，包含常用的静态方法：
 * <strong>
 * 1.是否含有中文
 * </strong>
 *
 * @author ramer
 */
@Slf4j
public class StringUtils{
    /**
     * Check whether the given {@code String} is empty.
     *
     * @param str the str
     * @return the boolean
     */
    public static boolean isEmpty(Object str) {
        return (str == null || "".equals(str));
    }

    /**
     * Check whether the given {@code CharSequence} contains actual <em>text</em>.
     *
     * @param str the str
     * @return the boolean
     */
    public static boolean hasText(CharSequence str) {
        if (!hasLength(str)) {
            return false;
        }
        for (int i = 0, strLen = str.length(); i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check whether the given {@code String} contains Chinese.
     *
     * @param str the str
     * @return the boolean
     */
    public static boolean hasChinese(String str) {
        if (str.getBytes().length != str.length()) {
            log.debug("has Chinese");
            return true;
        }
        return false;
    }

    /**
     * 
     * Check whether the given {@code String} has length.
     *
     * @param str the str
     * @return the boolean
     */
    public static boolean hasLength(CharSequence str) {
        return str != null && str.length() > 0;
    }

    /**
     * Concat strings.
     *
     * @param values the values
     * @return the string
     */
    public static String concat(final Object... values) {
        if (values == null) {
            return "";
        }
        final StringBuilder sb = new StringBuilder();
        for (final Object value : values) {
            if (value == null) {
                sb.append("");
            } else {
                sb.append(value.toString());
            }
        }
        return sb.toString();
    }
}
