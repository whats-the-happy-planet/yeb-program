package com.xxxx.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.server.pojo.Admin;
import com.xxxx.server.pojo.RespBean;
import com.xxxx.server.pojo.Role;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author shi
 * @since 2021-04-16
 */
@Service
public interface IAdminService extends IService<Admin> {


    /**
     * 登录
     * @param username
     * @param password
     * @param code
     * @param request
     * @return
     */
    RespBean login(String username, String password, String code, HttpServletRequest request);

    /**
     * 通过用户名返回查询对象
     * @return
     */
    Admin quryAdminByName(String name);

    /**
     * 通过用户名找到对应的角色
     * @return
     */
    List<Admin> quryRoleByAdmin(Integer id);


    /**
     * 通过用户信息找到对应的角色
     * @return
     */
    List<Role> quryRoles(Integer id);


    Admin getAdminByUserName(String username);

    List<Role> getRolesByAdminId(Integer id);

    /**
     * 更新当前用户信息
     * @param admin
     * @return
     */
    Integer updateAdmin(Admin admin);

    /**
     * 更新用户密码
     * @param oldPass
     * @param pass
     * @param adminId
     * @return
     */
    RespBean updateAdminPassword(String oldPass, String pass, Integer adminId);
}
