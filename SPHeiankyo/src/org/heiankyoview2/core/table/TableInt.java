package org.heiankyoview2.core.table;

/**
 * ��������\�`���Ŋi�[����N���X
 */
public class TableInt {
	int array[] = null;
	int size = 0;
	final int INITIAL_SIZE = 100;
	
	int minIValue = (int) 1.0e+30;
	int maxIValue = (int) -1.0e+30;
	
	/**
	 * int�`���Œl��ݒ肷��
	 * @param value �l
	 */
	void setValue(int id, int value) {
		if(id <= 0) {
			System.out.println("  TableInt(1): id(" + id + ") is out of range.");
			return;
		}
		else if(array == null) {
			setSize(id);
			size = id;
		}
		else if(id > size && id <= array.length) {
			size = id;
			//System.out.println("   TableInt.setValue* size=" + size + " id=" + id);
		}
		else if(id > array.length) {
			resize(array.length * 2);
			size = id;
			//System.out.println("   TableInt.setValue** size=" + size + " id=" + id + " length=" + array.length);
		}
		array[id - 1] = value;

		if (maxIValue < value)
			maxIValue = value;
		if (minIValue > value)
			minIValue = value;
		
		//System.out.println("   id=" + id + " value=" + value + " min=" + minIValue  + " max=" + maxIValue);
	}

	/**
	 * int�`���Œl��Ԃ�
	 */
	int getInt(int id) {
		if(id <= 0 || id > size) {
			System.out.println("  TableInt(2): id(" + id + ") is out of range.");
			return 0;
		}
		return array[id - 1];
	}
	
	/**
	 * �e�[�u���̏�����
	 */
	void clear() {
		array = null;
		size = 0;
		minIValue = (int) 1.0e+30;
		maxIValue = (int) -1.0e+30;
		
	}
	
	/**
	 * �i�[����l�̌����Z�b�g����
	 * @param length �l�̌�
	 */
	void setSize(int length) {
		size = length;
		if(length < INITIAL_SIZE)
			length = INITIAL_SIZE;
		array = new int[length];
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
		/*
		if(length < array.length) return;
		if(length < (array.length * 2))
			length = array.length * 2;
			*/
		int newarray[] = new int[length];
		for(int i = 0; i < array.length; i++)
			newarray[i] = array[i];
		array = newarray;
	}

	/**
	 * �i�[����l�̍ő�l�� int �`���Ŏw�肷��
	 * @param maxi �ő�l
	 */
	public void setMaxIValue(int maxi) {
		maxIValue = maxi;
	}

	/**
	 * �i�[����l�̍ő�l�� int �`���ŕԂ�
	 * @return �ő�l
	 */
	public int getMaxIValue() {
		return maxIValue;
	}


	/**
	 * �i�[����l�̍ŏ��l�� int �`���Ŏw�肷��
	 * @param mini �ŏ��l
	 */
	public void setMinIValue(int mini) {
		minIValue = mini;
	}

	/**
	 * �i�[����l�̍ŏ��l�� int �`���ŕԂ�
	 * @return �ŏ��l
	 */
	public int getMinIValue() {
		return minIValue;
	}
	
	/**
	 * Node�̐F�⍂�����Z�o���邽�߂̒l��Ԃ�
	 * @param id �l���i�[���Ă���ID
	 * @return Node�̐F�⍂�����Z�o���邽�߂̒l�i�����Ƃ���0.0�`1.0�A�s����ID�̏ꍇ�͕��l�j
	 */
	double getAppearanceValue(int id) {
		if (maxIValue <= minIValue)
			return 0.5;
		if(id < 0)
			return 0.5;
		
		double value =	(double) (array[id] - minIValue)
				/ (double) (maxIValue - minIValue);
		if(value > 0.5)
		//System.out.println("      id=" + id + " value=" + value);
		if (value < 0.0)
			value = 0.0;
		if (value > 1.0)
			value = 1.0;

		return value;
	}

}
