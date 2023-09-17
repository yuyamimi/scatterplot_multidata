package org.heiankyoview2.core.fileio;

import org.heiankyoview2.core.tree.Link;
import org.heiankyoview2.core.tree.Tree;
import org.heiankyoview2.core.tree.Branch;
import org.heiankyoview2.core.tree.Node;
import org.heiankyoview2.core.frame.Frame;
import org.heiankyoview2.core.frame.TreeFrame;
import java.io.*;
import java.util.*;


/**
 * tree�t�@�C���ւ̏����o��
 * @author itot
 */
public class FrameFileWriter {

	/* var */
	Tree tree = null;
	TreeFrame tf = null;
	FileOutput output = null;
	String dummy = " ";
	int numframe = 0, numnode = 0, numdimension = 0;
	
	/**
	 * Constructor
	 * @param outputFile �o�͐�tree�t�@�C��
	 * @param tree Tree
	 */
	public FrameFileWriter(File outputFile, Tree tree) {

		output = new FileOutput(outputFile);
		this.tree = tree;
		this.tf = tree.getTreeFrame();
	}

	/**
	 * frame�t�@�C���������o��
	 * @return ���������true
	 */
	public boolean writeFrame() {
		boolean ret = true;

		if (tree == null) {
			output.close();
			return false;
		}

		// Frame��
		numframe = tf.getNumFrames();
		String linebuf1 = "numframe  " + Integer.toString(numframe);
		output.println(linebuf1);
		
		// Node��
		numnode = tf.getNumNodes();
		String linebuf2 = "numnode  " + Integer.toString(numnode);
		output.println(linebuf2);

		// �ϐ��̎�����
		numdimension = tf.getNumValues();
		String linebuf3 = "numdimension  " + Integer.toString(numdimension);
		output.println(linebuf3);
		
		for (int i = 1; i <= numframe; i++) {
			writeOneFrame(tf.getFrame(i), i);
		}

		output.close();
		return ret;

	}


	/**
	 * frame�t�@�C����1��Frame�̏��������o��
	 * @param frame
	 * @param id
	 * @return 
	 */
	boolean writeOneFrame(Frame frame, int id) {
		boolean ret = true;

		// Frame��ID�Ǝ������o��
		output.println(dummy);
		double time = frame.getTime();
		String linebuf1 = "frame " + Integer.toString(id) + " " + Double.toString(time);
		output.println(linebuf1);

		// �eNode���Ƃɒl���o��
		for(int i = 1; i <= numnode; i++) {
			String linebuf2 = "fn " + Integer.toString(i);
			output.print(linebuf2);
			
			// �e�������Ƃɒl���o��
			for(int j = 1; j <= numdimension; j++) {
				String linebuf3 = " ";
				switch(tf.getValueType(j)) {
				case TreeFrame.FRAME_STRING:
					String svalue = frame.getString(j, i);
					linebuf3 += svalue;
					break;
				case TreeFrame.FRAME_DOUBLE:
					double dvalue = frame.getDouble(j, i);
					linebuf3 += Double.toString(dvalue);
					break;
				case TreeFrame.FRAME_INT:
					int ivalue = frame.getInt(j, i);
					linebuf3 += Integer.toString(ivalue);
					break;
				}
				output.print(linebuf3);
			}
			
			// 1��Node�ɂ���1����s
			output.println("");
			
		}
		
		
		return ret;
	}

}
