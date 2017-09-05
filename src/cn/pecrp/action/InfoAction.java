package cn.pecrp.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

import cn.pecrp.entity.Label;
import cn.pecrp.service.InfoService;
import net.sf.json.JSONObject;

public class InfoAction extends ActionSupport {
	
	private InfoService infoService;
	public void setInfoService(InfoService infoService) {
		this.infoService = infoService;
	}


	//�޸�����
	public String changePass() throws IOException {
		System.out.println("changePass...action...");
		
		//���request��response����
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/json;charset=utf-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		PrintWriter out = response.getWriter();
				
		JSONObject json = new JSONObject();
		try{
			
			String oldPass = request.getParameter("oldPass");
			String newPass = request.getParameter("newPass");
			
			int flag = infoService.changePass(oldPass,newPass);
			if(flag == 1) {
				json.put("msg", "1");                   //�޸ĳɹ�  
			} else if(flag == -1) { 
				json.put("msg", "-1");                  //ԭ�������
			} else {
				json.put("msg", "0");                   //�޸�ʧ��
			}
			
		} catch (Exception e) {
			System.out.println(e.toString());
			json.put("msg","0");                      //�޸�ʧ��
		} finally{
			out.write(json.toString());
			out.flush();
			out.close();
		}
		return null;
	}
	
	//��������_��ȡ��֤��
	public String forgetPassGetVCode() throws IOException {
		System.out.println("forgetPassGetVCode...action...");
		
		//���request��response����
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/json;charset=utf-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		PrintWriter out = response.getWriter();
		
		JSONObject json = new JSONObject();
		try{
			String username = request.getParameter("username");
			String email = request.getParameter("email");
			
			int flag = infoService.forgetPassGetVCode(username,email);
			if(flag == -1) {
				json.put("msg","-1");                   //�û��������䲻ƥ��
			} else if (flag == 1) {
				json.put("msg", "1");                   //��������֤��
			} else {
				json.put("msg", "0");                   //δ֪����
			}
			
		} catch (Exception e) {
			System.out.println(e.toString());
			json.put("msg", "0");              
		} finally{
			out.write(json.toString());
			out.flush();
			out.close();
		}
		
		return null;
	}
	
	//��������_�޸�����
	public String forgetPassChange() throws IOException {
		System.out.println("forgetPassChange...action...");
		//���request��response����
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/json;charset=utf-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		PrintWriter out = response.getWriter();
		
		JSONObject json = new JSONObject();
		try{
			String vcode = request.getParameter("vcode");
			String password = request.getParameter("password");
			//����֤���Ƿ���ȷ
			boolean flag = infoService.forgetPassCmpVCode(vcode);
			if(flag == true) {
				//��ȷ�������޸�����
				flag = infoService.forgetPassChange(password);
				if(flag == true) {
					json.put("msg", "1");                   //�޸ĳɹ�
				} else {
					json.put("msg","0");                    //�޸�ʧ��
				}
			} else {
				json.put("msg", "-1");                      //��֤�����
			} 
			
		}catch (Exception e) {
			json.put("msg","0");
		} finally{
			out.write(json.toString());
			out.flush();
			out.close();
		}
		
		return null;
	}
	
	//�޸��ǳ�
	public String changeNickname() throws IOException {
		System.out.println("changeNickname...action...");
		//���request��response����
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/json;charset=utf-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		PrintWriter out = response.getWriter();
		
		JSONObject json = new JSONObject();
		try{
			String nickname = request.getParameter("nickname");
			boolean flag = infoService.changeNickname(nickname);
			if(flag == false) {
				json.put("msg","0");                  //�޸�ʧ��
			} else {
				json.put("msg",nickname);                  //�޸ĳɹ�
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			json.put("msg","0");              
		} finally {
			out.write(json.toString());
			out.flush();
			out.close();
		}
		
		return null;
	}
	
	//�޸��û��ı�ǩ
	public String changeLabel() throws IOException {
		System.out.println("changeLabel...action...");
		
		//���request��response����
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/json;charset=utf-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		PrintWriter out = response.getWriter();
		
		JSONObject json = new JSONObject();
		try{
			String lids = request.getParameter("lids");         //���յ��ַ�����ʽ��labels����
			boolean flag = infoService.changeLabel(lids);
			if(flag == true) {   
				json.put("msg", "1");                           //�ı�ɹ�
			} else {
				json.put("msg", "0");                           //�ı�ʧ��
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			json.put("msg", "0");
		} finally {
			out.write(json.toString());
			out.flush();
			out.close();
		}
		return null;
	}
	
}
