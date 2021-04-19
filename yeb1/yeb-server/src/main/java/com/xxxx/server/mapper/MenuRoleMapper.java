package com.xxxx.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxxx.server.pojo.MenuRole;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author shi
 * @since 2021-04-16
 */
public interface MenuRoleMapper extends BaseMapper<MenuRole> {
    /**
     * 根据角色ID查询角色下的所有权限
     * @param rid
     * @return
     */
    List<MenuRole> selectByRid(Integer rid);
}
