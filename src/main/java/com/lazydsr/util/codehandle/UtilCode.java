package com.lazydsr.util.codehandle;

import com.lazydsr.util.Util;
import sun.misc.BASE64Decoder;

import java.io.UnsupportedEncodingException;

/**
 * UtilCode
 * PROJECT_NAME: lazydsr-utils
 * PACKAGE_NAME: com.lazydsr.util.codehandle
 * Created by Lazy on 2017/12/18 10:48
 * Version: 0.1
 * Info: Base64编码与解码
 */
public class UtilCode {


    /**
     * Base64编码
     *
     * @param code 需要编码的字符串
     * @return 使用format格式编码后的字符串
     */
    public static String base64eEncode(String code) {

        return base64eEncode(code, Util.UTF_8);

    }

    /**
     * Base64解码
     *
     * @param code 需要解码的字符串
     * @return 使用format格式解码后的字符串
     */
    public static String base64Decode(String code) {

        return base64Decode(code, Util.UTF_8);

    }

    /**
     * Base64编码
     *
     * @param code   需要编码的字符串
     * @param format 编码格式
     * @return 使用format格式编码后的字符串
     */
    public static String base64eEncode(String code, String format) {
        if (code == null)
            return null;
        String res = "";
        try {
            res = new sun.misc.BASE64Encoder().encode(code.getBytes(format));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Base64解码
     *
     * @param code   需要解码的字符串
     * @param format 编码格式
     * @return 使用format格式解码后的字符串
     */
    public static String base64Decode(String code, String format) {
        if (code == null)
            return null;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            byte[] b = decoder.decodeBuffer(code);
            return new String(b, format);
        } catch (Exception e) {
            return null;
        }
    }
}
