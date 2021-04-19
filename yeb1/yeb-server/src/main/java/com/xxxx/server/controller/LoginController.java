package com.xxxx.server.controller;

import com.xxxx.server.pojo.Admin;
import com.xxxx.server.pojo.LoginParam;
import com.xxxx.server.pojo.RespBean;
import com.xxxx.server.service.IAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@Api(value = "登录")
public class LoginController {

    @Resource
    private IAdminService adminService;

    @ApiOperation(value = "登录接口")
    @PostMapping("/login")
    public RespBean login(LoginParam loginParam){
        return adminService.login(loginParam.getUserName(),loginParam.getPassword());
    }


    @ApiOperation(value = "退出登录")
    @GetMapping("/logout")
    public RespBean logout(){
        return RespBean.success("退出成功");
    }


    @ApiOperation(value = "根据用户名查询对象")
    @PostMapping("/quryAdminByName")
    public Admin quryAdminByName(String name){
        return adminService.quryAdminByName(name);
    }
}
