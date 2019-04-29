package com.mall.portal.service;

import com.mall.common.CommonResult;
import com.mall.mbg.model.UserInfo;

import java.util.Map;

public interface UserService {

    CommonResult addUser(UserInfo user);

    CommonResult editUser(UserInfo user);

    CommonResult deleteUser(int userId);

    UserInfo getUserById(int userId);

    UserInfo userLogin(Map paramMap);
}
