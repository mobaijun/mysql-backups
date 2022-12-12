package com.mobai.utils;

/**
 * Software：IntelliJ IDEA 2021.2 x64
 * Author: https://www.mobaijun.com
 * Date: 2021/12/7 14:16
 * ClassName:CountsUtils
 * 类描述： 通用工具类
 */
public class CountsUtils {

    /**
     * 文件后缀
     */
    public static final String FILE_SUFFIX = ".sql";

    /**
     * 判断操作系统类型、Linux|Windows
     */
    public static boolean isSystem(String osName) {
        Boolean flag = null;
        if (osName.startsWith("windows")) {
            flag = true;
        } else if (osName.startsWith("linux")) {
            flag = false;
        }
        return Boolean.TRUE.equals(flag);
    }
}
