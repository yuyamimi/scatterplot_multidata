package ocha.itolab.hidden2.datagen.abeja.shop1;

public class OneDay {
	String date; //���t
	int revenue;  //����
	int guest1; //�w���l��
	int guest2; //���q�l��
	double ratio; //���㗦
	double perguest; //�q�P��
	double aveunit; //���ϔ��㏤�i�P��
	double avenum; //���ϔ���_��
	double revguest; //���q�l�������蔄��
	
	boolean weatherflag = false;
	double mintemp = 1.0e+30, maxtemp = -1.0e+30; //�C��
	double sumrain = 0.0; //�~����
	double sumsnow = 0.0; //�~���
	double sumsnoc = 0.0; //�ϐ��
	double sumsunt = 0.0; //���Ǝ���
	double maxwind = -1.0e+30; //����
	
	boolean isHoliday = true;
	boolean isBadWeather = true;
	String month = "0";
	
}
