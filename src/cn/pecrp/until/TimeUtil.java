package cn.pecrp.until;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

public class TimeUtil {
	
	//��ȡʱ��  ���غ��뼶ʱ��
	public String getTime() {
		System.out.println("getTime...util...");
		
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		Long date = calendar.getTime().getTime();            //��ȡ����ʱ��
		
		//String dateStringPaString = sdf.format(date);
		//System.out.println(dateStringPaString);
		
		return date.toString();
	}
	
	public boolean cmpTime(String time) {
		System.out.println("cmpTime...util...");
		long tempTime = Long.parseLong(time);
		System.out.println("tempTime"+tempTime);
		
		//�ڻ�ȡ���ڵ�ʱ��
		Calendar calendar = Calendar.getInstance();
		Long date = calendar.getTime().getTime();            //��ȡ����ʱ��
		System.out.println("date"+date);
		
		if(date - tempTime > 600000 ) {
			return false;
		} else {
			return true;
		}
		
	}
}
