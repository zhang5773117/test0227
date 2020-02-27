package com.zb.fegin;

import com.zb.pojo.QgGoodsMessage;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(name = "GOODSSERVER")
public interface GoodsFeginClient {
    @PostMapping("/updateStaus")
    public int updateStaus( QgGoodsMessage qgGoodsMessage);

    @GetMapping("/showUserBuyGoods/{uid}")
    public List<QgGoodsMessage> showUserBuyGoods(@PathVariable("uid") String uid);
}
