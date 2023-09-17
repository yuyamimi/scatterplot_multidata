package org.heiankyoview2.core.table;

import java.util.*;
import java.text.SimpleDateFormat;

/**
 * Tree�̑�����\�����邽�߂̕\�`���f�[�^���i�[����N���X
 * @author itot
 */
public class Table {

	public static final int TABLE_NONE = 0;
	public static final int TABLE_STRING = 1;
	public static final int TABLE_DOUBLE = 2;
	public static final int TABLE_INT = 3;
	public static final int TABLE_DATE = 4;

	String name;
	int type = TABLE_NONE;
	
	TableString ts;
	TableDouble td;
	TableInt    ti;
	TableDate   ta;
	

	/**
	 * Constructor
	 */
	public Table(int type) {
		setType(type);
	}

	/**
	 * Constructor
	 */
	public Table() {
		this.type = TABLE_NONE;
	}
		
	/**
	 * �e�[�u���̏�����
	 */
	public void clear() {
		switch(type) {
		case TABLE_STRING:
			ts.clear();
			break;
		case TABLE_DOUBLE:
			td.clear();
			break;
		case TABLE_INT:
			ti.clear();
			break;
		case TABLE_DATE:
			ta.clear();
			break;
		}
	}

	
	/**
	 * �i�[����l�̃f�[�^�^���Z�b�g����
	 * @param type �f�[�^�^ �i1:String, 2:double, 3:int�j
	 */
	public void setType(int type) {
		this.type = type;
		switch(type) {
		case TABLE_STRING:
			ts = new TableString();
			break;
		case TABLE_DOUBLE:
			td = new TableDouble();
			break;
		case TABLE_INT:
			ti = new TableInt();
			break;
		case TABLE_DATE:
			ta = new TableDate();
			break;
		}
	}

	/**
	 * �i�[����l�̃f�[�^�^��Ԃ�
	 * @return �f�[�^�^ �i1:String, 2:double, 3:int�j
	 */
	public int getType() {
		return type;
	}

	/**
	 * �e�[�u���̖��O���Z�b�g����
	 * @param name ���O
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * �e�[�u���̖��O��Ԃ�
	 * @return ���O
	 */
	public String getName() {
		return name;
	}


	/**
	 * �e�[�u���ɒl���Z�b�g����
	 * @param id �e�[�u������ID
	 * @param value �Z�b�g����l
	 */
	public void set(int id, String value) {
			ts.setValue(id, value);		
	}

	/**
	 * �e�[�u���ɒl���Z�b�g����
	 * @param id �e�[�u������ID
	 * @param value �Z�b�g����l
	 */
	public void set(int id, double value) {
			td.setValue(id, value);		
	}
	
	/**
	 * �e�[�u���ɒl���Z�b�g����
	 * @param id �e�[�u������ID
	 * @param value �Z�b�g����l
	 */
	public void set(int id, int value) {
			ti.setValue(id, value);		
	}
	
	/**
	 * �e�[�u���ɒl���Z�b�g����
	 * @param id �e�[�u������ID
	 * @param value �Z�b�g����l
	 */
	public void set(int id, Date value) {
			ta.setValue(id, value);		
	}
	
	/**
	 * String�`���̃e�[�u������l�𓾂�
	 * @param Id �e�[�u������ID
	 * @return String�`���̒l
	 */
	public String getString(int id) {
		return ts.getString(id);
	}

	/**
	 * double�`���̃e�[�u������l�𓾂�
	 * @param Id �e�[�u������ID
	 * @return double�`���̒l
	 */
	public double getDouble(int id) {
		return td.getDouble(id);
	}

	/**
	 * int�`���̃e�[�u������l�𓾂�
	 * @param Id �e�[�u������ID
	 * @return int�`���̒l
	 */
	public int getInt(int id) {
		return ti.getInt(id);
	}

	/**
	 * Date�`���̃e�[�u������l�𓾂�
	 * @param Id �e�[�u������ID
	 * @return String�`���̒l
	 */
	public Date getDate(int id) {
		return ta.getDate(id);
	}

	

	/**
	 * �i�[����l�̌����Z�b�g����
	 * @param type �l�̌�
	 */
	public void setSize(int size) {
		switch(type) {
		case TABLE_STRING:
			ts.setSize(size);
			break;
		case TABLE_DOUBLE:
			td.setSize(size);
			break;
		case TABLE_INT:
			ti.setSize(size);
			break;
		case TABLE_DATE:
			ta.setSize(size);
			break;
		}
	}

	/**
	 * �i�[����l�̌���Ԃ�
	 * @return �l�̌�
	 */
	public int getSize() {
		int size = 0;
		
		switch(type) {
		case TABLE_STRING:
			size = ts.getSize();
			break;
		case TABLE_DOUBLE:
			size = td.getSize();
			break;
		case TABLE_INT:
			size = ti.getSize();
			break;
		case TABLE_DATE:
			size = ta.getSize();
			break;
		}
		
		return size;
	}

	
	/**
	 * �T�C�Y���ĕύX����
	 */
	public void resize(int newsize) {
		switch(type) {
		case TABLE_STRING:
			ts.resize(newsize);
			break;
		case TABLE_DOUBLE:
			td.resize(newsize);
			break;
		case TABLE_INT:
			ti.resize(newsize);
			break;
		case TABLE_DATE:
			ta.resize(newsize);
			break;
		}
	}
	
	
	
	/**
	 * Node�̐F�⍂�����Z�o���邽�߂̒l��Ԃ�
	 * @param id �l���i�[���Ă���ID
	 * @return Node�̐F�⍂�����Z�o���邽�߂̒l�i�����Ƃ���0.0�`1.0�A�s����ID�̏ꍇ�͕��l�j
	 */
	public double getAppearanceValue(int id) {
		double value = -1.0;
		
		switch(type) {
		case TABLE_STRING:
			value = ts.getAppearanceValue(id);
			break;
		case TABLE_DOUBLE:
			value = td.getAppearanceValue(id);
			break;
		case TABLE_INT:
			value = ti.getAppearanceValue(id);
			break;
		case TABLE_DATE:
			value = ta.getAppearanceValue(id);
			break;
		}

		return value;
	}

}
