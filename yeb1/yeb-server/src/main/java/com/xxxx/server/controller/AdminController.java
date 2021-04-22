package com.xxxx.server.controller;


import com.xxxx.server.service.IAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author shi
 * @since 2021-04-16
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private IAdminService adminService;
    @Resource
    private IRoleService roleService;

    @ApiOperation(value = "通过用户名查找操作员")
    @GetMapping("/")
    public List<Admin> queryAdminByUserName(String keywords){
        return adminService.queryAdminByUserName(keywords);
    }

    @ApiOperation(value = "更改操作员信息")
    @PutMapping("/")
    public RespBean update(@RequestBody Admin admin){
        if(adminService.updateById(admin)){
            return RespBean.success("更改成功");
        }
        return RespBean.error("更改失败");
    }

    //只有系统管理员才可以删除操作员
    @ApiOperation(value = "删除操作员")
    @DeleteMapping("/{id}")
    public RespBean deleteById(@PathVariable Integer id){
        if ("admin".equals(AdminUtils.getCurrentAdmin().getUsername())) {
            if(adminService.removeById(id)){
                return RespBean.success("更新成功");
            }
            return RespBean.error("更新失败");
        }else{
            return  RespBean.error("您没有权限删除，请联系管理员");
        }
    }

    @ApiOperation(value = "查找操作员所拥有的的角色")
    @GetMapping("/roles")
    public List<Role> getAllRoles(){
        return roleService.list();
    }

    @ApiOperation(value = "更新操作员的角色")
    @PutMapping("/role")
    public RespBean updateRole(Integer adminId,Integer[] rids){
        return adminService.updateRole(adminId,rids);
    }
}
