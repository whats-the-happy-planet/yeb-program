package com.xxxx.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxxx.server.mapper.*;
import com.xxxx.server.pojo.*;
import com.xxxx.server.service.IEmployeeService;
import com.xxxx.server.utils.IDCardUtil;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * <p>
 * 服务实现类
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

    @Resource
    private NationMapper nationMapper;

    @Resource
    private JoblevelMapper joblevelMapper;

    @Resource
    private PositionMapper positionMapper;

    @Resource
    private PoliticsStatusMapper politicsStatusMapper;

    @Resource
    private DepartmentMapper departmentMapper;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private MailLogMapper mailLogMapper;
//    @Resource
//    private  EmployeeMapper employeeMapper;

    @Override
    public RespPageBean getEmployeeWithSalary(Integer currentPage, Integer size) {
        Page<Employee> page = new Page<>(currentPage, size);
        IPage<Employee> employeeIPage = employeeMapper.getEmployeeWithSalary(page);
        RespPageBean respPageBean = new RespPageBean(employeeIPage.getTotal(),employeeIPage.getRecords());
        return respPageBean;
    }


    //    @Override
//    public ResultObject queryAllByName(String name, Integer currentPage) {
//        ResultObject resultObject=new ResultObject();
//        List<Employee> employees = employeeMapper.queryAllByName(name);
//        resultObject.setData(employees);
//        return resultObject;
//
////        if(currentPage==null){
////            currentPage=1;
////        }
////        Page<Employee> page = new Page<>(currentPage,10);
////        if(name!=null &&name!=""){
//////            QueryWrapper<Employee> query = new QueryWrapper<>();
//////            query.like("name",name);
//////            //return (List<Employee>) employeeMapper.selectPage(page,query);
//////            return employeeMapper.selectList(query);
////            return employeeMapper.queryAllByName(name);
////        }
////        //return (List<Employee>) employeeMapper.selectPage(page,null);
////        return employeeMapper.selectList(null);
//    }
    @Override
    public ResultObject qyeryAll(Integer currentPage, Integer size, Employee employee, LocalDate[] beginDataScope) {
        Page<Employee> page = new Page<>(currentPage, size);
        IPage<Employee> employees = employeeMapper.qyeryAll(page, employee, beginDataScope);
        ResultObject resultObject = new ResultObject(employees.getTotal(),employees.getRecords());
        return resultObject;
    }

    @Override
    public List<Employee> getEmployee(Integer id) {
        return employeeMapper.getEmployee(id);
    }

    @Override
    public ResultObject queryAllByName(String name, Integer currentPage) {
        return null;
    }

    @Override
    public RespBean updateEmployee(Employee employee) {
        if(checkCardId(employee.getIdCard())){
            return RespBean.error("身份证格式不正确");
        }
        if (!checkPhone(employee.getPhone())){
            return RespBean.error("手机格式不正确");
        }
        if (employeeMapper.updateById(employee) == 1) {
            return RespBean.success("修改成功");
        }
        return RespBean.error("修改失败");
    }

    @Override
    public RespBean insertEmployee(Employee employee) {
        if(checkCardId(employee.getIdCard())){
            return RespBean.error("身份证格式不正确");
        }
        if (!checkPhone(employee.getPhone())){
            return RespBean.error("手机格式不正确");
        }

//        LocalDate beginContract = employee.getBeginContract();
//        LocalDate endContract = employee.getEndContract();
//        long until = beginContract.until(endContract, ChronoUnit.DAYS);
//        DecimalFormat decimalFormat = new DecimalFormat("##.00");
//        employee.setContractTerm(Double.parseDouble(decimalFormat.format(until / 3)));
        if (employeeMapper.insert(employee) == 1) {

            Employee emp = employeeMapper.getEmployee(employee.getId()).get(0);
            String msgId = UUID.randomUUID().toString();
            MailLog mailLog=new MailLog();
            mailLog.setMsgId(msgId);
            mailLog.setEid(employee.getId());
            mailLog.setStatus(0);
            mailLog.setRouteKey(MailContext.MAIL_ROUTING_KEY_NAME);
            mailLog.setExchange(MailContext.MAIL_EXCHANGE_NAME);
            mailLog.setCount(0);
            mailLog.setTryTime(LocalDateTime.now().plusMinutes(MailContext.MSG_TIMEOUT));
            mailLog.setCreateTime(LocalDateTime.now());
            mailLog.setUpdateTime(LocalDateTime.now());
            mailLogMapper.insert(mailLog);
            rabbitTemplate.convertAndSend(MailContext.MAIL_EXCHANGE_NAME,MailContext.MAIL_ROUTING_KEY_NAME,emp,new CorrelationData(msgId));
            return RespBean.success("添加成功");
        }
        return RespBean.error("添加失败");
    }


    private boolean checkCardId(String idCard) {
        if (IDCardUtil.IDCardValidate(idCard) == "YES") {
            return false;
        }
        return true;
    }

    private boolean checkPhone(String phone) {
        return isChinaPhoneLegal(phone) || isHKPhoneLegal(phone);
    }

    @Override
    public RespBean deleteEmployee(Integer id) {
        if (employeeMapper.deleteById(id) == 1) {
            return RespBean.success("删除成功");
        }
        return RespBean.success("删除失败");
    }

    @Override
    public List<Nation> queryAllNation() {
        return nationMapper.selectList(null);
    }

    @Override
    public List<Joblevel> queryAllJoblevel() {
        return joblevelMapper.selectList(null);
    }

    @Override
    public List<Position> queryAllPosition() {
        return positionMapper.selectList(null);
    }

    @Override
    public List<PoliticsStatus> queryAllPolit() {
        return politicsStatusMapper.selectList(null);
    }

    @Override
    public RespBean maxWorkID() {
        List<Map<String, Object>> maps = employeeMapper.selectMaps(new QueryWrapper<Employee>().select("max(workID)"));
        return RespBean.success(null, String.format("%08d", Integer.parseInt(maps.get(0).get("max(workID)").toString()) + 1));
    }

    @Override
    public List<Department> queryDeps() {
        return departmentMapper.selectList(null);
    }


    /**
     * 大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数
     * 此方法中前三位格式有：
     * 13+任意数
     * 145,147,149
     * 15+除4的任意数(不要写^4，这样的话字母也会被认为是正确的)
     * 166
     * 17+3,5,6,7,8
     * 18+任意数
     * 198,199
     *
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
     *
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
