package com.gerrywen.seckill.dao;

import com.gerrywen.seckill.domain.MiaoshaUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * program: spring-boot-seckill->MiaoshaUserDao
 * description:
 * author: gerry
 * created: 2020-03-05 20:31
 **/
@Mapper
public interface MiaoshaUserDao {
    /**
     * 根据主键ID查询秒杀用户
     * @param id
     * @return
     */
    @Select("select * from miaosha_user where id = #{id}")
    public MiaoshaUser getById(@Param("id") long id);

    /**
     * 根据主键ID修改密码
     * @param toBeUpdate
     */
    @Update("update miaosha_user set password = #{password} where id = #{id}")
    public void update(MiaoshaUser toBeUpdate);
}
