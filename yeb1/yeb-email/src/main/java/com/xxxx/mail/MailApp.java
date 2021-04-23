package com.xxxx.mail;

import com.xxxx.server.pojo.MailContext;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
/**
 * Hello world!
 *
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class MailApp
{
    public static void main( String[] args )
    {
        SpringApplication.run(MailApp.class);
    }


    @Bean
    public Queue queue(){
        return new Queue(MailContext.MAIL_QUEUE_NAME);
    }
}
