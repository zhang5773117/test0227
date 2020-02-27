package com.zb.controller;

import com.zb.service.QgGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.zb.dto.*;

@RestController
public class QgGoodsController {


    @Autowired
    private QgGoodsService qgGoodsService;

    @GetMapping("/initQgGoods/{goodsId}")
    public String initQgGoods(@PathVariable("goodsId") String goodsId) {
        try {
            return qgGoodsService.initGoodsToRedisById(goodsId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @GetMapping("/qg/{token}/{goodsId}")
    public Dto qg(@PathVariable("token") String token ,@PathVariable("goodsId") String goodsId){
        try {
            return qgGoodsService.qg(token,goodsId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return DtoUtil.returnFail("服务异常","66666");
    }



}
