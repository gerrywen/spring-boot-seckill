package com.gerrywen.seckill.dto;

import com.gerrywen.seckill.validator.IsMobile;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * program: spring-boot-seckill->LoginDTO
 * description:
 * author: gerry
 * created: 2020-03-05 21:12
 **/
@Data
public class LoginDTO {

    @NotNull
    @IsMobile
    private String mobile;

    @NotNull
    @Length(min=32)
    private String password;
}
