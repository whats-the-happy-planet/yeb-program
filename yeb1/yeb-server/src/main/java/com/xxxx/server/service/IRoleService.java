package com.xxxx.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.server.pojo.Role;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author shi
 * @since 2021-04-16
 */
public interface IRoleService extends IService<Role> {
    /**
     * 添加角色
     * @param role
     */
    void addRole(Role role);

    /**
     * 删除角色
     * @param rid
     */
    void deleteRole(Integer rid);
}
