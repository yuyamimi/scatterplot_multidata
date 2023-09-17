package org.heiankyoview2.core.xmlio;

import java.io.*;



/**
 * �t�@�C���ǂݍ��݂̂��߂̃��[�e�B���e�B�N���X
 * @author itot
 */
public class XmlFileInput {

	/* var */
	File inputFile = null;
	BufferedReader inputData; 
	
	/**
	 * Constructor
	 * @param inputFile �ǂݍ��݃t�@�C��
	 */
	public XmlFileInput(File inputFile) {
		this.inputFile = inputFile;
		try { 
			inputData = new BufferedReader(new FileReader(inputFile));
		} catch (FileNotFoundException e) {
  			System.err.println("Error: " + inputFile.toString() + " does not exist.");
		}
	}

	/**
	 * �t�@�C�����ǂݍ��݂ł����Ԃɂ��邩�m�F����
	 * @return �ǂݍ��݂ł����Ԃł����true
	 */
	public boolean ready() { 
		try {
			return inputData.ready();
		} catch (IOException e) {
			System.err.println(e); 
			return false;
		}
	}

	/**
	 * �t�@�C���̒��g��1�s�ǂݍ���
	 * @return �ǂݍ���1�s�̕�����
	 */
	public String read() { 
		try {
			return inputData.readLine();
		} catch (IOException e) {
			System.err.println(e); 
		}
		return null; // ERROR ?
	}

	/**
	 * �t�@�C���ǂݍ��݂��I�����A�t�@�C�������
	 */
	public void close() { 
		try {
			inputData.close();
		} catch (IOException e) {
			System.err.println(e); 
		}
	}
}
