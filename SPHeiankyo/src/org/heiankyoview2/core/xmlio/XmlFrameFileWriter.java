package org.heiankyoview2.core.xmlio;

import java.io.File;

import org.heiankyoview2.core.frame.Frame;
import org.heiankyoview2.core.frame.TreeFrame;
import org.heiankyoview2.core.tree.Tree;

public class XmlFrameFileWriter {

	/* var */
	Tree tree = null;
	TreeFrame tf = null;
	XmlFileOutput output = null;
	String dummy = " ";
	int numframe = 0, numnode = 0, numdimension = 0;
	
	/**
	 * Constructor
	 * @param outputFile �o�͐�tree�t�@�C��
	 * @param tree Tree
	 */
	public XmlFrameFileWriter(File outputFile, Tree tree) {

		output = new XmlFileOutput(outputFile);
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
		
		// Node��
		numnode = tf.getNumNodes();

		// �ϐ��̎�����
		numdimension = tf.getNumValues();
		
		// <frames>�^�O���J��
		String linebuf1 = "<frames ";
		linebuf1 += ("numnode=\"" + Integer.toString(numnode) + "\" ");
		linebuf1 += ("numdimension=\"" + Integer.toString(numdimension) + "\">");
		output.println(linebuf1);
		
		// frame���Ƃ�
		for (int i = 1; i <= numframe; i++) {
			writeOneFrame(tf.getFrame(i), i);
		}
		
		// <frames>�^�O�����
		output.println("</frames>");
		
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

		// <frame> �^�O���J��
		double time = frame.getTime();
		String linebuf1 = " <frame name=\"" + Integer.toString(id) + "\" time=\"" + Double.toString(time) + "\">";
		output.println(linebuf1);

		// �eNode���Ƃɒl���o��
		for(int i = 1; i <= numnode; i++) {
			String linebuf2 = "  <framenode id=\"" + Integer.toString(i) + "\" value=\"";
			output.print(linebuf2);
			
			// �e�������Ƃɒl���o��
			for(int j = 1; j <= numdimension; j++) {
				String linebuf3 = "";
				if(j != 1) linebuf3 = ",";
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
			output.println("\" />");
			
		}
		
		// <frame>�^�O�����
		output.println(" </frame>");
		
		
		return ret;
	}

}
