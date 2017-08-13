package cn.pecrp.dao;

import java.util.List;

import org.springframework.orm.hibernate5.HibernateTemplate;

import cn.pecrp.entity.User;

public class InfoDaoImpl implements InfoDao {

	private HibernateTemplate hibernateTemplate; 
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	
	//ͨ��uid�õ�����
	@Override
	public String getPass(int uid) {
		System.out.println("getPass...dao...");
		
		try{
			User user = hibernateTemplate.get(User.class, uid);
			if(user != null)
				return user.getPassword();
			else return null;
		}catch (Exception e) {
			return null;
		}
	}

	
	//ͨ��uid�޸�����
	@Override
	public boolean changePass(int uid, String newPass) {
		System.out.println("changPass...dao...");
		try{
			
			User user = hibernateTemplate.get(User.class, uid);
			user.setPassword(newPass);
			hibernateTemplate.update(user);
			return true;
			
		}catch (Exception e) {
			System.out.println(e.toString());
			return false;
		}
	}

	//ͨ���û����������ѯ�û�
	@Override
	public int searchUser(String username, String email) {
		System.out.println("searchUser...dao..");
		try{
			@SuppressWarnings("unchecked")
			List<User> list = (List<User>)hibernateTemplate.find("from User where username = ? and email = ?",username,email);
			if(list.size() == 0) {
				return -1;                     	//��ƥ��
			} else {
				return list.get(0).getUid();	//ƥ�䲢�ҷ���uid
			}
			
		}catch (Exception e) {
			System.out.println(e.toString());
			return -1;
		}
		
		
	}
	
}
