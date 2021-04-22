package com.xxxx.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.server.pojo.Department;
import com.xxxx.server.pojo.RespBean;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author shi
 * @since 2021-04-16
 */
public interface IDepartmentService extends IService<Department> {

    List<Department> getAllDepartments();

    RespBean addDep(Department dep);

    RespBean deleteDep(Integer id);

}
