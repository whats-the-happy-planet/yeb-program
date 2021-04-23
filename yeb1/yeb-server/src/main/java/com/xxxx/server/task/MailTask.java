package com.xxxx.server.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xxxx.server.mapper.EmployeeMapper;
import com.xxxx.server.pojo.Employee;
import com.xxxx.server.pojo.MailContext;
import com.xxxx.server.pojo.MailLog;
import com.xxxx.server.service.IMailLogService;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class MailTask {
    @Resource
    private IMailLogService mailLogService;
    @Resource
    private EmployeeMapper employeeMapper ;
    @Resource
    private RabbitTemplate rabbitTemplate;
    /**
     *邮件发送定时任务
     * 10秒钟一次
     */
    @Scheduled(cron = "0/10 * * * * ?")
    public void mailTask(){
        List<MailLog> list = mailLogService.list(new QueryWrapper<MailLog>().eq("status", 0).lt("tryTime", LocalDateTime.now()));
        list.forEach(mailLog -> {
            System.out.println(mailLog);
            if (3<=mailLog.getCount()){
                mailLogService.update(new UpdateWrapper<MailLog>().set("status",2).eq("msgId",mailLog.getMsgId()));
            }
            mailLogService.update(new UpdateWrapper<MailLog>().set("count",mailLog.getCount()+1).set("updateTime",LocalDateTime.now()).set("tryTime",LocalDateTime.now().plusMinutes(MailContext.MSG_TIMEOUT)));
            Employee emp = employeeMapper.getEmployee(mailLog.getEid()).get(0);
            rabbitTemplate.convertAndSend(MailContext.MAIL_EXCHANGE_NAME,MailContext.MAIL_ROUTING_KEY_NAME,emp,new CorrelationData(mailLog.getMsgId()));
        });
    }
}
