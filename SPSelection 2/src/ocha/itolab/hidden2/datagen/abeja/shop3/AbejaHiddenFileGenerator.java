package ocha.itolab.hidden2.datagen.abeja.shop3;

import java.io.*;

public class AbejaHiddenFileGenerator {
	static DataSet data = null;
	static String hiddenfilename;
	
	static String path = "C:/itot/projects/Abeja/data/Apparel-AdaptRetailing/";
	static String shopfilename = "FiveShops-Sep-to-Jan.csv";
	static String weatherfilename = "Weather-Tokyo-Koshigaya-Tsujido.csv";

	
	
	public static void main(String args[]) {
		data = ShopFileReader.read(path + shopfilename);
		WeatherFileReader.read(data, path + weatherfilename);

		data.postprocess();
		hiddenfilename = shopfilename.substring(0, (shopfilename.length() - 4)) + "__a_hiddenformat.csv";		
		AbejaHiddenFileWriter.write(data, path + hiddenfilename);
	}
	
}
