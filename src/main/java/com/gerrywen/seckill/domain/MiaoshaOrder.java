package com.gerrywen.seckill.domain;

import lombok.Data;

/**
 * program: spring-boot-seckill->MiaoshaOrder
 * description:
 * author: gerry
 * created: 2020-03-07 08:42
 **/
@Data
public class MiaoshaOrder {
    private Long id;
    private Long userId;
    private Long  orderId;
    private Long goodsId;
}
