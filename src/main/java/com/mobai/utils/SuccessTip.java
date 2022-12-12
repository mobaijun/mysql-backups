package com.mobai.utils;

import org.springframework.http.HttpStatus;

/**
 * 返回给前台的成功提示
 * @author mobai
 */
public class SuccessTip extends Tip {

    private Object data;

    public SuccessTip() {
        super.code = HttpStatus.OK.value();
        super.message = HttpStatus.OK.getReasonPhrase();
    }

    public SuccessTip(Object obj) {
        super.code = HttpStatus.OK.value();
        super.message = HttpStatus.OK.getReasonPhrase();
        data = obj;
    }

    public SuccessTip(int code, String message) {
        super.code = code;
        super.message = message;
    }

    public SuccessTip(String message, Object obj) {
        super.code = HttpStatus.OK.value();
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
