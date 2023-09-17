package org.heiankyoview2.core.draw;

import org.heiankyoview2.core.tree.Tree;
import org.heiankyoview2.core.tree.Branch;
import org.heiankyoview2.core.tree.Node;
import org.heiankyoview2.core.table.TreeTable;

import java.util.*;

/**
 * �`�揈���̃N���X
 * @author itot
 */
public class DrawerUtility {

	Tree tree = null;
	TreeTable tg;
	Transformer view = null;
	Buffer dbuf = null;

	Node nodearray[];
	int numNode;

	double p1[] = new double[3];
	double p2[] = new double[3];
	double p3[] = new double[3];
	double p4[] = new double[3];
	int imageSize[] = new int[2];

	Node pickedNode = null;

	DepthComparator dcomp;
	TreeSet treeSet;

	/**
	 * Constructor
	 * @param width �`��̈�̕�
	 * @param height �`��̈�̍���
	 */
	public DrawerUtility(int width, int height) {
		imageSize[0] = width;
		imageSize[1] = height;
	}

	/**
	 * Tree���Z�b�g����
	 * @param tree Tree
	 */
	public void setTree(Tree tree) {
		this.tree = tree;
		tg = tree.table;
	}

	/**
	 * Buffer ���Z�b�g����
	 * @param dbuf Buffer
	 */
	public void setBuffer(Buffer dbuf) {
		this.dbuf = dbuf;
	}

	/**
	 * View���Z�b�g����
	 * @param view View
	 */
	public void setTransformer(Transformer view) {
		this.view = view;
	}

	/**
	 * �`��̈�̃T�C�Y��ݒ肷��
	 * @param width �`��̈�̕�
	 * @param height �`��̈�̍���
	 */
	public void setWindowSize(int width, int height) {
		imageSize[0] = width;
		imageSize[1] = height;
	}

	/**
	 * �h��Ԃ����̖ʂ��I���e���E�����𔻒肷��
	 * @param px ��ʏ��x���W�l
	 * @param py ��ʏ��y���W�l
	 * @param e1 1�ڂ̒��_�̍��W�l
	 * @param e2 2�ڂ̒��_�̍��W�l
	 * @return �I���e�Ȃ�true
	 */
	int whichSide(int px, int py, double e1[], double e2[]) {
		double a = (e1[1] - (double) py) * (e2[0] - (double) px);
		double b = (e1[0] - (double) px) * (e2[1] - (double) py);
		if (a > b)
			return -1;
		if (a < b)
			return 1;

		return 0;
	}

	/**
	 * �s�b�N�����ʒu���l�p�`�ʂ̓������𔻒肷��
	 * @param px ��ʏ��x���W�l
	 * @param py ��ʏ��y���W�l
	 * @param pp1 1�ڂ̒��_�̍��W�l
	 * @param pp2 2�ڂ̒��_�̍��W�l
	 * @param pp3 3�ڂ̒��_�̍��W�l
	 * @param pp4 4�ڂ̒��_�̍��W�l
	 * @return �����Ȃ�true
	 */
	public boolean isInside(
		int px,
		int py,
		double pp1[],
		double pp2[],
		double pp3[],
		double pp4[]) {

		boolean flag1 = false, flag2 = false;
		int ret;

		ret = whichSide(px, py, pp1, pp2);
		if (ret > 0)
			flag1 = true;
		if (ret < 0)
			flag2 = true;

		ret = whichSide(px, py, pp2, pp3);
		if (ret > 0)
			flag1 = true;
		if (ret < 0)
			flag2 = true;

		ret = whichSide(px, py, pp3, pp4);
		if (ret > 0)
			flag1 = true;
		if (ret < 0)
			flag2 = true;

		ret = whichSide(px, py, pp4, pp1);
		if (ret > 0)
			flag1 = true;
		if (ret < 0)
			flag2 = true;

		if (flag1 == false || flag2 == false)
			return true;
		return false;
	}

	/**
	 * ���W�l��ϊ�����
	 * @param x �����W�n��x���W�l
	 * @param y �����W�n��y���W�l
	 * @param z �����W�n��z���W�l
	 * @param select �l�p�`�̒��_����肷��ID�i1�`4�̂����ꂩ�̐����j
	 */
	public double[] transformPosition(
		double x,
		double y,
		double z,
		int select) {
		double pos[];

		pos = p1;
		if (select == 2)
			pos = p2;
		if (select == 3)
			pos = p3;
		if (select == 4)
			pos = p4;

		//
		// Centering
		//
		if (view.getTreeSize() > 0.0000001) {
			x = (x - view.getTreeCenter(0)) / view.getTreeSize();
			y = (y - view.getTreeCenter(1)) / view.getTreeSize();
			z = (z - view.getTreeCenter(2)) / view.getTreeSize();
		}

		//
		// Fit to the image size
		//
		int wh = (imageSize[0] < imageSize[1]) ? imageSize[0] : imageSize[1];
		wh /= 2;
		x *= wh;
		y *= wh;
		z *= wh;

		//
		// Rotation & Shift
		//
		pos[0] =
			x * view.getViewRotate(0)
				+ y * view.getViewRotate(1)
				+ z * view.getViewRotate(2);
//				+ view.getViewShift(0);
		pos[1] =
			x * view.getViewRotate(4)
				+ y * view.getViewRotate(5)
				+ z * view.getViewRotate(6);
//				+ view.getViewShift(1);
		pos[2] =
			x * view.getViewRotate(8)
				+ y * view.getViewRotate(9)
				+ z * view.getViewRotate(10);
//				+ view.getViewShift(2);
		
		
		//
		// Scaling
		//
		pos[0] = pos[0] * view.getViewScale() + view.getViewShift(0) + 0.5 * (double) imageSize[0];
		pos[1] = pos[1] * view.getViewScale() + view.getViewShift(1) + 0.5 * (double) imageSize[1];
		pos[2] = pos[2] * view.getViewScale();

		return pos;
	}

	/**
	 * ���_����̐[���Ń\�[�g����Node�z��𐶐����� �i���s���\�[�g�@�ɂ��`��̂��߁j
	 */
	public Node[] createSortedNodeArray() {

		dcomp = new DepthComparator();
		treeSet = new TreeSet(dcomp);


		if (dbuf.getRootDisplayBranch() != null)
			depthSortNodes(dbuf.getRootDisplayBranch());
		else
			depthSortNodes(tree.getRootBranch());

		//
		// Copy the sorted nodes to an array
		//
		numNode = treeSet.size();
		if (numNode <= 0)
			return null;
		nodearray = new Node[numNode];

		for (int i = 0; i < numNode; i++) {
			Object obj = treeSet.first();
			treeSet.remove(obj);
			nodearray[i] = (Node) obj;
		}

		return nodearray;
	}

	/**
	 * Node�����_����̐[���Ń\�[�g����
	 * @param branch Branch
	 */
	void depthSortNodes(Branch branch) {
		Branch childBranch;
		Node node;
		int i;

		//
		// for each (NON-PARENT) node:
		//     Insert nodes into TreeSet
		//
		for (i = 1; i <= branch.getNodeList().size(); i++) {
			node = branch.getNodeAt(i);
			childBranch = node.getChildBranch();
			if (childBranch != null)
				continue;
			treeSet.add((Object) node);
		}

		//
		// for each (PARENT) node:
		//     Recursive call for child branches
		//
		for (i = 1; i <= branch.getNodeList().size(); i++) {
			node = branch.getNodeAt(i);
			childBranch = node.getChildBranch();
			if (childBranch == null)
				continue;
			depthSortNodes(childBranch);
		}
	}

	/**
	 * Node�̎��_����̐[�����Ǘ�����N���X
	 * @author itot
	 */
	class DepthComparator implements Comparator {

		/**
		 * Constructor
		 */
		public DepthComparator() {
		}

		/**
		 * 2��Node�̐[�����r����
		 * @param obj1 1�ڂ�Node
		 * @param obj2 2�ڂ�Node
		 */
		public int compare(Object obj1, Object obj2) {

			Node node1 = (Node) obj1;
			Node node2 = (Node) obj2;

			double d1 =
				node1.getX() * view.getViewRotate(8)
					+ node1.getY() * view.getViewRotate(9)
					+ node1.getZ() * view.getViewRotate(10);
			double d2 =
				node2.getX() * view.getViewRotate(8)
					+ node2.getY() * view.getViewRotate(9)
					+ node2.getZ() * view.getViewRotate(10);

			if (d1 - d2 > 0.000001)
				return 1;
			if (d2 - d1 > 0.000001)
				return -1;

			int v1 = node1.getId() + node1.getCurrentBranch().getId() * 10000;
			int v2 = node2.getId() + node2.getCurrentBranch().getId() * 10000;
			int sub = v1 - v2;

			return sub;
		}

	}

}
