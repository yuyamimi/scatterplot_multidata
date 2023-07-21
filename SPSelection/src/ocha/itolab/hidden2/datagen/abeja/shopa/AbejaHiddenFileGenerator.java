package ocha.itolab.hidden2.datagen.abeja.shopa;

import java.io.*;

public class AbejaHiddenFileGenerator {
	static DataSet data = null;
	static String hiddenfilename;
	/*
	static String path = "C:/itot/projects/Abeja/data/ShopA-April-to-June/";
	static String shopfilename = "ShopA-2017-4-1_2017-6-30.csv";
	static String weatherprefix = "Weather";
	*/
	
	static String path = "C:/itot/projects/Abeja/data/Shop-Koshigaya-and-Osaka/";
	static String shopfilename = "Bsha-Aten-Koshigaya-2016-05-01-2017-07-31.csv";
	static String weatherprefix = "Koshigaya";
	/*
	static String path = "C:/itot/projects/Abeja/data/Shop-Koshigaya-and-Osaka/";
	static String shopfilename = "Bsha-Bten-Osaka-2016-05-01-2017-07-31.csv";
	static String weatherprefix = "Osaka";
	*/
	
	public static void main(String args[]) {
		File dir = new File(path);
		String filelist[] = dir.list();
		data = ShopFileReader.read(path + shopfilename);
		for(int i = 0; i < filelist.length; i++) {
			System.out.println(filelist[i]);
			if(filelist[i].startsWith(weatherprefix) == false) continue;
			if(filelist[i].endsWith(".csv") == false) continue;
			//WeatherPerHourFileReader.read(data, path + filelist[i]);
			WeatherPerDayFileReader.read(data, path + filelist[i]);
		}

		data.postprocess();
		hiddenfilename = shopfilename.substring(0, (shopfilename.length() - 4)) + "__a_hiddenformat.csv";		
		AbejaHiddenFileWriter.write(data, path + hiddenfilename);
	}
	
}
