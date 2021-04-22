package com.xxxx.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xxxx.server.pojo.Admin;
import com.xxxx.server.pojo.AdminLogin;
import com.xxxx.server.pojo.LoginParam;
import com.xxxx.server.pojo.RespBean;
import com.xxxx.server.service.IAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RestController
@Api(value = "登录")
public class LoginController {

    @Resource
    private IAdminService adminService;

    @ApiOperation(value = "登录成功返回token")
    @PostMapping("/login")
    public RespBean login(@RequestBody AdminLogin adminLogin, HttpServletRequest request) {
        System.out.println(adminLogin.getUsername());
        request.getSession().setAttribute("username",adminLogin.getUsername());
        return adminService.login(adminLogin.getUsername(),
                adminLogin.getPassword(), adminLogin.getCode()
                , request);
    }

    @ApiOperation(value = "获取当前登录用户信息")
    @GetMapping("/admin/info")
    public Admin getAdminInfo(Principal principal){
        if (principal == null){
            return null;
        }
        String username = principal.getName();
        Admin admin = adminService.getAdminByUserName(username);
        //System.out.println("admin = " + admin);
        admin.setPassword(null);
        admin.setRoles(adminService.getRolesByAdminId(admin.getId()));
        return admin;
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
