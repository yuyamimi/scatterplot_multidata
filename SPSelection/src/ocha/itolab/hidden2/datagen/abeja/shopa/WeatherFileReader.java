package ocha.itolab.hidden2.datagen.abeja.shopa;

import java.io.*;
import java.util.*;

public class WeatherFileReader {
	static BufferedReader reader = null;
	static DataSet data = null;
	
	public static void read(DataSet d, String filename) {
		data = d;
		open(filename);
		
		int numline = 0;
		try {
			while(true) {
				String line = reader.readLine();
				if(line == null) break;
				if(line.length() <= 0) continue;
				numline++; if(numline <= 4) continue;

				StringTokenizer token = new StringTokenizer(line, ",", true);
				boolean vflag = true;
				int vcount = 0;
				OneDay od = null;
				//System.out.println(line);
				while(token.countTokens() > 0) {
					String v = token.nextToken();
					//System.out.println("    [" + v + "] count=" + vcount);
					if(v.startsWith(",") == true && vflag == false) {
						vflag = true; continue;
					}
					else if(v.startsWith(",") == true && vflag == true) {
						if(v.startsWith(",") == true) v = "0";
					}
					else {
						vflag = false;
					}
					vcount++;
					switch(vcount) {
					case 1:
						od = processDate(v);  continue;
					case 5:
						processTemperature(v, od);  continue;
					case 8:
						processRain(v, od); continue;
					case 11:
						processSnow(v, od); continue;
					case 14:
						processSnowCover(v, od); continue;
					case 17:
						processSunTime(v, od); continue;
					case 20:
						processWind(v, od); continue;
				
					}
					
					
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		close();
	}
	
	
	static OneDay processDate(String v) {
		OneDay od = null;
		StringTokenizer token = new StringTokenizer(v);
		String d = token.nextToken();
		
		for(int i = 0; i < data.days.size(); i++) {
			od = data.days.get(i);
			if(d.compareTo(od.date) == 0) {
				od.weatherflag = true;
				return od;
			}
		}
		
		return null;
	}
	
	static void processTemperature(String v, OneDay od) {
		if(od == null) return;
		double value = Double.parseDouble(v);
		if(od.mintemp > value) od.mintemp = value;
		if(od.maxtemp < value) od.maxtemp = value;
	}
	
	static void processRain(String v, OneDay od) {
		if(od == null) return;
		double value = Double.parseDouble(v);
		od.sumrain += value;
	}
	
	static void processSnow(String v, OneDay od) {
		if(od == null) return;
		double value = Double.parseDouble(v);
		od.sumsnow += value;
	}
	
	static void processSnowCover(String v, OneDay od) {
		if(od == null) return;
		double value = Double.parseDouble(v);
		od.sumsnoc += value;
	}
	
	static void processSunTime(String v, OneDay od) {
		if(od == null) return;
		double value = Double.parseDouble(v);
		od.sumsunt += value;
	}
	
	static void processWind(String v, OneDay od) {
		if(od == null) return;
		double value = Double.parseDouble(v);
		if(od.maxwind < value) od.maxwind = value;
	}
	
	static void open(String filename) {
		try {
			File file = new File(filename);
			reader = new BufferedReader(new FileReader(file));
			reader.ready();
		} catch (Exception e) {
			System.err.println(e);
		}
	}
	
	static public void close() {
		try {
			reader.close();
		} catch (Exception e) {
			System.err.println(e);
		}
	}
}
