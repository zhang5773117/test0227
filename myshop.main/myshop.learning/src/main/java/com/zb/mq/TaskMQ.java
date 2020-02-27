package com.zb.mq;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.zb.config.RabbitConfig;
import com.zb.entity.Learning;
import com.zb.entity.Task;
import com.zb.service.LearningService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaskMQ {

    @Autowired
    private LearningService learningService;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = RabbitConfig.XC_LEARNING_ADDCHOOSECOURSE)
    public void reviceTaks(Task task , Message message , Channel channel){
        System.out.println(task.getRequest_body());
        Learning learning = JSON.parseObject(task.getRequest_body(), Learning.class);
        String ok = learningService.addCourse(learning,task);
        if(ok.equals("ok")){
            rabbitTemplate.convertAndSend(RabbitConfig.EX_LEARNING_ADDCHOOSECOURSE,RabbitConfig.XC_LEARNING_FINISHADDCHOOSECOURSE_KEY,task);
        }
        System.out.println(ok);
    }
}
