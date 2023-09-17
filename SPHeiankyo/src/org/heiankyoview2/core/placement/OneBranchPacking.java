package org.heiankyoview2.core.placement;

import org.heiankyoview2.core.tree.Tree;
import org.heiankyoview2.core.tree.Branch;
import org.heiankyoview2.core.tree.Node;


/**
 * 1��Branch�ɑ΂����ʔz�u�����iTemplate�Ȃ��j
 * @author itot
 */
public class OneBranchPacking {

	Object sortedNodeArray[] = null;
	Object tmpNodeArray[] = null;
	double rectSize[], posX[], posY[], extension;

	final double margin = 0.5;
	final double ASPECT_VS_AREA = 1.0;
	
	boolean rflag = false;
	int numNode;

	PackingGrid pg;

	Tree tree;
	Branch branch;

	/**
	 * Constructor
	 * @param pg PackingGrid
	 */
	public OneBranchPacking(PackingGrid pg) {
		this.pg = pg;
		posX = new double[2];
		posY = new double[2];
		rectSize = new double[2];
	}

	/**
	 * 1��Branch�ɑ�����Node����ʔz�u����
	 * @param tree Tree
	 * @param branch Branch
	 */
	public void placeBranchNodes(Tree tree, Branch branch) {

		this.tree = tree;
		this.branch = branch;

		//
		// Pre-processing:
		//     * Generate a super rectangle
		//     * Sort nodes in the order of sizes
		//
		sortedNodeArray = quickSortNodes();

		//
		// Main-process:
		//     * Place the first node
		//
		Node node = (Node) sortedNodeArray[branch.getNodeList().size() - 1];
		rectSize[0] = (margin + node.getWidth()) * 2.0;
		rectSize[1] = (margin + node.getHeight()) * 2.0;
		placeFirstNode(node);
		node.setPlaced(true);

		//
		// Main-process:
		//     * Place the remaining nodes one-by-one
		//
		for (int i = branch.getNodeList().size() - 2; i >= 0; i--) {
			node = (Node) sortedNodeArray[i];
			rectSize[0] = (margin + node.getWidth()) * 2.0;
			rectSize[1] = (margin + node.getHeight()) * 2.0;
			placeOtherNode(node);
			node.setPlaced(true);
		}
	}


	/**
	 * Node��ʐςŃ\�[�g����
	 * @return �\�[�g���ꂽNode�̔z��
	 */
	public Object[] quickSortNodes() {

		int numNode = branch.getNodeList().size();

		sortedNodeArray = new Object[numNode];
		tmpNodeArray = new Object[numNode];

		for (int i = 0; i < numNode; i++) {
			Node node = branch.getNodeAt(i + 1);
			if (node.getPlaced())
				continue;
			sortedNodeArray[i] = (Object) node;
		}

		quickSortNodesWithRange(0, (numNode - 1));

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

		int i, j, k, r2;
		double size1, size2, size3;

		//
		//  If less than 10 links in the range:
		//     Bubble Sort	
		//
		if (range2 - range1 < 100000) {
			r2 = range2 - 1;
			for (i = range1; i < r2; i++) {
				for (j = range2; j > i; j--) {
					node1 = (Node) sortedNodeArray[j];
					node2 = (Node) sortedNodeArray[j - 1];
					size1 = node1.getWidth() * node1.getHeight();
					size2 = node2.getWidth() * node2.getHeight();

					if (size1 >= size2)
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
		size1 = node1.getWidth() * node1.getHeight();
		size2 = node2.getWidth() * node2.getHeight();
		size3 = (size1 > size2) ? size1 : size2;

		j = range1;
		k = range2;
		for (i = range1; i <= range2; i++) {
			node1 = (Node) sortedNodeArray[i];
			size1 = node1.getWidth() * node1.getHeight();
			if (size1 < size3)
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
	 * �ŏ��́i�ő�́jNode����ʔz�u����
	 * @param node �ŏ��́i�ő�́jNode
	 */
	void placeFirstNode(Node node) {
		pg.initGrid();
		pg.setXPos(0, -node.getWidth() - margin);
		pg.setXPos(1, node.getWidth() + margin);
		pg.setYPos(0, -node.getHeight() - margin);
		pg.setYPos(1, node.getHeight() + margin);
		pg.setFlag(0, node.getId());

		node.setX(0.0);
		node.setY(0.0);
		node.setZ(0.0);
		node.setNX(0.0);
		node.setNY(0.0);
		node.setNZ(0.0);

	}

	/**
	 * 2�ڈȍ~��Node��1����ʔz�u����
	 * @param node Node
	 */
	void placeOtherNode(Node node) {
		int center[], flag = 0, distance = 0;

		extension = -1.0e+30;

		// Try placing the rectangle at candidate positions
		listCandidates(node);
		flag = visitCandidates(node);

		// if the rectangle can be placed
		node.setX((posX[0] + posX[1]) * 0.5);
		node.setNX((posX[0] + posX[1]) * 0.5);
		node.setY((posY[0] + posY[1]) * 0.5);
		node.setNY((posY[0] + posY[1]) * 0.5);
		node.setZ(0.0);
		node.setNZ(0.0);
		updateGrid(node);

	}

	/**
	 * Node�𑽐��̌��ʒu�ɔz�u���Ă݂�
	 * @param node Node
	 * @return �z�u���ʁi1:����, 0:���Ƃ��čl������, -1:�z�u�s�j
	 */
	int visitCandidates(Node node) {
		int i, j, ret = -1;
		double xmm[] = new double[2];
		double ymm[] = new double[2];

		//
		// for each candidate due to priority
		//
		for (j = 0; j <= 3; j++) {
			for (i = 0; i < pg.candidate[j].size(); i++) {
				double pos[] = (double[]) pg.candidate[j].elementAt(i);
				xmm[0] = pos[0] - (margin + node.getWidth());
				xmm[1] = pos[0] + (margin + node.getWidth());
				ymm[0] = pos[1] - (margin + node.getHeight());
				ymm[1] = pos[1] + (margin + node.getHeight());
				ret = trialPlacement(node, xmm, ymm, true);
				if (ret > 0)
					return ret;
			}
		}

		//
		// for each last candidates
		//
		for (i = 0; i < pg.candidate[4].size(); i++) {
			double pos[] = (double[]) pg.candidate[4].elementAt(i);
			xmm[0] = pos[0] - (margin + node.getWidth());
			xmm[1] = pos[0] + (margin + node.getWidth());
			ymm[0] = pos[1] - (margin + node.getHeight());
			ymm[1] = pos[1] + (margin + node.getHeight());
			ret = trialPlacement(node, xmm, ymm, false);
		}

		return ret;
	}

	
	/**
	 * Node��1�ӏ��̌��ʒu�ɂ��߂��ɒu���Ă݂�
	 * @param node Node
	 * @param xmm ���ʒu��x���W�l�i�̍ŏ��l�ƍő�l�j
	 * @param ymm ���ʒu��y���W�l�i�̍ŏ��l�ƍő�l�j
	 * @param oflag ��ʈʒu�̓����ł����true
	 * @return �z�u���ʁi1:����, 0:���Ƃ��čl������, -1:�z�u�s�j
	 */
	int trialPlacement(Node node, double xmm[], double ymm[], boolean oflag) {
		int xmax, xmin, ymax, ymin, i = 0, j = 0;

		// Specify the range of cells that should be checked	
		if (oflag == true) {
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
			if (xmm[0] >= pg.xPositions[0]
				&& xmm[1] <= pg.xPositions[pg.xSize]
				&& ymm[0] >= pg.yPositions[0]
				&& ymm[1] <= pg.yPositions[pg.ySize]) {
				posX[0] = xmm[0];
				posX[1] = xmm[1];
				posY[0] = ymm[0];
				posY[1] = ymm[1];
				extension = 0.0;
				return 1;
			}
		}

		// if it does not overlap, but overflow ...
		double x1 = pg.xPositions[0] < xmm[0] ? pg.xPositions[0] : xmm[0];
		double x2 =
			pg.xPositions[pg.xSize] > xmm[1] ? pg.xPositions[pg.xSize] : xmm[1];
		double y1 = pg.yPositions[0] < ymm[0] ? pg.yPositions[0] : ymm[0];
		double y2 =
			pg.yPositions[pg.ySize] > ymm[1] ? pg.yPositions[pg.ySize] : ymm[1];
		double newWidth = x2 - x1;
		double newHeight = y2 - y1;
		double oldWidth = pg.xPositions[pg.xSize] - pg.xPositions[0];
		double oldHeight = pg.yPositions[pg.ySize] - pg.yPositions[0];
		double newAsp =
			(newWidth > newHeight)
				? newWidth / newHeight
				: newHeight / newWidth;
		double oldAsp =
			(oldWidth > oldHeight)
				? oldWidth / oldHeight
				: oldHeight / oldWidth;
		double e = 0.0;
		double e1 = newAsp / oldAsp;
		if (branch == tree.getRootBranch())
			e = e1;
		else {
			double e2 = (newWidth * newHeight) / (oldWidth * oldHeight);
			e = e1 * ASPECT_VS_AREA + e2 * (1.0 - ASPECT_VS_AREA);
		}

		if (e > 0.0 && (e < extension || extension < 0.0)) {
			posX[0] = xmm[0];
			posX[1] = xmm[1];
			posY[0] = ymm[0];
			posY[1] = ymm[1];
			extension = e;
		}

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
		//
		divideGridX(xmin, posX[0]);
		divideGridX(xmax, posX[1]);

		//
		// Divide the grid according to pg.yPositions
		//
		divideGridY(ymin, posY[0]);
		divideGridY(ymax, posY[1]);

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
	 * �����`�z�u�̌��ʒu�����X�g�A�b�v����
	 * @param node Node
	 */
	void listCandidates(Node node) {
		for (int i = 0; i < 5; i++)
			pg.candidate[i].clear();

		//
		// for each subspace of the grid
		//
		for (int i = 0; i < pg.ySize; i++) {
			for (int j = 0; j < pg.xSize; j++) {
				int id1, id2, id3, n = 0;
				int id = i * pg.xSize + j;
				if (pg.xyFlags[id] >= 0)
					continue;

				//
				// first candidate position
				//
				double pos1[] = new double[2];
				pos1[0] = pg.xPositions[j] + margin + node.getWidth();
				pos1[1] = pg.yPositions[i] + margin + node.getHeight();
				if (j == 0 || i == 0)
					pg.candidate[3].add((Object) pos1);
				else {
					id1 = (i - 1) * pg.xSize + (j - 1);
					id2 = (i - 1) * pg.xSize + j;
					id3 = i * pg.xSize + (j - 1);
					addCandidatePosition(pos1, id1, id2, id3);
				}

				//
				// second candidate position
				//
				double pos2[] = new double[2];
				pos2[0] = pg.xPositions[j + 1] - margin - node.getWidth();
				pos2[1] = pg.yPositions[i] + margin + node.getHeight();
				if (j == (pg.xSize - 1) || i == 0)
					pg.candidate[3].add((Object) pos2);
				else {
					id1 = (i - 1) * pg.xSize + (j + 1);
					id2 = (i - 1) * pg.xSize + j;
					id3 = i * pg.xSize + (j + 1);
					addCandidatePosition(pos2, id1, id2, id3);
				}

				//
				// third candidate position
				//
				double pos3[] = new double[2];
				pos3[0] = pg.xPositions[j] + margin + node.getWidth();
				pos3[1] = pg.yPositions[i + 1] - margin - node.getHeight();
				if (j == 0 || i == (pg.ySize - 1))
					pg.candidate[3].add((Object) pos3);
				else {
					id1 = (i + 1) * pg.xSize + (j - 1);
					id2 = (i + 1) * pg.xSize + j;
					id3 = i * pg.xSize + (j - 1);
					addCandidatePosition(pos3, id1, id2, id3);
				}

				//
				// fourth candidate position
				//
				double pos4[] = new double[2];
				pos4[0] = pg.xPositions[j + 1] - margin - node.getWidth();
				pos4[1] = pg.yPositions[i + 1] - margin - node.getHeight();
				if (j == (pg.xSize - 1) || i == (pg.ySize - 1))
					pg.candidate[3].add((Object) pos4);
				else {
					id1 = (i + 1) * pg.xSize + (j + 1);
					id2 = (i + 1) * pg.xSize + j;
					id3 = i * pg.xSize + (j + 1);
					addCandidatePosition(pos4, id1, id2, id3);
				}

			}
		}

		//
		// Two outside candidates
		//
		double pos5[] = new double[2];
		pos5[0] = pg.xPositions[0] + margin + node.getWidth();
		pos5[1] = pg.yPositions[pg.ySize] + margin + node.getHeight();
		pg.candidate[4].add((Object) pos5);

		double pos6[] = new double[2];
		pos6[0] = pg.xPositions[pg.xSize] + margin + node.getWidth();
		pos6[1] = pg.yPositions[0] + margin + node.getHeight();
		pg.candidate[4].add((Object) pos6);

	}


	/**
	 * 1�̌��ʒu�����X�g�ɒǉ�����
	 * @param pos ��ʏ�̍��W�l
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

}
