package com.zb.service.impl;

import com.zb.config.RabbitConfig;
import com.zb.dto.Dto;
import com.zb.dto.DtoUtil;
import com.zb.entity.QgOrderPay;
import com.zb.entity.QgOrders;
import com.zb.mapper.QgOrderPayMapper;
import com.zb.service.QgOrderPayService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class QgOrderPayServiceImpl implements QgOrderPayService {

    @Autowired
    private QgOrderPayMapper qgOrderPayMapper;

    @Autowired
    private com.zb.mapper.QgOrderMapper qgOrderMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public Dto userBuy(String out_trade_no, String trade_no)throws Exception {
        QgOrderPay qgOrderPay=new QgOrderPay();
        qgOrderPay.setId(com.zb.util.IdWorker.getId());
        qgOrderPay.setOrder_number(out_trade_no);
        qgOrderPay.setPay_number(trade_no);
        qgOrderPay.setStatus("402001");
        int num = qgOrderPayMapper.addOrderPay(qgOrderPay);
        //向任务表中添加一条数据
        com.zb.entity.QgOrders qgOrders=new QgOrders();
        qgOrders.setOrder_number(out_trade_no);
        qgOrders.setStatus("401002");

        if(num>0){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    boolean flag = true;
                    while(flag) {
                        //查询修改的订单编号是不是401002
                        QgOrders order = qgOrderMapper.findOrderByOrderNumber(out_trade_no);
                        if(order.getStatus().equals("401002")) {
                            break;
                        }
                        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_TOPIC_INFORM, "inform.pay", qgOrders);
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
        return com.zb.dto.DtoUtil.returnSuccess("支付成功！");
    }
}
