package com.xxxx.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxxx.server.pojo.AdminRole;
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
public interface AdminRoleMapper extends BaseMapper<AdminRole> {
    //通过操作员的id和角色id来对某个操作员的角色修改
    Integer updateRole(@Param("adminId") Integer adminId, @Param("rids") Integer[] rids);
    /**
     * 根据角色ID查询角色下的所有用户
     * @param rid
     * @return
     */
    List<AdminRole> selectByRid(Integer rid);
}
