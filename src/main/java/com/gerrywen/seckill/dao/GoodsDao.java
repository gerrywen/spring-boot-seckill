package com.gerrywen.seckill.dao;

import com.gerrywen.seckill.domain.MiaoshaGoods;
import com.gerrywen.seckill.vo.GoodsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * program: spring-boot-seckill->GoodsDao
 * description:
 * author: gerry
 * created: 2020-03-07 08:39
 **/
@Mapper
public interface GoodsDao {
    /**
     * 查询秒杀商品
     * @return
     */
    @Select("select g.*,mg.stock_count, mg.start_date, mg.end_date,mg.miaosha_price from miaosha_goods mg left join goods g on mg.goods_id = g.id")
    public List<GoodsVO> listGoodsVo();

    /**
     * 根据商品ID查询秒杀商品信息
     * @param goodsId
     * @return
     */
    @Select("select g.*,mg.stock_count, mg.start_date, mg.end_date,mg.miaosha_price from miaosha_goods mg left join goods g on mg.goods_id = g.id where g.id = #{goodsId}")
    public GoodsVO getGoodsVoByGoodsId(@Param("goodsId")long goodsId);

    /**
     * 根据ID扣库存
     * @param g
     * @return
     */
    @Update("update miaosha_goods set stock_count = stock_count - 1 where goods_id = #{goodsId} and stock_count > 0")
    public int reduceStock(MiaoshaGoods g);

    /**
     * 根据商品ID重置库存
     * @param g
     * @return
     */
    @Update("update miaosha_goods set stock_count = #{stockCount} where goods_id = #{goodsId}")
    public int resetStock(MiaoshaGoods g);
}
