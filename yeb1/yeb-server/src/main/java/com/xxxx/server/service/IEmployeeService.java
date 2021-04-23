package com.xxxx.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.server.pojo.*;

import java.time.LocalDate;
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

    ResultObject queryAllByName(String name,Integer currentPage);

    RespBean updateEmployee(Employee employee);

    RespBean insertEmployee(Employee employee);

    RespBean deleteEmployee(Integer id);

    List<Nation> queryAllNation();

    List<Joblevel> queryAllJoblevel();

    List<Position> queryAllPosition();

    List<PoliticsStatus> queryAllPolit();

    RespBean maxWorkID();

    List<Department> queryDeps();

    ResultObject qyeryAll(Integer currentPage, Integer size, Employee employee, LocalDate[] beginDataScope);

    List<Employee> getEmployee(Integer id);
}
