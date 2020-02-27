package com.zb.job;

import com.zb.entity.Task;
import com.zb.service.QgTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TaskJob  {
    @Autowired
    private QgTaskService qgTaskService;
    @Scheduled(cron = "0/3 * * * * *")
    public void send(){
        List<Task> taskList = qgTaskService.findBeforOneMinuteTaskList();
        if(taskList.size()<=0){
            System.out.println("没有任务.....");
        }
        for (Task task : taskList) {
            System.out.println(task.getRequest_body()+"\t"+task.getMq_exchange()+"\t"+task.getMq_routingkey());
            //锁的机制
            if(qgTaskService.getTask(task.getId(),task.getVersion())>0) {
                qgTaskService.publish(task.getId(), task.getMq_exchange(), task.getMq_routingkey());
            }
        }
    }

}
