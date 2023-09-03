package ocha.itolab.hidden2.datagen.abeja.shop1;

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
			line += (od.mintemp + "," + od.maxtemp);
			line += ("," + od.sumrain + "," + od.sumsnow + "," + od.sumsnoc);
			line += ("," + od.sumsunt + "," + od.maxwind);
			line += ("," + od.revenue + "," + od.guest1 + "," + od.guest2);
			line += ("," + od.ratio + "," + od.perguest);
			//line += ("," + od.aveunit + "," + od.avenum + "," + od.revguest);
			line += ("," + od.aveunit + "," + od.avenum);
			if(od.isHoliday == true) line += ",1"; else line += ",0";
			if(od.isBadWeather == true) line += ",1"; else line += ",0";
			line += ("," + od.month);
			line += ("," + od.date);
			println(line);
		}
				
		close();
	}
	
	
	static void writeTopLines() {
		// first line	
		//println("Explain,Explain,Explain,Explain,Explain,Explain,Explain,Objective,Objective,Objective,Objective,Objective,Objective,Objective,Objective,Category,Category,Name");
		//println("MinTemp,MaxTemp,SumRain,SumSnow,SumSnowC,SumSunTime,MaxWind,Revenue,Guest1,Guest2,Ratio,PerGuest,AveUnit,AveNum,RevGuest,Weekend,BadWeather,Date");
	
		println("Explain,Explain,Explain,Explain,Explain,Explain,Explain,Objective,Objective,Objective,Objective,Objective,Objective,Objective,Category,Category,Category,Name");
		println("MinTemp,MaxTemp,SumRain,SumSnow,SumSnowC,SumSunTime,MaxWind,Revenue,Guest1,Guest2,Ratio,PerGuest,AveUnit,AveNum,Holiday,BadWeather,Month,Date");
	}
	

	static BufferedWriter open(String filename) {	
		try {
			 writer = new BufferedWriter(
			    		new FileWriter(new File(filename)));
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
