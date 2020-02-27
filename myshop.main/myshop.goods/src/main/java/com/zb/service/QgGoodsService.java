package com.zb.service;

public interface QgGoodsService {

    public String initGoodsToRedisById(String goodsId) throws  Exception;

    public int checkGoods(String goodsId) throws  Exception;

    public int lockGoods(String goodsId , String userId) throws  Exception;

    public com.zb.dto.Dto qg(String token , String goodsId) throws Exception;
}
