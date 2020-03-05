package com.gerrywen.seckill.dao;

import com.gerrywen.seckill.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * program: spring-boot-seckill->UserDao
 * description: 用户数据层
 * author: gerry
 * created: 2020-03-05 20:27
 **/
@Mapper
public interface UserDao {
    @Select("select * from user where id = #{id}")
    public User getById(@Param("id") int id);

    @Insert("insert into user(id, name)values(#{id}, #{name})")
    public int insert(User user);
}
