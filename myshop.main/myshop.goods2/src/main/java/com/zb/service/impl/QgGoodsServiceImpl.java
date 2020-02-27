package com.zb.service.impl;

import com.alibaba.fastjson.JSON;
import com.zb.dto.Dto;
import com.zb.entity.XcUser;
import com.zb.feign.XcUserFeginClient;
import com.zb.mapper.QgGoodsMapper;
import com.zb.mapper.QgGoodsMessageMapper;
import com.zb.pojo.QgGoods;
import com.zb.pojo.QgGoodsMessage;
import com.zb.service.QgGoodsService;
import com.zb.util.RedisUtils;
import com.zb.vo.GoodsVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class QgGoodsServiceImpl implements QgGoodsService {

    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private QgGoodsMapper qgGoodsMapper;
    @Autowired
    private QgGoodsMessageMapper qgGoodsMessageMapper;

    @Autowired
    private XcUserFeginClient xcUserFeginClient;

    @Override
    public synchronized Dto qg(String token, String goodsId) throws Exception {
        //验证用户是否登录
        XcUser currentUser = xcUserFeginClient.getCurrentUser(token);
        if (currentUser == null) {
            return com.zb.dto.DtoUtil.returnFail("用户为登录", "0000");
        }
        //检查库存
        int val = this.checkGoods(goodsId);
        if (val == 0) {
            return com.zb.dto.DtoUtil.returnFail("库存不足", "0001");
        }
        //执行用户锁定商品
        val = this.lockGoods(goodsId, currentUser.getId());
        if (val > 0) {
            return com.zb.dto.DtoUtil.returnSuccess("抢购成功", "11111");
        }
        return com.zb.dto.DtoUtil.returnFail("抢购失败", "0002");
    }

    @Override
    public String initGoodsToRedisById(String goodsId) throws Exception {
        //获取抢购的商品信息
        QgGoods goods = qgGoodsMapper.getQgGoodsById(Long.parseLong(goodsId));
        //初始化到redis中
        String key = "goods:" + goodsId;
        GoodsVo goodsVo = new GoodsVo();
        BeanUtils.copyProperties(goods, goodsVo);
        //查询临时记录表中的count
        Map<String, Object> param = new HashMap<>();
        param.put("goodsId", goods.getId());
        Integer count = qgGoodsMessageMapper.getQgGoodsMessageCountByMap(param);
        goodsVo.setCurrentStock(goods.getStock() - count);
        //将信息初始化到redis
        redisUtils.set(key, JSON.toJSONString(goodsVo));
        return "ok";
    }

    @Override
    public int checkGoods(String goodsId) throws Exception {
        String key = "goods:" + goodsId;
        //查询redis中的商品信息
        String json = redisUtils.get(key).toString();
        //商品信息
        GoodsVo goodsVo = JSON.parseObject(json, GoodsVo.class);
        //验证是否有库存
        if (goodsVo.getCurrentStock() > 0) {
            return 1;
        }
        return 0;
    }

    @Override
    public int lockGoods(String goodsId, String userId) throws Exception {
        String key = "goods:" + goodsId;
        QgGoodsMessage goodsMessage = new QgGoodsMessage();
        //获取商品的信息
        String json = redisUtils.get(key).toString();
        //封装临时商品对象
        GoodsVo goodsVo = JSON.parseObject(json, GoodsVo.class);
        //主键
        goodsMessage.setId(com.zb.util.IdWorker.getId());
        goodsMessage.setStatus(0 + "");
        goodsMessage.setAmount(goodsVo.getPrice());
        goodsMessage.setGoodsId(goodsId);
        goodsMessage.setUserId(userId);
        //修改redis中的库存数据
        goodsVo.setCurrentStock(goodsVo.getCurrentStock() - 1);
        redisUtils.set(key, JSON.toJSONString(goodsVo));
        //执行临时抢购的商品添加
        return qgGoodsMessageMapper.insertQgGoodsMessage(goodsMessage);
    }


}
