package com.txy.common.base;

import java.io.File;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.springframework.ui.velocity.VelocityEngineUtils;

@Component
public class SendEmailTools
{
	
	private JavaMailSenderImpl mailSender;
	
	/**
	 * 模板引擎
	 */
	private VelocityEngine velocityEngine;
	
	@Autowired
	public void setMailSender(JavaMailSenderImpl mailSender)
	{
		this.mailSender = mailSender;
	}
	
	@Autowired
	public void setVelocityEngine(VelocityEngine velocityEngine)
	{
		this.velocityEngine = velocityEngine;
	}
	
	public void send(final Map<String, Object> model, final String subject, final String vmfile, final String[] mailTo, final String[] files)
	{
		MimeMessagePreparator preparator = new MimeMessagePreparator()
		{
			// 注意MimeMessagePreparator接口只有这一个回调函数
			public void prepare(MimeMessage mimeMessage)
				throws Exception
			{
				// 这是一个生成Mime邮件简单工具，如果不使用GBK这个，中文会出现乱码
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
				// 设置接收方的email地址
				message.setTo(mailTo);
				// 设置邮件主题
				message.setSubject(subject);
				// 设置发送方地址
				message.setFrom(mailSender.getUsername());
				// 注意模板中有中文要加GBK，model中存放的是要替换模板中字段的值
				String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, vmfile, "UTF-8", model);
				// 将发送的内容赋值给MimeMessageHelper,后面的true表示内容解析成html
				message.setText(text, true);
				// 如果您不想解析文本内容，可以使用false或者不添加这项
				FileSystemResource fileSystemRes;
				// 添加附件
				for (String file : files)
				{
					fileSystemRes = new FileSystemResource(new File(file));// 读取附件
					message.addAttachment(file, fileSystemRes);// 向email中添加附件
				}
			}
		};
		mailSender.send(preparator);// 发送邮件
	}
	
}
