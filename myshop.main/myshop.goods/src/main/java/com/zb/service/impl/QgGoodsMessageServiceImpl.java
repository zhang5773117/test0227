package com.zb.service.impl;

import com.zb.mapper.QgGoodsMessageMapper;
import com.zb.pojo.QgGoods;
import com.zb.pojo.QgGoodsMessage;
import com.zb.service.QgGoodsMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QgGoodsMessageServiceImpl implements QgGoodsMessageService {
    @Autowired
    private QgGoodsMessageMapper qgGoodsMessageMapper;
    @Override
    public int updateGoods(QgGoodsMessage qgGoods) throws Exception {
        return qgGoodsMessageMapper.updateQgGoodsMessageByUser(qgGoods);
    }

    @Override
    public List<QgGoodsMessage> showUserBuyGoods(String uid) throws Exception {
        Map<String,Object> param= new HashMap<>();
        param.put("userId",uid);
        return qgGoodsMessageMapper.getQgGoodsMessageListByMap(param);
    }
}
