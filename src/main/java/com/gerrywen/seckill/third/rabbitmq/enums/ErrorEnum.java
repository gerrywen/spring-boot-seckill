package com.gerrywen.seckill.third.rabbitmq.enums;

/**
 * description:
 *
 * @author wenguoli
 * @date 2020/3/5 8:41
 */
public enum ErrorEnum {
    PARAMETER_ERROR(400, "参数错误"),
    PERMISSION_INSUFFICIENT(401, "权限不足"),
    REQUEST_ERROR(500, "请求失败"),
    ;
    /**
     * 返回码
     */
    private int code;

    /**
     * 返回结果信息
     */
    private String message;

    ErrorEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
