package com.yx.utils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * User: NMY
 * Date: 16-8-27
 */
public enum GenResult {
    SUCCESS(0, "request success"),
    FAILED(1, "request failed"),
    NO_DATA(2, "data not exists"),
    PARAMS_ERROR(3, "parameters error");
    int msgCode;
    String message;

    private GenResult(int msgCode, String message) {
        this.msgCode = msgCode;
        this.message = message;
    }

    public Map<String, Object> genResult() {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("msgCode", msgCode);
        map.put("message", message);
        return map;
    }

    public Map<String, Object> genResult(Object data) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("msgCode", msgCode);
        map.put("message", message);
        map.put("data", data);
        return map;
    }

    public int getMsgCode() {
        return msgCode;
    }

    public void setMsgCode(int msgCode) {
        this.msgCode = msgCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}