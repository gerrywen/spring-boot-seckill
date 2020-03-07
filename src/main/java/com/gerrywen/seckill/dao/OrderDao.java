package com.gerrywen.seckill.dao;

import com.gerrywen.seckill.domain.MiaoshaOrder;
import com.gerrywen.seckill.domain.OrderInfo;
import org.apache.ibatis.annotations.*;

/**
 * program: spring-boot-seckill->OrderDao
 * description:
 * author: gerry
 * created: 2020-03-07 08:50
 **/
@Mapper
public interface OrderDao {
    /**
     * 根据用户ID和商品ID查询秒杀订单
     * @param userId
     * @param goodsId
     * @return
     */
    @Select("select * from miaosha_order where user_id=#{userId} and goods_id=#{goodsId}")
    public MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(@Param("userId")long userId, @Param("goodsId")long goodsId);

    /**
     * 自增ID插入
     *
     * @param orderInfo
     * @return
     */
    @Insert("insert into order_info(user_id, goods_id, goods_name, goods_count, goods_price, order_channel, status, create_date)values("
            + "#{userId}, #{goodsId}, #{goodsName}, #{goodsCount}, #{goodsPrice}, #{orderChannel},#{status},#{createDate} )")
    @SelectKey(keyColumn="id", keyProperty="id", resultType=long.class, before=false, statement="select last_insert_id()")
    public long insert(OrderInfo orderInfo);

    /**
     * 根据用户ID和商品ID和订单ID，将数据插入秒杀订单
     * @param miaoshaOrder
     * @return
     */
    @Insert("insert into miaosha_order (user_id, goods_id, order_id)values(#{userId}, #{goodsId}, #{orderId})")
    public int insertMiaoshaOrder(MiaoshaOrder miaoshaOrder);

    /**
     * 根据订单主键ID查询订单信息
     * @param orderId
     * @return
     */
    @Select("select * from order_info where id = #{orderId}")
    public OrderInfo getOrderById(@Param("orderId")long orderId);

    /**
     * 清除所有订单信息
     */
    @Delete("delete from order_info")
    public void deleteOrders();

    /**
     * 清除所有秒杀订单
     */
    @Delete("delete from miaosha_order")
    public void deleteMiaoshaOrders();
}
