package com.zb.mapper;

import com.zb.entity.XcUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface XcUserMapper {

    public XcUser findUserByusername(@Param("username") String username);

}
