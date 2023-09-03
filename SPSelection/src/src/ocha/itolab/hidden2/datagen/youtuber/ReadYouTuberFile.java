package ocha.itolab.hidden2.datagen.youtuber;

import java.io.*;
import java.util.*;

public class ReadYouTuberFile {

	static ArrayList<YouTuber> list = new ArrayList<YouTuber>();
	static BufferedReader reader = null;
	static int numline = 0;
	
	static ArrayList<YouTuber> read(String filename) {
		list.clear();
		open(filename);
		
		try {
			while(true) {
				String line = reader.readLine();
				if(line == null) break;
				numline++;
				if(numline <= 2) continue;
				StringTokenizer token = new StringTokenizer(line, ",");
				YouTuber y = new YouTuber();
				list.add(y);
				y.date = Integer.parseInt(token.nextToken());
				y.nummovie = Integer.parseInt(token.nextToken());
				y.frequency = Double.parseDouble(token.nextToken());
				y.numfan = Integer.parseInt(token.nextToken());
				y.maxplay  = Integer.parseInt(token.nextToken());
				y.totalplay  = Long.parseLong(token.nextToken());
				y.instagram = Integer.parseInt(token.nextToken());
				y.twitter = Integer.parseInt(token.nextToken());
				y.tiktok = Integer.parseInt(token.nextToken());
				y.subchannel = Integer.parseInt(token.nextToken());
				y.member = Integer.parseInt(token.nextToken());
				y.office = Integer.parseInt(token.nextToken());
				y.gender = Integer.parseInt(token.nextToken());
				y.numperson = Integer.parseInt(token.nextToken());
				y.professional = Integer.parseInt(token.nextToken());
				y.name = token.nextToken();
				
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		close();
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
