package com.lazydsr.util.codehandle;

import org.junit.Test;

/**
 * UtilCodeTest
 * PROJECT_NAME: lazydsr-utils
 * PACKAGE_NAME: com.lazydsr.util.codehandle
 * Created by Lazy on 2017/12/18 12:57
 * Version: 0.1
 * Info: @TODO:...
 */
public class UtilCodeTest {
    @Test
    public void UtilCodeTest1() {
        String code = "1234abcd";
        System.out.println(code);
        String encode = UtilCode.base64eEncode(code);
        System.out.println(encode);
        String decode = UtilCode.base64Decode(encode);
        System.out.println(decode);
    }
}
