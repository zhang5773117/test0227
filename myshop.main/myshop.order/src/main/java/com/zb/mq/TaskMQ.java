package com.zb.mq;

import com.rabbitmq.client.Channel;
import com.zb.config.RabbitConfig;
import com.zb.entity.Task;
import com.zb.mapper.QgTaskMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaskMQ {

    @Autowired
    private QgTaskMapper qgTaskMapper;

    @RabbitListener(queues = RabbitConfig.XC_LEARNING_FINISHADDCHOOSECOURSE)
    public void reviceFinsh(Task task , Message message , Channel channel){
        if(task!=null){
            System.out.println("删除任务成功！");
            qgTaskMapper.deleteTask(task.getId());
        }
    }
}
