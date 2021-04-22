package com.xxxx.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xxxx.server.pojo.Menu;
import com.xxxx.server.pojo.MenuRole;
import com.xxxx.server.pojo.RespBean;
import com.xxxx.server.pojo.Role;
import com.xxxx.server.service.IRoleService;
import com.xxxx.server.service.impl.MenuRoleServiceImpl;
import com.xxxx.server.service.impl.MenuServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/system/basic/permiss")
public class PermissionController {
    @Resource
    private IRoleService roleService;
    @Resource
    private MenuServiceImpl menuService;
    @Resource
    private MenuRoleServiceImpl menuRoleService;

    @ApiOperation(value = "查看所有角色")
    @GetMapping("/")
    public List<Role> getAllRoles() {
        return roleService.list();
    }

    @ApiOperation(value = "添加角色")
    @PostMapping("/role")
    public RespBean addRole(@RequestBody Role role) {
        roleService.addRole(role);
        return RespBean.success("添加角色成功");
    }

    @ApiOperation(value = "删除角色")
    @DeleteMapping("/role/{rid}")
    public RespBean deleteRole(Integer rid) {
        roleService.deleteRole(rid);
        return RespBean.success("删除角色成功");
    }

    @ApiOperation(value = "查询所有菜单包含子菜单")
    @GetMapping("/menus")
    public List<Menu> getAllMenus() {
        return menuService.getAllMenus();
    }

    @ApiOperation(value = "根据角色ID查询菜单ID")
    @GetMapping("/mid/{rid}")
    public List<Integer> getMidByRid(@PathVariable Integer rid) {
        //QueryWrapper<MenuRole> wrapper = new QueryWrapper<>();
        //wrapper.eq("rid",rid);
        //List<MenuRole> menuRoleList = menuRoleService.list(wrapper);

        return menuRoleService.list(new QueryWrapper<MenuRole>().eq("rid", rid))
                .stream().map(MenuRole::getMid).collect(Collectors.toList());
    }

    @ApiOperation(value = "更新角色菜单")
    @PutMapping("/")
    public RespBean updateMenuRole(Integer rid, Integer[] mids) {
        return menuRoleService.updateMenuRole(rid, mids);
    }
}
