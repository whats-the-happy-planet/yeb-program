package com.xxxx.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxxx.server.pojo.Admin;
import com.xxxx.server.pojo.RespBean;
import com.xxxx.server.pojo.Role;
import org.apache.ibatis.annotations.Param;

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

    //根据输入的用户名查找操作员
    List<Admin> queryAdminByUserName(@Param("id")Integer id,@Param("keywords")String keywords);

}
