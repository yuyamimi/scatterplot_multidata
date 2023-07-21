package ocha.itolab.hidden2.datagen.youtuber;

import java.util.*;
import java.io.*;

public class YouTubeFileGenerator1 {
	static String path = "C:/itot/projects/InfoVis/Hidden/HiddenAsuka/data/";
	static String moviefile = "movies.nakai20210129.csv";
	static String youtuberfile = "youtubers.nakai20210129.csv";
	static String outfile = "youtube.hidden.nakai20210129.csv";
	static ArrayList<Movie> movielist;
	static ArrayList<YouTuber> youtuberlist;
	static BufferedWriter writer = null;
	
	public static void main(String args[]) {
		movielist = ReadMovieFile.read(path+moviefile);
		youtuberlist = ReadYouTuberFile.read(path+youtuberfile);
		
		open(path+outfile);
		writeHeader();
		
		for(int i = 0; i < movielist.size(); i++) {	
			Movie m = movielist.get(i);
			YouTuber y = searchYouTuber(m);
			if(y == null) continue;
			String line = y.date + "," + y.nummovie + "," + y.numfan + "," + y.maxplay + "," +
						  y.instagram + "," +  y.twitter + "," + y.tiktok + "," + 
						  y.frequency + "," + y.totalplay + "," + 
						  m.length + "," + m.date + "," + 
						  m.numplay + "," + m.higheval + "," + m.loweval + "," +
						  m.comments + "," + m.hashtags + "," + m.colab + "," +
						  y.subchannel + ","  + y.member + "," + y.office + "," + 
						  y.gender + ","  + y.numperson + "," + y.professional + "," +
						  y.name + "," + m.name + "," + m.url;
			println(line);
		}
			
		close();
		
	}
	
	static void writeHeader() {
		String  line1 = "Explain,Explain,Explain,Explain,Explain,Explain,"
					  + "Explain,Explain,Explain,Explain,Explain,"
				      + "Objective,Objective,Objective,Objective,"
					  + "Category,Category,Category,Category,Category,Category,"
				      + "Category,Category,Category,Category,Category";
		println(line1);
		String  line2 = "Y-Date,Y-NumMovie,Y-NumFan,Y-MaxPlay,Y-Instagram,Y-Twitter,Y-TikTok,"
				      + "Y-Frequency,Y-TotalPlay,M-Length,M-Date,M-NumPlay,M-HighEval,M-LowEval,"
				      + "M-Comments,M-HashTags,M-Colab,Y-SubChannel,Y-Member,Y-Office,"
				      + "Y-Gender,Y-NumPerson,Y-Professional,Y-Name,M-name,M-URL";
		println(line2);
	}
	
	
	
	static YouTuber searchYouTuber(Movie m) {		
		for(int i = 0; i < youtuberlist.size(); i++) {
			YouTuber y = youtuberlist.get(i);
			if(m.youtuber.compareTo(y.name) == 0)
				return y;
		}
		return null;
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

