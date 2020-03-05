package com.gerrywen.seckill.mapper;

import com.gerrywen.seckill.model.MiaoshaOrder;
import com.gerrywen.seckill.model.MiaoshaOrderExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MiaoshaOrderMapper {
    long countByExample(MiaoshaOrderExample example);

    int deleteByExample(MiaoshaOrderExample example);

    int deleteByPrimaryKey(Long id);

    int insert(MiaoshaOrder record);

    int insertSelective(MiaoshaOrder record);

    List<MiaoshaOrder> selectByExample(MiaoshaOrderExample example);

    MiaoshaOrder selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") MiaoshaOrder record, @Param("example") MiaoshaOrderExample example);

    int updateByExample(@Param("record") MiaoshaOrder record, @Param("example") MiaoshaOrderExample example);

    int updateByPrimaryKeySelective(MiaoshaOrder record);

    int updateByPrimaryKey(MiaoshaOrder record);
}