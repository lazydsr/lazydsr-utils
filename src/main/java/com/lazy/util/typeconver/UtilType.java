package com.lazy.util.typeconver;

/**
 * Coding......
 * Created by D.SR on 2017/3/9.
 */
public class UtilType {
    public static String null2String(Object object) {
        return object == null ? "" : object.toString();
    }

    public static int null2Int(Object o) {
        return o == null ? 0 : string2Int(null2String(o));
    }

    public static int string2Int(String s) {
        return Integer.parseInt(null2String(s));
    }

    public static Long string2Long(String s) {
        return Long.parseLong(null2String(s));
    }

    public static int long2Int(long l) {
        return new Long(l).intValue();

    }
}
