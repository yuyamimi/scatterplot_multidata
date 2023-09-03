package ocha.itolab.hidden2.datagen.abeja.shop2;

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
				numline++; if(numline <= 7) continue;

				StringTokenizer token = new StringTokenizer(line, ",", true);
				boolean vflag = true;
				int vcount = 0;
				ArrayList odlist = null;
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
						odlist = processDate(v);  continue;
					//Koshigaya
					case 5:
						if(odlist == null) continue;
						double sumrain = Double.parseDouble(v);
						for(int i = 0; i < odlist.size(); i++) {
							OneDay od = (OneDay)odlist.get(i);
							od.sumrain = sumrain;
						}
						continue;
					case 9:
						if(odlist == null) continue;
						double sumsunt = Double.parseDouble(v);
						for(int i = 0; i < odlist.size(); i++) {
							OneDay od = (OneDay)odlist.get(i);
							od.sumsunt = sumsunt;
						}
						continue;
					case 34:
						if(odlist == null) continue;
						double maxtemp = Double.parseDouble(v);
						for(int i = 0; i < odlist.size(); i++) {
							OneDay od = (OneDay)odlist.get(i);
							od.maxtemp = maxtemp;
						}
						continue;
					case 39:
						if(odlist == null) continue;
						double mintemp = Double.parseDouble(v);
						for(int i = 0; i < odlist.size(); i++) {
							OneDay od = (OneDay)odlist.get(i);
							od.mintemp = mintemp;
						}
						continue;
					case 50:
						if(odlist == null) continue;
						double maxwind = Double.parseDouble(v);
						for(int i = 0; i < odlist.size(); i++) {
							OneDay od = (OneDay)odlist.get(i);
							od.maxwind = maxwind;
						}
						continue;
					
					}
					
					
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		close();
	}
	
	
	static ArrayList processDate(String v) {
		ArrayList list = null;
		OneDay od = null;
		StringTokenizer token = new StringTokenizer(v);
		String date = token.nextToken();
		StringTokenizer token2 = new StringTokenizer(date, "/");
		String y = token2.nextToken();
		String m = token2.nextToken();
		String d = token2.nextToken();
		if(m.length() == 1) m = "0" + m;
		if(d.length() == 1) d = "0" + d;
		date = y + "-" + m + "-" + d;
		
		for(int i = 0; i < data.days.size(); i++) {
			od = data.days.get(i);
			if(date.compareTo(od.date) == 0) {
				od.weatherflag = true;
				if(list == null)
					list = new ArrayList();
				list.add(od);
			}
		}
		
		return list;
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
