package cn.pecrp.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

import cn.pecrp.entity.User;
import cn.pecrp.service.UserService;
import net.sf.json.JSONObject;

public class UserAction extends ActionSupport {
	
	private UserService userService;
	public void setUserService(UserService userService) {
		this.userService = userService;
	}


	//�û���¼
	public String login() throws IOException {
		System.out.println("login...action...");
		
		//���request��response����
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/json;charset=utf-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		PrintWriter out = response.getWriter();
				
		JSONObject json = new JSONObject();
		try{
			//��ȡֵ
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			
			//���Ե�¼
			boolean flag = userService.login(username,password);
			
			//��¼ʧ�ܷ���0   �ɹ�����1
			if(flag == false) {
				json.put("msg", "0");
			} else {
				json.put("msg", "1");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			json.put("msg","0");
			
		} finally {
			out.write(json.toString());
			out.flush();
			out.close();
		}
		return null;
	}
	
	
	//�û�ע��
	public String register() throws IOException {
		System.out.println("register...action...");
		//���request��response����
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/json;charset=utf-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		PrintWriter out = response.getWriter();
		
		JSONObject json = new JSONObject();
		try{
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			String nickname = request.getParameter("nickname");
			String email 	= request.getParameter("email");
			String vcode 	= request.getParameter("vcode");
			
			//�Ȳ��Ҹ��û����Ƿ�ע��
			boolean flag = userService.searchUser(username);
			
			if(flag == true) { 
				json.put("msg","3");                  //�û����ظ�
			} else {
				//����֤���Ƿ���ȷ�Լ��Ƿ�ʧЧ
				flag = userService.cmpVCode(vcode);
				
				if(flag == true){
					
					User user = new User();
					user.setUsername(username);
					user.setPassword(password);
					user.setNickname(nickname);
					user.setEmail(email);				
					boolean flag2 =userService.register(user);
					
					if(flag2 == true) {
						json.put("msg", "1");             //ע��ɹ�
					
					} else {
						json.put("msg","0");              //ע��ʧ��
					}
				} else {
					System.out.println("��֤��ƥ��ʧ��");
					json.put("msg", "0");                 //��֤��ƥ��ʧ��
				}
			}
			
		} catch(Exception e) {
			System.out.println("ע���쳣");
			json.put("msg", "0");                     //ע�� �쳣
		} finally {
			out.write(json.toString());
			out.flush();
			out.close();
		}
		
		return null;
	}
	
	//��ȡ������֤��
	public String getVCode() throws IOException {
		System.out.println("getVCode...action...");
		//���request��response����
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/json;charset=utf-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		PrintWriter out = response.getWriter();
		
		JSONObject json = new JSONObject();
		try{
			String email = request.getParameter("email");
			
			boolean flag = userService.getVCode(email);
			if(flag == true) {
				json.put("msg","1");                 //��������֤�벢���͸����û�
			} else {
				json.put("msg","0");                 //δ��ȡ��
			}
			
		}catch (Exception e) {
			json.put("msg","0");
		}finally {
			out.write(json.toString());
			out.flush();
			out.close();
		}
		
		return null;
	}
	
}
