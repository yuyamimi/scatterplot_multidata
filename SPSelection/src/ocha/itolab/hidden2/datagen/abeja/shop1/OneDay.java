package ocha.itolab.hidden2.datagen.abeja.shop1;

public class OneDay {
	String date; //“ú•t
	int revenue;  //”„ã
	int guest1; //w“ül”
	int guest2; //—ˆ‹ql”
	double ratio; //”ƒã—¦
	double perguest; //‹q’P‰¿
	double aveunit; //•½‹Ï”ƒã¤•i’P‰¿
	double avenum; //•½‹Ï”ƒã“_”
	double revguest; //—ˆ‹ql”‚ ‚½‚è”„ã
	
	boolean weatherflag = false;
	double mintemp = 1.0e+30, maxtemp = -1.0e+30; //‹C‰·
	double sumrain = 0.0; //~…—Ê
	double sumsnow = 0.0; //~á—Ê
	double sumsnoc = 0.0; //Ïá—Ê
	double sumsunt = 0.0; //“úÆŠÔ
	double maxwind = -1.0e+30; //•—‘¬
	
	boolean isHoliday = true;
	boolean isBadWeather = true;
	String month = "0";
	
}
