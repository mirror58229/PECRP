package cn.pecrp.service;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.apache.struts2.ServletActionContext;

import cn.pecrp.dao.UserDao;

@Transactional
public class UserService {
	private UserDao userDao;
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
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

}
