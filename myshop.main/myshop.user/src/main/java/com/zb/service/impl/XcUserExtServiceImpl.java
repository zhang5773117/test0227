package com.zb.service.impl;

import com.zb.entity.Company;
import com.zb.entity.XcUser;
import com.zb.entity.XcUserExt;
import com.zb.mapper.XcCompanyMapper;
import com.zb.mapper.XcUserMapper;
import com.zb.service.XcUserExtService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class XcUserExtServiceImpl implements XcUserExtService {
    @Autowired(required = false)
    private XcUserMapper xcUserMapper;
    @Autowired(required = false)
    private XcCompanyMapper xcCompanyMapper;
    @Override
    public XcUserExt getXcUserExtByUsername(String username) {
        //根据用户名称查询用户信息
        XcUser userByusername = xcUserMapper.findUserByusername(username);
        //根据哟用户编号查询企业信息
        Company companyByUserId = xcCompanyMapper.getCompanyByUserId(userByusername.getId());
        //跟用户编号查询权限集合
        //自己完善（今天的家庭作业....）
        XcUserExt xcUserExt =new XcUserExt();
        //将userByusername属性拷贝到xcUserExt中，两个类型中的属性名称一模一样的才好拷贝
        BeanUtils.copyProperties(userByusername,xcUserExt);
        xcUserExt.setCompanyId(companyByUserId.getName());
        return xcUserExt;
    }
}
