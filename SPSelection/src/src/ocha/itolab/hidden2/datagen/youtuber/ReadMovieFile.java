package ocha.itolab.hidden2.datagen.youtuber;

import java.io.*;
import java.util.*;

public class ReadMovieFile {
	static ArrayList<Movie> list = new ArrayList<Movie>();
	static BufferedReader reader = null;
	static int numline = 0;
	
	static ArrayList<Movie> read(String filename) {
		list.clear();
		open(filename);
		numline = 0;
		
		try {
			while(true) {
				String line = reader.readLine();
				if(line == null) break;
				numline++;
				if(numline == 1) continue;
				StringTokenizer token = new StringTokenizer(line, ",");
				Movie m = new Movie();
				list.add(m);
				m.length = Integer.parseInt(token.nextToken());
				m.date = Integer.parseInt(token.nextToken());
				m.numplay = Integer.parseInt(token.nextToken());
				m.higheval = Integer.parseInt(token.nextToken());
				m.loweval = Integer.parseInt(token.nextToken());
				m.comments = Integer.parseInt(token.nextToken());
				m.hashtags = Integer.parseInt(token.nextToken());
				m.colab = Integer.parseInt(token.nextToken());
				m.gender = Integer.parseInt(token.nextToken());
				m.numperson = Integer.parseInt(token.nextToken());
				m.professional = Integer.parseInt(token.nextToken());
				m.name = token.nextToken();
				m.youtuber = token.nextToken();
				m.url = token.nextToken();
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
