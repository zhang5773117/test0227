package com.zb.vo;

import java.io.Serializable;

public class MqMessage implements Serializable {
    private String token;
    private String goodsId;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }
}
