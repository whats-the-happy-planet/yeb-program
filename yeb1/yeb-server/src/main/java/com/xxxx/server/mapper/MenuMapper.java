package com.xxxx.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxxx.server.pojo.Menu;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author shi
 * @since 2021-04-16
 */
public interface MenuMapper extends BaseMapper<Menu> {

    //根据用户ID查询对应权限
    List<Menu> getMenuByAdminId(Integer id);

    //根据资源查询角色列表
    List<Menu> getRoleByMenuId();
}
