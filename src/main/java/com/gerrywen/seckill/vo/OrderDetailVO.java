package com.gerrywen.seckill.vo;

import com.gerrywen.seckill.domain.OrderInfo;
import lombok.Data;

/**
 * program: spring-boot-seckill->OrderDetailVO
 * description:
 * author: gerry
 * created: 2020-03-07 09:57
 **/
@Data
public class OrderDetailVO {
    private GoodsVO goods;
    private OrderInfo order;
}
