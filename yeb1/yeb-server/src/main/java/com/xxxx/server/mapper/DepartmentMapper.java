package com.xxxx.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxxx.server.pojo.Department;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author shi
 * @since 2021-04-16
 */
public interface DepartmentMapper extends BaseMapper<Department> {

    List<Department> getAllDepartments(Integer parentId);

    void addDep(Department dep);

    void deleteDep(Department dep);
}
