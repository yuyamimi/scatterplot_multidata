package org.heiankyoview2.core.xmlio;
import java.io.*;


/**
 * �t�@�C�������o���̂��߂̃��[�e�B���e�B�N���X
 * @author itot
 */
public class XmlFileOutput {

	/* var */
	File outputFile = null;
	BufferedWriter outputData;

	/**
	 * Constructor
	 * @param inputFile �ǂݍ��݃t�@�C��
	 */
	public XmlFileOutput(File outputFile) {
		this.outputFile = outputFile; 
		try {
			outputData = new BufferedWriter(new FileWriter(outputFile));
		} catch (IOException e) {
			System.err.println("Error: exception in " + outputFile.toString() + ".");
		}
	}

	/**
	 * ��������t�@�C���ɏ����o��
	 * @param line
	 */
	public void print(String line) {
		try {
			outputData.write(line, 0, line.length());
			outputData.flush();
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	/**
	 * ��������t�@�C���ɏ����o���A���s����
	 * @param line
	 */
	public void println(String line) {
		try {
			outputData.write(line, 0, line.length());
			outputData.flush();
			outputData.newLine();
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	/**
	 * �t�@�C�������o�����I�����A�t�@�C�������
	 */
	public void close() {
		try {
			outputData.close();
		} catch (IOException e) {
			System.err.println(e);
		}
	}

}
