package ocha.itolab.hidden2.datagen.abeja.shop3;

import java.io.*;
import java.util.*;

public class ShopFileReader {
	static BufferedReader reader = null;
	static DataSet data;
	
	
	public static DataSet read(String filename) {
		data = new DataSet();
		open(filename);
		
		int count = 0;
		try {
			while(true) {
				String line = reader.readLine();
				count++;
				if(line == null) break;
				if(line.length() <= 0) continue;
				if(count <= 1) continue;
				StringTokenizer token = new StringTokenizer(line, ",");
				OneDay od = new OneDay();
				od.shopname = token.nextToken();
				String date = token.nextToken();
				System.out.println(date);
				//if(date.indexOf("2018-01") < 0) continue;
				od.date = date.substring(0, date.length() - 8).replace(" ", "");
				od.revenue = Integer.parseInt(nextToken(token));
				od.pass = Integer.parseInt(nextToken(token));
				od.enter = Double.parseDouble(nextToken(token));
				od.visitors = Integer.parseInt(nextToken(token));
				od.conversion = Double.parseDouble(nextToken(token));
				od.transactions = Integer.parseInt(nextToken(token));
				od.revenue_tran = Double.parseDouble(nextToken(token));
				od.revenue_item = Double.parseDouble(nextToken(token));
				od.item_tran = Double.parseDouble(nextToken(token));
				od.revenue_visit = Double.parseDouble(nextToken(token));
				data.days.add(od);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
		close();
		return data;
	}

	
	static String nextToken(StringTokenizer token) {
		String ret = token.nextToken();
		if(ret.compareTo("-") == 0) return "0";
		return ret;
	}
	
	
	static void open(String filename) {
		try {
			File file = new File(filename);
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
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
