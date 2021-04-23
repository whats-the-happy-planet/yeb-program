package com.xxxx.mail;

import com.rabbitmq.client.Channel;
import com.xxxx.server.pojo.Employee;
import com.xxxx.server.pojo.MailContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import java.util.Date;

@Component
public class MailReceive {
    private static final Logger LOGGER=LoggerFactory.getLogger(MailReceive.class);
    @Resource
    private JavaMailSender javaMailSender;
    @Resource
    private MailProperties mailProperties;
    @Resource
    private TemplateEngine templateEngine;
    @Resource
    private RedisTemplate redisTemplate;
    @RabbitListener(queues = MailContext.MAIL_QUEUE_NAME)
    public void handler(Message message, Channel channel){
        Employee employee = (Employee) message.getPayload();
        MessageHeaders headers = message.getHeaders();
        //消息序列
        Long tag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);

        String msgId = (String) headers.get("spring_returned_message_correlation");
        HashOperations hashOperations = redisTemplate.opsForHash();

        try {
            if (hashOperations.entries("mail_log").containsKey(msgId)){
                LOGGER.error("==============消息已经被消费=========",msgId);
                channel.basicAck(tag,false);
                return;
            }
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
            //发件人
            helper.setFrom(mailProperties.getUsername());
            //收件人
            helper.setTo(employee.getEmail());
            System.out.println(employee.getEmail());
            //主题
            helper.setSubject("入职欢迎邮件");
            //发送的时间
            helper.setSentDate(new Date());
            //发送的内容
            Context context = new Context();
            context.setVariable("name",employee.getName());
            context.setVariable("joblevelName",employee.getJoblevel().getName());
            context.setVariable("departmentName",employee.getDepartment().getName());
            context.setVariable("posName",employee.getPosition().getName());

            String mail = templateEngine.process("mail", context);
            helper.setText(mail,true);
            //发送邮件
            javaMailSender.send(mimeMessage);

            System.out.println("邮件发送成功");
            /*将消息Id直接存在redis*/
            hashOperations.put("mail_log",msgId,"ok");
//            手动确认消息
            channel.basicAck(tag,false);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("邮件发送失败");
        }
    }
}
