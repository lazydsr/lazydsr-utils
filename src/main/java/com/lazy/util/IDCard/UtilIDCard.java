package com.lazy.util.IDCard;

import com.lazy.util.time.UtilDateTime;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * UtilIDCard
 * PROJECT_NAME: lazy
 * PACKAGE_NAME: com.lazy.com.lazydsr.util.IDCard
 * Created by Lazy on 2017/5/13 20:16
 * Version: 0.1
 * Info: 身份证号码工具类，获取身份证的相关信息
 * 本类中除了verifyIDCard外，其他方法均默认为身份证号码为正确的，
 * 所以使用其他方法前建议先使用verifyIDCard进行正确性的验证
 */

public class UtilIDCard {
    final static Map<Integer, String> provinceCode = new HashMap<Integer, String>();

    static {
        provinceCode.put(11, "北京");
        provinceCode.put(12, "天津");
        provinceCode.put(13, "河北");
        provinceCode.put(14, "山西");
        provinceCode.put(15, "内蒙古");
        provinceCode.put(21, "辽宁");
        provinceCode.put(22, "吉林");
        provinceCode.put(23, "黑龙江");
        provinceCode.put(31, "上海");
        provinceCode.put(32, "江苏");
        provinceCode.put(33, "浙江");
        provinceCode.put(34, "安徽");
        provinceCode.put(35, "福建");
        provinceCode.put(36, "江西");
        provinceCode.put(37, "山东");
        provinceCode.put(41, "河南");
        provinceCode.put(42, "湖北");
        provinceCode.put(43, "湖南");
        provinceCode.put(44, "广东");
        provinceCode.put(45, "广西");
        provinceCode.put(46, "海南");
        provinceCode.put(50, "重庆");
        provinceCode.put(51, "四川");
        provinceCode.put(52, "贵州");
        provinceCode.put(53, "云南");
        provinceCode.put(54, "西藏");
        provinceCode.put(61, "陕西");
        provinceCode.put(62, "甘肃");
        provinceCode.put(63, "青海");
        provinceCode.put(64, "宁夏");
        provinceCode.put(65, "新疆");
        provinceCode.put(71, "台湾");
        provinceCode.put(81, "香港");
        provinceCode.put(82, "澳门");
        provinceCode.put(91, "国外");
    }

    /**
     * 验证身份证信息
     *
     * @param IDCard 身份证号码
     * @return true  or false
     */
    public static boolean verifyIDCard(String IDCard) {
        String _IDCard = IDCard;
        if (IDCard.length() == 15)
            IDCard = IDCard15To18(IDCard);

        String reg = "^[1-9]\\d{5}(19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";
        if (!Pattern.matches(reg, IDCard))
            return false;
        //验证省份
        String _provinceCode = IDCard.substring(0, 2);
        if (!provinceCode.containsKey(Integer.parseInt(_provinceCode)))
            return false;
        //@TODO:验证后四位地址码东西太多，暂时不验证了
        //验证生日
        int _year = Integer.parseInt(IDCard.substring(6, 10));
        int _month = Integer.parseInt(IDCard.substring(10, 12));
        int _day = Integer.parseInt(IDCard.substring(12, 14));
        if (UtilDateTime.getDateByStr("" + _year + "-" + _month + "-" + _day).after(new Date()) || _year < 1900)
            return false;
        switch (_month) {
            case 4:
            case 6:
            case 9:
            case 11:
                if (_day > 30)
                    return false;
                break;
            case 2:
                if (UtilDateTime.isLeapYear(_year)) {
                    if (_day > 29)
                        return false;
                } else {
                    if (_day > 28)
                        return false;
                }
                break;

        }
        //验证校验码
        if (_IDCard.length() == 18) {
            if (!IDCard.substring(17).equals(getCheckCode(IDCard.substring(0, 17))))
                return false;
        }
        return true;
    }

    /**
     * 通过身份证号码获取出生日期String
     *
     * @param IDCard 身份证号码
     * @return 返回出生日期字符串，格式如下1991-06-01
     */
    public static String getBirthdayStrByIDCard(String IDCard) {
        String birthday = "";
        if (IDCard == null || (IDCard.length() != 15 && IDCard.length() != 18)) {
            return "";
        }
        if (IDCard.length() == 15)
            IDCard = IDCard15To18(IDCard);
        if (IDCard.equals(""))
            return "";

        return IDCard.substring(6, 10) + "-" + IDCard.substring(10, 12) + "-" + IDCard.substring(12, 14);
    }

    /**
     * 通过身份证号码获取出生日期Date
     *
     * @param IDCard 身份证号码
     * @return 返回出生日期Date
     */
    public static Date getBirthdayDateByIDCard(String IDCard) {
        String birthday = getBirthdayStrByIDCard(IDCard);

        return UtilDateTime.getDateByStr(birthday);
    }

    /**
     * 通过身份证号码获取年龄Int
     *
     * @param IDCard 身份证号码
     * @return 返回年龄Int
     */
    public static int getAgeByIDCard(String IDCard) {
        int age = 0;
        String birthDay = getBirthdayStrByIDCard(IDCard);
        if (birthDay.length() != 10)
            return -1;
        int _year = Integer.parseInt(birthDay.substring(0, 4));
        int _month = Integer.parseInt(birthDay.substring(5, 7));
        int _day = Integer.parseInt(birthDay.substring(8));
        int _curYear = UtilDateTime.getCurrYear();
        int _curMonth = UtilDateTime.getCurrMonth();
        int _curDay = UtilDateTime.getCurrDay();

        age = _curYear - _year;
        if (_month > _curMonth)
            age--;
        else {
            if (_month == _curMonth) {
                if (_day > _curDay) {
                    age--;
                }
            }
        }

        return age;
    }

    public static String getSexByIDCard(String IDCard) {
        if (IDCard.length() == 15)
            IDCard = IDCard15To18(IDCard);
        if (IDCard.equals(""))
            return "";
        int _temp = Integer.parseInt(IDCard.substring(14, 17));
        return _temp % 2 == 0 ? "女" : "男";
    }

    /**
     * 15位身份证号码转为18位
     *
     * @param IDCard 15位身份证号码
     * @return 18位身份证号码
     */
    public static String IDCard15To18(String IDCard) {
        String retId = "";
        String id17 = "";

        try {
            id17 = IDCard.substring(0, 6) + "19" + IDCard.substring(6);
            // 通过模得到对应的校验码 cc[y]
            retId = id17 + getCheckCode(id17);
        } catch (Exception e) {
            System.out.println("UtilIDCard Exception:" + e);
            return "";
        }
        return retId;
    }

    private static String getCheckCode(String code17) {
        try {
            int sum = 0;
            int y = 0;
            // 定义数组存放加权因子（weight factor）
            int[] wf = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
            // 定义数组存放校验码（check code）
            String[] cc = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};

            // 十七位数字本体码加权求和
            for (int i = 0; i < 17; i++) {
                sum = sum + Integer.valueOf(code17.substring(i, i + 1)) * wf[i];
            }
            // 计算模
            y = sum % 11;
            return cc[y];
        } catch (Exception e) {
            System.out.println("lazy UtilIDCard Exception:" + e);
            return "";
        }
    }

}
