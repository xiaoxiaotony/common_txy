package com.txy.common.base;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;
 
@Configuration
public class EmailConfigDataUtil {
 
    //从配置文件中读取相应的邮件配置属性
	@Value("${email.host}")
    public  String emailHost;
	@Value("${email.username}")
	public  String userName;
	@Value("${email.password}")
	public  String password;
	@Value("${mail.smtp.auth}") 
	public String mailAuth;
    //JavaMailSender用来发送邮件的类
    public @Bean JavaMailSenderImpl  mailSender(){
       JavaMailSenderImpl ms = new JavaMailSenderImpl();
       ms.setHost(emailHost);
       ms.setUsername(userName);
       ms.setPassword(password);
       Properties pp = new Properties();
       pp.setProperty("mail.smtp.auth", mailAuth);
       ms.setJavaMailProperties(pp);
       return ms;   
    }
}