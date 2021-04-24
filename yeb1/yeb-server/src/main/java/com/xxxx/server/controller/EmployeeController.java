package com.xxxx.server.controller;


import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.xxxx.server.pojo.*;
import com.xxxx.server.service.INationService;
import com.xxxx.server.service.impl.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDate;
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

    @Resource
    private INationService nationService;

    @Resource
    private PoliticsStatusServiceImpl politicsStatusService;

    @Resource
    private PositionServiceImpl positionService;

    @Resource
    private JoblevelServiceImpl joblevelService;

    @Resource
    private DepartmentServiceImpl departmentService;

    @GetMapping("/basic")
    @ApiOperation(value = "用户名查询")
    public ResultObject queryAllByName(@RequestParam(defaultValue = "1")Integer currentPage, @RequestParam(defaultValue = "10")Integer size, Employee employee, LocalDate[] beginDataScope){
        //return employeeService.queryAllByName(name,currentPage);
        return employeeService.qyeryAll(currentPage,size,employee,beginDataScope);
    }

    @PutMapping("/basic")
    @ApiOperation( value = "修改")
    public RespBean updateEmployee(@RequestBody Employee employee){
       return employeeService.updateEmployee(employee);
    }

    @PostMapping("/basic")
    @ApiOperation(value = "添加")
    public RespBean addAndUpdateEmployee(@RequestBody Employee employee){
       return employeeService.insertEmployee(employee);
    }

    @DeleteMapping("/basic/{id}")
    @ApiOperation(value = "删除")
    public RespBean deleteEmployee(@PathVariable Integer id){

        return employeeService.deleteEmployee(id);
    }

    @GetMapping("/basic/nations")
    @ApiOperation(value = "查询所有民族")
    public List<Nation> queryAllNation(){
       return employeeService.queryAllNation();
    }

    @GetMapping("/basic/joblevels")
    @ApiOperation(value = "查询所有jobs")
    public List<Joblevel> queryAllJoblevel(){
        return employeeService.queryAllJoblevel();
    }

    @GetMapping("/basic/positions")
    @ApiOperation(value = "查询所有positions")
    public List<Position> queryAllPosition(){
        return employeeService.queryAllPosition();
    }

    @GetMapping("/basic/politicsstatus")
    @ApiOperation(value = "查询所有政治面貌")
    public List<PoliticsStatus> queryAllPolit(){
        return employeeService.queryAllPolit();
    }

    @GetMapping("/basic/maxWorkID")
    @ApiOperation(value = "最大ID")
    public RespBean maxWorkID(){
        return employeeService.maxWorkID();
    }

    @GetMapping("/basic/deps")
    @ApiOperation(value = "查询所有部门")
    public List<Department> queryDeps(){
        return employeeService.queryDeps();
    }

    @GetMapping(value = "/basic/export",produces = "application/octet-stream")
    @ApiOperation(value = "导出员工数据")
    public void exportEmployee(HttpServletResponse response){
        List<Employee> list=  employeeService.getEmployee(null);
        ExportParams exportParams = new ExportParams("员工表", "员工表", ExcelType.HSSF);
        Workbook workbook = null;

        if(null!=list && list.size() !=0){  list.get(0);}
            workbook = ExcelExportUtil.exportExcel(exportParams, Employee.class, list);

        ServletOutputStream outputStream = null;
        try {
            response.setHeader("content-type","application/octet-stream");
            response.setHeader("content-disposition","attachment;filename="+ URLEncoder.encode("员工表.xls","utf-8"));
            outputStream=response.getOutputStream();
            workbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (outputStream!=null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @PostMapping("/basic/import")
    @ApiOperation(value = "导入员工数据")
    public RespBean importEmployee(MultipartFile file){
        ImportParams importParams = new ImportParams();
        importParams.setTitleRows(1);
        List<Nation> nationList=nationService.list();
        List<Joblevel> joblist = joblevelService.list();
        List<PoliticsStatus> politlist = politicsStatusService.list();
        List<Position> poslist = positionService.list();
        List<Department> departlist = departmentService.list();
        try {
            //名族ID
            List<Employee> result = ExcelImportUtil.importExcel(file.getInputStream(),
                    Employee.class, importParams);
            result.forEach(employee -> {
                employee.setNationId(nationList.get(nationList.indexOf(new Nation(employee.getNation().getName()))).getId());
                employee.setJobLevelId(joblist.get(joblist.indexOf(new Joblevel(employee.getJoblevel().getName()))).getId());
                employee.setPoliticId(politlist.get(politlist.indexOf(new PoliticsStatus(employee.getPoliticsStatus().getName()))).getId());
                employee.setPosId(poslist.get(poslist.indexOf(new Position(employee.getPosition().getName()))).getId());
                employee.setDepartmentId(departlist.get(departlist.indexOf(new Department(employee.getDepartment().getName()))).getId());
            });
            if (employeeService.saveBatch(result)){
                return RespBean.success("导入成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespBean.error("导入失败");
    }
}
