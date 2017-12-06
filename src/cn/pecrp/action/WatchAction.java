package cn.pecrp.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;
import cn.pecrp.entity.Video;
import cn.pecrp.service.WatchService;

public class WatchAction extends ActionSupport {
	
	private WatchService watchService;
	public void setWatchService(WatchService watchService) {
		this.watchService = watchService;
	}
	
	//用户点开视频，准备观看
	public String watch() throws IOException{
		System.out.println("watch...action...");
		
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/json;charset=utf-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		PrintWriter out = response.getWriter();
				
		JSONObject json = new JSONObject();
		try{
			String vid = request.getParameter("vid");
			int flag = watchService.watch(vid);
			if(flag == -1){
				json.put("msg", "0");                    //未知错误
			} else {
				json.put("msg", flag);                    //可以访问
			}
		}catch (Exception e) {
			System.out.println(e.toString());
			json.put("msg", "0");                         //异常
		}finally {
			out.write(json.toString());
			out.flush();
			out.close();
		}
		
		return null;
	}
	
	//点赞
	public String zan() throws IOException{
		System.out.println("zan...action...");
		
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/json;charset=utf-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		PrintWriter out = response.getWriter();
				
		JSONObject json = new JSONObject();
		try{
			String vid = request.getParameter("vid");
			String flag = watchService.zan(vid);
			if(flag.equals("-1")){
				json.put("msg", "-1");                   //错误
			} else {
				String[] tmpArray = flag.split("a");
				json.put("msg", tmpArray[1]);            //msg存视频的赞数
				json.put("isZan", tmpArray[0]);          //isZan表示此人目前对此视频的状态
			}
			
		}catch (Exception e) {
			System.out.println(e.toString());
			json.put("msg", "-1");
		}finally {
			out.write(json.toString());
			out.flush();
			out.close();
		}
		
		return null;
	}
	
	//快速收藏
	public String collect() throws IOException {
		System.out.println("collect...action...");
		
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/json;charset=utf-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		PrintWriter out = response.getWriter();
		

		JSONObject json = new JSONObject();
		try{
			String vid = request.getParameter("vid");
			String flag = watchService.collect(vid);
			
			if(flag == null) {
				json.put("msg", "-1");								//返回-1，有错误
			} else {
				String[] tmpArray = flag.split("a");
				json.put("msg", tmpArray[1]);           			//msg存视频的收藏
				json.put("isCollect", tmpArray[0]);        		    //isCollect表示此人是否收藏此视频
			}
			
			
		} catch (Exception e) {
			System.out.println(e.toString());
			json.put("mas", "-1");									//返回-1，有异常
		} finally {
			out.write(json.toString());
			out.flush();
			out.close();
		}
		
		return null;
	}
	
	//修改收藏夹
	
}
