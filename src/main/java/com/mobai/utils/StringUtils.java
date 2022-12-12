package com.mobai.utils;

/**
 * Software：IntelliJ IDEA 2021.2 x64
 * Author: <a href="https://www.mobaijun.com">...</a>
 * Date: 2021/12/7 15:51
 * ClassName:StringUtils
 * 类描述：字符串工具类
 * @author mobai
 */
public class StringUtils {

    /**
     * 切割url字符串
     * celGGIO1.Alm73.stVal
     * jdbc:mysql://localhost:3306/backups?
     *
     * @param str 参数类型
     */
    public static String subStringDatabase(String str) {
        return str.substring(str.indexOf("6") + 2, str.indexOf("?"));
    }
}
