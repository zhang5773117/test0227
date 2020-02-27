package com.zb.controller;

import com.zb.dto.Dto;
import com.zb.dto.DtoUtil;
import com.zb.entity.QgOrders;
import com.zb.service.QgOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class QgOrderController {
    @Autowired
    private QgOrderService qgOrderService;

    @GetMapping("/findOrderByUser/{uid}")
    public Dto<List<QgOrders>> findOrderByUser(@PathVariable("uid") String uid) {
        List<QgOrders> orderByUser = qgOrderService.findOrderByUser(uid);
        return DtoUtil.returnSuccess("ok", orderByUser);
    }

    @GetMapping("/createOrder/{token}")
    public Dto createOrder(@PathVariable("token") String token ){
        return qgOrderService.createOrder(token);
    }

}
