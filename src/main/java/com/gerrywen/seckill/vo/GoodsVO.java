package com.gerrywen.seckill.vo;

import com.gerrywen.seckill.domain.Goods;
import lombok.Data;

import java.util.Date;

/**
 * program: spring-boot-seckill->GoodsVo
 * description:
 * author: gerry
 * created: 2020-03-07 08:40
 **/
@Data
public class GoodsVO extends Goods {

    private Double miaoshaPrice;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;
}
