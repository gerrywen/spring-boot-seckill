package com.gerrywen.seckill.vo;

import com.gerrywen.seckill.domain.MiaoshaUser;
import lombok.Data;

/**
 * program: spring-boot-seckill->GoodsDetailVO
 * description:
 * author: gerry
 * created: 2020-03-07 09:53
 **/
@Data
public class GoodsDetailVO {
    private int miaoshaStatus = 0;
    private int remainSeconds = 0;
    private GoodsVO goods;
    private MiaoshaUser user;
}
