package ocha.itolab.hidden2.datagen;

import java.io.*;
import java.util.*;

public class RangeCategoryEmbedder {
	static int RANGEID = 76;
	static int NUMRANGE = 3;
	static BufferedReader reader;
	static BufferedWriter writer;
	static ArrayList<double[]> valuelist = new ArrayList<double[]>();
	
	static String datapath = "C:/itot/projects/InfoVis/Hidden/HiddenAsuka/data/";
	static String infile = "armoga_som_asuka.csv";
	static String outfile = "armoga_som_category" + RANGEID + ".csv";
	
	public static void main(String args[]) {

		// open reader
		openReader(datapath+infile);
		
		// read file
		int counter = 0;
		String line0 = "", line1 = "";
		try {
			while(true) {
				String line = reader.readLine();
				if(line == null) break;
				if(counter <= 1) {
					StringTokenizer token = new StringTokenizer(line, ",");
					int c = 0;
					while(token.countTokens() > 0) {
						String word = token.nextToken();
						if(c == RANGEID) {
							c = -1; continue;
						}
						if(counter == 0)
							line0 += (word + ",");
						if(counter == 1)
							line1 += (word + ",");
						if(c >= 0) c++;
					}
					if(counter == 0)
						line0 += "Category";
					if(counter == 1)
						line1 += "Range";
				}
				else {
					StringTokenizer token = new StringTokenizer(line, ",");
					double v[] = new double[token.countTokens()];
					int c = 0;
					while(token.countTokens() > 0) {
						String word = token.nextToken();
						v[c++] = Double.parseDouble(word);
					}
					valuelist.add(v);
				}
				
				counter++;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		// close reader
		closeReader();
		
		// calculate the range of the values
		double min = 1.0e+30, max = -1.0e+30;
		for(int i = 0; i < valuelist.size(); i++) {
			double v[] = valuelist.get(i);
			min = (v[RANGEID] < min) ? v[RANGEID] : min;
			max = (v[RANGEID] > max) ? v[RANGEID] : max;
		}
		
		// open writer
		openWriter(datapath+outfile);
		println(line0);
		println(line1);
		
		// for each line of values
		for(int i = 0; i < valuelist.size(); i++) {
			double v[] = valuelist.get(i);
			String line2 = "";
			for(int j = 0; j < v.length; j++) {
				if(j == RANGEID) continue;
				line2 += (Double.toString(v[j]) + ",");
			}
			int rid = (int)(NUMRANGE * (v[RANGEID] - min) / (max - min));
			if(rid < 0) rid = 0;
			if(rid >= NUMRANGE) rid = NUMRANGE - 1;
			line2 += Integer.toString(rid);
			println(line2);
			
		}
		
		// close writer
		closeWriter();
	}

	
	static void openReader(String filename) {
		try {
			File file = new File(filename);
			reader = new BufferedReader(new FileReader(file));
			reader.ready();
		} catch (Exception e) {
			System.err.println(e);
		}
	}
	
	static public void closeReader() {
		try {
			reader.close();
		} catch (Exception e) {
			System.err.println(e);
		}
	}
	
	static BufferedWriter openWriter(String filename) {	
		try {
			 writer = new BufferedWriter(
			    		new FileWriter(new File(filename)));
		} catch (Exception e) {
			System.err.println(e);
			return null;
		}
		return writer;
	}
	

	static void closeWriter() {
		
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
