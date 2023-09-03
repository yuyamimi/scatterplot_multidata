package ocha.itolab.hidden2.datagen.abeja.shop2;

public class OneDay {
	String date; //日付
	String shopname; //店名
	int revenue;  //売上
	int visitors; //来客人数
	double conversion; //買上率
	int transactions; //決済
	double revenue_tran; //決済あたり売上
	double revenue_item; //商品あたり売上
	double item_tran; //決済あたり商品数
	double revenue_visit; //来客人数あたり売上
	
	boolean weatherflag = false;
	double mintemp = 1.0e+30, maxtemp = -1.0e+30; //気温
	double sumrain = 0.0; //降水量
	//double sumsnow = 0.0; //降雪量
	//double sumsnoc = 0.0; //積雪量
	double sumsunt = 0.0; //日照時間
	double maxwind = -1.0e+30; //風速
	
	boolean isHoliday = true;
	boolean isBadWeather = true;
	String month = "0";
	
}
