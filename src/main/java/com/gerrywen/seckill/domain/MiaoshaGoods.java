package com.gerrywen.seckill.domain;

import lombok.Data;

import java.util.Date;

/**
 * program: spring-boot-seckill->MiaoshaGoods
 * description:
 * author: gerry
 * created: 2020-03-07 08:42
 **/
@Data
public class MiaoshaGoods {
    private Long id;
    private Long goodsId;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;
}
