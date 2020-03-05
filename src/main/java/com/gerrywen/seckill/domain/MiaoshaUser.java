package com.gerrywen.seckill.domain;

import lombok.Data;

import java.util.Date;

/**
 * program: spring-boot-seckill->MiaoshaUser
 * description:
 * author: gerry
 * created: 2020-03-05 20:30
 **/
@Data
public class MiaoshaUser {
    private Long id;
    private String nickname;
    private String password;
    private String salt;
    private String head;
    private Date registerDate;
    private Date lastLoginDate;
    private Integer loginCount;
}
