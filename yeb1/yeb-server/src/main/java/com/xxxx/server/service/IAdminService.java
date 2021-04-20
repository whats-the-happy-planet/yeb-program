package com.xxxx.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.server.pojo.Admin;
import com.xxxx.server.pojo.RespBean;
import com.xxxx.server.pojo.Role;
import org.springframework.stereotype.Service;

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
     * @return
     */
    RespBean login(String username, String password);

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

    /*
    通过用户查找除了登陆人员之外的操作员
     */
    List<Admin> queryAdminByUserName(String userName);

    //通过操作员id和角色id来更新
    RespBean updateRole(Integer id, Integer[] rids);
}
