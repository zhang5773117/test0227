package com.zb.controller;

import com.zb.pojo.QgGoodsMessage;
import com.zb.service.QgGoodsMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class QgGoodsMessageController {
    @Autowired
    private QgGoodsMessageService qgGoodsMessageService;

    @PostMapping("/updateStaus")
    public int updateStaus( QgGoodsMessage qgGoodsMessage){
        try {
            return qgGoodsMessageService.updateGoods(qgGoodsMessage);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    @GetMapping("/showUserBuyGoods/{uid}")
    public  List<QgGoodsMessage> showUserBuyGoods(@PathVariable("uid") String uid){
        try {
            return qgGoodsMessageService.showUserBuyGoods(uid);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
