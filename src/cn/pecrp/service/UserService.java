package cn.pecrp.service;

import java.util.Calendar;

import javax.servlet.http.HttpSession;
import org.apache.struts2.ServletActionContext;
import org.springframework.transaction.annotation.Transactional;   //ע���������������İ�һ����Ҫ��

import cn.pecrp.dao.UserDao;
import cn.pecrp.entity.User;
import cn.pecrp.until.MailUtil;
import cn.pecrp.until.TimeUtil;
import javassist.bytecode.stackmap.BasicBlock.Catch;

@Transactional
public class UserService {
	private UserDao userDao;
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	private MailUtil mailUtil;
	public void setMailUtil(MailUtil mailUtil) {
		this.mailUtil = mailUtil;
	}
	
	private TimeUtil timeUtil;
	public void setTimeUtil(TimeUtil timeUtil) {
		this.timeUtil = timeUtil;
	}

	//��¼
	public boolean login(String username, String password) {
		System.out.println("login...service...");
		
		int flag = userDao.searchUser(username, password);
		if(flag == 0) {
			//������
			return false;
		} else {
			//�����û�  uid����session
			HttpSession session = ServletActionContext.getRequest().getSession();
			session.setAttribute("uid", flag);
			return true;
		}
	}
	
	//�����û��������û�
	public boolean searchUser(String username) {
		System.out.println("searchUser...service...");
		
		int flag = userDao.searchUser(username);
		
		if(flag != 0) {
			return true;
		}
		else return false;
	}
	
	//ע��
	public boolean register(User user) {
		System.out.println("register...service...");
		
		int flag = userDao.addUser(user);
		
		if(flag != 0) {
			HttpSession session = ServletActionContext.getRequest().getSession();
			session.setAttribute("uid", flag);               //�û�id���� session
			return true;
		} else {
			return false;
		}
			
	}
	
	//�����ʼ���ȡ��֤��
	public boolean getVCode(String email) {
		System.out.println("getVCode...service...");
		//�������5��֤��
	    Integer x =(int)((Math.random()*9+1)*10000);  
	    String text = x.toString(); 
		boolean flag = mailUtil.sendMail(email, text);
		if(flag == true){
			//���ͳɹ�������֤���ʱ���¼
			String nowTime = timeUtil.getTime();
			
			//����session  ��֤��#ʱ��
			HttpSession session = ServletActionContext.getRequest().getSession();
			session.setAttribute("vcodeTime",text+"#"+nowTime);
			System.out.println(session.getAttribute("vcodeTime"));
			return true;
			
		} else {
			return false;
		}
	}
	
	//�Ƚ���֤���Ƿ���ȷ�Լ��Ƿ�ʧЧ
	public boolean cmpVCode(String vcode) {
		System.out.println("cmpVCode...service...");
		
		try{
			HttpSession session = ServletActionContext.getRequest().getSession();
			String vcodeTime =  (String) session.getAttribute("vcodeTime");
			String vcodeTimeArray[] = vcodeTime.split("#");
			
			//�ȱȽ���֤���Ƿ���ȷ
			if(vcodeTimeArray[0].equals(vcode)) {
				boolean flag = timeUtil.cmpTime(vcodeTimeArray[1]);
				
				if(flag == true){
					return true;
				}
				
			}
			
			return false;
			
		} catch (Exception e) {
			System.out.println(e.toString());
			return false;
		}	 
	}

	//�������е��û���Ϣ
	public User getUserInfo() {
		System.out.println("getUserInfo...service...");
		
		HttpSession session = ServletActionContext.getRequest().getSession();
		
		try{
			//����id�õ����û���������Ϣ
			User user = userDao.getUserInfo((int)session.getAttribute("uid"));
			return user;
		} catch (Exception e) {
			System.out.println(e.toString());
			return null;
		}
	}

}
