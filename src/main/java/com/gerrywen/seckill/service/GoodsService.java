package com.gerrywen.seckill.service;

import com.gerrywen.seckill.dao.GoodsDao;
import com.gerrywen.seckill.domain.MiaoshaGoods;
import com.gerrywen.seckill.vo.GoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * program: spring-boot-seckill->GoodsService
 * description:
 * author: gerry
 * created: 2020-03-07 09:03
 **/
@Service
public class GoodsService {

    @Autowired
    GoodsDao goodsDao;


    public List<GoodsVO> listGoodsVo(){
        return goodsDao.listGoodsVo();
    }

    public GoodsVO getGoodsVoByGoodsId(long goodsId) {
        return goodsDao.getGoodsVoByGoodsId(goodsId);
    }

    public boolean reduceStock(GoodsVO goods) {
        MiaoshaGoods g = new MiaoshaGoods();
        g.setGoodsId(goods.getId());
        int ret = goodsDao.reduceStock(g);
        return ret > 0;
    }

    public void resetStock(List<GoodsVO> goodsList) {
        for(GoodsVO goods : goodsList ) {
            MiaoshaGoods g = new MiaoshaGoods();
            g.setGoodsId(goods.getId());
            g.setStockCount(goods.getStockCount());
            goodsDao.resetStock(g);
        }
    }

}
