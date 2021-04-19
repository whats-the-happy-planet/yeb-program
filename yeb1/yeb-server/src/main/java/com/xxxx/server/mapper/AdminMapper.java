package com.xxxx.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxxx.server.pojo.Admin;
import com.xxxx.server.pojo.Role;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author shi
 * @since 2021-04-16
 */
public interface AdminMapper extends BaseMapper<Admin> {

    //根据用户信息查询对应角色
    List<Admin> quryRoleByAdmin(Integer id);

    //根据ID查询角色
    List<Role> quryRoles(Integer id);
}
