package com.zb.mapper;

import com.zb.entity.Learning;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LearningMapper {

    public Learning findLearningByUserAndCourese(@Param("userId") String userId , @Param("courseId") String courseId);

    public int updateLearning(Learning learning);

    public int saveLearning(Learning learning);
}
