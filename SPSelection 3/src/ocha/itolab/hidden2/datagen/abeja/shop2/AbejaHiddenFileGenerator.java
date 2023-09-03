package ocha.itolab.hidden2.datagen.abeja.shop2;

import java.io.*;

public class AbejaHiddenFileGenerator {
	static DataSet data = null;
	static String hiddenfilename;
	
	static String path = "C:/itot/projects/Abeja/data/SevenShops-June-to-August/";
	static String shopfilename = "SevenShops-06-01-to-08-31.csv";
	static String weatherfilename = "Weather-06-01-to-08-31.csv";

	
	
	public static void main(String args[]) {
		data = ShopFileReader.read(path + shopfilename);
		WeatherFileReader.read(data, path + weatherfilename);

		data.postprocess();
		hiddenfilename = shopfilename.substring(0, (shopfilename.length() - 4)) + "__a_hiddenformat.csv";		
		AbejaHiddenFileWriter.write(data, path + hiddenfilename);
	}
	
}
