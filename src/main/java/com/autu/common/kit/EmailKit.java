 
package com.autu.common.kit;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.autu.common._config.BlogContext;

/**
 * 邮件发送工具类
 */
public class EmailKit {
	
 
 
	public static Ret sendEmail(String emailServer, String fromEmail, String password, String toEmail, String title, String content) throws AddressException,MessagingException {
		
		// 获取系统属性
		Properties properties = System.getProperties();

		// 设置邮件服务器
		properties.setProperty("mail.smtp.host", emailServer);

		properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		properties.put("mail.smtp.socketFactory.port", "465");
		properties.put("mail.smtp.port", "465");
		
		//SSLSocketFactory类的端口
		
		properties.put("mail.smtp.auth", "true");
 
		// 获取默认session对象
		Session session = Session.getInstance(properties, new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(fromEmail, password); // 发件人邮件用户名、密码
			}
		});
		 
		// 创建默认的 MimeMessage 对象
		MimeMessage message = new MimeMessage(session);

		// Set From: 头部头字段
	 
			message.setFrom(new InternetAddress(fromEmail));
			// Set To: 头部头字段
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));

			// Set Subject: 头部头字段
			message.setSubject(title);

			// 设置消息体
			
			BodyPart html = new MimeBodyPart();
			Multipart mainPart = new MimeMultipart();
			// 设置HTML内容
			html.setContent(content, "text/html; charset=utf-8");
			mainPart.addBodyPart(html);
			// 将MiniMultipart对象设置为邮件内容
			message.setContent(mainPart);
			// 发送消息
			Transport.send(message);
	 
		
		return Ret.fail();
		
	}
	
	public  static boolean sendEmail(String toEmail,String title,String content) throws AddressException,MessagingException {
		Ret result= sendEmail(BlogContext.emailConfig.getEmailServer(), BlogContext.emailConfig.getFromEmail(), BlogContext.emailConfig.getEmailPassword()
					, toEmail, title, content);
		return result.isOk();
	}

 
	
 
	
	public static void main(String[] args) {
		Ret ret;
		try {
			ret = sendEmail(
					"abc.com",              // 邮件发送服务器地址
					"no-reply@abc.com",		// 发件邮箱
					null,					// 发件邮箱密码
					"test@test.com",		// 收件地址
					"邮件标题",              // 邮件标题
					"content");
			System.out.println("发送返回值: " + ret);
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}				// 邮件内容

	}
}
		
		
	
	


