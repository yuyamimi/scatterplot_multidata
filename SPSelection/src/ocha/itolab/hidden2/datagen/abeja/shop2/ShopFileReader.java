package ocha.itolab.hidden2.datagen.abeja.shop2;

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
				if(count <= 2) continue;
				StringTokenizer token = new StringTokenizer(line, ",");
				OneDay od = new OneDay();
				data.days.add(od);
				od.shopname = token.nextToken();
				String date = token.nextToken();
				od.date = date.substring(0, date.length() - 8).replace(" ", "");
				od.revenue = Integer.parseInt(token.nextToken());
				token.nextToken();
				token.nextToken();
				od.visitors = Integer.parseInt(token.nextToken());
				od.conversion = Double.parseDouble(token.nextToken());
				od.transactions = Integer.parseInt(token.nextToken());
				od.revenue_tran = Double.parseDouble(token.nextToken());
				od.revenue_item = Double.parseDouble(token.nextToken());
				od.item_tran = Double.parseDouble(token.nextToken());
				od.revenue_visit = Double.parseDouble(token.nextToken());
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
