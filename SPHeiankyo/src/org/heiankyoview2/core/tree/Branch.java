package org.heiankyoview2.core.tree;

import java.util.*;
import java.awt.Color;
import org.heiankyoview2.core.table.NodeTablePointer;

/**
 * �K�w�^�f�[�^��1�K�w���\������Node��Branch���`����
 * 
 * @author itot
 */
public class Branch {

	int id;
	String name;
	int level;
	Color color;

	// ����Branch �ɑ����� Node 
	Vector nodeList;
	Node parentNode;
	
	// ���� Branch �������� Tree
	Tree tree;


	/**
	 * Constructor
	 * @param id
	 * @param numNodes
	 * @param tree
	 */
	public Branch(int id, int numNodes, Tree tree) {
		nodeList = new Vector();
		setId(id);
		setNumNode(numNodes);
		level = 0;
		this.tree = tree;
	}

	/**
	 * Constructor
	 * @param id
	 * @param tree
	 */
	public Branch(int id, Tree tree) {
		this(id, 0, tree);
	}

	
	/**
	 * �m�[�h�̐���ݒ肷��
	 * @param �m�[�h�̐�
	 * 
	 */
	public void setNumNode(int numnode) {
		if (nodeList.size() != 0) {
			System.err.println("Error:nodeList already exists");
		} else {
			for (int i = 1; i <= numnode; i++) {
				Node node = new Node(i, this);
				nodeList.add(node);
			}
		}
	}

	/**
	 * �m�[�h�̐���Ԃ�
	 * @return �m�[�h�̐�
	 */
	public int getNumNode() {
		return nodeList.size();
	}

	/**
	 * �m�[�h�̃��X�g��Ԃ�
	 * @return �m�[�h�̃��X�g
	 */
	public Vector getNodeList() {
		return nodeList;
	}

	/**
	 * ����ԍ��̃m�[�h��Ԃ�
	 */
	public Node getNodeAt(int index) {
		if (index > 0 && index <= nodeList.size()) {
			return (Node) nodeList.elementAt(index - 1);
		} else {
			System.out.println("Error: bad index(branch-node) [" + index + "].");
			return null;
		}
	}


	/**
	 * 1�̃m�[�h��ǉ��������A���̃m�[�h��Ԃ�
	 * @return �V�����m�[�h
	 */
	public Node getOneNewNode() {
		Node node = new Node((nodeList.size() + 1), this);
		nodeList.add(node);
		return node;
	}

	/** 
	 * �m�[�h��1�ǉ�����
	 * @param �ǉ�����m�[�h
	 */
	public void addOneNode(Node node) {
		nodeList.add(node);
	}

	/**
	 * 1�̃m�[�h���폜����
	 * @param �폜����m�[�h
	 */
	public void deleteOneNode(Node node) {

		if (nodeList.size() == 0) {
			System.err.println("Error:nodeList does not exist");
		} else {
			nodeList.remove(node);
		}
	}


	/**
	 * �O���[�v�̔ԍ���ݒ肷��
	 * @param �O���[�v�̔ԍ�
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * �O���[�v�̔ԍ���Ԃ�
	 * @return �O���[�v�̔ԍ�
	 */
	public int getId() {
		return id;
	}

	/**
	 * �O���[�v�̐[����ݒ肷��
	 * @param �O���[�v�̐[��
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * �O���[�v�̐[����Ԃ�
	 * @return �O���[�v�̐[��
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * �O���[�v�̖��O��ݒ肷��
	 * @param �O���[�v�̖��O
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * �O���[�v�̖��O��Ԃ�
	 * @return �O���[�v�̖��O
	 */
	public String getName() {
		return name;
	}

	/**
	 * �O���[�v�̐e�m�[�h��ݒ肷��
	 * @param �O���[�v�̐e�m�[�h
	 */
	public void setParentNode(Node parentNode) {
		this.parentNode = parentNode;
	}

	/**
	 * �O���[�v�̐e�m�[�h��Ԃ�
	 * @return �O���[�v�̐e�m�[�h
	 */
	public Node getParentNode() {
		return parentNode;
	}

	
	/**
	 * �e�m�[�h�̃T�C�Y���Z�o����
	 * templateFlag == true �ł���΃e���v���[�g�O���t
	 */
	public void calcParentNodeSize(boolean templateFlag) {

		double gMinp[] = { 1.0e+20, 1.0e+20, 1.0e+20 };
		double gMaxp[] = { -1.0e+20, -1.0e+20, -1.0e+20 };
		double gCenter[] = new double[3];
		double gSize[] = new double[3];

		Node parentNode = this.getParentNode();
		if (parentNode == null)
			return;

		//
		// for each node of the branch
		//
		for (int i = 1; i <= this.getNodeList().size(); i++) {
			Node node = this.getNodeAt(i);
			double tmp;

			/*
			double x = node.getNX();
			double y = node.getNY();
			double z = node.getNZ();
			double width  = node.getNwidth();
			double height = node.getNheight();
			double depth  = node.getNdepth();
			*/

			double x = node.getX();
			double y = node.getY();
			double z = node.getZ();
			double width = node.getWidth();
			double height = node.getHeight();
			double depth = node.getDepth();

			tmp = x + width;
			if (gMaxp[0] < tmp)
				gMaxp[0] = tmp;
			tmp = x - width;
			if (gMinp[0] > tmp)
				gMinp[0] = tmp;

			tmp = y + height;
			if (gMaxp[1] < tmp)
				gMaxp[1] = tmp;
			tmp = y - height;
			if (gMinp[1] > tmp)
				gMinp[1] = tmp;

			tmp = z + depth;
			if (gMaxp[2] < tmp)
				gMaxp[2] = tmp;
			tmp = z - depth;
			if (gMinp[2] > tmp)
				gMinp[2] = tmp;
		}

		//
		// Calculate the center and size of the branch
		//
		if (this.getNodeList().size() > 0) {
			gCenter[0] = (gMinp[0] + gMaxp[0]) * 0.5;
			gCenter[1] = (gMinp[1] + gMaxp[1]) * 0.5;
			gCenter[2] = (gMinp[2] + gMaxp[2]) * 0.5;
			gSize[0] = (gMaxp[0] - gMinp[0]) * 0.5 + 1.0;
			gSize[1] = (gMaxp[1] - gMinp[1]) * 0.5 + 1.0;
			gSize[2] = (gMaxp[2] - gMinp[2]) * 0.5 + 1.0;
		} else {
			gCenter[0] = gCenter[1] = gCenter[2] = 0.0;
			gSize[0] = gSize[1] = gSize[2] = 1.0;
		}

		//
		// Set the size of the branch to the parent node
		//
		parentNode.setSize(gSize[0], gSize[1], gSize[2]);
		parentNode.setCoordinate(gCenter[0], gCenter[1], gCenter[2]);
		parentNode.setNsize(gSize[0], gSize[1], gSize[2]);

		if (templateFlag == false)
			parentNode.setNCoordinate(gCenter[0], gCenter[1], gCenter[2]);

	}


	public void exchangeParentNodeOrder(Node node) {
		nodeList.removeElement((Object) node);
		for (int i = 0; i < nodeList.size(); i++) {
			Node n = (Node) nodeList.elementAt(i);
			n.setId(i + 1);
		}
		node.setId(nodeList.size());
		nodeList.addElement(node);
	}
	
	/**
	 * �z�u���I������A�Ƃ����t���O�����Z�b�g����
	 */
	public void resetPlaceFlag() {
		for(int i = 0; i < nodeList.size(); i++) {
			Node node = (Node)nodeList.elementAt(i);
			node.setPlaced(false);
			// temporary
			node.setId(i + 1);
		}
	}

}
