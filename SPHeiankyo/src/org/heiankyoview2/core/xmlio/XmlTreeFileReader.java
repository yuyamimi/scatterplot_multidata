package org.heiankyoview2.core.xmlio;

import org.heiankyoview2.core.tree.Tree;
import org.heiankyoview2.core.tree.Branch;
import org.heiankyoview2.core.tree.Node;
import org.heiankyoview2.core.table.TreeTable;
import org.heiankyoview2.core.table.NodeTablePointer;
import org.heiankyoview2.core.table.Table;

import java.io.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;


/**
 * xml�t�@�C����ǂݍ���
 * 
 * @author itot
 */
public class XmlTreeFileReader {

	File xmlFile;
	Document doc;
	Tree tree;
	
	/**
	 * Constructor
	 * 
	 * @param inputFile
	 *            �ǂݍ��݃t�@�C��
	 */
	public XmlTreeFileReader(File inputFile) {

		try {
			// �h�L�������g�r���_�[�t�@�N�g���𐶐�
			DocumentBuilderFactory dbfactory = DocumentBuilderFactory
					.newInstance();
			// �h�L�������g�r���_�[�𐶐�
			DocumentBuilder builder = dbfactory.newDocumentBuilder();
			// �p�[�X�����s����Document�I�u�W�F�N�g���擾
			doc = builder.parse(inputFile);
			// ���[�g�v�f���擾�i�^�O���Fmessage�j

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * DOM�\����ǂݍ��݁A��������Tree��Ԃ�
	 * @return ��������Tree
	 */
	public Tree getTree() {
		tree = new Tree(); //create tree
		readFile(tree);
		return tree;
	}
	
	
	/**
	 * DOM�\����parse���āATree���\������f�[�^���\�z����
	 * @param tree Tree
	 * @return ���������true
	 */
	public boolean readFile(Tree tree) {
	
		/*
		 * Document�̐擪�m�[�h�𓾂�
		 */
		org.w3c.dom.Node  treeNode = doc.getFirstChild();
		String treeName = treeNode.getNodeName();
		if(treeName.compareTo("tree") != 0) return false;
		
		/*
		 * tree�m�[�h����tables�m�[�h��branch�m�[�h�𓾂�
		 */
		org.w3c.dom.NodeList  nodelist = treeNode.getChildNodes();
		org.w3c.dom.Node  tablesNode = null, rbranchNode = null;
		for(int i = 0; i < nodelist.getLength(); i++) {
			org.w3c.dom.Node node = nodelist.item(i);
			if(node.getNodeName().compareTo("tables") == 0)
				tablesNode = node;
			if(node.getNodeName().compareTo("branch") == 0)
				rbranchNode = node;
		}
		if(tablesNode == null || rbranchNode == null)
				return false;
		
		/*
		 * tables�m�[�h�̏���
		 */
		allocateTables(tree, tablesNode);
		
		/*
		 * branch�m�[�h�̏���
		 */
		Branch branch = tree.getOneNewBranch();
		tree.setRootBranch(branch);
		allocateBranch(tree, rbranchNode);
		
		return true;
	}

	
	/**
	 * �e�[�u�����m�ۂ���
	 * @param tree Tree
	 * @param tablesNode XML��������tables�^�O�ɑΉ�����m�[�h
	 */
	void allocateTables(Tree tree, org.w3c.dom.Node tablesNode) {
		TreeTable tg = tree.table;
		
		/*
		 * �e�[�u���̌����m�肷��
		 */
		int numTables = 0;
		org.w3c.dom.NodeList  nodelist = tablesNode.getChildNodes();
		for(int i = 0; i < nodelist.getLength(); i++) {
			org.w3c.dom.Node node = nodelist.item(i);
			if(node.getNodeName() == null ||
			   node.getNodeName().compareTo("table") != 0) continue;
			numTables++;
		}
		tg.setNumTable(numTables);
		
		/*
		 * �e�e�[�u�����Ƃ�
		 */
		int tid = 1;
		for(int i = 0; i < nodelist.getLength(); i++) {
			org.w3c.dom.Node node = nodelist.item(i);
			if(node.getNodeName() == null ||
			   node.getNodeName().compareTo("table") != 0) continue;
			Table table = new Table();
			tg.setTable(tid++, table);
			
			
			/*
			 * �e�[�u���̖��O
			 */
			org.w3c.dom.Element element = (org.w3c.dom.Element)node;
			String tablename = element.getAttribute("name");
			table.setName(tablename);
			
			/*
			 * �e�[�u���̌^
			 */
			String tabletype = element.getAttribute("type");
			if(tabletype.compareTo("string") == 0) 
				table.setType(table.TABLE_STRING);
			if(tabletype.compareTo("double") == 0) 
				table.setType(table.TABLE_DOUBLE);
			if(tabletype.compareTo("int") == 0) 
				table.setType(table.TABLE_INT);
			
			/*
			 * �e�[�u���̗v�f��
			 */
			org.w3c.dom.NodeList nodelist2 = node.getChildNodes();
			int numelements = 0;
			for(int j = 0; j < nodelist2.getLength(); j++) {
				org.w3c.dom.Node node2 = nodelist2.item(j);
				if(node2.getNodeName().compareTo("tableline") != 0) continue;
				numelements++;
			}
			table.setSize(numelements);
			
			/*
			 * �e�[�u���̊e�v�f
			 */
			int eid = 1;
			for(int j = 0; j < nodelist2.getLength(); j++) {
				org.w3c.dom.Node node2 = nodelist2.item(j);
				if(node2.getNodeName().compareTo("tableline") != 0) continue;
				org.w3c.dom.Element element2 = (org.w3c.dom.Element)node2;
				String value = element2.getAttribute("value");
					
				if (table.getType() == table.TABLE_STRING) { // String
					table.set(eid++, value);
				}
				if (table.getType() == table.TABLE_DOUBLE) { // Double
					table.set(eid++, Double.parseDouble(value));
				}
				if (table.getType() == table.TABLE_INT) { // Int
					table.set(eid++, Integer.parseInt(value));
				}
			}

		}
		
	}
	

	/**
	 * �O���[�v���m�ۂ���
	 * @param tree Tree
	 * @param tablesNode XML��������tables�^�O�ɑΉ�����m�[�h
	 */
	void allocateBranch(Tree tree, org.w3c.dom.Node branchNode) {
		int numbranchs = tree.getBranchList().size();
		Branch branch = (Branch)tree.getBranchList().elementAt(numbranchs - 1);
		org.w3c.dom.NodeList  xmlnodelist = branchNode.getChildNodes();
		
		/*
		 * �e�X��XML�m�[�h�ɂ���
		 */
		for(int i = 0; i < xmlnodelist.getLength(); i++) {
			org.w3c.dom.Node xmlnode = xmlnodelist.item(i);
			
			/*
			 * Branch����Table�ւ̃|�C���^
			 */
			if(xmlnode.getNodeName().compareTo("tablepointer") == 0) {
				Node node = branch.getParentNode();
				setTablePointer(tree, node, xmlnode);
			}
			
			/*
			 * Node
			 */
			if(xmlnode.getNodeName().compareTo("node") == 0) {
				Node node = branch.getOneNewNode();
				node.setChildBranch(null);
				setOneNode(tree, node, xmlnode);
			}
			
			/*
			 * Branch
			 */
			if(xmlnode.getNodeName().compareTo("branch") == 0) {
				Node node = branch.getOneNewNode();
				Branch cbranch = tree.getOneNewBranch();
				cbranch.setLevel(branch.getLevel() + 1);
				node.setChildBranch(cbranch);
				cbranch.setParentNode(node);
				allocateBranch(tree, xmlnode);
			}
			
		}
		
	}
	
	
	/**
	 * �O���[�v���m�ۂ���
	 * @param tree Tree
	 * @param node Node
	 * @param xmlnode XML��������node�^�O�ɑΉ�����m�[�h
	 */
	void setOneNode(Tree tree, Node node, org.w3c.dom.Node xmlnode) {
		org.w3c.dom.NodeList  xmlnodelist = xmlnode.getChildNodes();
		
		/*
		 * �e�X��XML�m�[�h�ɂ���
		 */
		for(int i = 0; i < xmlnodelist.getLength(); i++) {
			org.w3c.dom.Node xmlnode2 = xmlnodelist.item(i);
			if(xmlnode2.getNodeName().compareTo("tablepointer") != 0) 
				continue;
			setTablePointer(tree, node, xmlnode2);
		}
	}
	
	
	/**
	 * �e�[�u���|�C���^��ݒ肷��
	 * @param tree Tree
	 * @param node Node
	 * @param xmlnode XML��������node�^�O�ɑΉ�����m�[�h
	 */
	void setTablePointer(Tree tree, Node node, org.w3c.dom.Node xmlnode) {
		TreeTable tg = tree.table;
		NodeTablePointer tn = node.table;
		tn.setNumId(tg.getNumTable());
		
		org.w3c.dom.Element element = (org.w3c.dom.Element)xmlnode;
		String tablename = element.getAttribute("tablename");
		String lineid = element.getAttribute("tablelineid");
		
		/*
		 * �e�[�u���|�C���^�ɏ����ꂽ���O�ɑΉ�����Table����肷��
		 */
		Table table = null;  int ii = -1;
		for(int i = 1; i <= tg.getNumTable(); i++) {
			Table t = tg.getTable(i);
			if(t.getName().compareTo(tablename) == 0) {
				table = t;  ii = i;  break;
			}
		}
		if(table == null) return;
		
		/*
		 * �e�[�v���|�C���^���Z�b�g����
		 */
		int id = Integer.parseInt(lineid);
		tn.setId(ii, id);
		
	}

}