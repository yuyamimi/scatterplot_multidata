package org.heiankyoview2.core.draw; 

import org.heiankyoview2.core.tree.Tree;
import org.heiankyoview2.core.tree.Branch;
import org.heiankyoview2.core.tree.Node;
import org.heiankyoview2.core.table.TreeTable;

import java.util.*;

/**
 * HeianView�̕`��̈�ւ̕\�����e���o�b�t�@����
 * @author itot
 */
public class DefaultBuffer implements Buffer {
	Tree tree;
	TreeTable tg;
	Branch rootDisplayBranch = null;
	Node pickedNode = null;

	Vector annotationList;

	/**
	 * �A�m�e�[�V�������i�[����N���X
	 * @author itot
	 */
	public class Annotation {
		String annotation;
		double size;
		double position[];

		/**
		 * Constructor
		 */
		public Annotation() {
			position = new double[3];
		}

		/**
		 * �A�m�e�[�V�����̃T�C�Y���Z�b�g����
		 * @param size �A�m�e�[�V�����̃T�C�Y
		 */
		public void setSize(double size) {
			this.size = size;
		}
		
		/**
		 * �A�m�e�[�V�����̃T�C�Y��Ԃ�
		 * @return �A�m�e�[�V�����̃T�C�Y
		 */
		public double getSize() {
			return size;
		}

		/**
		 * �A�m�e�[�V�����̕�������Z�b�g����
		 * @param annotation �A�m�e�[�V�����̕�����
		 */
		public void setAnnotation(String annotation) {
			this.annotation = annotation;
		}
		
		/**
		 * �A�m�e�[�V�����̕������Ԃ�
		 * @return �A�m�e�[�V�����̕�����
		 */
		public String getAnnotation() {
			return annotation;
		}

		/**
		 * �A�m�e�[�V�����̍��W�l���Z�b�g����
		 * @param position �A�m�e�[�V�����̍��W�l
		 */
		public void setPosition(double position[]) {
			this.position[0] = position[0];
			this.position[1] = position[1];
			this.position[2] = position[2];
		}
		
		/**
		 * �A�m�e�[�V�����̍��W�l��Ԃ�
		 * @return �A�m�e�[�V�����̍��W�l
		 */
		public double[] getPosition() {
			return this.position;
		}
	}

	/**
	 * Constructor
	 */
	public DefaultBuffer() {
		pickedNode = null;
		annotationList = new Vector();
	}

	/**
	 * Tree���Z�b�g����
	 * @param tree Tree
	 */
	public void setTree(Tree newTree) {
		tree = newTree;
		tg = tree.table;
		rootDisplayBranch = tree.getRootBranch();

		addBranchAnnotations();
	}


	/**
	 * Tree�̑S�̂�\�����邩�A����Node�̉��ʊK�w������\�����邩�A���Z�b�g����
	 * @param sw �\����؂�ւ��邽�߂̐��l�i0:�S�́A1:�s�b�N����Node�̉��ʊK�w�̂݁A2:���݂�1�K�w��܂Łj
	 */
	public void updatePartialDisplay(int sw) {

		//
		// display whole the tree
		//
		if (sw == 0) {
			rootDisplayBranch = tree.getRootBranch();
		}

		//
		// display only under the clicked branch
		//
		if (sw == 1) {
			Node pnode = getPickedNode();

			//
			// cannot display partial, so display original
			//
			if (pnode == null)
				return;
			Branch pbranch = pnode.getChildBranch();
			if (pbranch == null)
				return;

			//
			// switch whole to partial
			//
			else {
				rootDisplayBranch = pbranch;
				pickedNode = null;
			}
		}

		//
		// display only under the parent branch
		//
		if (sw == 2) {

			//
			// if currently whole the branch is displayed
			//
			if (rootDisplayBranch == tree.getRootBranch())
				return;

			//
			// switch to the upper branch
			//
			else {
				rootDisplayBranch =
					rootDisplayBranch.getParentNode().getCurrentBranch();
				pickedNode = null;
			}
		}

	}

	/**
	 * �O���t�̕\����̍�branch��Ԃ�
	 * @return �\����̍�Branch
	 */
	public Branch getRootDisplayBranch() {
		return rootDisplayBranch;
	}


	/**
	 * �A�m�e�[�V������1�ǉ�����
	 * @param name �A�m�e�[�V�����̕�����
	 * @param size �A�m�e�[�V�����̃T�C�Y
	 * @param position �A�m�e�[�V�����̍��W�l�i�����W�n�Łj
	 */
	void addOneAnnotation(String name, double size, double position[]) {

		int j;
		Annotation a, a2;

		if (name == null || name.length() <= 0)
			return;

		//
		// Insert the node into the sorted list
		//  (It should be replaced by faster implementation) 
		//
		for (j = (annotationList.size() - 1); j >= 0; j--) {
			a = (Annotation) annotationList.elementAt(j);
			if (size > a.getSize())
				continue;

			if (j < 100000) {
				a2 = new Annotation();
				a2.setAnnotation(name);
				a2.setSize(size);
				a2.setPosition(position);
				annotationList.insertElementAt(a2, (j + 1));
			}
			break;

		}
		if (j < 0) {
			a2 = new Annotation();
			a2.setAnnotation(name);
			a2.setSize(size);
			a2.setPosition(position);
			annotationList.insertElementAt(a2, 0);
		}
	}

	/**
	 * �A�m�e�[�V�����̕\���ΏۂƂȂ�Node��1�ǉ�����
	 * @param node �A�m�e�[�V�����̕\���ΏۂƂȂ�Node
	 */
	void addAnnotatedOneNode(Node node) {

		String name = tg.getNodeAttributeName(
					node, tg.getNameType());
		if (name == null || name.length() <= 0) {
			name = node.getChildBranch().getName();
		}
		if (name == null || name.length() <= 0 || name.startsWith("null")) {
			return;
		}

		double size = node.getWidth() * node.getHeight();
		double position[] = new double[3];
		position[0] = node.getX() - node.getWidth();
		position[1] = node.getY() - node.getHeight();
		position[2] = node.getZ();

		addOneAnnotation(name, size, position);
	}

	/**
	 * Branch���\������Node�ɑ΂��āA�A�m�e�[�V�����̕\���ΏۂƂȂ�Node��ǉ�����
	 * @param branch Branch
	 * @param flag true�Ȃ�ΎqBranch��ǐՂ���
	 */
	void addAnnotatedNodesOneBranch(Branch branch, boolean flag) {

		int i;

		if (branch == rootDisplayBranch) {
			flag = true;
			addAnnotatedOneNode(branch.getParentNode());
		}

		//
		// for each node in the branch
		//
		for (i = 1; i <= branch.getNodeList().size(); i++) {
			if (flag == false)
				break;
			Node node = branch.getNodeAt(i);
			if (node.getChildBranch() == null)
				continue;
			addAnnotatedOneNode(node);
		}

		//
		// for each child branch of this branch
		//
		for (i = 1; i <= branch.getNodeList().size(); i++) {
			Node node = branch.getNodeAt(i);
			if (node.getChildBranch() != null) {
				addAnnotatedNodesOneBranch(node.getChildBranch(), flag);
			}
		}

	}

	/**
	 * �eBranch�����ǂ�A�A�m�e�[�V�����̕\���ΏۂƂȂ�Node��ǉ�����
	 */
	public void addBranchAnnotations() {

		if (tree == null)
			return;
		annotationList.clear();

		Branch branch = tree.getRootBranch();

		if (branch == rootDisplayBranch)
			addAnnotatedNodesOneBranch(branch, true);
		else
			addAnnotatedNodesOneBranch(branch, false);
	}


	/**
	 * �A�m�e�[�V�������X�g���ID����͂��āA����ɑΉ�����A�m�e�[�V�����̕������Ԃ�
	 * @param id �A�m�e�[�V�������X�g���ID
	 * @return �A�m�e�[�V�����̕�����
	 */
	public String getAnnotationName(int id) {
		if (annotationList.size() <= 0)
			return " ";
		if (id >= annotationList.size())
			id = annotationList.size() - 1;
		Annotation a = (Annotation) annotationList.elementAt(id);
		return a.getAnnotation();
	}

	/**
	 * �A�m�e�[�V�������X�g���ID����͂��āA����ɑΉ�����A�m�e�[�V�����̍��W�l��Ԃ�
	 * @param id �A�m�e�[�V�������X�g���ID
	 * @return �A�m�e�[�V�����̍��W�l
	 */
	public double[] getAnnotationPosition(int id) {
		if (annotationList.size() <= 0) {
			double[] ret = { 0.0, 0.0, 0.0 };
			return ret;
		}
		if (id >= annotationList.size())
			id = annotationList.size() - 1;
		Annotation a = (Annotation) annotationList.elementAt(id);
		return a.getPosition();
	}

	/**
	 * �A�m�e�[�V�������X�g���ID����͂��āA����ɑΉ�����A�m�e�[�V�����̃T�C�Y��Ԃ�
	 * @param id �A�m�e�[�V�������X�g���ID
	 * @return �A�m�e�[�V�����̃T�C�Y
	 */
	public double getAnnotationSize(int id) {
		if (annotationList.size() <= 0)
			return 0.0;
		if (id >= annotationList.size())
			id = annotationList.size() - 1;
		Annotation a = (Annotation) annotationList.elementAt(id);
		return a.getSize();
	}

	/**
	 * �s�b�N���ꂽNode��Ԃ�
	 * @return �s�b�N���ꂽNode
	 */
	public Node getPickedNode() {
		return pickedNode;
	}

	/**
	 * �s�b�N���ꂽNode���Z�b�g����
	 * @param node �s�b�N���ꂽNode
	 */
	public void setPickedNode(Node node) {
		pickedNode = node;
	}

	/**
	 * �A�m�e�[�V�����̑�����Ԃ�
	 * @return �A�m�e�[�V�����̑���
	 */
	public int getNumAnnotation() {
		return annotationList.size();
	}

}
