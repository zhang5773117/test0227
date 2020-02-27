package com.zb.service;

import com.zb.pojo.QgGoods;
import com.zb.pojo.QgGoodsMessage;

import java.util.List;

public interface QgGoodsMessageService {
    public int updateGoods(QgGoodsMessage qgGoods) throws  Exception;

    public List<QgGoodsMessage>showUserBuyGoods(String uid) throws  Exception;
}
