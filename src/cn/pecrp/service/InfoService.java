package cn.pecrp.service;

import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.springframework.transaction.annotation.Transactional;

import cn.pecrp.dao.InfoDao;

@Transactional
public class InfoService {
	
	private InfoDao infoDao;
	public void setInfoDao(InfoDao infoDao) {
		this.infoDao = infoDao;
	}
	
	
	//ͨ�������������
	public int changePass(String oldPass,String newPass) {
		System.out.println("changePass...service...");
		
		//���ж������Ƿ���ȷ  ��ȡ���û������� �û��϶�����session��
		HttpSession session = ServletActionContext.getRequest().getSession();
		int uid = (int)session.getAttribute("uid");
		
		//ͨ���û�id�����벢�Ƚ�oldPass
		String password = infoDao.getPass(uid);
		
		if(oldPass.equals(password)) {
			
			//ͨ��uid�޸�����
			boolean flag = infoDao.changePass(uid,newPass);
			if(flag == true) {
				return 1;           //�޸ĳɹ�
			} else {
				return 0;           //δ֪����
			}
			
		} else {
			return -1;              //�¾����벻һ��
		}
	}
	
}
