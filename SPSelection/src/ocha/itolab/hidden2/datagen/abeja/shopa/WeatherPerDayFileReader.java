package ocha.itolab.hidden2.datagen.abeja.shopa;

import java.io.*;
import java.util.*;

public class WeatherPerDayFileReader {
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
					/*//Koshigaya
					case 5:
						od.sumrain = Double.parseDouble(v); continue;
					case 9:
						od.sumsnow = Double.parseDouble(v); continue;
					case 13:
						od.sumsnoc = Double.parseDouble(v); continue;
					case 16:
						od.sumsunt = Double.parseDouble(v); continue;
					case 40:
						od.maxtemp = Double.parseDouble(v); continue;
					case 45:
						od.mintemp = Double.parseDouble(v); continue;
					case 57:
						od.maxwind = Double.parseDouble(v); continue;
					*/
					//Osaka
					case 5:
						od.sumrain = Double.parseDouble(v); continue;
					case 9:
						od.sumsnow = Double.parseDouble(v); continue;
					case 13:
						od.sumsnoc = Double.parseDouble(v); continue;
					case 20:
						od.sumsunt = Double.parseDouble(v); continue;
					case 45:
						od.maxtemp = Double.parseDouble(v); continue;
					case 50:
						od.mintemp = Double.parseDouble(v); continue;
					case 55:
						od.maxwind = Double.parseDouble(v); continue;
						
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
