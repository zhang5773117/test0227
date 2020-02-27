package com.zb.feign;

import com.zb.dto.Dto;
import com.zb.entity.QgOrders;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient("ORDERSERVER")
public interface QgOrderFeignClient {
    /**
     *
     * @param uid
     * @return
     */
    @GetMapping("/findOrderByUser/{uid}")
    public Dto<List<QgOrders>> findOrderByUser(@PathVariable("uid") String uid);

}
