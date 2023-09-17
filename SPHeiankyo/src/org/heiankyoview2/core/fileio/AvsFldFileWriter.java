
package org.heiankyoview2.core.fileio;

import java.io.File;
import java.util.Vector;

import org.heiankyoview2.core.tree.Tree;
import org.heiankyoview2.core.tree.Branch;
import org.heiankyoview2.core.tree.Node;
import org.heiankyoview2.core.table.TreeTable;
import org.heiankyoview2.core.table.NodeTablePointer;
import org.heiankyoview2.core.table.Table;


public class AvsFldFileWriter {
	
	/* var */
	Tree tree = null;
	FileOutput output = null;
	TreeTable tg;
	Table table;
	int tableType = -1, heightType = -1;
	
	/**
	 * Constructor
	 * @param outputFile �o�͐�tree�t�@�C��
	 * @param tree Tree
	 */
	public AvsFldFileWriter(File outputFile, Tree tree) {

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
		 * ���l�������o���ׂ��e�[�u�������
		 */
		tg = tree.table;
		heightType = tg.getHeightType();
		if(heightType <= 0) return false;
		table = tg.getTable(heightType + 1);
		tableType = table.getType();
		if(tableType != 2 && tableType != 3) return false;
		
		
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
		output.println("# AVS field file");
		output.println("ndim=1");
		output.println("nspace=2");
		output.println("dim1=24");
		output.println("veclen=1");
		output.println("data=float");
		output.println("field=irregular");
		output.println("variable 1 file=./tmp.fld filetype=ascii skip=12 offset=0 stride=3");
		output.println("coord 1 file=./tmp.fld filetype=ascii skip=12 offset=1 stride=3");
		output.println("coord 2 file=./tmp.fld filetype=ascii skip=12 offset=2 stride=3");		
		output.println("");
		output.println("#data   X    Y");
	}
	
	/*
	 * 1�O���[�v�ɏ�������m�[�h�̏��������o��
	 */
	void writeDataOneBranch(Branch branch){

		/**
		 * �e�m�[�h���Ƃ�
		 */
		Vector nodelist = branch.getNodeList();
		for(int i = 0; i < nodelist.size(); i++) {
			Node node = (Node)nodelist.elementAt(i);
			
			/*
			 * �m�[�h�Ɏq�O���[�v�������
			 */
			if(node.getChildBranch() != null) {
				writeDataOneBranch(node.getChildBranch());
				continue;
			}
			
			NodeTablePointer tn = node.table;
			int id = tn.getId(heightType);

			// double
			if(tableType == 2) {
				double value = table.getDouble(id);
				output.println(value + " " + node.getX() + " " + node.getY());
			}
			
			// int
			if(tableType == 3) {
				int value = table.getInt(id);
				output.println(value + " " + node.getX() + " " + node.getY());
			}
			
		}
	}
	
}
