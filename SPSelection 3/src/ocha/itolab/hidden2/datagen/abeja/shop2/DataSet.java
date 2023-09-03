package ocha.itolab.hidden2.datagen.abeja.shop2;

import java.util.*;

import ocha.itolab.hidden2.core.tool.HolidayList;

public class DataSet {
	ArrayList<OneDay> days = new ArrayList<OneDay>();
	
	public void postprocess() {
		for(int i = 0; i < days.size(); i++) {
			OneDay od = days.get(i);
			setWeekend(od);
			setBadWeather(od);
			setMonth(od);
		}
	}
	
	void setWeekend(OneDay od) {
		//System.out.println("** [" + od.date + "]");
		StringTokenizer token = new StringTokenizer(od.date, "-");
		int y = Integer.parseInt(token.nextToken());
		int m = Integer.parseInt(token.nextToken());
		int d = Integer.parseInt(token.nextToken().replace(" ", ""));
		Calendar cal = Calendar.getInstance();
		cal.set(y, (m-1), d);
		int dow = cal.get(Calendar.DAY_OF_WEEK);
		if(dow == cal.SATURDAY || dow == cal.SUNDAY)
			od.isHoliday = true;
		else od.isHoliday = false;
		//System.out.println("   y=" + y + " m=" + m + " d=" + d + " dow=" + dow);
		
		if(od.isHoliday == false) {
			for(int i = 0; i < HolidayList.holidays.length; i++) {
				if(HolidayList.holidays[i].compareTo(od.date) == 0) {
					od.isHoliday = true;  break;
				}
			}
		}
		
	}
	
	void setBadWeather(OneDay od) {
		od.isBadWeather = false;
		if(od.sumrain > 0) od.isBadWeather = true;
	}
	
	void setMonth(OneDay od) {
		StringTokenizer token = new StringTokenizer(od.date, "-");
		token.nextToken();
		od.month = token.nextToken();
		if(od.month.length() == 1) 
			od.month = "0" + od.month;
	}
	
}
