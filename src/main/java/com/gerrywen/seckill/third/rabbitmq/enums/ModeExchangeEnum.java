package com.gerrywen.seckill.third.rabbitmq.enums;

/**
 * description:
 *
 * @author wenguoli
 * @date 2020/3/4 17:33
 */
public enum ModeExchangeEnum {

    /**
     * direct
     */
    DIRECT("direct"),
    /**
     * fanout
     */
    FANOUT("fanout"),

    /**
     * topic
     */
    TOPIC("topic"),

    /**
     * delay
     */
    DELAY("delay"),

    /**
     * headers
     */
    HEADERS("headers");

    private String code;

    private ModeExchangeEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
