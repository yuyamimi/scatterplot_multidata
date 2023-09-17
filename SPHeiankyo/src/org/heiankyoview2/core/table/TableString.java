package org.heiankyoview2.core.table;


/**
 * ���������\�`���Ŋi�[����N���X
 */
public class TableString {
	String array[] = null;
	int size = 0;
	final int INITIAL_SIZE = 100;
	
	
	/**
	 * String�`���Œl��ݒ肷��
	 */
	void setValue(int id, String value) {
		if(id <= 0) {
			System.out.println("  TableString: id(" + id + ") is out of range.");
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
		
	}
	
	
	/**
	 * String�`���Œl��Ԃ�
	 */
	String getString(int id) {
		if(id <= 0 || id > size) {
			System.out.println("  TableString: id(" + id + "/" + size + ") is out of range.");
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
	}
	
	/**
	 * �i�[����l�̌����Z�b�g����
	 * @param length �l�̌�
	 */
	void setSize(int length) {
		size = length;
		if(length < INITIAL_SIZE)
			length = INITIAL_SIZE;
		array = new String[length];
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
		String newarray[] = new String[length];
		for(int i = 0; i < array.length; i++)
			newarray[i] = array[i];
		array = newarray;
	}
	
	
	/**
	 * Node�̐F�⍂�����Z�o���邽�߂̒l��Ԃ�
	 * @param id �l���i�[���Ă���ID
	 * @return Node�̐F�⍂�����Z�o���邽�߂̒l�i�����Ƃ���0.0�`1.0�A�s����ID�̏ꍇ�͕��l�j
	 */
	double getAppearanceValue(int id) {
		double value = (double) id / (double) size;
		return value;
	}
}
