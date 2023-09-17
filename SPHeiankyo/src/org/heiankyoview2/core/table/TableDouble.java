package org.heiankyoview2.core.table;

/**
 * ��������\�`���Ŋi�[����N���X
 */
public class TableDouble {
	double array[] = null;
	int size = 0;
	final int INITIAL_SIZE = 100;
	
	double minDValue = 1.0e+30;
	double maxDValue = -1.0e+30;

	/**
	 * double�`���Œl��ݒ肷��
	 */
	void setValue(int id, double value) {

		if(id <= 0) {
			System.out.println("  TableDouble: id(" + id + ") is out of range.");
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

		if (maxDValue < value)
			maxDValue = value;
		if (minDValue > value)
			minDValue = value;
	}
	
	/**
	 * double�`���Œl��Ԃ�
	 */
	double getDouble(int id) {
		if(id <= 0 || id > size) {
			System.out.println("  TableDouble: id(" + id + "/" + size + ") is out of range.");
			return 0.0;
		}
		return array[id - 1];
	}
	
	/**
	 * �e�[�u���̏�����
	 */
	void clear() {
		array = null;
		size = 0;
		minDValue = 1.0e+30;
		maxDValue = -1.0e+30;
	}
	
	/**
	 * �i�[����l�̌����Z�b�g����
	 * @param length �l�̌�
	 */
	void setSize(int length) {
		size = length;
		if(length < INITIAL_SIZE)
			length = INITIAL_SIZE;
		array = new double[length];
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
		double newarray[] = new double[length];
		for(int i = 0; i < array.length; i++)
			newarray[i] = array[i];
		array = newarray;
	}
	
	/**
	 * �i�[����l�̍ő�l�� double �`���Ŏw�肷��
	 * @param maxd �ő�l
	 */
	public void setMaxDValue(double maxd) {
		maxDValue = maxd;
	}

	/**
	 * �i�[����l�̍ő�l�� double �`���ŕԂ�
	 * @return �ő�l
	 */
	public double getMaxDValue() {
		return maxDValue;
	}

	/**
	 * �i�[����l�̍ŏ��l�� double �`���Ŏw�肷��
	 * @param mind �ŏ��l
	 */
	public void setMinDValue(double mind) {
		minDValue = mind;
	}

	/**
	 * �i�[����l�̍ŏ��l�� double �`���ŕԂ�
	 * @return �ŏ��l
	 */
	public double getMinDValue() {
		return minDValue;
	}

	
	/**
	 * Node�̐F�⍂�����Z�o���邽�߂̒l��Ԃ�
	 * @param id �l���i�[���Ă���ID
	 * @return Node�̐F�⍂�����Z�o���邽�߂̒l�i�����Ƃ���0.0�`1.0�A�s����ID�̏ꍇ�͕��l�j
	 */
	double getAppearanceValue(int id) {
		if (maxDValue <= minDValue)
			return 0.5;

		double value = (array[id] - minDValue) / (maxDValue - minDValue);
		if (value < 0.0)
			value = 0.0;
		if (value > 1.0)
			value = 1.0;

		return value;
	}
}
