package com.xxxx.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.server.pojo.Menu;
import com.xxxx.server.pojo.Role;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author shi
 * @since 2021-04-16
 */
public interface IMenuService extends IService<Menu> {

    //根据登录用户名查询拥有的权限
    List<Menu> getMenuByAdminName();

    //根据当前资源，查询可使用他的角色
    List<Menu> getRoleByMenuId();
}
