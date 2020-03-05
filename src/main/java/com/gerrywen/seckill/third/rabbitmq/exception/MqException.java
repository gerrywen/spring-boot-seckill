package com.gerrywen.seckill.third.rabbitmq.exception;

import com.gerrywen.seckill.third.rabbitmq.enums.ErrorEnum;

import java.text.MessageFormat;

/**
 * description: 业务异常处理
 *
 * @author wenguoli
 * @date 2020/3/5 8:40
 */
public class MqException extends RuntimeException {

    private Integer code;

    public String msg;

    public MqException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public MqException(String msg) {
        super(msg);
        this.code = ErrorEnum.REQUEST_ERROR.getCode();
        this.msg = msg;
    }

    /**
     * 自定义返回状态信息
     *
     **/
    public MqException(ErrorEnum errorEnum) {
        super(errorEnum.getMessage());
        this.code = errorEnum.getCode();
        this.msg = errorEnum.getMessage();
    }

    /**
     * 自定义返回状态信息
     *
     * @param errorEnum
     * @param argArray     值
     *                     eg:
     *                     returnStatus中的message:店铺：{0},名字：{1}
     *                     new BusinessException(ReturnStatus.SS,"天猫","张三");
     */
    public MqException(ErrorEnum errorEnum, Object... argArray) {
        super(replace(errorEnum.getMessage(), argArray));
        this.code = errorEnum.getCode();
        this.msg = replace(errorEnum.getMessage(), argArray);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    private static String replace(String str, Object... argArray) {
        return MessageFormat.format(str, argArray);
    }
}
