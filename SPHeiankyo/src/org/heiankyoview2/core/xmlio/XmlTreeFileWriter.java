package org.heiankyoview2.core.xmlio;

import java.io.File;

import org.heiankyoview2.core.tree.*;
import org.heiankyoview2.core.table.*;

public class XmlTreeFileWriter {

	/* var */
	Tree tree = null;
	XmlFileOutput output = null;
	int numBranch;
	String dummy = " ";

	/**
	 * Constructor
	 * @param outputFile �o�͐�XML�t�@�C��
	 * @param tree Tree
	 */
	public XmlTreeFileWriter(File outputFile, Tree tree) {

		output = new XmlFileOutput(outputFile);
		this.tree = tree;
	}

	/**
	 * XML�t�@�C���������o��
	 * @return ���������true
	 */
	public boolean writeTree() {
		boolean ret = true;
		
		if (tree == null) {
			output.close();
			return false;
		}
		
		/*
		 * Tree�^�O�̊J�n
		 */
		output.println("<tree>");
		
		/*
		 * Tables�^�O
		 */
		if((ret = writeTables()) == false) {
			output.close();
			return false;
		}
		
		/*
		 * Branch�^�O
		 */
		if((ret = writeBranches()) == false) {
			output.close();
			return false;
		}
		
		/*
		 * Tree�^�O�̏I��
		 */
		output.println("</tree>");
		
		output.close();
		return ret;
	}
	
	
	/**
	 * XML�t�@�C����Table�̑S���������o��
	 * @return ���������true
	 */
	boolean writeTables() {
		boolean ret = true;
		TreeTable tg = tree.table;
		
		/*
		 * Tables�^�O�̊J�n
		 */
		output.println(" <tables>");

		/*
		 * �eTable���Ƃ�
		 */
		for(int i = 1; i <= tg.getNumTable(); i++) {
			Table table = tg.getTable(i);
			if((ret = writeOneTable(table)) == false) {
				output.close();
				return ret;
			}
		}
		
		/*
		 * Tables�^�O�̏I��
		 */
		output.println(" </tables>");
		return ret;
	}
	
	/**
	 * XML�t�@�C����Branch�̑S���������o��
	 * @return ���������true
	 */
	boolean writeBranches() {
		boolean ret = true;
		TreeTable tg = tree.table;
		
		/*
		 * �eBranch���Ƃ�
		 */
		Branch rootBranch = tree.getRootBranch();
		if((ret = writeOneBranch(rootBranch)) == false) {
			output.close();
			return ret;
		}
		
		return ret;
	}
	
	/**
	 * XML�t�@�C����1��Table�̏��������o��
	 * @return ���������true
	 */
	boolean writeOneTable(Table table) {
		boolean ret = true;
		String name = table.getName();
		String type = "";
		if(table.getType() == table.TABLE_STRING) type = "string";
		if(table.getType() == table.TABLE_DOUBLE) type = "double";
		if(table.getType() == table.TABLE_INT)    type = "int";
		if(table.getType() == table.TABLE_DATE)   type = "date";
		
		/*
		 * Table�^�O�̊J�n
		 */
		output.println("  <table name=\"" + name + "\" type=\"" + type + "\" >");
		
		/*
		 * Table���\������e�s���Ƃ�
		 */
		for(int i = 1; i <= table.getSize(); i++) {
			String value = "";
			if(table.getType() == table.TABLE_STRING)
				value = table.getString(i);
			if(table.getType() == table.TABLE_DOUBLE)
				value = Double.toString(table.getDouble(i));
			if(table.getType() == table.TABLE_INT)
				value = Integer.toString(table.getInt(i));
			output.println("   <tableline id=\"" + i + "\" value=\"" + value + "\" />");
		}
		
		/*
		 * Table�^�O�̏I��
		 */
		output.println("  </table>");
		return ret;
	}
	
	/**
	 * XML�t�@�C����1��Branch�̏��������o��
	 * @return ���������true
	 */
	boolean writeOneBranch(Branch branch) {
		boolean ret = true;
		int indent = branch.getLevel() * 2;
		
		/*
		 * Branch�^�O�̊J�n
		 */
		for(int i = 1; i <= indent; i++)
			output.print(" ");
		output.println("<branch>");
		
		/*
		 * �z�u�ς݂ł���΁A���̏����L�q����
		 */
		Node pnode = branch.getParentNode();
		if(pnode.getPlaced() == true) {
			for(int i = 1; i <= (indent + 1); i++)
				output.print(" ");
			String sizetext =
				"<branchsize width="
				+ pnode.getWidth()
				+ " height="
				+ pnode.getHeight()
				+ " depth="
				+ pnode.getDepth()
				+ ">";
			output.println(sizetext);
			for(int i = 1; i <= (indent + 1); i++)
				output.print(" ");
			String postext =
				"<branchpos x="
				+ pnode.getX()
				+ " y="
				+ pnode.getY()
				+ " z="
				+ pnode.getZ()
				+ ">";
			output.println(postext);
		}
		
		/*
		 * Branch��TablePointer
		 */
		writeTablePointer(branch.getParentNode(), (indent + 1), true);
		
		/*
		 * �eNode���Ƃ�
		 */
		for(int i = 0; i < branch.getNodeList().size(); i++) {
			Node node = (Node)branch.getNodeList().elementAt(i);
			if(node.getChildBranch() != null) continue;
			for(int j = 1; j <= (indent + 1); j++)
				output.print(" ");
			output.println("<node>");
			
			/*
			 * �z�u�ς݂ł���΁A���̏����L�q����
			 */
			if(node.getPlaced() == true) {
				for(int j = 1; j <= (indent + 2); j++)
					output.print(" ");
				String sizetext =
					"<nodesize width="
					+ node.getWidth()
					+ " height="
					+ node.getHeight()
					+ " depth="
					+ node.getDepth()
					+ ">";
				output.println(sizetext);
				for(int j = 1; j <= (indent + 2); j++)
					output.print(" ");
				String postext =
					"<nodepos x="
					+ node.getX()
					+ " y="
					+ node.getY()
					+ " z="
					+ node.getZ()
					+ ">";
				output.println(postext);
			}
			
			writeTablePointer(node, (indent + 2), true);
			for(int j = 1; j <= (indent + 1); j++)
				output.print(" ");
			output.println("</node>");
		}
		
		/*
		 * �eChild Branch���Ƃ�
		 */
		for(int i = 0; i < branch.getNodeList().size(); i++) {
			Node node = (Node)branch.getNodeList().elementAt(i);
			if(node.getChildBranch() == null) continue;
			writeOneBranch(node.getChildBranch());
		}
		
		/*
		 * Branch�^�O�̏I��
		 */
		for(int i = 1; i <= indent; i++)
			output.print(" ");
		output.println("</branch>");

		return ret;
	}
	

	/**
	 * XML�t�@�C����1��Node�i�܂���Branch�j��TablePointer������������
	 */
	void writeTablePointer(Node node, int indent, boolean branchFlag) {
		NodeTablePointer tn = node.table;
		TreeTable tg = tree.table;
		for(int i = 1; i <= tn.getNumId(); i++) {
			Table table = tg.getTable(i);
			String name = table.getName();
			int id = tn.getId(i);
			for(int j = 1; j <= indent; j++)
				output.print(" ");
			output.println("<tablepointer tablename=\"" + name + "\" tablelineid=\"" + id + "\" />");
		}
		
	}
	
}
