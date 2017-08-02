package cn.pecrp.until;

import  java.util.Properties;

import  org.springframework.mail.SimpleMailMessage; 
import  org.springframework.mail.javamail.JavaMailSenderImpl; 

public class MailUtil {
	
	private JavaMailSenderImpl senderImpl;
	public void setSenderImpl(JavaMailSenderImpl senderImpl) {
		this.senderImpl = senderImpl;
	}
	
	private SimpleMailMessage mailMessage;
	public void setMailMessage(SimpleMailMessage mailMessage) {
		this.mailMessage = mailMessage;
	}
	
	private Properties prop;
	public void setProp(Properties prop) {
		this.prop = prop;
	}


	public boolean sendMail (String to,String text) {
		System.out.println("sendMail...util...");
		
		try{
			//�趨mail server
			senderImpl.setHost("smtp.163.com");
			
			// �����ռ��ˣ��ļ��� �����鷢�Ͷ���ʼ�
			// String[] array = new String[]    {"sun111@163.com","sun222@sohu.com"};    
			// mailMessage.setTo(array);  
			
			mailMessage.setTo(to); 
		    mailMessage.setFrom( "15850685753@163.com" ); 
		    mailMessage.setSubject( "������ѧ�����Ƽ�ƽ̨������֤��" ); 
		    mailMessage.setText("����������ѧ�����Ƽ�ƽ̨,����������֤��:  "+text+" ,10����֮����Ч��"); 
			
		    senderImpl.setUsername("15850685753@163.com");
		    senderImpl.setPassword("L19970604");
		    
		    prop.put("mail.smtp.auth","true");
		    prop.put("mail.smtp.timeout","25000");
		    senderImpl.setJavaMailProperties(prop);
		    
		    //�����ʼ�
		    senderImpl.send(mailMessage);
		    
		    System.out.println("�����ʼ��ɹ�");
		    
		    return true;
		}catch (Exception e) {
			System.out.println("�����ʼ�ʧ��");
			return false;
		}
	}

}
