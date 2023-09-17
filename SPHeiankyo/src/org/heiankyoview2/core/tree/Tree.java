
package org.heiankyoview2.core.tree;

import java.util.*;
import org.heiankyoview2.core.table.TreeTable;
import org.heiankyoview2.core.table.NodeTablePointer;
import org.heiankyoview2.core.frame.TreeFrame;

/**
 * �K�w�^�f�[�^�̃f�[�^�\���S�̂�\���N���X 
 */
public class Tree {

	/**
	 * �m�[�h�����Ɋi�[��������
	 */
	Vector nodeList; 

	/**
	 * �����N�����Ɋi�[��������
	 */
	Vector linkList; // an array of all links
	
	/**
	 * �O���[�v�����Ɋi�[��������
	 */
	Vector branchList = null; // an array of all branches
	
	/**
	 * �ŏ�ʊK�w�ƂȂ�O���[�v����уm�[�h
	 */
	Branch rootBranch; //a root branch  
	Node rootNode;

	/**
	 * Tree�ɕt������\�f�[�^�Ǝ��ԃf�[�^
	 */
	public TreeTable table; 
	public TreeFrame frame;
	String frameFilename = "";
	
	/**
	 * ��ʔz�u�ɎQ�Ƃ���e���v���[�gTree
	 */
	Tree templateTree = null;

	/**
	 * Constructor
	 * @param numBranches
	 */
	public Tree(int numBranches) {
		branchList = new Vector();
		nodeList = new Vector();
		linkList = new Vector();
		rootNode = new Node(0, null);
		table = new TreeTable(this);
		setNumBranch(numBranches);
	}

	/**
	 * Constructor
	 */
	public Tree() {
		this(0);
	}


	/**
	 * �\�f�[�^�̎��̂�Ԃ�
	 */
	public TreeTable getTreeTable() {
		return table;
	}

	
	/**
	 * ���ԃf�[�^�̎��̂�ݒ肷��
	 */
	public void setTreeFrame(TreeFrame tf) {
		frame = tf;
	}
	
	
	/**
	 * ���ԃf�[�^�̎��̂�Ԃ�
	 */
	public TreeFrame getTreeFrame() {
		return frame;
	}
	
	
	/**
	 * ���ԃf�[�^�̃t�@�C������ݒ肷��
	 */
	public void setFrameFilename(String filename) {
		frameFilename = filename;
	}
	
	
	/**
	 * ���ԃf�[�^�̃t�@�C������Ԃ�
	 */
	public String getFrameFilename() {
		return frameFilename;
	}
	
	/**
	 * �O���[�v�����Z�b�g����
	 * @param �O���[�v��
	 */
	public void setNumBranch(int numBranches) {
		branchList.clear();
		for(int i = 0; i < numBranches; i++) {
			Branch branch = new Branch((i + 1), this);
			branchList.add((Object)branch);
		}
	}
	
	/**
	 * �O���[�v����Ԃ�
	 * @return �O���[�v��
	 */
	public int getNumBranch() {
		return branchList.size();
	}

	/**
	 * �O���[�v�̃��X�g��Ԃ�
	 * @return �O���[�v�̃��X�g
	 */
	public Vector getBranchList() {
		return branchList;
	}

	/**
	 * �������ԍ��̃O���[�v��Ԃ�
	 * @param �O���[�v�̔ԍ� (1����numBranch�܂�)
	 * @return �O���[�v
	 */
	public Branch getBranchAt(int id) {
		if (id < 1 || id > branchList.size()) {
			return null;
		} else
			return (Branch) branchList.elementAt(id - 1);

	}

	/**
	 * �m�[�h����Ԃ�
	 * @return �m�[�h��
	 */
	public int getNumNode() {
		return nodeList.size();
	}

	/**
	 * 1�̃m�[�h��ǉ�����
	 * @param �m�[�h
	 */
	public void addNode(Node node) {
		nodeList.add(node);
	}

	/**
	 * �������ԍ��̃m�[�h��Ԃ�
	 * @param �m�[�h�̔ԍ� (1����numNode�܂�)
	 * @return �m�[�h
	 */
	public Node getNodeAt(int index) {
		if (index > 0 && index <= nodeList.size()) {
			return (Node) nodeList.elementAt(index - 1);
		} else {
			System.out.println("Error: bad index(tree-node) [" + index + "].");
			return null;
		}
	}

	
	/**
	 * �����N����Ԃ�
	 * @return �����N��
	 */
	public int getNumLink() {
		return linkList.size();
	}

	/**
	 * �����N�����Z�b�g����
	 * @param �����N��
	 */
	public void setNumLink(int numLinks) {
		linkList.clear();
		for(int i = 0; i < numLinks; i++) {
			Link link = new Link(i + 1);
			linkList.add((Object)link);
		}
	}

	/**
	 * 1�̃����N��ǉ�����
	 * @param �����N
	 */
	public void addLink(Link link) {
		linkList.add(link);
	}

	/**
	 * �����N�̃��X�g��Ԃ�
	 * @return �����N�̃��X�g
	 */
	public Vector getLinkList() {
		return linkList;
	}

	/**
	 * �������ԍ��̃����N��Ԃ�
	 * @param �����N�̔ԍ� (1����numLink�܂�)
	 * @return �����N
	 */
	public Link getLinkAt(int index) {
		if (index > 0 && index <= linkList.size()) {
			return (Link) linkList.elementAt(index - 1);
		} else {
			System.out.println("Error: bad index(tree-link) [" + index + "].");
			return null;
		}
	}

	/**
	 * �V���������N��1�������A�����Ԃ�
	 * @return �V���������N
	 */
	public Link getOneNewLink() {

		Link link = null;

		if (linkList.size() == 0) {
			System.err.println("Error:linkList does not exists");
		} else {
			link = new Link((linkList.size() + 1));
			linkList.add(link);
		}
		return link;
	}

	
	/**
	 * �V�����O���[�v��1�������A�����Ԃ�
	 * @return �V�����O���[�v
	 */
	public Branch getOneNewBranch() {
		Branch branch = new Branch((branchList.size() + 1), this);
		branchList.add(branch);
		
		return branch;
	}

	/**
	 * 1�̃O���[�v���폜����
	 * @param �폜����O���[�v
	 */
	public void deleteOneBranch(Branch branch) {

		if (branchList.size() == 0) {
			System.err.println("Error:branchList does not exists");
		} else {
			branchList.remove(branch);
		}
	}


	/**
	 * �ŏ�ʊK�w�̃O���[�v���w�肷��
	 * @param �ŏ�ʊK�w�̃O���[�v
	 */
	public void setRootBranch(Branch rootBranch) {

		this.rootBranch = rootBranch;
		rootBranch.setParentNode(rootNode);
		rootNode.setChildBranch(rootBranch);

		rootBranch.calcParentNodeSize(false);
	}

	/**
	 * �ŏ�ʊK�w�̃O���[�v��Ԃ�
	 * @return �ŏ�ʊK�w�̃O���[�v
	 */
	public Branch getRootBranch() {
		return rootBranch;
	}

	/**
	 * �ŏ�ʊK�w�̃m�[�h��Ԃ�
	 * @return �ŏ�ʊK�w�̃m�[�h
	 */
	public Node getRootNode() {
		return rootNode;
	}


	/**
	 * �e���v���[�g�O���t��ݒ肷��
	 * @param �e���v���[�g�O���t
	 */
	public void setTemplateTree(Tree ttree) {
		templateTree = ttree;
	}

	/**
	 * �e���v���[�g�O���t��Ԃ�
	 * @return �e���v���[�g�O���t
	 */
	public Tree getTemplateTree() {
		return templateTree;
	}
	
	
	/**
	 * �z�u���I������A�Ƃ����t���O�����Z�b�g����
	 */
	public void resetPlaceFlag() {
		for(int i = 0; i < branchList.size(); i++) {
			Branch branch = (Branch)branchList.elementAt(i);
			branch.resetPlaceFlag();
		}
	}
	
	
}