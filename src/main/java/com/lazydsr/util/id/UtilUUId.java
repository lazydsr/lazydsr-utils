package com.lazydsr.util.id;


import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

/**
 * UtilUUId
 * PROJECT_NAME: lazy-utils
 * PACKAGE_NAME: com.lazy.com.lazydsr.util.id
 * Created by Lazy on 2017/6/30 23:10
 * Version: 0.1
 * Info: @TODO:...
 */
public class UtilUUId {
    /*
     * The random number generator used by this class to create random
     * based UUIDs.
     */
    private static volatile SecureRandom numberGenerator = null;

    public final static int UUIDLENGTH = 26;

    final static char[] DIGITS = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
            'J', 'K', 'M', 'N', 'P', 'Q', 'R', 'S',
            'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '1',
            '2', '3', '4', '5', '6', '7', '8', '9'
    };

    //    public static byte[] randomBytes() {
//        SecureRandom ng = numberGenerator;
//        if (ng == null) {
//            numberGenerator = ng = new SecureRandom();
//        }
//        byte[] randomBytes = new byte[16];
//        ng.nextBytes(randomBytes);
//        return randomBytes;
//    }
    public static String getId() {
        return randomUUID();
    }

    public static String randomUUID() {
        SecureRandom ng = numberGenerator;
        if (ng == null) {
            numberGenerator = ng = new SecureRandom();
        }

        byte[] randomBytes = new byte[16];
        ng.nextBytes(randomBytes);
        randomBytes[6] &= 0x0f;  /* clear version        */
        randomBytes[6] |= 0x40;  /* set to version 4     */
        randomBytes[8] &= 0x3f;  /* clear variant        */
        randomBytes[8] |= 0x80;  /* set to IETF variant  */
        long msb = 0, lsb = 0;
        int i;
        for (i = 0; i < 8; i++)
            msb = (msb << 8) | (randomBytes[i] & 0xff);
        for (i = 8; i < 16; i++)
            lsb = (lsb << 8) | (randomBytes[i] & 0xff);

        char[] s = new char[32];
        int index = 0;
        for (i = 0; index < 16; i += 5) {
            s[index++] = DIGITS[(int) ((msb >>> i) & 0x1f)];
        }
        for (i = 0; index < 32; i += 5) {
            s[index++] = DIGITS[(int) ((lsb >>> i) & 0x1f)];
        }
        return new String(s);
    }


    public void test() {
        Map<String, String> map = null;
        for (int i = 0; i < 1000000000; i++) {
            System.out.print("第" + i + "次");
            map = new HashMap<String, String>();
            for (int j = 0; j < 1000000; j++) {
//            System.out.println(randomUUID());
                map.put(randomUUID(), null);
            }
            if (map.size() != 1000000) {
                System.out.println("异常" + map.size());
                break;
            } else {
                System.out.println("   正常");
            }
            map = null;
        }

    }
}
