package com.mall.portal.service.impl;

import com.mall.common.CommonResult;
import com.mall.mbg.mapper.UserInfoMapper;
import com.mall.mbg.model.UserInfo;
import com.mall.portal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public CommonResult addUser(UserInfo user) {
//        UserInfo userInfo  = new UserInfo();
//        userInfo.setUserName("张三");
//        userInfo.setUserPass("123456");
//        userInfo.setUserAddr("枫林三路");
        userInfoMapper.insert(user);

        return CommonResult.success(null,"注册成功");
    }

    @Override
    public CommonResult editUser(UserInfo user) {
        userInfoMapper.updateByPrimaryKeySelective(user);
        return CommonResult.success(null,"修改成功");
    }

    @Override
    public CommonResult deleteUser(int userId) {
        userInfoMapper.deleteByPrimaryKey(userId);
        return CommonResult.success(null,"删除成功");
    }

    @Override
    public UserInfo getUserById(int userId) {
        return userInfoMapper.selectByPrimaryKey(userId);
    }

    @Override
    public UserInfo userLogin(Map paramMap) {
        return userInfoMapper.selectByLogin(paramMap);
    }

}
