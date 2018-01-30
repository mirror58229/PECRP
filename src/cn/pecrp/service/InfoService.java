package cn.pecrp.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.transaction.annotation.Transactional;

import cn.pecrp.dao.InfoDao;
import cn.pecrp.entity.Label;
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

	//通过旧密码改密码
	public int changePass(String oldPass,String newPass) {
		System.out.println("changePass...service...");
		
		//先判断密码是否正确  获取该用户的密码 用户肯定存在session中
		HttpSession session = ServletActionContext.getRequest().getSession();
		int uid = (int)session.getAttribute("uid");
		
		//通过用户id找密码并比较oldPass
		String password = infoDao.getPass(uid);
		
		if(oldPass.equals(password)) {
			
			//通过uid修改密码
			boolean flag = infoDao.changePass(uid,newPass);
			if(flag == true) {
				return 1;           //修改成功
			} else {
				return 0;           //未知错误
			}
			
		} else {
			return -1;              //新旧密码不一致
		}
	}
	
	//忘记密码_获取验证码
	public int forgetPassGetVCode(String username,String email)	{
		System.out.println("forgetPassGetVCode...service...");
		
		//先核对用户名和邮箱是否匹配
		int flag = infoDao.searchUser(username,email);
		
		if(flag == -1) {
			return -1;  			//不匹配
		} else {
			//uid存入session
			HttpSession session = ServletActionContext.getRequest().getSession();
			session.setAttribute("uid",flag);
			
			//发送邮箱验证码
			//随机生成5验证码
		    Integer x =(int)((Math.random()*9+1)*10000);  
		    String text = x.toString(); 
			boolean flag2 = mailUtil.sendMail(email, text);
			if(flag2 == true){
				//发送成功，把验证码和时间记录
				String nowTime = timeUtil.getTime();
				
				//存入session  验证码#时间
				session.setAttribute("vcodeTime",text+"#"+nowTime);
				System.out.println(session.getAttribute("vcodeTime"));
				return 1;                  //发送成功
				
			} else {
				return 0;
			}

		}
	}
	
	//忘记密码_比对验证码
	public boolean forgetPassCmpVCode(String vcode) {
		System.out.println("cmpVCode...service...");
		
		try{
			HttpSession session = ServletActionContext.getRequest().getSession();
			String vcodeTime =  (String) session.getAttribute("vcodeTime");
			String vcodeTimeArray[] = vcodeTime.split("#");
			
			//先比较验证码是否正确
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

	//忘记密码_修改密码
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

	//修改昵称
	public boolean changeNickname(String nickname) {
		System.out.println("changeNickname..service...");
		
		HttpSession session = ServletActionContext.getRequest().getSession();
		try{
			boolean flag = infoDao.changeNickname((int)session.getAttribute("uid"),nickname);
			if(flag == false) {
				return false;         //修改失败
			} else {
				return true;          //修改成功
			}
		}catch (Exception e) {
			System.out.println(e.toString());
			return false;
		}
	}

	//修改用户的标签
	public HashMap<Integer,String> changeLabel(String lids) {
		System.out.println("changeLabel..service...");
		
		HttpSession session = ServletActionContext.getRequest().getSession();
		try{		
			int[] lidInt;
			//根据lids字符串的到数字数组
			if(lids.equals("a")) {                               //表示用户清除所有标签             
				lidInt = new int[0];
			}
			
			else {
				String[] lidsArray = lids.split("a");
				lidInt = new int[lidsArray.length];            //标签编号数组
				int cnt = 0;
				for(String i : lidsArray) {
					lidInt[cnt] = Integer.parseInt(i);
					cnt ++;
				}
			}
			
			//再改变用户的标签
			Set<Label> flag = infoDao.changeLabel((int)session.getAttribute("uid"),lidInt);
			
			//存到map中
			HashMap<Integer,String> labels = new HashMap<Integer,String>();
			for(Label it:flag){
				labels.put(it.getLid(), it.getLabelName());
			}
			
			return labels;
			
		} catch (Exception e) {
			System.out.println(e.toString());
			return null;
		}
	}

	//上传文件
	public String upFile(long maximumSize, String allowedTypes, File upload, String uploadFileName,
			String uploadContentType) {
		System.out.println("upFile...service...");
		HttpSession session = ServletActionContext.getRequest().getSession();
		
		try{
			//新建文件
			File uploadFile = new File(ServletActionContext.getServletContext().getRealPath("upload"));
			if(!uploadFile.exists()) {  
	            uploadFile.mkdir();  
	        } 
			
			//验证文件大小
	        if (maximumSize < upload.length()) {  
	            return "-1";                       //文件过大
	            
	        } else {
	        	//验证文件格式
	        	boolean flag = false;  
	            String[]  allowedTypesStr = allowedTypes.split(",");    //这是在配置文件中 按照逗号隔开
	            
	            for (int i = 0; i < allowedTypesStr.length; i++) {  
	                if (uploadContentType.equals(allowedTypesStr[i])) {  
	                    flag = true;                              
	                }  
	            }  
	            
	            if (flag == false) {  
	                return "-2";                     //文件格式错误
	                
	            } else {
	            	//文件名为"uid_img"+uid+"后缀"
	            	String[] uploadFileNameArray = uploadFileName.split("\\.");
	            	uploadFileName = "uid_img" + session.getAttribute("uid") + "." + uploadFileNameArray[1];
	            	String path = uploadFile.getPath()+ "\\" + uploadFileName;
	            	
	            	//准备上传
		            FileUtils.copyFile(upload, new File(path));  
		            //删除临时文件 
		            upload.delete(); 
		            
		            //相对路径
		            String rePath = "upload" + "\\" + uploadFileName;
		            //写入数据库
		            boolean flag2 = infoDao.upImg((int)session.getAttribute("uid"), rePath);
		            if(flag2 == false) {
		            	return "0";                  //失败
		            } else { 
		            	System.out.println(ServletActionContext.getServletContext().getContextPath());
		            	return rePath;               //成功
		            }
		      
	            }
	        }
		} catch (Exception e) {
			System.out.println(e.toString());
			return "0";
		}
		
	}

	//得到用户未拥有的标签
	public Set<Label> labelNotHave(int uid) {
		System.out.println("labelNotHave...service...");
		
		try{
			List<Label> labelList = infoDao.getAllLabel();
			Set<Label> userLabel = infoDao.getUserLabel(uid);
			Set<Label> labelSet = new HashSet<Label>();
			
			//将所有的标签List放到新建的Set中
			for(Label l : labelList){
				labelSet.add(l);
			}
			
			//从Set中删除掉用户已经拥有的label
			for(Label l :userLabel){
				labelSet.remove(l);
			}
			
			return labelSet;
		} catch (Exception e) {
			System.out.println(e.toString());
			return null;
		}
	}
	
}
