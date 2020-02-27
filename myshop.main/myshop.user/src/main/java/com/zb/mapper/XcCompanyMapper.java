package com.zb.mapper;

import com.zb.entity.Company;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface XcCompanyMapper {
    public Company getCompanyByUserId(@Param("userid") String userid);
}
