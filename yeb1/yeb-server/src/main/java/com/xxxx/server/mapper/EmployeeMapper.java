package com.xxxx.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxxx.server.pojo.Employee;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author shi
 * @since 2021-04-16
 */
public interface EmployeeMapper extends BaseMapper<Employee> {
    /**
     *查询用户信息
     * @param name
     * @return
     */
    List<Employee> queryAllByName(String name);

    /**
     * 修改信息
     * @param employee
     * @return
     */
    Integer updateEmployee(Employee employee);

    /**
     * 增加信息
     * @param employee
     * @return
     */
    Integer insertEmployee(Employee employee);

    List<Employee> maxWorkID();

    IPage<Employee> qyeryAll(Page<Employee> page, @Param("employee")Employee employee, @Param("beginDataScope")LocalDate[] beginDataScope);
    //List<Employee> qyeryAll(Page<Employee> page, @Param("employee")Employee employee, @Param("beginDataScope")LocalDate[] beginDataScope);

    List<Employee> getEmployee(Integer id);

}
