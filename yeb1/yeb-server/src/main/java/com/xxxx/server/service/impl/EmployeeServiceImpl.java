package com.xxxx.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxxx.server.mapper.EmployeeMapper;
import com.xxxx.server.pojo.Employee;
import com.xxxx.server.pojo.RespBean;
import com.xxxx.server.service.IEmployeeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author shi
 * @since 2021-04-16
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements IEmployeeService {
    @Resource
    private EmployeeMapper employeeMapper;
    @Resource
    private PaginationInterceptor paginationInterceptor;

    @Override
    public List<Employee> queryAllByName(String name, Integer currentPage) {

        return employeeMapper.queryAllByName(name);
//        if(currentPage==null){
//            currentPage=1;
//        }
//        Page<Employee> page = new Page<>(currentPage,10);
//        if(name!=null &&name!=""){
////            QueryWrapper<Employee> query = new QueryWrapper<>();
////            query.like("name",name);
////            //return (List<Employee>) employeeMapper.selectPage(page,query);
////            return employeeMapper.selectList(query);
//            return employeeMapper.queryAllByName(name);
//        }
//        //return (List<Employee>) employeeMapper.selectPage(page,null);
//        return employeeMapper.selectList(null);
    }

    @Override
    public Integer updateEmployee(Employee employee) {
           return employeeMapper.updateById(employee);
    }

    @Override
    public Integer insertEmployee(Employee employee) {
        checkData(employee);
        return employeeMapper.insert(employee);
    }

    private void checkData(Employee employee) {
        if (employee.getName()==null||employee.getName()==""){
            RespBean.error("姓名为空");
        }
        if (employee.getGender()==null&&employee.getGender()==""){
            RespBean.error("性别为空");
        }
        if (employee.getBirthday()==null){
            RespBean.error("出生日期为空");
        }
        if (employee.getPoliticId()==null){
            RespBean.error("政治面貌为空");
        }
        if (employee.getNationId()==null){
            RespBean.error("名族不能为空");
        }
        if (employee.getNativePlace()==null||employee.getNativePlace()==""){
            RespBean.error("籍贯不能为空");
        }
        if (employee.getEmail()==null||employee.getEmail()==""){
            RespBean.error("邮箱为空");
        }
        if (employee.getAddress()==null&&employee.getAddress()==""){
            RespBean.error("联系地址为空");
        }

        if (employee.getPhone()==null||employee.getPhone()==""){
            RespBean.error("电话为空");
        }
        if (!checkPhone(employee.getPhone())){
            RespBean.error("电话格式不正确");
        }
        if (employee.getWedlock()==null||employee.getWedlock()==""||(employee.getWedlock()!="已婚"&&employee.getWedlock()!="未婚"&&employee.getWedlock()!="离异")){
            RespBean.error("婚姻状况的信息不正确");
        }

    }

    private boolean checkPhone(String phone) {
       return isChinaPhoneLegal(phone) || isHKPhoneLegal(phone);
    }

    @Override
    public void deleteEmployee(Employee employee) {
       if (employee.getName()==null||employee.getName()==""){
           RespBean.error("用户名为空");
       }
    }

    /**
     *
     * 大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数
     * 此方法中前三位格式有：
     * 13+任意数
     * 145,147,149
     * 15+除4的任意数(不要写^4，这样的话字母也会被认为是正确的)
     * 166
     * 17+3,5,6,7,8
     * 18+任意数
     * 198,199
     * @param str
     * @return 正确返回true
     * @throws PatternSyntaxException
     */
    public static boolean isChinaPhoneLegal(String str) throws PatternSyntaxException {
        // ^ 匹配输入字符串开始的位置
        // \d 匹配一个或多个数字，其中 \ 要转义，所以是 \\d
        // $ 匹配输入字符串结尾的位置
        String regExp = "^((13[0-9])|(14[5,7,9])|(15[0-3,5-9])|(166)|(17[3,5,6,7,8])" +
                "|(18[0-9])|(19[8,9]))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 香港手机号码8位数，5|6|8|9开头+7位任意数
     * @param str
     * @return 正确返回true
     * @throws PatternSyntaxException
     */
    public static boolean isHKPhoneLegal(String str) throws PatternSyntaxException {
        // ^ 匹配输入字符串开始的位置
        // \d 匹配一个或多个数字，其中 \ 要转义，所以是 \\d
        // $ 匹配输入字符串结尾的位置
        String regExp = "^(5|6|8|9)\\d{7}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }
}
