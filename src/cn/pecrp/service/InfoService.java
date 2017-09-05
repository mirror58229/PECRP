package cn.pecrp.service;

import javax.mail.Session;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.springframework.transaction.annotation.Transactional;

import cn.pecrp.dao.InfoDao;
import cn.pecrp.until.MailUtil;
import cn.pecrp.until.TimeUtil;

@Transactional
public class InfoService {
	
	private InfoDao infoDao;
	public void setInfoDao(InfoDao infoDao) {
		this.infoDao = infoDao;
	}
	
	private MailUtil mailUtil;
	public void setMailUtil(MailUtil mailUtil) {
		this.mailUtil = mailUtil;
	}
	
	private TimeUtil timeUtil;
	public void setTimeUtil(TimeUtil timeUtil) {
		this.timeUtil = timeUtil;
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
	
	//��������_��ȡ��֤��
	public int forgetPassGetVCode(String username,String email)	{
		System.out.println("forgetPassGetVCode...service...");
		
		//�Ⱥ˶��û����������Ƿ�ƥ��
		int flag = infoDao.searchUser(username,email);
		
		if(flag == -1) {
			return -1;  			//��ƥ��
		} else {
			//uid����session
			HttpSession session = ServletActionContext.getRequest().getSession();
			session.setAttribute("uid",flag);
			
			//����������֤��
			//�������5��֤��
		    Integer x =(int)((Math.random()*9+1)*10000);  
		    String text = x.toString(); 
			boolean flag2 = mailUtil.sendMail(email, text);
			if(flag2 == true){
				//���ͳɹ�������֤���ʱ���¼
				String nowTime = timeUtil.getTime();
				
				//����session  ��֤��#ʱ��
				session.setAttribute("vcodeTime",text+"#"+nowTime);
				System.out.println(session.getAttribute("vcodeTime"));
				return 1;                  //���ͳɹ�
				
			} else {
				return 0;
			}

		}
	}
	
	//��������_�ȶ���֤��
	public boolean forgetPassCmpVCode(String vcode) {
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

	//��������_�޸�����
	public boolean forgetPassChange(String password) {
		System.out.println("forgetPassChange..service...");
		
		HttpSession session = ServletActionContext.getRequest().getSession();
		try{
			boolean flag = infoDao.changePass((int)session.getAttribute("uid"),password);
			if(flag == true) {
				return true;
			} else {
				return false;
			}
		}catch (Exception e) {
			System.out.println(e.toString());
			return false;
		}
		
	}

	//�޸��ǳ�
	public boolean changeNickname(String nickname) {
		System.out.println("changeNickname..service...");
		
		HttpSession session = ServletActionContext.getRequest().getSession();
		try{
			boolean flag = infoDao.changeNickname((int)session.getAttribute("uid"),nickname);
			if(flag == false) {
				return false;         //�޸�ʧ��
			} else {
				return true;          //�޸ĳɹ�
			}
		}catch (Exception e) {
			System.out.println(e.toString());
			return false;
		}
	}

	//�޸��û��ı�ǩ
	public boolean changeLabel(String lids) {
		System.out.println("changeLabel..service...");
		
		HttpSession session = ServletActionContext.getRequest().getSession();
		try{
			//����lids�ַ����ĵ���������
			String[] lidsArray = lids.split("a");
			int[] lidInt = new int[lidsArray.length];            //��ǩ�������
			int cnt = 0;
			for(String i : lidsArray) {
				lidInt[cnt] = Integer.parseInt(i);
				cnt ++;
			}
			
			//�ٸı��û��ı�ǩ
			boolean flag = infoDao.changeLabel((int)session.getAttribute("uid"),lidInt);
			if(flag == true) {
				return true;
			} else {
				return false;
			}
			
		} catch (Exception e) {
			System.out.println(e.toString());
			return false;
		}
	}
	
}
