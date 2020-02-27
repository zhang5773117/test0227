package com.zb.mapper;

import com.zb.entity.TaskHis;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TaskHisMapper  {

    public TaskHis findHisById(@Param("id") String id);

    public int insertHis(TaskHis taskHis);
}
