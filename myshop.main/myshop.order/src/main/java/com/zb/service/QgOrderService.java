package com.zb.service;

import com.zb.entity.QgOrders;

import java.util.List;

/**
 * @author zhangsan
 */
public interface QgOrderService {
    /**
     *
     * @param uid
     * @return
     */
    public List<QgOrders> findOrderByUser( String uid);

    public com.zb.dto.Dto createOrder(String token );

}
