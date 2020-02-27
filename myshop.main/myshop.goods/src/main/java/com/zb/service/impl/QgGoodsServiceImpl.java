package com.zb.service.impl;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.AMQP;
import com.zb.config.RabbitConfig;
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
import com.zb.vo.MqMessage;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rabbitmq.client.Channel;

import java.io.IOException;
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
//    @Autowired
//    private com.zb.util.MqUtil mqUtil;



    @Autowired
    private RabbitTemplate rabbitTemplate;

    public RabbitTemplate.ConfirmCallback confirmCallback=new RabbitTemplate.ConfirmCallback() {
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String s) {
            if(ack){
                System.out.println("成功投递数据：投递的数据商品信息是:"+correlationData);
            }else{
                System.out.println("消息投递失败：投递的数据商品信息是:"+correlationData);
            }
        }
    };

    public RabbitTemplate.ReturnCallback returnCallback=new RabbitTemplate.ReturnCallback() {
        @Override
        public void returnedMessage(Message message, int i, String s, String s1, String s2) {

        }
    };



    @Override
    public  Dto qg(String token, String goodsId) throws Exception {
        MqMessage mqMessage =new MqMessage();
        mqMessage.setGoodsId(goodsId);
        mqMessage.setToken(token);
        CorrelationData correlationData=new CorrelationData(JSON.toJSONString(mqMessage));
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_TOPIC_INFORM,"inform.qg",mqMessage,correlationData);
        return  com.zb.dto.DtoUtil.returnSuccess("请在排队中请稍后..");
    }

    @RabbitListener(queues =RabbitConfig.QUEUE_QG )
    @RabbitHandler
    public void reviceQgMessage(MqMessage  mqMessage , org.springframework.messaging.Message message, Channel channel){
        Long tag  =(Long) message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);

        String token = null;
        String goodsId = null;
        token= mqMessage.getToken();
        goodsId= mqMessage.getGoodsId();
        //验证用户是否登录
        XcUser currentUser = xcUserFeginClient.getCurrentUser(token);
        if (currentUser == null) {
            return ;
        }
        String key="lock:"+goodsId;
        try {
            while(!redisUtils.lock(key)){
                Thread.sleep(3000);
            }
            //检查库存
            int val = this.checkGoods(goodsId);
            if (val == 0) {
                return ;
            }
            //执行用户锁定商品
            val = this.lockGoods(goodsId, currentUser.getId());
            if (val > 0) {
                channel.basicAck(tag,false);
                System.out.println("nack"+goodsId+"..."+token);
                return ;
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                channel.basicNack(tag,false,false);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }finally {
            redisUtils.unLock(key);

        }
        return;
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
