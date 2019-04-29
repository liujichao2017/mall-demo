package com.mall.portal.controller;

import com.mall.common.CommonResult;
import com.mall.mbg.model.UserInfo;
import com.mall.portal.domain.User;
import com.mall.portal.service.MailService;
import com.mall.portal.service.RedisService;
import com.mall.portal.service.UserService;
import com.mall.portal.util.JwtTokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Controller
@RequestMapping("/user")
@Api(value = "用户管理", produces = "用户管理API",consumes="用户管理API", protocols = "http")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private MailService mailService;

    @Autowired
    private TemplateEngine templateEngine;

    @ApiOperation(value = "获取用户详细信息", notes = "根据url的id来获取用户详细信息")
    @RequestMapping(value = "/getUserInfo/{id}", method = RequestMethod.GET)
    @ResponseBody
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Long", paramType = "path")
    public User getUser(@PathVariable Long id) {
        User user = new User();
        user.setId(111l);
        user.setAge(22);
        user.setName("4444");
        return user;
    }

    @ResponseBody
    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    public CommonResult addUser(@RequestParam String username,
                                 @RequestParam String password,
                                 @RequestParam String useraddr) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserName(username);
        userInfo.setUserPass(password);
        userInfo.setUserAddr(useraddr);
        return userService.addUser(userInfo);
    }

    @ResponseBody
    @RequestMapping(value = "/editUser", method = RequestMethod.POST)
    public CommonResult editUser(@RequestParam int userid,
                                @RequestParam String username,
                                @RequestParam String password,
                                @RequestParam String useraddr) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(userid);
        userInfo.setUserName(username);
        userInfo.setUserPass(password);
        userInfo.setUserAddr(useraddr);
        return userService.editUser(userInfo);
    }

    @ResponseBody
    @RequestMapping(value = "/deleteUser", method = RequestMethod.POST)
    public CommonResult editUser(@RequestParam int userid) {

        return userService.deleteUser(userid);
    }

    @ResponseBody
    @RequestMapping(value = "/getUserById", method = RequestMethod.POST)
    public UserInfo getUserById(@RequestParam int userid) {

        return userService.getUserById(userid);
    }

    @ResponseBody
    @RequestMapping(value = "/userLogin", method = RequestMethod.POST)
    public CommonResult userLogin(@RequestParam String username,
                                  @RequestParam String userpass) {
        Map paramMap = new HashMap<String,String>();
        paramMap.put("username",username);
        paramMap.put("userpass",userpass);
        UserInfo user = userService.userLogin(paramMap);
        if(user != null){
            String token = jwtTokenUtil.generateToken(paramMap);
            redisService.set("usertoken","usertoken:" + token);
            return CommonResult.success(token,"登录成功");
        }
        else{
            return CommonResult.failed("用户名或密码错误");
        }

    }

    @ResponseBody
    @RequestMapping(value = "/sendMail", method = RequestMethod.POST)
    public CommonResult userLogin(@RequestParam String toAddr,
                                  @RequestParam String mailSubject,
                                  @RequestParam String mailContent){
        String filePath = "/Users/liujichao/Documents/ideaprojects/mall-demo/mall-portal/src/main/resources/templates/images/mail.jpg";
        String rscId = "picture";
        mailService.sendInlineResourceMail(toAddr,mailSubject,mailContent,filePath,rscId);

        //创建邮件正文
//        Context context = new Context();
//        context.setVariable("id", "006");
//        String emailContent = templateEngine.process("emailTemplate", context);
//
//        mailService.sendHtmlMail(toAddr,mailSubject,emailContent);
        return CommonResult.success("发送成功11");
    }
}
