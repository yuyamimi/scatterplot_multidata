package ocha.itolab.hidden2.datagen.abeja.shopa;

import java.io.*;
import java.util.*;

public class ScatterplotDistanceCalculator {
	static BufferedReader reader;
	static String filename = "C:/itot/paper/InfoVis/SPSelection/SpComb/score3.csv";
	static int NUM = 12;
	
	public static void main(String args[]) {
		ArrayList<double[]> list1 = new ArrayList<double[]>();
		ArrayList<double[]> list2 = new ArrayList<double[]>();
		int count = 0;
		
		open(filename);
		
		try { 
			while(true) {
				String line = reader.readLine();
				if(line == null) break;
				StringTokenizer token = new StringTokenizer(line, ",");
				double v[] = new double[5];
				for(int i = 0; i < v.length; i++) {
					v[i] = Double.parseDouble(token.nextToken());
				}
				if(count < NUM) list1.add(v);
				else list2.add(v);
				count++;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}

		close();
		
		printStatistics(list1, "1");
		printStatistics(list2, "2");
		
	}
	
	
	static void printStatistics(ArrayList<double[]> list, String word) {
		int NUMRANK = 20;
		double dist[] = new double[list.size() * (list.size() - 1) / 2];
		int count1[] = new int[NUMRANK];
		int count2[] = new int[NUMRANK];
		
		for(int i = 0; i < list.size(); i++) {
			double v1[] = list.get(i);
			for(int j = (i + 1); j < list.size(); j++) {
				double v2[] = list.get(j);
				double len1 = 0.0, len2 = 0.0, inner = 0.0;
				for(int k = 0; k < 4; k++) {
					len1 += (v1[k] * v1[k]);
					len2 += (v2[k] * v2[k]);
					inner += (v1[k] * v2[k]);
				}
				if(len1 < 1.0e-8 || len2 < 1.0e-8) continue;
				inner /= (Math.sqrt(len1) * Math.sqrt(len2));
				double aw = Math.abs(len1 * len2 - inner * inner);				
				double aarea = 0.5 * Math.sqrt(aw);
				//System.out.println(word + "  i=" + i + " j=" + j + " aarea=" + aarea);
				int iarea = (int)(aarea * NUMRANK);
				if(iarea < 0) iarea = 0;
				if(iarea >= NUMRANK) iarea = NUMRANK - 1;
				count1[iarea] += 1;
			}
			//System.out.println(word + "  max=" + v1[4]);
			int imax = (int)(v1[4] * NUMRANK);
			if(imax < 0) imax = 0;
			if(imax >= NUMRANK) imax = NUMRANK - 1;
			count2[imax] += 1;
		}
		
		int sum1 = 0;
		for(int i = 0; i < NUMRANK; i++)
			sum1 += count1[i];
		for(int i = 0; i < NUMRANK; i++)
			System.out.print((double)count1[i]/(double)sum1 + ",");
		System.out.println("");
		
		int sum2 = 0;
		for(int i = 0; i < NUMRANK; i++)
			sum2 += count2[i];
		for(int i = 0; i < NUMRANK; i++)
			System.out.print((double)count2[i]/(double)sum2 + ",");
		System.out.println("");
		
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
