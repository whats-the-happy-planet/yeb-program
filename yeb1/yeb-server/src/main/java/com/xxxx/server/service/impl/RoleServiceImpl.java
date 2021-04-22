package com.xxxx.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.xxxx.server.mapper.AdminRoleMapper;
import com.xxxx.server.mapper.MenuRoleMapper;
import com.xxxx.server.mapper.RoleMapper;
import com.xxxx.server.pojo.Joblevel;
import com.xxxx.server.pojo.Role;
import com.xxxx.server.service.IRoleService;
import com.xxxx.server.utils.AssertUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author shi
 * @since 2021-04-16
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private AdminRoleMapper adminRoleMapper;
    @Resource
    private MenuRoleMapper menuRoleMapper;

    @Override
    public void addRole(Role role) {
        AssertUtil.isTrue(role == null, "请填写添加信息");
        AssertUtil.isTrue(StringUtils.isBlank(role.getName()), "角色名称不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(role.getNameZh()), "中文全称不能为空");

        //角色表中的英文角色名都是以ROLE_开头的
        String name = role.getName();
        if (!name.startsWith("ROLE_")) {
            name = "ROLE_" + role.getName();
            role.setName(name);
        }
        //判断英文角色名是否可用
        QueryWrapper<Role> wrapper = new QueryWrapper<>();
        wrapper.eq("name", name);
        Role dbRole = roleMapper.selectOne(wrapper);
        AssertUtil.isTrue(dbRole != null, "英文角色名已存在");

        //判断英文角色名是否可用
        QueryWrapper<Role> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("nameZh", role.getNameZh());
        Role dbRole1 = roleMapper.selectOne(wrapper1);
        AssertUtil.isTrue(dbRole1 != null, "中文全称已存在");


        AssertUtil.isTrue(roleMapper.insert(role) < 1, "角色添加失败");

    }

    @Override
    public void deleteRole(Integer rid) {
        AssertUtil.isTrue(rid == null, "待删角色ID不能为空");
        AssertUtil.isTrue(roleMapper.selectById(rid) == null, "待删角色不存在");
        AssertUtil.isTrue(adminRoleMapper.selectByRid(rid).size() > 0, "角色下存在其他用户无法删除，请先调整角色下的所有用户");
        AssertUtil.isTrue(menuRoleMapper.selectByRid(rid).size() > 0, "角色下有其余权限，请先调整角色下所有的权限");

        AssertUtil.isTrue(roleMapper.deleteById(rid) < 1, "删除角色失败");
    }
}
