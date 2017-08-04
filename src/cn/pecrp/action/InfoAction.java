package cn.pecrp.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

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
	
	
}
