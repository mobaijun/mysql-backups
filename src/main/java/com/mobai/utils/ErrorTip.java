package com.mobai.utils;

import org.springframework.http.HttpStatus;

/**
 * Software：IntelliJ IDEA 2021.2 x64
 * Author: https://www.mobaijun.com
 * Date: 2021/12/7 14:33
 * ClassName:ErrorTip
 * 类描述： 错误提示
 */
public class ErrorTip extends Tip{
    private Object data;

    public ErrorTip() {
        super();
        this.code = HttpStatus.INTERNAL_SERVER_ERROR.value();
        super.message = HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();
    }

    public ErrorTip(int code, String message) {
        super();
        this.code = code;
        this.message = message;
    }

    public ErrorTip(int code, String message, Object obj) {
        super.code = code;
        super.message = message;
        this.data = obj;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
