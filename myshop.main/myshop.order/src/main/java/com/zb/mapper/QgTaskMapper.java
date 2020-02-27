package com.zb.mapper;

import com.zb.entity.Task;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface QgTaskMapper {

    public List<Task> findBeforOneMinuteTaskList();

    public Task findTaskById(@Param("id") String id);

    public int updateTaskTime(@Param("id") String id);

    public int updateTaskVersion(@Param("id") String id ,@Param("version")Integer version);

    public int deleteTask(@Param("id") String id);
}
