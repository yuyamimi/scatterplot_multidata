package ocha.itolab.hidden2.datagen.abeja.shop2;

public class OneDay {
	String date; //“ú•t
	String shopname; //“X–¼
	int revenue;  //”„ã
	int visitors; //—ˆ‹ql”
	double conversion; //”ƒã—¦
	int transactions; //ŒˆÏ
	double revenue_tran; //ŒˆÏ‚ ‚½‚è”„ã
	double revenue_item; //¤•i‚ ‚½‚è”„ã
	double item_tran; //ŒˆÏ‚ ‚½‚è¤•i”
	double revenue_visit; //—ˆ‹ql”‚ ‚½‚è”„ã
	
	boolean weatherflag = false;
	double mintemp = 1.0e+30, maxtemp = -1.0e+30; //‹C‰·
	double sumrain = 0.0; //~…—Ê
	//double sumsnow = 0.0; //~á—Ê
	//double sumsnoc = 0.0; //Ïá—Ê
	double sumsunt = 0.0; //“úÆŠÔ
	double maxwind = -1.0e+30; //•—‘¬
	
	boolean isHoliday = true;
	boolean isBadWeather = true;
	String month = "0";
	
}
