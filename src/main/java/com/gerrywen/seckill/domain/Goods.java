package com.gerrywen.seckill.domain;

import lombok.Data;

/**
 * program: spring-boot-seckill->Goods
 * description:
 * author: gerry
 * created: 2020-03-07 08:41
 **/
@Data
public class Goods {

    private Long id;
    private String goodsName;
    private String goodsTitle;
    private String goodsImg;
    private String goodsDetail;
    private Double goodsPrice;
    private Integer goodsStock;
}
