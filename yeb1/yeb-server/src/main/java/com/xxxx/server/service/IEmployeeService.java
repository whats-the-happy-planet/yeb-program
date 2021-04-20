package com.xxxx.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.server.pojo.Employee;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author shi
 * @since 2021-04-16
 */
public interface IEmployeeService extends IService<Employee> {

    List<Employee> queryAllByName(String name,Integer currentPage);

    Integer updateEmployee(Employee employee);

    Integer insertEmployee(Employee employee);

    void deleteEmployee(Employee employee);

}
