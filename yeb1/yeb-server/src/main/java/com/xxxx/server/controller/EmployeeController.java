package com.xxxx.server.controller;


import com.xxxx.server.pojo.Employee;
import com.xxxx.server.pojo.RespBean;
import com.xxxx.server.service.impl.EmployeeServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author shi
 * @since 2021-04-16
 */
@RestController
@RequestMapping("/employee")
@Api(value = "员工基本资料")
public class EmployeeController {
    @Resource
    private EmployeeServiceImpl employeeService;

    @GetMapping("/basic")
    @ApiOperation(value = "用户名模糊查询")
    public List<Employee> queryAllByName(String name,Integer currentPage){
        return employeeService.queryAllByName(name,currentPage);
    }

    @PutMapping("/basic")
    @ApiOperation(value = "修改")
    public RespBean updateEmployee(Employee employee){
        employeeService.updateEmployee(employee);
        return RespBean.success("修改成功");
    }

    @PostMapping("/basic")
    @ApiOperation(value = "添加")
    public RespBean addAndUpdateEmployee(Employee employee){
//        if (employee.getId()!=null){
//            employeeService.updateEmployee(employee);
//            return RespBean.success("修改成功");
//        }
        employeeService.insertEmployee(employee);
        return RespBean.success("增加成功");
    }

    @DeleteMapping("/basic")
    @ApiOperation(value = "删除")
    public RespBean deleteEmployee(Employee employee){
        employeeService.deleteEmployee(employee);
        return  RespBean.success("删除成功");
    }
}
