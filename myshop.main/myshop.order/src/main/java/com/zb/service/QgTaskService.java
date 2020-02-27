package com.zb.service;

import com.zb.entity.Task;

import java.util.List;

public interface QgTaskService {
    public List<Task> findBeforOneMinuteTaskList();

    public void publish(String taskId , String exchange , String routingKey);

    public int getTask(String id , Integer version);
}
