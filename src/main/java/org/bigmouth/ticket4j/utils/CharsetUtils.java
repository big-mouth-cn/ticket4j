package org.bigmouth.ticket4j.utils;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang3.StringUtils;


public class CharsetUtils {

    public static String convert(String string) {
        return convert(string, "UTF-8");
    }
    
    public static String convert(String string, String charset) {
        try {
            if (StringUtils.isBlank(string))
                return null;
            return new String(string.getBytes("ISO-8859-1"), charset);
        }
        catch (UnsupportedEncodingException e) {
            return null;
        }
    }
}
