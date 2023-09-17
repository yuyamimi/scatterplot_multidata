package org.heiankyoview2.core.fileio;

import java.io.*;



/**
 * �t�@�C���ǂݍ��݂̂��߂̃��[�e�B���e�B�N���X
 * @author itot
 */
public class FileInput {

	/* var */
	File inputFile = null;
	String filename = null;
	boolean streamFlag = false;
	BufferedReader inputData; 
	
	/**
	 * Constructor
	 * @param inputFile �ǂݍ��݃t�@�C��
	 */
	public FileInput(File inputFile) {
		this.inputFile = inputFile;
		try { 
			inputData = new BufferedReader(new FileReader(inputFile));
		} catch (FileNotFoundException e) {
  			System.err.println("Error: " + inputFile.toString() + " does not exist.");
		}
	}
	
	/**
	 * Constructor
	 * @param inputFile �ǂݍ��݃t�@�C��
	 */
	public FileInput(String filename, boolean streamFlag) {
		this.filename = filename;
		this.streamFlag = streamFlag;
		if(streamFlag == false) {
			try { 
				inputFile = new File(filename);
				inputData = new BufferedReader(new FileReader(inputFile));
			} catch (Exception e) {
				System.err.println("Error: " + inputFile.toString() + " does not exist.");
			}
		}
		else {
			ClassLoader cl = this.getClass().getClassLoader();
			InputStream inputStream = null;
			try {
				inputStream = cl.getResource(filename).openStream();
				inputData = new BufferedReader(new InputStreamReader(inputStream));
			} catch (Exception e) {
				System.err.println("Error: " + filename + " does not exist.");
				e.printStackTrace();
			}
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
