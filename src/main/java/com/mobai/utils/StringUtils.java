package com.mobai.utils;

/**
 * Software：IntelliJ IDEA 2021.2 x64
 * Author: https://www.mobaijun.com
 * Date: 2021/12/7 15:51
 * ClassName:StringUtils
 * 类描述：字符串工具类
 */
public class StringUtils {

    /**
     * 切割url字符串
     * celGGIO1.Alm73.stVal
     * jdbc:mysql://localhost:3306/backups?
     *
     * @param str 参数类型
     * @return
     */
    public static String subStringDatabase(String str) {
        return str.substring(str.indexOf("6") + 2, str.indexOf("?"));
    }
}
