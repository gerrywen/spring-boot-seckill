package com.gerrywen.seckill.dao;

import com.gerrywen.seckill.domain.MiaoshaUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * program: spring-boot-seckill->MiaoshaUserDao
 * description:
 * author: gerry
 * created: 2020-03-05 20:31
 **/
@Mapper
public interface MiaoshaUserDao {
    @Select("select * from miaosha_user where id = #{id}")
    public MiaoshaUser getById(@Param("id") long id);
}
