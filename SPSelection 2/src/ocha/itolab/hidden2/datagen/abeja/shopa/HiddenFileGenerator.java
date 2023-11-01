package ocha.itolab.hidden2.datagen.abeja.shopa;

public class HiddenFileGenerator {
	static DataSet data = null;
	static String path = "C:/itot/projects/Abeja/data/ShopA-April-to-June/";
	static String shopfilename = "ShopA-2017-4-1_2017-6-30.csv";
	static String weatherfilename = "Weather-June.csv";
	static String hiddenfilename;
	
	public static void main(String args[]) {
		data = ShopFileReader.read(path + shopfilename);
		WeatherPerHourFileReader.read(data, path + weatherfilename);
		
		hiddenfilename = shopfilename.substring(0, (shopfilename.length() - 4)) + "__"
				       + weatherfilename.substring(0, (weatherfilename.length() - 4)) + "__hiddenformat.csv";
		
		HiddenFileWriter.write(data, path + hiddenfilename);
	}
	
}
