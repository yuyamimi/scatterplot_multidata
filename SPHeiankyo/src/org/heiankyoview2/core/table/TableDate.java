package org.heiankyoview2.core.table;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * ��������\�`���Ŋi�[����N���X
 */
public class TableDate {
	Date array[] = null;
	int size = 0;
	final int INITIAL_SIZE = 100;

	long minAValue = (long) 1.0e+30;
	long maxAValue = (long) -1.0e+30;

	SimpleDateFormat sdf = new SimpleDateFormat();

	
	
	/**
	 * Date�`���Œl��ݒ肷��
	 * @param value �l
	 */
	void setValue(int id, Date value) {

		if(id <= 0) {
			System.out.println("  TableDate: id(" + id + ") is out of range.");
			return;
		}
		else if(array == null) {
			setSize(id);
			size = id;
		}
		else if(id > size && id <= array.length) {
			size = id;
		}
		else if(id > array.length) {
			resize(array.length * 2);
			size = id;
		}
		array[id - 1] = value;

		long ivalue = value.getTime();
		if (maxAValue < ivalue)
			maxAValue = ivalue;
		if (minAValue > ivalue)
			minAValue = ivalue;
	}
	
	
	/**
	 * Date�`���Œl��Ԃ�
	 */
	Date getDate(int id) {
		if(id <= 0 || id > size) {
			System.out.println("  TableDate: id(" + id + ") is out of range.");
			return null;
		}
		return array[id - 1];
	}
	
	
	/**
	 * �e�[�u���̏�����
	 */
	void clear() {
		array = null;
		size = 0;
		minAValue = (long) 1.0e+30;
		maxAValue = (long) -1.0e+30;
	}
	
	/**
	 * �i�[����l�̌����Z�b�g����
	 * @param length �l�̌�
	 */
	void setSize(int length) {
		size = length;
		if(length < INITIAL_SIZE)
			length = INITIAL_SIZE;
		array = new Date[length];
	}
	
	
	/**
	 * �i�[����l�̌����Q�b�g����
	 */
	int getSize() {
		return size;
	}
	
	
	/**
	 * �i�[����l�̌����ăZ�b�g����
	 * @param length �l�̌�
	 */
	void resize(int length) {
		if(length < array.length) return;
		if(length < (array.length * 2))
			length = array.length * 2;
		Date newarray[] = new Date[length];
		for(int i = 0; i < array.length; i++)
			newarray[i] = array[i];
		array = newarray;
	}

	/**
	 * �i�[����l�̍ő�l�� long �`���Ŏw�肷��
	 * @param maxi �ő�l
	 */
	public void setMaxAValue(long maxa) {
		maxAValue = maxa;
	}

	/**
	 * �i�[����l�̍ő�l�� long �`���ŕԂ�
	 * @return �ő�l
	 */
	public long getMaxAValue() {
		return maxAValue;
	}

	/**
	 * �i�[����l�̍ŏ��l�� long �`���Ŏw�肷��
	 * @param mini �ŏ��l
	 */
	public void setMinAValue(long mina) {
		minAValue = mina;
	}

	/**
	 * �i�[����l�̍ŏ��l�� long �`���ŕԂ�
	 * @return �ŏ��l
	 */
	public long getMinAValue() {
		return minAValue;
	}


	/**
	 * Node�̐F�⍂�����Z�o���邽�߂̒l��Ԃ�
	 * @param id �l���i�[���Ă���ID
	 * @return Node�̐F�⍂�����Z�o���邽�߂̒l�i�����Ƃ���0.0�`1.0�A�s����ID�̏ꍇ�͕��l�j
	 */
	double getAppearanceValue(int id) {
		if (maxAValue <= minAValue)
			return 0.5;

		long ivalue = array[id].getTime();
		double value = (double) (ivalue - minAValue)
				/ (double) (maxAValue - minAValue);
		if (value < 0.0)
			value = 0.0;
		if (value > 1.0)
			value = 1.0;
		
		return value;
	}
}
