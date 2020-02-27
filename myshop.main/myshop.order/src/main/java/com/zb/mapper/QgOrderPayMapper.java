package com.zb.mapper;

import com.zb.entity.QgOrderPay;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface QgOrderPayMapper {
    public int addOrderPay(QgOrderPay qgOrderPay)throws Exception;
}
