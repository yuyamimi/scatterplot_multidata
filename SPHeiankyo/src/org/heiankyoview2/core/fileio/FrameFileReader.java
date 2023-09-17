package org.heiankyoview2.core.fileio;

import org.heiankyoview2.core.tree.Tree;
import org.heiankyoview2.core.tree.Link;
import org.heiankyoview2.core.tree.Branch;
import org.heiankyoview2.core.tree.Node;
import org.heiankyoview2.core.frame.TreeFrame;
import org.heiankyoview2.core.frame.Frame;

import java.io.*;
import java.util.*;
import java.awt.Image;
import java.net.URL;
import javax.imageio.*;

/**
 * tree�t�@�C����ǂݍ���
 * @author itot
 */
public class FrameFileReader {

	/* var */
	FileInput input = null;
	Tree tree = null;
	TreeFrame tf = null;
	int numframe = 0;

	/**
	 * Constructor
	 * @param inputFile �ǂݍ��݃t�@�C��
	 */
	public FrameFileReader(File inputFile) {
		input = new FileInput(inputFile);
	}

	/**
	 * tree�t�@�C����ǂݍ��݁A��������Tree��Ԃ�
	 * @return ��������Tree
	 */
	public void getFrame(Tree tree) {
		this.tree = tree;
		tf = new TreeFrame();
		tree.setTreeFrame(tf);
		readFile();
		input.close();

	}

	/**
	 * frame�t�@�C����parse���āATree���\������f�[�^���\�z����
	 * @param tree Tree
	 * @return ���������true
	 */
	public boolean readFile() {

		String lineBuffer;
		Frame frame = null;

		while (input.ready() ) {
			if((lineBuffer = input.read()) == null) break;
			
			//read .NVW file
			if (lineBuffer.startsWith("#")) {
				continue; //skip comment line
			} else if (lineBuffer.startsWith("numframe")) {
				setNumFrame(lineBuffer.substring(8).trim());
			} else if (lineBuffer.startsWith("numnode")) {
				setNumNode(lineBuffer.substring(7).trim());
				continue;
			} else if (lineBuffer.startsWith("numdimension")){
				setNumDimension(lineBuffer.substring(12).trim());
				continue;
			} else if (lineBuffer.startsWith("frame")) {
				frame = setFrame(lineBuffer.substring(5).trim());
			} else if (lineBuffer.startsWith("fn")) {
				setFrameNode(frame, lineBuffer.substring(2).trim());
				continue;
			} else if (lineBuffer.startsWith("framenode")) {
				setFrameNode(frame, lineBuffer.substring(9).trim());
				continue;
			} 
		}

		return true;
	}


	/**
	 * Frame����ݒ肷��
	 * @param lineBuffer
	 */
	void setNumFrame(String lineBuffer) {
		StringTokenizer tokenBuffer = new StringTokenizer(lineBuffer);
		numframe = Integer.parseInt(tokenBuffer.nextToken());
		// Frame���̂� setFrame ���\�b�h�Ŋm�ۂ���
	}

	
	/**
	 * Node����ݒ肷��
	 * @param lineBuffer
	 */
	void setNumNode(String lineBuffer) {
		StringTokenizer tokenBuffer = new StringTokenizer(lineBuffer);
		int numnode = Integer.parseInt(tokenBuffer.nextToken());
		tf.setNumNodes(numnode);
	}

	
	/**
	 * Dimension����ݒ肷��
	 * @param lineBuffer
	 */
	void setNumDimension(String lineBuffer) {
		StringTokenizer tokenBuffer = new StringTokenizer(lineBuffer);
		int numdim = Integer.parseInt(tokenBuffer.nextToken());
		tf.setNumValues(numdim);
	}
	
	
	/**
	 * 1��Frame��ݒ肵�A�����Ԃ�
	 * @param lineBuffer
	 * @return
	 */
	Frame setFrame(String lineBuffer) {
		
		if(tf.getNumFrames() <= 0) {
			for(int i = 1; i <= numframe; i++)
				tf.addOneFrame();
		}
		
		Frame frame = null;
		StringTokenizer tokenBuffer = new StringTokenizer(lineBuffer);
		if(tokenBuffer.countTokens() < 2)
			return null;
		int frameId = Integer.parseInt(tokenBuffer.nextToken());
		frame = tf.getFrame(frameId);
		double time = Double.parseDouble(tokenBuffer.nextToken());
		frame.setTime(time);
		return frame;
	}
	

	/**
	 * 1��Node�ɑ΂���Frame����ݒ肷��
	 * @param frame
	 * @param lineBuffer
	 */
	void setFrameNode(Frame frame, String lineBuffer) {
		if(frame == null) return;
		int numValues = tf.getNumValues();
		StringTokenizer tokenBuffer = new StringTokenizer(lineBuffer);
		if(tokenBuffer.countTokens() < (1 + numValues))
			return;
		int nodeId = Integer.parseInt(tokenBuffer.nextToken());
		
		for(int i = 1; i <= numValues; i++) {
			String value = tokenBuffer.nextToken();
			int type = tf.getValueType(i);
			switch(type) {
			case TreeFrame.FRAME_STRING:
				frame.set(i, nodeId, value);
				break;
			case TreeFrame.FRAME_DOUBLE:
				double dvalue = Double.parseDouble(value);
				frame.set(i, nodeId, dvalue);
				break;
			case TreeFrame.FRAME_INT:
				int ivalue = Integer.parseInt(value);
				frame.set(i, nodeId, ivalue);
				break;
			}
		}
		
	}
	
}
