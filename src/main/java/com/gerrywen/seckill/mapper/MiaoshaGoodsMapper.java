package com.gerrywen.seckill.mapper;

import com.gerrywen.seckill.model.MiaoshaGoods;
import com.gerrywen.seckill.model.MiaoshaGoodsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MiaoshaGoodsMapper {
    long countByExample(MiaoshaGoodsExample example);

    int deleteByExample(MiaoshaGoodsExample example);

    int deleteByPrimaryKey(Long id);

    int insert(MiaoshaGoods record);

    int insertSelective(MiaoshaGoods record);

    List<MiaoshaGoods> selectByExample(MiaoshaGoodsExample example);

    MiaoshaGoods selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") MiaoshaGoods record, @Param("example") MiaoshaGoodsExample example);

    int updateByExample(@Param("record") MiaoshaGoods record, @Param("example") MiaoshaGoodsExample example);

    int updateByPrimaryKeySelective(MiaoshaGoods record);

    int updateByPrimaryKey(MiaoshaGoods record);
}