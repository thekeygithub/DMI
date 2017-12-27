package com.dhcc.ts.library.solr.mailUtil;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * @描述：简单的发送邮件方法
 * @作者：SZ
 * @时间：2017年5月11日 上午9:51:24
 */
public class SendEmail {
	private String host = "mail.teshijuhe.com"; // smtp服务器
	private String from = "ts-alarm@teshijuhe.com"; // 发件人地址
	private String user = "ts-alarm@teshijuhe.com"; // 用户名
	private String pwd = "Ebao123"; // 密码
	private String to = "zhe1.su@ebaonet.cn"; // 收件人地址
	private String affix = ""; // 附件地址
	private String affixName = ""; // 附件名称

	public static void sendEmail(String subject, String details, int usedCC, int userType) {
		SendEmail se = new SendEmail();
		String cc = "";
		if (1 == userType) {
			cc = "tao.huang@ebaonet.cn";
		} else if (2 == userType) {
			cc = "feng.xia@ebaonet.cn";
		}
		se.send(subject, details, usedCC, cc);
	}

	/**
	 * @描述：
	 * 
	 * @作者：SZ
	 * @时间：2017年5月11日 上午11:57:16
	 * @param subject
	 * @param details
	 * @param usedCC
	 *            为1时增加抄送人
	 * @return
	 */
	private boolean send(String subject, String details, int usedCC, String cc) {
		Properties props = new Properties();

		// 设置发送邮件的邮件服务器的属性（这里使用网易的smtp服务器）
		props.put("mail.smtp.host", host);
		// 需要经过授权，也就是有户名和密码的校验，这样才能通过验证（一定要有这一条）
		props.put("mail.smtp.auth", "true");

		// 用刚刚设置好的props对象构建一个session
		Session session = Session.getDefaultInstance(props);

		// 有了这句便可以在发送邮件的过程中在console处显示过程信息，供调试使
		// 用（你可以在控制台（console)上看到发送邮件的过程）
		session.setDebug(true);

		// 用session为参数定义消息对象
		MimeMessage message = new MimeMessage(session);
		try {
			// 加载发件人地址
			message.setFrom(new InternetAddress(from));
			// 加载收件人地址
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

			// 增加抄送人地址
			if (1 == usedCC) {
				for (String address : cc.split(",")) {
					message.addRecipient(Message.RecipientType.CC, new InternetAddress(address));
				}
			}

			// 加载标题
			message.setSubject(subject);

			// 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
			Multipart multipart = new MimeMultipart();

			// 设置邮件的文本内容
			BodyPart contentPart = new MimeBodyPart();
			contentPart.setText(details);
			multipart.addBodyPart(contentPart);
			// 添加附件
			if (!"".equals(affix)) {
				BodyPart messageBodyPart = new MimeBodyPart();
				DataSource source = new FileDataSource(affix);
				// 添加附件的内容
				messageBodyPart.setDataHandler(new DataHandler(source));
				// 添加附件的标题
				// 这里很重要，通过下面的Base64编码的转换可以保证你的中文附件标题名在发送时不会变成乱码
				sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
				messageBodyPart.setFileName("=?GBK?B?" + enc.encode(affixName.getBytes()) + "?=");
				multipart.addBodyPart(messageBodyPart);

			}

			// 将multipart对象放到message中
			message.setContent(multipart);
			// 保存邮件
			message.saveChanges();
			// 发送邮件
			Transport transport = session.getTransport("smtp");
			// 连接服务器的邮箱
			transport.connect(host, user, pwd);
			// 把邮件发送出去
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void main(String[] args) {
		SendEmail.sendEmail("医疗数据交换平台MTS服务异常", "请联系夏峰、黄涛检查原因！", 1, 1);
	}
}
