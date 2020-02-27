package com.zb.service.impl;

import com.zb.entity.Learning;
import com.zb.entity.Task;
import com.zb.entity.TaskHis;
import com.zb.mapper.LearningMapper;
import com.zb.mapper.TaskHisMapper;
import com.zb.service.LearningService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LearningServiceImpl implements LearningService {

    @Autowired
    private LearningMapper learningMapper;

    @Autowired
    private TaskHisMapper taskHisMapper;
    @Override
    public String addCourse(Learning learning, Task task) {
        //查询是否存储课程记录
        Learning learningByUserAndCourese = learningMapper.findLearningByUserAndCourese(learning.getUser_id(), learning.getCourse_id());
        if(learningByUserAndCourese==null){
            Learning newLearning =new Learning();
            newLearning.setId(com.zb.util.IdWorker.getId());
            newLearning.setCharge("charge");
            newLearning.setCourse_id(learning.getCourse_id());
            newLearning.setEnd_time(learning.getEnd_time());
            newLearning.setPrice(25.0);
            newLearning.setStart_time(learning.getStart_time());
            newLearning.setStatus("41001");
            newLearning.setUser_id(learning.getUser_id());
            newLearning.setValid("valid");
            learningMapper.saveLearning(newLearning);
        }else{
            learningByUserAndCourese.setStart_time(learning.getStart_time());
            learningByUserAndCourese.setEnd_time(learning.getEnd_time());
            learningMapper.updateLearning(learningByUserAndCourese);
        }
        TaskHis hisById = taskHisMapper.findHisById(task.getId());
        if(hisById==null){
            hisById=new TaskHis();
            BeanUtils.copyProperties(task,hisById);
            taskHisMapper.insertHis(hisById);
        }
        return "ok";
    }
}
