package com.zb.mapper;

import com.zb.entity.QgOrders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface QgOrderMapper {
    /**
     *
     * @param uid
     * @return
     */
    public List<QgOrders> findOrderByUser(@Param("uid") String uid);

    public int saveOrder(QgOrders qgOrders);

    public int deleteOrder( @Param("oid") String oid);

    public int updateOrderStatus(QgOrders qgOrders);

    public QgOrders findOrderByOrderNumber(@Param("orderNumber") String orderNumber);


}
