package ocha.itolab.hidden2.datagen.abeja.shopa;

import java.io.*;
import java.util.*;

public class ShopFileReader {
	static BufferedReader reader = null;
	static DataSet data;
	
	
	public static DataSet read(String filename) {
		data = new DataSet();
		open(filename);
		
		boolean isFirst = true;
		try {
			while(true) {
				String line = reader.readLine();
				if(line == null) break;
				if(line.length() <= 0) continue;
				if(isFirst) {
					isFirst = false;  continue;
				}
				StringTokenizer token = new StringTokenizer(line, ",");
				OneDay od = new OneDay();
				data.days.add(od);
				od.date = token.nextToken();
				od.revenue = Integer.parseInt(token.nextToken());
				od.guest1 = Integer.parseInt(token.nextToken());
				od.guest2 = Integer.parseInt(token.nextToken());
				od.ratio = Double.parseDouble(token.nextToken().replace("%", ""));
				od.perguest = Integer.parseInt(token.nextToken());
				od.aveunit = Integer.parseInt(token.nextToken());
				od.avenum = Double.parseDouble(token.nextToken());
				//od.revguest = Double.parseDouble(token.nextToken());
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
		close();
		return data;
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
