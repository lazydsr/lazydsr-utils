package com.lazydsr.util.MD5;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * UtilMD5
 * PROJECT_NAME: lazy
 * PACKAGE_NAME: com.lazy.com.lazydsr.util.UtilMD5
 * Created by Lazy on 2017/5/13 20:16
 * Version: 0.1
 * Info: MD5工具类
 */
public class UtilMD5 {
    /**
     * 将字符串转换为大写的MD5
     *
     * @param str 需要加密的字符串
     * @return 加密之后的大写MD5
     */
    public static String getMD5UpCase(String str) {
        return DigestUtils.md5Hex(str).toUpperCase();
    }

    /**
     * 将字符串转换为小写的MD5
     *
     * @param str 需要加密的字符串
     * @return 加密之后的小写MD5
     */
    public static String getMD5LowCase(String str) {
        return DigestUtils.md5Hex(str).toLowerCase();
    }
}
