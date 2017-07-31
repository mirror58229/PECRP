package cn.pecrp.service;

import javax.servlet.http.HttpSession;
import org.apache.struts2.ServletActionContext;
import org.springframework.transaction.annotation.Transactional;   //ע���������������İ�һ����Ҫ��

import cn.pecrp.dao.UserDao;
import cn.pecrp.entity.User;

@Transactional
public class UserService {
	private UserDao userDao;
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
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

}
