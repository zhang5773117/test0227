package com.zb.service.impl;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.zb.config.RabbitConfig;
import com.zb.dto.Dto;
import com.zb.entity.QgOrders;
import com.zb.entity.XcUser;
import com.zb.fegin.GoodsFeginClient;
import com.zb.feign.XcUserFeginClient;
import com.zb.mapper.QgOrderMapper;
import com.zb.pojo.QgGoodsMessage;
import com.zb.service.QgOrderService;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QgOrderServiceImpl implements QgOrderService {
    @Autowired
    private QgOrderMapper qgOrderMapper;

    @Autowired
    private XcUserFeginClient userFeginClient;

    @Autowired
    private GoodsFeginClient goodsFeginClient;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public List<QgOrders> findOrderByUser(String uid) {
        return qgOrderMapper.findOrderByUser(uid);
    }

    @Override
    public Dto createOrder(String token) {
        XcUser currentUser = userFeginClient.getCurrentUser(token);
        if (currentUser == null) {
            return com.zb.dto.DtoUtil.returnFail("没有查询到用户", "1000");
        }
        //查询商品时候，加条件，查询状态为0的数据，
        //根据自己的业务查询商品信息
        List<QgGoodsMessage> qgGoodsMessages = goodsFeginClient.showUserBuyGoods(currentUser.getId());
        if (qgGoodsMessages == null || qgGoodsMessages.size() <= 0) {
            return com.zb.dto.DtoUtil.returnFail("没有查询到交易的商品", "1001");
        }
        Double price = 0.0;
        for (QgGoodsMessage qgGoodsMessage : qgGoodsMessages) {
            price = qgGoodsMessage.getAmount();
        }
        String orderId = com.zb.util.IdWorker.getId();
        QgOrders qgOrders = new QgOrders();
        qgOrders.setOrder_number(orderId);
        qgOrders.setInitial_price(price.floatValue());
        qgOrders.setPrice(price.floatValue());
        qgOrders.setStatus("401001");
        qgOrders.setDetails(JSON.toJSONString(qgGoodsMessages));
        qgOrders.setUser_id(currentUser.getId());
        qgOrderMapper.saveOrder(qgOrders);
        //一个订单有多件商品信息今天的代码 对象换成集合
        //作业.....
        try {
            //数据的一致性
            goodsFeginClient.updateStaus(qgGoodsMessages.get(0));
//            throw new Exception("异常");
        } catch (Exception e) {
            e.printStackTrace();
            sendMsg(orderId);
        }
        return com.zb.dto.DtoUtil.returnSuccess("下单成功！");
    }

    public void sendMsg(String orderId) {
        Map<String ,Object> param = new HashMap<>();
        param.put("orderId",orderId);
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_TOPIC_INFORM, "inform.order", param, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                return message;
            }
        });
    }

    @RabbitListener(queues = RabbitConfig.QUEUE_CREATEORDER)
    public void reviceorder(Map<String ,Object> param , Message message , Channel channel){
        String orderId = param.get("orderId").toString();
        System.out.println("删除没有成功的数据!");
        try {
            qgOrderMapper.deleteOrder(orderId);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,false);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    @RabbitListener(queues = RabbitConfig.QUEUE_CREATEPAY)
    public void reviceupdateOrderStatus(QgOrders orders , Message message , Channel channel ){
        qgOrderMapper.updateOrderStatus(orders);
    }

}
