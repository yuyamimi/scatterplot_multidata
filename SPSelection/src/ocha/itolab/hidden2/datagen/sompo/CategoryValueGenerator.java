package ocha.itolab.hidden2.datagen.sompo;

import java.io.*;
import java.util.*;

public class CategoryValueGenerator {
	static BufferedReader reader;
	static BufferedWriter writer;
	static String path = "C:/itot/projects/InfoVis/Hidden/data/SOMPO/";
	static String infile = "health_english_small1.csv";
	static String outfile = "health_english_small1_morecategory.csv";
	
	static int C_AGE = 0, C_WEIGHT = 10, C_BMI = 11;
	
	public static void main(String args[]) {
		openReader(path + infile);
		openWriter(path + outfile);
		
		int counter = 0, totalc = 0;
		String outline;
		try {
			while(true) {
				String line = reader.readLine();
				counter++;
				if(line == null) break;
				StringTokenizer token = new StringTokenizer(line, ",");
				if(counter == 1) {
					totalc = token.countTokens();
					outline = token.nextToken();
					for(int i = 1; i < (totalc - 1); i++) 
						outline += "," + token.nextToken();
					outline += ",Category,Category,Category," + token.nextToken();
					writeOneLine(outline);
					continue;
				}
				if(counter == 2) {
					outline = token.nextToken();
					for(int i = 1; i < (totalc - 1); i++) 
						outline += "," + token.nextToken();
					outline += ",AgeRank,WeightRank,BMIRank," + token.nextToken();
					writeOneLine(outline);
					continue;
				}
				if(counter >= 3) {
					outline = token.nextToken();
					String age = agerank(outline);
					String weight = "";
					String bmi = "";
					for(int i = 1; i < (totalc - 1); i++) {
						String t = token.nextToken();
						outline += "," + t;
						if(i == C_WEIGHT)
							weight = weightrank(t);
						if(i == C_BMI)
							bmi = bmirank(t);
					}
					outline += "," + age + "," + weight + "," + bmi + "," + token.nextToken();
					writeOneLine(outline);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		closeReader();
		closeWriter();
	}
	
	
	static String agerank(String token) {
		String ret = "";
		int age = Integer.parseInt(token);
		if(age < 60) ret = "60Î–¢–ž";
		else if(age < 65) ret = "60`64Î";
		else if(age < 70) ret = "65`69Î";
		else if(age < 75) ret = "70`74Î";
		else if(age < 80) ret = "75`79Î";
		else if(age < 85) ret = "80`84Î";
		else if(age < 90) ret = "85`89Î";
		else  ret = "90ÎˆÈã";
		
		return ret;
	}
	
	static String weightrank(String token) {
		String ret = "";
		double weight = Double.parseDouble(token);
		if(weight < 30) ret = "30kg–¢–ž";
		else if(weight < 40) ret = "30`40kg";
		else if(weight < 50) ret = "40`50kg";
		else if(weight < 60) ret = "50`60kg";
		else if(weight < 70) ret = "60`70kg";
		else if(weight < 80) ret = "70`80kg";
		else if(weight < 90) ret = "80`90kg";
		else  ret = "90kgˆÈã";
		
		return ret;
	}
	
	static String bmirank(String token) {
		String ret = "";
		double bmi = Double.parseDouble(token);
		if(bmi < 10) ret = "10–¢–ž";
		else if(bmi < 15) ret = "10`15";
		else if(bmi < 20) ret = "15`20";
		else if(bmi < 25) ret = "20`25";
		else if(bmi < 30) ret = "25`30";
		else if(bmi < 35) ret = "30`35";
		else if(bmi < 40) ret = "35`40";
		else  ret = "40ˆÈã";
		
		return ret;
	}
	
	static void openReader(String filename) {
		System.out.println("open " + filename);
		
		try {
			File file = new File(filename);
			reader = new BufferedReader(new FileReader(file));
			reader.ready();
		} catch (Exception e) {
			System.err.println(e);
		}
	}
	

	static void closeReader() {
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
			 System.out.println("FileWriter: " + filename);
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
	

	static void writeOneLine(String word) {
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
