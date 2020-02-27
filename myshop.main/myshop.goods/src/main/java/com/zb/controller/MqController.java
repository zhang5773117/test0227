package com.zb.controller;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jms.annotation.JmsListener;
//import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
//import com.zb.util.*;

@RestController
public class MqController {

//    @Autowired
//    private MqUtil mqUtil;


//    @GetMapping("/send")
//    public String sendMQ(){
//        mqUtil.sendQueueMessage("queueMess","springboot的队列的消息");
//        mqUtil.sendTopicMessage("topicMess","springboot的主题的消息");
//        return "ok";
//    }
//
//    @JmsListener(destination = "queueMess")
//    public void reviceMessageQueue(Object message){
//        System.out.println(message.toString());
//    }
//    @JmsListener(destination = "topicMess")
//    public void reviceMessageTopic(Object message){
//        System.out.println(message.toString());
//    }


}
