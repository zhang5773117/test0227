package com.zb.vo;

import java.io.Serializable;

/**
 * reids对商品信息的数据模型
 */
public class GoodsVo implements Serializable {
    //主键
    private String id;
    //商品名称
    private String goodsName;
    //商品单价
    private Double price;
    //当前的库存信息高并发下，修改库存数据
    private Integer currentStock;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(Integer currentStock) {
        this.currentStock = currentStock;
    }
}
