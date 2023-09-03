package ocha.itolab.hidden2.datagen.abeja.shopa;

import java.io.*;

public class HiddenFileWriter {
	static BufferedWriter writer = null;
	
	public static void write(DataSet data, String filename) {		
		open(filename);
		writeTopLines();
		
		for(int i = 0; i < data.days.size(); i++) {
			OneDay od = data.days.get(i);
			if(od.weatherflag == false) continue;
			String line = Integer.toString(od.revenue);
			line += ("," + od.guest1 + "," + od.guest2 + "," + od.ratio + "," + od.perguest);
			line += ("," + od.aveunit + "," + od.avenum + "," + od.revguest);
			line += ("," + od.mintemp + "," + od.maxtemp);
			line += ("," + od.sumrain + "," + od.sumsnow + "," + od.sumsnoc);
			line += ("," + od.sumsunt + "," + od.maxwind);
			line += ("," + od.date);
			println(line);
		}
				
		close();
	}
	
	
	static void writeTopLines() {
		println("Numeric,Numeric,Numeric,Numeric,Numeric,Numeric,Numeric,Numeric,Numeric,Numeric,Numeric,Numeric,Numeric,Numeric,Numeric,Category");
		println("Revenue,Guest1,Guest2,Ratio,PerGuest,AveUnit,AveNum,RevGuest,MinTemp,MaxTemp,SumRain,SumSnow,SumSnowC,SumSunTime,MaxWind,Date");
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
