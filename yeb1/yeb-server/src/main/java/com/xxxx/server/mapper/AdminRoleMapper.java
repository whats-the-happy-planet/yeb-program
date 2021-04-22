package com.xxxx.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxxx.server.pojo.AdminRole;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author shi
 * @since 2021-04-16
 */
public interface AdminRoleMapper extends BaseMapper<AdminRole> {
    /**
     * 根据角色ID查询角色下的所有用户
     * @param rid
     * @return
     */
    List<AdminRole> selectByRid(Integer rid);
}
