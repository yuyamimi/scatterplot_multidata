package ocha.itolab.hidden2.datagen.abeja.shop3;

public class OneDay {
	String date; //���t
	String shopname; //�X��
	int revenue;  //����
	int pass;  //�X�O�ʍs��
	double enter;  // ���X��
	int visitors; //���q�l��
	double conversion; //���㗦
	int transactions; //�w������
	double revenue_tran; //�q�P��
	double revenue_item; //���ϔ��㏤�i�P��
	double item_tran; //���ϔ���_��
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
