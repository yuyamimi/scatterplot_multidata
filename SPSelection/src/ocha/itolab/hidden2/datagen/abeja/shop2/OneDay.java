package ocha.itolab.hidden2.datagen.abeja.shop2;

public class OneDay {
	String date; //���t
	String shopname; //�X��
	int revenue;  //����
	int visitors; //���q�l��
	double conversion; //���㗦
	int transactions; //����
	double revenue_tran; //���ς����蔄��
	double revenue_item; //���i�����蔄��
	double item_tran; //���ς����菤�i��
	double revenue_visit; //���q�l�������蔄��
	
	boolean weatherflag = false;
	double mintemp = 1.0e+30, maxtemp = -1.0e+30; //�C��
	double sumrain = 0.0; //�~����
	//double sumsnow = 0.0; //�~���
	//double sumsnoc = 0.0; //�ϐ��
	double sumsunt = 0.0; //���Ǝ���
	double maxwind = -1.0e+30; //����
	
	boolean isHoliday = true;
	boolean isBadWeather = true;
	String month = "0";
	
}
