package com.zb.controller;

import com.alibaba.fastjson.JSON;
import com.zb.entity.XcUser;
import com.zb.entity.XcUserExt;
import com.zb.mapper.XcUserMapper;
import com.zb.service.XcUserExtService;
import com.zb.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class XcUserController {
    @Autowired
    private XcUserExtService xcUserExtService;

    @Autowired
    private RedisUtils redisUtils;

    @PostMapping(value = "/getUserLogin/{username}")
    public XcUserExt getUserLogin(@PathVariable("username") String username){
        return xcUserExtService.getXcUserExtByUsername(username);
    }

    @GetMapping(value="/initUser")
    public String initUser(){
        for (int i = 0; i < 1000; i++) {
            XcUser xcUser=new XcUser();
            xcUser.setId(i+1+"");
            xcUser.setPhone("13111111111");
            String key ="token:"+(1+i);
            redisUtils.set(key, JSON.toJSONString(xcUser));
        }
        return "ok";
    }


    @GetMapping(value = "/currentUser/{token}")
    public XcUser getCurrentUser(@PathVariable("token") String token){
        Object json = redisUtils.get(token);
        if(json!=null) {
            XcUser user = JSON.parseObject(json.toString(), XcUser.class);
            return user;
        }
        return null;
    }



}
