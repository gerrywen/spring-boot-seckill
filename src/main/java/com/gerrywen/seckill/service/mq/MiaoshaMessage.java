package com.gerrywen.seckill.service.mq;

import com.gerrywen.seckill.domain.MiaoshaUser;
import lombok.Data;

/**
 * program: spring-boot-seckill->MiaoshaMessage
 * description:
 * author: gerry
 * created: 2020-03-07 11:18
 **/
@Data
public class MiaoshaMessage {
    private MiaoshaUser user;
    private long goodsId;
}
