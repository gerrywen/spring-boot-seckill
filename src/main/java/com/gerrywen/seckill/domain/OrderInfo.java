package com.gerrywen.seckill.domain;

import lombok.Data;

import java.util.Date;

/**
 * program: spring-boot-seckill->OrderInfo
 * description:
 * author: gerry
 * created: 2020-03-07 08:43
 **/
@Data
public class OrderInfo {
    private Long id;
    private Long userId;
    private Long goodsId;
    private Long  deliveryAddrId;
    private String goodsName;
    private Integer goodsCount;
    private Double goodsPrice;
    private Integer orderChannel;
    private Integer status;
    private Date createDate;
    private Date payDate;
}
