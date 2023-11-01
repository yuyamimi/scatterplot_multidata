package ocha.itolab.hidden2.datagen.abeja.shop3;

import java.io.*;

public class AbejaHiddenFileWriter {
	static BufferedWriter writer = null;
	
	public static void write(DataSet data, String filename) {		
		open(filename);
		writeTopLines();
		
		for(int i = 0; i < data.days.size(); i++) {
			OneDay od = data.days.get(i);
			if(od.weatherflag == false) continue;
			String line = "";
			line += (od.mintemp + "," + od.maxtemp + "," + od.sumrain);
			line += ("," + od.sumsunt + "," + od.maxwind);
			line += ("," + od.revenue + "," + od.pass + "," + od.enter);
			line += ("," + od.visitors + "," + od.conversion);
			line += ("," + od.transactions + "," + od.revenue_tran);
			line += ("," + od.revenue_item + "," + od.item_tran + "," + od.revenue_visit);
			if(od.isHoliday == true) line += ",1"; else line += ",0";
			if(od.isBadWeather == true) line += ",1"; else line += ",0";
			line += ("," + od.month);
			line += ("," + od.shopname);
			line += ("," + od.date);
			println(line);
		}
				
		close();
	}
	
	
	static void writeTopLines() {
		// first line	
		//println("Explain,Explain,Explain,Explain,Explain,Explain,Explain,Objective,Objective,Objective,Objective,Objective,Objective,Objective,Objective,Category,Category,Name");
		//println("MinTemp,MaxTemp,SumRain,SumSnow,SumSnowC,SumSunTime,MaxWind,Revenue,Guest1,Guest2,Ratio,PerGuest,AveUnit,AveNum,RevGuest,Weekend,BadWeather,Date");
	
		println("Explain,Explain,Explain,Explain,Explain,Objective,Objective,Objective,Objective,Objective,Objective,Objective,Objective,Objective,Objective,Category,Category,Category,Category,Name");
		println("MinTemp,MaxTemp,SumRain,SumSunTime,MaxWind,Revenue,Pass,Enter,Visitor,Conversion,Transaction,RevTran,RevItem,ItemTran,RevVisit,Holiday,BadWeather,Month,ShopName,Date");
	}
	

	static BufferedWriter open(String filename) {	
		try {
			 writer = new BufferedWriter(
					 new OutputStreamWriter(new FileOutputStream(new File(filename)),"SJIS"));
		} catch (Exception e) {
			System.err.println(e);
			return null;
		}
		return writer;
	}
	

	static void close() {
		
		try {
			writer.close();
		} catch (Exception e) {
			System.err.println(e);
			return;
		}
	}
	

	static void println(String word) {
		try {
			writer.write(word, 0, word.length());
			writer.flush();
			writer.newLine();
		} catch (Exception e) {
			System.err.println(e);
			return;
		}
	}	
	
	
	
	
	
	
}
