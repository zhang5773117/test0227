package com.zb.service.impl;

import com.zb.entity.Task;
import com.zb.mapper.QgTaskMapper;
import com.zb.service.QgTaskService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QgTaskServiceImpl implements QgTaskService {
    @Autowired
    private QgTaskMapper qgTaskMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public List<Task> findBeforOneMinuteTaskList() {
        return qgTaskMapper.findBeforOneMinuteTaskList();
    }


    @Override
    public void publish(String taskId, String exchange, String routingKey) {
        Task taskById = qgTaskMapper.findTaskById(taskId);
        if(taskById!=null){
            rabbitTemplate.convertAndSend(exchange,routingKey,taskById);
            qgTaskMapper.updateTaskTime(taskId);
        }
    }

    @Override
    public int getTask(String id, Integer version) {
        return qgTaskMapper.updateTaskVersion(id,version);
    }


}
