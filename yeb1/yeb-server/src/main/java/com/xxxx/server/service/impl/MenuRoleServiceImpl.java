package com.xxxx.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxxx.server.mapper.MenuMapper;
import com.xxxx.server.mapper.MenuRoleMapper;
import com.xxxx.server.pojo.MenuRole;
import com.xxxx.server.pojo.RespBean;
import com.xxxx.server.service.IMenuRoleService;
import com.xxxx.server.utils.AssertUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author shi
 * @since 2021-04-16
 */
@Service
public class MenuRoleServiceImpl extends ServiceImpl<MenuRoleMapper, MenuRole> implements IMenuRoleService {
    @Resource
    private MenuRoleMapper menuRoleMapper;
    @Resource
    private MenuMapper menuMapper;

    @Override
    @Transactional
    public RespBean updateMenuRole(Integer rid, Integer[] mids) {
        AssertUtil.isTrue(rid==null,"角色ID不能为空");
        menuRoleMapper.delete(new QueryWrapper<MenuRole>().eq("rid", rid));

        if (mids == null || mids.length == 0) {
            return RespBean.success("角色菜单初始化更新成功");
        }
        AssertUtil.isTrue(menuMapper.selectBatchIds(Arrays.asList(mids)).size()!=mids.length,"菜单ID错误,你输入正确的菜单编号");

        AssertUtil.isTrue(menuRoleMapper.insertMenusRole(rid, mids) != mids.length, "更新失败");
        return RespBean.success("更新成功");
    }
}
