package org.heiankyoview2.core.placement;

import org.heiankyoview2.core.tree.Tree;
import org.heiankyoview2.core.tree.Branch;
import org.heiankyoview2.core.tree.Node;
import org.heiankyoview2.core.table.TreeTable;

/**
 * 1��Branch�ɑ΂����ʔz�u�����iTemplate����j
 * @author itot
 */
public class OneBranchTemplatePacking {

	Object allNodeArray[] = null;
	Object sortedNodeArray[] = null;
	Object tmpNodeArray[] = null;
	double rectSize[], posX[], posY[], maxPos[], minPos[];
	double minPenalty, margin, minD, minE;
	int numSorted;

	boolean rflag = false;
	int numNode;
	Node largestNode, largestTemplateNode;

	Tree tree, templateTree;
	Branch templateBranch;

	PackingGrid pg;
	PositionTranslater pt;

	//
	// Configurable parameters
	//
	final double penaltyA = 0.5, penaltyB = 0.5;
	final double initMargin = 0.5;
	//final double penaltyCand2 = 0.05, penaltyCand3 = 0.1;
	final double penaltyCand2 = 0.05, penaltyCand3 = 0.1;
	//final double visitDistance = 10.0;
	final int numOutsideSample = 20;

	/**
	 * Constructor
	 * @param pg PackingGrid
	 */
	public OneBranchTemplatePacking(PackingGrid pg) {
		posX = new double[2];
		posY = new double[2];
		maxPos = new double[2];
		minPos = new double[2];
		rectSize = new double[2];
		this.pg = pg;
		pt = new PositionTranslater();
	}

	/**
	 * 1��Branch�ɑ�����Node����ʔz�u����
	 * @param tree Tree
	 * @param branch Branch
	 * @param templateTree �e���v���[�g��Tree
	 * @param templateBranch �e���v���[�g��Branch
	 */
	public void placeBranchNodes(
		Tree tree,
		Branch branch,
		Tree templateTree,
		Branch templateBranch) {

		this.tree = tree;
		this.templateTree = templateTree;
		this.templateBranch = templateBranch;

		if (branch.getNodeList().size() <= 0)
			return;

		//
		// Pre-processing:
		//     * Generate a super rectangle
		//     * Sort nodes in the order of sizes
		//
		//calcNormalizedPositions(templateBranch);
		sortedNodeArray =
			quickSortNodes(tree, templateTree, branch, templateBranch);

		//
		// Main-process:
		//      * Place the largest nodes
		//
		Node node = largestNode;
		Node templateNode =
			TemplateNodeIdentifier.findTemplateNode(tree, node);
		placeFirstNode(node, templateNode);
		node.setPlaced(true);
		
		//
		// Main-process:
		//      * Place nodes referring the template
		//
		for (int i = 0; i < numSorted; i++) {
			node = (Node) sortedNodeArray[i];
			templateNode =
				TemplateNodeIdentifier.findTemplateNode(tree, node);
			placeOtherNodeWithTemplate(branch, node, templateNode);
			node.setPlaced(true);
		}
		finalizePositions(branch);

		//
		// Main-process:
		//      * Place nodes WITHOUT referring the template
		//      * NOT DEVELOPED
		//

	}

	/**
	 * Node����͂��āA�Ή�����e���v���[�g���Node�𔭌�����
	 * @param tree Tree
	 * @param templateTree �e���v���[�g��Tree
	 * @param node Node
	 * @param templateBranch �e���v���[�g��Branch
	 * @return �e���v���[�g��Node
	 */
	/*
	Node findTemplateNode(
		Tree tree,
		Tree templateTree,
		Node node,
		Branch templateBranch) {

		Node templateNode = null;
		String ret1 = getNodeIdentifier(tree, node);

		for (int j = 0; j < templateBranch.getNodeList().size(); j++) {
			templateNode = (Node) templateBranch.getNodeList().elementAt(j);
			String ret2 = getNodeIdentifier(templateTree, templateNode);
			//System.out.println("  ret1=" + ret1 + "  ret2=" + ret2);
			if (ret2.startsWith(ret1))
				return templateNode;
		}

		return null;
	}
	*/
	
	/**
	 * Node�̎��ʎq�ƂȂ镶�����Ԃ�
	 * @param tree Tree
	 * @param node Node
	 * @return Node�̎��ʎq�ƂȂ镶����
	 */
	/*
	String getNodeIdentifier(Tree tree, Node node) {
		String name = "";
		
		//name = node.getName();
		//if (name != null && name.length() > 0) return name;
			
		TreeTable tg = tree.table;
		if (tg.getNameType() < 0)
			tg.setNameType(0);

		name = tg.getNodeAttributeName(node, tg.getNameType());
		//System.out.println("  name=" + name + " type=" + tg.getNameType());

		return name;
	}
	*/
	
	/**
	 * Node��ʐύő�Node����̋����Ń\�[�g����
	 * @return �\�[�g���ꂽNode�̔z��
	 */
	public Object[] quickSortNodes(
		Tree tre,
		Tree templateTree,
		Branch branch,
		Branch templateBranch) {

		Node node, tnode;
		int numNode = branch.getNodeList().size();
		int counter1, counter2;

		allNodeArray = new Object[numNode];
		sortedNodeArray = new Object[numNode];
		tmpNodeArray = new Object[numNode];

		//
		// find the node closest to the center
		//
		double minDist = 1.0e+30;
		
		for (int i = 0; i < numNode; i++) {
			node = (Node) branch.getNodeList().elementAt(i);
			node.setPlaced(false);
			Node ltnode =
				TemplateNodeIdentifier.findTemplateNode(tree, node);
			if (ltnode != null) {
				double dist =
					ltnode.getNX() * ltnode.getNX()
						+ ltnode.getNY() * ltnode.getNY();
				if (minDist > dist) {
					minDist = dist;
					allNodeArray[0] = (Object) node;
					largestNode = node;
					largestTemplateNode = ltnode;
				}
			}
		}

		//
		// Distingish nodes which it is described in template or not
		//
		counter1 = 1;
		counter2 = numNode - 1;
		for (int i = 0; i < numNode; i++) {
			node = (Node) branch.getNodeList().elementAt(i);
			if (node == allNodeArray[0])
				continue;
			tnode = TemplateNodeIdentifier.findTemplateNode(tree, node);
			if (tnode != null) {
				sortedNodeArray[counter1 - 1] = (Object) node;
				allNodeArray[counter1] = (Object) node;
				counter1++;
			} else {
				allNodeArray[counter2--] = (Object) node;
			}
		}

		numSorted = counter1 - 1;
		quickSortNodesWithRange(0, (numSorted - 1));

		return sortedNodeArray;
	}

	/**
	 * �N�C�b�N�\�[�g�̈�Ƃ��� range1�`range2 �͈̔͂��\�[�g����
	 * @param range1 �\�[�g�͈͂̍ŏ��l
	 * @param range2 �\�[�g�͈͂̍ő�l
	 */
	void quickSortNodesWithRange(int range1, int range2) {

		if (range2 - range1 < 2)
			return;

		Node node1, node2, node3;
		Node templateNode1, templateNode2, templateNode3;

		int i, j, k, r2;
		double dist1, dist2, dist3;

		//
		//  If less than 10 links in the range:
		//     Bubble Sort	
		//
		if (range2 - range1 < 1000) {
			r2 = range2 - 1;
			for (i = range1; i < r2; i++) {
				for (j = range2; j > i; j--) {
					node1 = (Node) sortedNodeArray[j];
					node2 = (Node) sortedNodeArray[j - 1];
					templateNode1 = 
						TemplateNodeIdentifier.findTemplateNode(tree, node1);
					templateNode2 = 
						TemplateNodeIdentifier.findTemplateNode(tree, node2);
						
					dist1 =
						templateNode1.getNX() * templateNode1.getNX()
							+ templateNode1.getNY() * templateNode1.getNY();
					dist2 =
						templateNode2.getNX() * templateNode2.getNX()
							+ templateNode2.getNY() * templateNode2.getNY();

					if (dist1 >= dist2)
						continue;
					node3 = (Node) sortedNodeArray[j];
					sortedNodeArray[j] = sortedNodeArray[j - 1];
					sortedNodeArray[j - 1] = (Object) node3;
				}
			}

			return;
		}

		//
		//  If more than 10 links in the range:
		//     Quick Sort
		//
		node1 = (Node) sortedNodeArray[range1];
		node2 = (Node) sortedNodeArray[range1 + 1];
		templateNode1 =
			TemplateNodeIdentifier.findTemplateNode(tree, node1);
		templateNode2 =
			TemplateNodeIdentifier.findTemplateNode(tree, node2);
			
		dist1 =
			templateNode1.getNX() * templateNode1.getNX()
				+ templateNode1.getNY() * templateNode1.getNY();
		dist2 =
			templateNode2.getNX() * templateNode2.getNX()
				+ templateNode2.getNY() * templateNode2.getNY();
		dist3 = (dist1 > dist2) ? dist1 : dist2;

		j = range1;
		k = range2;
		for (i = range1; i <= range2; i++) {
			node1 = (Node) sortedNodeArray[i];
			templateNode1 =
				TemplateNodeIdentifier.findTemplateNode(tree, node1);
			dist1 =
				templateNode1.getNX() * templateNode1.getNX()
					+ templateNode1.getNY() * templateNode1.getNY();

			if (dist1 < dist3)
				tmpNodeArray[j++] = (Object) node1;
			else
				tmpNodeArray[k--] = (Object) node1;
		}

		for (i = range1; i <= range2; i++) {
			sortedNodeArray[i] = tmpNodeArray[i];
		}

		//
		//  Recursive call
		//
		if (range1 < k) {
			quickSortNodesWithRange(range1, k);
		} else {
			quickSortNodesWithRange(range1, range1);
			if (j <= range1)
				j = range1 + 1;
		}
		if (j < range2)
			quickSortNodesWithRange(j, range2);
		else
			quickSortNodesWithRange(range2, range2);

	}

	/**
	 * �ʐύő�Node����̋������Z�o����
	 * @param templateNode �e���v���[�g���Node
	 * @return �ʐύő�Node����̋���
	 */
	/*
	double calcDistanceToLargestNode(Node templateNode) {
		double xDiff = largestTemplateNode.getNX() - templateNode.getNX();
		double yDiff = largestTemplateNode.getNY() - templateNode.getNY();
		double distance = xDiff * xDiff + yDiff * yDiff;
	
		return distance;
	}
	*/

	/**
	 * �ŏ��́i�ʐύő�́jNode����ʔz�u����
	 * @param node �ŏ��́i�ʐύő�́jNode
	 * @param templateNode �Ή�����e���v���[�g��Node
	 */
	void placeFirstNode(Node node, Node templateNode) {

		node.setTemplateFlag(true);
		node.setPlaced(true);

		pg.xSize = pg.ySize = 3;
		pg.xPositions = new double[4];
		pg.yPositions = new double[4];
		pg.xyFlags = new int[9];

		node.setNX(0.0);
		node.setNY(0.0);
		node.setNZ(0.0);
		node.setNwidth(templateNode.getNwidth());
		node.setNheight(templateNode.getNheight());

		pg.xPositions[0] = -1.0;
		minPos[0] = pg.xPositions[1] = node.getNX() - node.getNwidth();
		maxPos[0] = pg.xPositions[2] = node.getNX() + node.getNwidth();
		pg.xPositions[3] = 1.0;

		pg.yPositions[0] = -1.0;
		minPos[1] = pg.yPositions[1] = node.getNY() - node.getNheight();
		maxPos[1] = pg.yPositions[2] = node.getNY() + node.getNheight();
		pg.yPositions[3] = 1.0;

		for (int i = 0; i < 9; i++)
			pg.xyFlags[i] = -1;
		pg.xyFlags[4] = node.getId();

	}

	/**
	 * 2�ڈȍ~��Node��1����ʔz�u����
	 * @param branch Branch
	 * @param node Node
	 * @param templateNode �Ή�����e���v���[�g��Node
	 */
	void placeOtherNodeWithTemplate(
		Branch branch,
		Node node,
		Node templateNode) {
		int center[], flag = 0, distance = 0;

		//
		// Initilize for translation
		//
		pt.initializeX(
			-largestNode.getWidth(),
			largestNode.getWidth(),
			-largestNode.getNwidth(),
			largestNode.getNwidth());
		pt.initializeY(
			-largestNode.getHeight(),
			largestNode.getHeight(),
			-largestNode.getNheight(),
			largestNode.getNheight());
		node.setNwidth(pt.calcWidth(node.getWidth()));
		node.setNheight(pt.calcHeight(node.getHeight()));

		double mw = pt.calcWidth(initMargin);
		double mh = pt.calcHeight(initMargin);
		margin = (mw > mh) ? mw : mh;
		if (margin > node.getNwidth() * 0.5)
			margin = node.getNwidth() * 0.5;
		if (margin > node.getNheight() * 0.5)
			margin = node.getNheight() * 0.5;

		rectSize[0] = (margin + node.getNwidth()) * 2.0;
		rectSize[1] = (margin + node.getNheight()) * 2.0;

		minPenalty = 1.0e+30;
		node.setTemplateFlag(true);
		node.setPlaced(true);

		// Visit cells and try to place the rectangle
		listCandidates(node, templateNode);
		flag = visitCandidates(node, templateNode);
		System.out.println("  minD=" + minD + " minE=" + minE);

		// if the rectangle can be placed
		node.setNX((posX[0] + posX[1]) * 0.5);
		node.setNY((posY[0] + posY[1]) * 0.5);
		node.setNZ(0.0);

		updateGrid(node);
		updatePositions(branch);

	}

	/**
	 * Node�𑽐��̌��ʒu�ɔz�u���Ă݂�
	 * @param node Node
	 * @param templateNode �Ή�����e���v���[�g���Node
	 * @return �z�u���ʁi1:����, 0:���Ƃ��čl������, -1:�z�u�s�j
	 */
	int visitCandidates(Node node, Node templateNode) {
		int i, j, ret = -1;
		double xmm[] = new double[2];
		double ymm[] = new double[2];

		//
		// for each candidates due to priority
		//
		for (j = 0; j <= 3; j++) {
			for (i = 0; i < pg.candidate[j].size(); i++) {
				double pos[] = (double[]) pg.candidate[j].elementAt(i);
				xmm[0] = pos[0] - (margin + node.getNwidth());
				xmm[1] = pos[0] + (margin + node.getNwidth());
				ymm[0] = pos[1] - (margin + node.getNheight());
				ymm[1] = pos[1] + (margin + node.getNheight());
				ret = trialPlacement(node, templateNode, xmm, ymm, j);
			}
		}

		//
		// for each last candidates
		//
		for (i = 0; i < pg.candidate[4].size(); i++) {
			double pos[] = (double[]) pg.candidate[4].elementAt(i);
			xmm[0] = pos[0] - (margin + node.getNwidth());
			xmm[1] = pos[0] + (margin + node.getNwidth());
			ymm[0] = pos[1] - (margin + node.getNheight());
			ymm[1] = pos[1] + (margin + node.getNheight());
			ret = trialPlacement(node, templateNode, xmm, ymm, 4);
		}

		return ret;
	}

	/**
	 * �ʒu��^���āA�e���v���[�gNode�ւ̋������Z�o����
	 * @param pos �ʒu
	 * @param templateNode �e���v���[�gNode
	 * @return �^����ꂽ�ʒu����e���v���[�gNode�ւ̋���
	 */
	double calcDistanceToTemplate(double pos[], Node templateNode) {
		double xDiff = pos[0] - templateNode.getNX();
		double yDiff = pos[1] - templateNode.getNY();
		double distance = xDiff * xDiff + yDiff * yDiff;
		return distance;
	}

	/**
	 * ���ʒu��Node��u�����ۂ̃y�i���e�B�l���Z�o����
	 * @param xmm ���ʒu��x���W�l�i�̍ŏ��l�ƍő�l�j
	 * @param ymm ���ʒu��y���W�l�i�̍ŏ��l�ƍő�l�j
	 * @param templateNode �e���v���[�gNode
	 * @param extension ��L�̈�̊g���
	 * @param oflag �ʈʒu�̓����ł����true
	 * @return �y�i���e�B�l
	 */
	double calcPenalty(
		double xmm[],
		double ymm[],
		Node templateNode,
		double extension,
		int oflag) {
		double center[] = new double[2];
		center[0] = 0.5 * (xmm[0] + xmm[1]);
		center[1] = 0.5 * (ymm[0] + ymm[1]);
		double distance = calcDistanceToTemplate(center, templateNode);
		double penaltyC = 0.0;

		if (oflag == 2)
			penaltyC = penaltyCand2;
		if (oflag == 3)
			penaltyC = penaltyCand3;

		//double penalty = penaltyA * distance + penaltyB * extension + penaltyC;
		//double penalty = penaltyA * distance * penaltyB * extension;// + penaltyC;
		double penalty = penaltyA * distance * distance * penaltyB * extension;
		
		if (minPenalty > penalty) {
			posX[0] = xmm[0];
			posX[1] = xmm[1];
			posY[0] = ymm[0];
			posY[1] = ymm[1];
			minPenalty = penalty;
			minD = distance;
			minE = extension;
		}

		return penalty;
	}

	/**
	 * Node��1�ӏ��̌��ʒu�ɂ��߂��ɒu���Ă݂�
	 * @param node Node
	 * @param templateNode �e���v���[�gNode
	 * @param xmm ���ʒu��x���W�l�i�̍ŏ��l�ƍő�l�j
	 * @param ymm ���ʒu��y���W�l�i�̍ŏ��l�ƍő�l�j
	 * @param oflag ��ʈʒu�̓����ł����true
	 * @return �z�u���ʁi1:����, 0:���Ƃ��čl������, -1:�z�u�s�j
	 */
	int trialPlacement(
		Node node,
		Node templateNode,
		double xmm[],
		double ymm[],
		int oflag) {
		int xmax, xmin, ymax, ymin, i = 0, j = 0;

		//
		// To place inside grid:
		//    Specify the range of cells that should be checked	
		//
		if (oflag <= 3) {
			for (xmin = 0; xmin <= pg.xSize; xmin++) {
				if (xmm[0] < pg.xPositions[xmin] - 1.0e-4) {
					xmin--;
					break;
				}
			}
			if (xmin < 0)
				xmin = 0;
			if (xmin >= pg.xSize)
				xmin = pg.xSize - 1;

			for (xmax = xmin; xmax <= pg.xSize; xmax++) {
				if (xmm[1] < pg.xPositions[xmax] + 1.0e-4) {
					xmax--;
					break;
				}
			}
			if (xmax < 0)
				xmax = 0;
			if (xmax >= pg.xSize)
				xmax = pg.xSize - 1;

			for (ymin = 0; ymin <= pg.ySize; ymin++) {
				if (ymm[0] < pg.yPositions[ymin] - 1.0e-4) {
					ymin--;
					break;
				}
			}
			if (ymin < 0)
				ymin = 0;
			if (ymin >= pg.ySize)
				ymin = pg.ySize - 1;

			for (ymax = ymin; ymax <= pg.ySize; ymax++) {
				if (ymm[1] < pg.yPositions[ymax] + 1.0e-4) {
					ymax--;
					break;
				}
			}
			if (ymax < 0)
				ymax = 0;
			if (ymax >= pg.ySize)
				ymax = pg.ySize - 1;

			//
			// Overlap check
			//
			boolean overlap = false;
			for (i = ymin; i <= ymax; i++) {
				for (j = xmin; j <= xmax; j++) {
					int id = i * pg.xSize + j;
					if (pg.xyFlags[id] >= 0) {
						overlap = true;
						break;
					}
				}
				if (overlap == true)
					break;
			}
			if (overlap == true)
				return 0;

			// if it does not overlap, and does not overflow ...
			if (xmm[0] >= minPos[0]
				&& xmm[1] <= maxPos[0]
				&& ymm[0] >= minPos[1]
				&& ymm[1] <= maxPos[1]) {
				calcPenalty(xmm, ymm, templateNode, 0.0, oflag);
				return 0;
			}
		}

		// if it does not overlap, but overflow ...
		double x1 = minPos[0] < xmm[0] ? minPos[0] : xmm[0];
		double x2 = maxPos[0] > xmm[1] ? maxPos[0] : xmm[1];
		double y1 = minPos[1] < ymm[0] ? minPos[1] : ymm[0];
		double y2 = maxPos[1] > ymm[1] ? maxPos[1] : ymm[1];
		double newWidth = x2 - x1;
		double newHeight = y2 - y1;
		double oldWidth = maxPos[0] - minPos[0];
		double oldHeight = maxPos[1] - minPos[1];

		double newAsp =
			(newWidth > newHeight)
				? newWidth / newHeight
				: newHeight / newWidth;
		double oldAsp =
			(oldWidth > oldHeight)
				? oldWidth / oldHeight
				: oldHeight / oldWidth;
		double e =
			((newWidth + newHeight) - (oldWidth + oldHeight)) * newAsp / oldAsp;
		calcPenalty(xmm, ymm, templateNode, e, oflag);

		return 0;
	}

	/**
	 * ��ʂ̊i�q�����f�[�^���X�V����
	 * @param node Node
	 */
	void updateGrid(Node node) {
		int i, xmin = 0, xmax = 0, ymin = 0, ymax = 0;

		//
		// check pg.xPositions
		//
		for (i = 0; i < pg.xSize; i++) {
			if (Math.abs(posX[0] - pg.xPositions[i]) < 1.0e-4)
				xmin = -10;
			if (Math.abs(posX[1] - pg.xPositions[i]) < 1.0e-4)
				xmax = -10;

			if (xmin >= 0
				&& posX[0] > (pg.xPositions[i] + 1.0e-4)
				&& posX[0] < (pg.xPositions[i + 1] - 1.0e-4))
				xmin = i;
			if (xmax >= 0
				&& posX[1] > (pg.xPositions[i] + 1.0e-4)
				&& posX[1] < (pg.xPositions[i + 1] - 1.0e-4))
				xmax = i;
		}
		if (Math.abs(posX[0] - pg.xPositions[pg.xSize]) < 1.0e-4)
			xmin = -10;
		if (Math.abs(posX[1] - pg.xPositions[pg.xSize]) < 1.0e-4)
			xmax = -10;
		if (xmin >= 0 && posX[0] < pg.xPositions[0])
			xmin = -1;
		if (xmax >= 0 && posX[1] > pg.xPositions[pg.xSize])
			xmax = pg.xSize;

		//
		// check pg.yPositions
		//
		for (i = 0; i < pg.ySize; i++) {
			if (Math.abs(posY[0] - pg.yPositions[i]) < 1.0e-4)
				ymin = -10;
			if (Math.abs(posY[1] - pg.yPositions[i]) < 1.0e-4)
				ymax = -10;

			if (ymin >= 0
				&& posY[0] > (pg.yPositions[i] + 1.0e-4)
				&& posY[0] < (pg.yPositions[i + 1] - 1.0e-4))
				ymin = i;
			if (ymax >= 0
				&& posY[1] > (pg.yPositions[i] + 1.0e-4)
				&& posY[1] < (pg.yPositions[i + 1] - 1.0e-4))
				ymax = i;
		}
		if (Math.abs(posY[0] - pg.yPositions[pg.ySize]) < 1.0e-4)
			ymin = -10;
		if (Math.abs(posY[1] - pg.yPositions[pg.ySize]) < 1.0e-4)
			ymax = -10;
		if (ymin >= 0 && posY[0] < pg.yPositions[0])
			ymin = -1;
		if (ymax >= 0 && posY[1] > pg.yPositions[pg.ySize])
			ymax = pg.ySize;

		//
		// Divide the grid according to pg.xPositions
		// (CAUTION: xmax MUST BE PRIOR TO xmin)
		//
		divideGridX(xmax, posX[1]);
		divideGridX(xmin, posX[0]);

		//
		// Divide the grid according to pg.yPositions
		// (CAUTION: ymax MUST BE PRIOR TO ymin)
		//
		divideGridY(ymax, posY[1]);
		divideGridY(ymin, posY[0]);

		//
		// fill the grid by the placed node
		//
		fillGrid(node.getId());

	}

	/**
	 * ��ʂ̊i�q������x���W�l���X�V����
	 * @param idX �������ׂ��i�q�̈��x��������ID
	 * @param valueX �V���ɗŐ��������ׂ�x���W�l
	 */
	void divideGridX(int idX, double valueX) {

		if (idX < -1)
			return;

		pg.xSize++;
		int i, j;

		// Reproduce x-posistion information
		double newPositions[] = new double[pg.xSize + 1];
		for (i = 0; i <= idX; i++)
			newPositions[i] = pg.xPositions[i];
		newPositions[idX + 1] = valueX;
		for (i = (idX + 2); i <= pg.xSize; i++)
			newPositions[i] = pg.xPositions[i - 1];
		pg.xPositions = newPositions;

		// Reproduce rectangle occupation information
		int newFlags[] = new int[pg.xSize * pg.ySize];
		for (i = 0; i < (pg.xSize * pg.ySize); i++)
			newFlags[i] = -1;
		int id, id2;

		if (idX < 0) {
			for (i = 0; i < pg.ySize; i++) {
				for (j = 0; j < (pg.xSize - 1); j++) {
					id = i * pg.xSize + j + 1;
					id2 = i * (pg.xSize - 1) + j;
					newFlags[id] = pg.xyFlags[id2];
				}
			}
		} else if (idX == pg.xSize - 1) {
			for (i = 0; i < pg.ySize; i++) {
				for (j = 0; j < (pg.xSize - 1); j++) {
					id = i * pg.xSize + j;
					id2 = i * (pg.xSize - 1) + j;
					newFlags[id] = pg.xyFlags[id2];
				}
			}
		} else {
			for (i = 0; i < pg.ySize; i++) {
				for (j = 0; j < pg.xSize; j++) {
					id = i * pg.xSize + j;
					id2 =
						(j > idX)
							? (i * (pg.xSize - 1) + j - 1)
							: (i * (pg.xSize - 1) + j);
					newFlags[id] = pg.xyFlags[id2];
				}
			}
		}
		pg.xyFlags = newFlags;
	}

	/**
	 * ��ʂ̊i�q������y���W�l���X�V����
	 * @param idY �������ׂ��i�q�̈��y��������ID
	 * @param valueY �V���ɗŐ��������ׂ�y���W�l
	 */
	void divideGridY(int idY, double valueY) {
		if (idY < -1)
			return;

		pg.ySize++;
		int i, j;

		double newPositions[] = new double[pg.ySize + 1];
		for (i = 0; i <= idY; i++)
			newPositions[i] = pg.yPositions[i];
		newPositions[idY + 1] = valueY;
		for (i = (idY + 2); i <= pg.ySize; i++)
			newPositions[i] = pg.yPositions[i - 1];
		pg.yPositions = newPositions;

		int newFlags[] = new int[pg.xSize * pg.ySize];
		for (i = 0; i < (pg.xSize * pg.ySize); i++)
			newFlags[i] = -1;
		int id, id2;

		if (idY < 0) {
			for (i = 0; i < (pg.ySize - 1); i++) {
				for (j = 0; j < pg.xSize; j++) {
					id = (i + 1) * pg.xSize + j;
					id2 = i * pg.xSize + j;
					newFlags[id] = pg.xyFlags[id2];
				}
			}
		} else if (idY == pg.ySize - 1) {
			for (i = 0; i < (pg.ySize - 1); i++) {
				for (j = 0; j < pg.xSize; j++) {
					id = i * pg.xSize + j;
					id2 = i * pg.xSize + j;
					newFlags[id] = pg.xyFlags[id2];
				}
			}
		} else {
			for (i = 0; i < pg.ySize; i++) {
				for (j = 0; j < pg.xSize; j++) {
					id = i * pg.xSize + j;
					id2 =
						(i > idY)
							? ((i - 1) * pg.xSize + j)
							: (i * pg.xSize + j);
					newFlags[id] = pg.xyFlags[id2];
				}
			}
		}
		pg.xyFlags = newFlags;

	}

	/**
	 * ���łɒ����`���z�u����Ă���i�q�̈�𖄂߂��f�[�^���쐬����
	 * @param nodeId Node��ID
	 */
	void fillGrid(int nodeId) {
		int i, j, xmin = 0, xmax = 0, ymin = 0, ymax = 0;

		for (i = 0; i <= pg.xSize; i++) {
			if (Math.abs(posX[0] - pg.xPositions[i]) < 1.0e-4)
				xmin = i;
			if (Math.abs(posX[1] - pg.xPositions[i]) < 1.0e-4)
				xmax = i;
		}
		for (i = 0; i <= pg.ySize; i++) {
			if (Math.abs(posY[0] - pg.yPositions[i]) < 1.0e-4)
				ymin = i;
			if (Math.abs(posY[1] - pg.yPositions[i]) < 1.0e-4)
				ymax = i;
		}

		for (i = ymin; i < ymax; i++) {
			for (j = xmin; j < xmax; j++) {
				int id = i * pg.xSize + j;
				pg.xyFlags[id] = nodeId;
			}
		}
	}

	/**
	 * Branch���\������Node�̈ʒu���X�V����
	 * @param branch Branch
	 */
	void updatePositions(Branch branch) {
		int i;

		minPos[0] = minPos[1] = 2.0;
		maxPos[0] = maxPos[1] = -2.0;

		//
		// Initilize for translation
		//
		pt.initializeX(pg.xPositions[0], pg.xPositions[pg.xSize], -1.0, 1.0);
		pt.initializeY(pg.yPositions[0], pg.yPositions[pg.ySize], -1.0, 1.0);

		//
		// Translate grid-points
		//
		for (i = 0; i <= pg.xSize; i++)
			pg.xPositions[i] = pt.calcX(pg.xPositions[i]);
		for (i = 0; i <= pg.ySize; i++)
			pg.yPositions[i] = pt.calcY(pg.yPositions[i]);

		//
		// Translate placed nodes
		//
		for (i = 0; i < branch.getNodeList().size(); i++) {
			Node node = (Node) branch.getNodeList().elementAt(i);
			if (!node.getPlaced())
				continue;

			node.setNX(pt.calcX(node.getNX()));
			node.setNY(pt.calcY(node.getNY()));
			node.setNwidth(pt.calcWidth(node.getNwidth()));
			node.setNheight(pt.calcHeight(node.getNheight()));

			if (minPos[0] > node.getNX())
				minPos[0] = node.getNX();
			if (maxPos[0] < node.getNX())
				maxPos[0] = node.getNX();
			if (minPos[1] > node.getNY())
				minPos[1] = node.getNY();
			if (maxPos[1] < node.getNY())
				maxPos[1] = node.getNY();
		}

	}

	/**
	 * Branch���\������Node�̈ʒu���ŏI���肷��
	 * @param branch
	 */
	void finalizePositions(Branch branch) {
		int i;

		//
		// Initilize for translation
		//
		pt.initializeX(
			-largestNode.getNwidth(),
			largestNode.getNwidth(),
			-largestNode.getWidth(),
			largestNode.getWidth());
		pt.initializeY(
			-largestNode.getNheight(),
			largestNode.getNheight(),
			-largestNode.getHeight(),
			largestNode.getHeight());

		//
		// Translate grid-points
		//
		for (i = 0; i <= pg.xSize; i++)
			pg.xPositions[i] = pt.calcX(pg.xPositions[i]);
		for (i = 0; i <= pg.ySize; i++)
			pg.yPositions[i] = pt.calcY(pg.yPositions[i]);

		//
		// Translate placed nodes
		//
		for (i = 0; i < branch.getNodeList().size(); i++) {
			Node node = (Node) branch.getNodeList().elementAt(i);
			if (!node.getPlaced())
				continue;
			node.setX(pt.calcX(node.getNX()));
			node.setY(pt.calcY(node.getNY()));

		}

	}

	/**
	 * �����`�z�u�̌��ʒu�����X�g�A�b�v����
	 * @param node Node
	 * @param templateNode �e���v���[�gNode
	 */
	void listCandidates(Node node, Node templateNode) {
		double distance;

		for (int i = 0; i < 5; i++)
			pg.candidate[i].clear();

		//
		// for each subspace of the grid
		//
		for (int i = 0; i < pg.ySize; i++) {
			for (int j = 0; j < pg.xSize; j++) {
				int id1, id2, id3;
				int id = i * pg.xSize + j;
				if (pg.xyFlags[id] >= 0)
					continue;

				//
				// first candidate position
				//
				double pos1[] = new double[2];
				pos1[0] = pg.xPositions[j] + margin + node.getNwidth();
				pos1[1] = pg.yPositions[i] + margin + node.getNheight();
				distance = calcDistanceToTemplate(pos1, templateNode);
				//if (distance < visitDistance) {
				if (j == 0 || i == 0)
					pg.candidate[3].add((Object) pos1);
				else {
					id1 = (i - 1) * pg.xSize + (j - 1);
					id2 = (i - 1) * pg.xSize + j;
					id3 = i * pg.xSize + (j - 1);
					addCandidatePosition(pos1, id1, id2, id3);
				}
				//}

				//
				// second candidate position
				//
				double pos2[] = new double[2];
				pos2[0] = pg.xPositions[j + 1] - margin - node.getNwidth();
				pos2[1] = pg.yPositions[i] + margin + node.getNheight();
				distance = calcDistanceToTemplate(pos2, templateNode);
				//if (distance < visitDistance) {
				if (j == (pg.xSize - 1) || i == 0)
					pg.candidate[3].add((Object) pos2);
				else {
					id1 = (i - 1) * pg.xSize + (j + 1);
					id2 = (i - 1) * pg.xSize + j;
					id3 = i * pg.xSize + (j + 1);
					addCandidatePosition(pos2, id1, id2, id3);
				}
				//}

				//
				// third candidate position
				//
				double pos3[] = new double[2];
				pos3[0] = pg.xPositions[j] + margin + node.getNwidth();
				pos3[1] = pg.yPositions[i + 1] - margin - node.getNheight();
				distance = calcDistanceToTemplate(pos3, templateNode);
				//if (distance < visitDistance) {
				if (j == 0 || i == (pg.ySize - 1))
					pg.candidate[3].add((Object) pos3);
				else {
					id1 = (i + 1) * pg.xSize + (j - 1);
					id2 = (i + 1) * pg.xSize + j;
					id3 = i * pg.xSize + (j - 1);
					addCandidatePosition(pos3, id1, id2, id3);
				}
				//}

				//
				// fourth candidate position
				//
				double pos4[] = new double[2];
				pos4[0] = pg.xPositions[j + 1] - margin - node.getNwidth();
				pos4[1] = pg.yPositions[i + 1] - margin - node.getNheight();
				distance = calcDistanceToTemplate(pos4, templateNode);
				//if (distance < visitDistance) {
				if (j == (pg.xSize - 1) || i == (pg.ySize - 1))
					pg.candidate[3].add((Object) pos4);
				else {
					id1 = (i + 1) * pg.xSize + (j + 1);
					id2 = (i + 1) * pg.xSize + j;
					id3 = i * pg.xSize + (j + 1);
					addCandidatePosition(pos4, id1, id2, id3);
				}
				//}

			}
		}

		//
		// Outside candidates along xmin/xmax
		//
		for (int i = -numOutsideSample; i <= numOutsideSample; i++) {

			double pos5[] = new double[2];
			pos5[0] = pg.xPositions[0] - margin - node.getNwidth();
			pos5[1] = (double) i / (double) numOutsideSample;
			distance = calcDistanceToTemplate(pos5, templateNode);
			//if (distance < visitDistance)
			pg.candidate[4].add((Object) pos5);

			double pos6[] = new double[2];
			pos6[0] = pg.xPositions[pg.xSize] + margin + node.getNwidth();
			pos6[1] = (double) i / (double) numOutsideSample;
			distance = calcDistanceToTemplate(pos6, templateNode);
			//if (distance < visitDistance)
			pg.candidate[4].add((Object) pos6);
		}

		//
		// Outside candidates along ymin/ymax
		//
		for (int i = -numOutsideSample; i <= numOutsideSample; i++) {

			double pos7[] = new double[2];
			pos7[0] = (double) i / (double) numOutsideSample;
			pos7[1] = pg.yPositions[0] - margin - node.getNheight();
			distance = calcDistanceToTemplate(pos7, templateNode);
			//if (distance < visitDistance)
			pg.candidate[4].add((Object) pos7);

			double pos8[] = new double[2];
			pos8[0] = (double) i / (double) numOutsideSample;
			pos8[1] = pg.yPositions[pg.ySize] + margin + node.getNheight();
			distance = calcDistanceToTemplate(pos8, templateNode);
			//if (distance < visitDistance)
			pg.candidate[4].add((Object) pos8);
		}

	}

	/**
	 * 1�̌��ʒu�����X�g�ɒǉ�����
	 * @param pos ���K�����W�n�̍��W�l
	 * @param id1 �אڊi�q�̈�(1)��ID
	 * @param id2 �אڊi�q�̈�(2)��ID
	 * @param id3 �אڊi�q�̈�(3)��ID
	 */
	void addCandidatePosition(double pos[], int id1, int id2, int id3) {
		int n = 0;

		if (pg.xyFlags[id1] >= 0)
			n++;
		if (pg.xyFlags[id2] >= 0)
			n++;
		if (pg.xyFlags[id3] >= 0)
			n++;

		if (n == 3)
			pg.candidate[0].add((Object) pos);
		if (n == 2)
			pg.candidate[2].add((Object) pos);
		if (n == 1)
			pg.candidate[1].add((Object) pos);
		if (n == 0)
			pg.candidate[3].add((Object) pos);
	}


	/**
	 * ��ʍ��W�n�Ɛ��K�����W�n�̕ϊ����s�����߂̃N���X
	 * @author itot
	 */
	class PositionTranslater {
		double xa, ya, xb, yb;
		double invxa, invya, invxb, invyb;
		public PositionTranslater() {
			;
		}

		public void initializeX(
			double minXin,
			double maxXin,
			double minXout,
			double maxXout) {

			xa = (maxXout - minXout) / (maxXin - minXin);
			xb = maxXout - xa * maxXin;
			invxa = (maxXin - minXin) / (maxXout - minXout);
			invxb = maxXin - invxa * maxXout;

		}

		public void initializeY(
			double minYin,
			double maxYin,
			double minYout,
			double maxYout) {

			ya = (maxYout - minYout) / (maxYin - minYin);
			yb = maxYout - ya * maxYin;
			invya = (maxYin - minYin) / (maxYout - minYout);
			invyb = maxYin - invya * maxYout;

		}

		public double calcX(double inX) {
			return xa * inX + xb;
		}

		public double calcY(double inY) {
			return ya * inY + yb;
		}

		public double calcWidth(double inW) {
			return xa * inW;
		}

		public double calcHeight(double inH) {
			return ya * inH;
		}

		public double calcInvX(double inX) {
			return invxa * inX + invxb;
		}

		public double calcInvY(double inY) {
			return invya * inY + invyb;
		}

		public double calcInvWidth(double inW) {
			return invxa * inW;
		}

		public double calcInvHeight(double inH) {
			return invya * inH;
		}

	}

}
