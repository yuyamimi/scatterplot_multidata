
package org.heiankyoview2.core.fileio;

import java.io.File;
import java.util.Vector;

import org.heiankyoview2.core.tree.Tree;
import org.heiankyoview2.core.tree.Branch;
import org.heiankyoview2.core.tree.Node;

public class AvsMgfFileWriter {

	/* var */
	Tree tree = null;
	FileOutput output = null;

	
	/**
	 * Constructor
	 * @param outputFile �o�͐�tree�t�@�C��
	 * @param tree Tree
	 */
	public AvsMgfFileWriter(File outputFile, Tree tree) {

		output = new FileOutput(outputFile);
		this.tree = tree;
	}

	/**
	 * fld�t�@�C���������o��
	 * @return ���������true
	 */
	public boolean writeData() {
		boolean ret = true;
		
		
		/**
		 * �w�b�_���̏����o��
		 */
		writeHeader();
		
		/**
		 * rootBranch ����f�[�^�����o�����o��
		 */
		writeDataOneBranch(tree.getRootBranch());
		
		return true;
	}
	
	
	/**
	 * �w�b�_�����t�@�C���ɏ�������
	 */
	void writeHeader() {
		output.println("# Micro AVS Geom:2.10");
	}
	
	/*
	 * 1�O���[�v�ɏ�������m�[�h�̏��������o��
	 */
	void writeDataOneBranch(Branch branch){

		/*
		 * ���Y�m�[�h�̏�����������
		 */
		output.println("polyline");
		output.println("pline_sample");
		output.println("vertex");
		output.println("5");
		
		Node parentNode = branch.getParentNode();
		double xmax = parentNode.getX() + parentNode.getWidth();
		double xmin = parentNode.getX() - parentNode.getWidth();
		double ymax = parentNode.getY() + parentNode.getHeight();
		double ymin = parentNode.getY() - parentNode.getHeight();
		
		output.println(xmax + " " + ymax + " 0.0");
		output.println(xmin + " " + ymax + " 0.0");
		output.println(xmin + " " + ymin + " 0.0");
		output.println(xmax + " " + ymin + " 0.0");
		output.println(xmax + " " + ymax + " 0.0");
		output.println("");
		
		/**
		 * �e�m�[�h���Ƃ�
		 */
		Vector nodelist = branch.getNodeList();
		for(int i = 0; i < nodelist.size(); i++) {
			Node node = (Node)nodelist.elementAt(i);
			
			/*
			 * �m�[�h�Ɏq�O���[�v���Ȃ���Ώȗ�
			 */
			if(node.getChildBranch() == null) continue;
			writeDataOneBranch(node.getChildBranch());
		}
	}
	
}
