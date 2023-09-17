package org.heiankyoview2.core.util;
import java.util.Calendar;


public class ExpirationChecker {
	
	// �L�������̔N�ƌ����L�q����
	static int yearlimit = 3000;
	static int monthlimit = 11;
	static int limit = calcLimit(yearlimit, monthlimit);
	
	/**
	 * �L�����������ۂ����`�F�b�N����
	 */
	public static boolean isValid() {
		
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int value = calcLimit(year, month);
		
		if(value > limit) return false;
		return true;
	}
	
	/**
	 * �g�p�����𔻒肷�邽�߂̐����l���Z�o����
	 */
	static int calcLimit(int y, int m) {
		return (y - 2000) * 12 + m;
	}
	
}
