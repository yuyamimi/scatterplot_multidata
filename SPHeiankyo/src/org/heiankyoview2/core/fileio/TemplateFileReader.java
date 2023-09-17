package org.heiankyoview2.core.fileio;

import org.heiankyoview2.core.tree.Tree;
import org.heiankyoview2.core.tree.Link;
import org.heiankyoview2.core.tree.Branch;
import org.heiankyoview2.core.tree.Node;
import org.heiankyoview2.core.table.TreeTable;
import java.io.*;
import java.util.*;


/**
 * Template�t�@�C����ǂݍ���
 * @author itot
 */
public class TemplateFileReader {

	/* var */
	FileInput input = null;
	Tree tree = null;

	TableFileReader table = null;

	Vector branchList = null;
	int numBranches;
	int attributeType;

	/**
	 * Constructor
	 * @param inputFile �ǂݍ��݃t�@�C��
	 */
	public TemplateFileReader(File inputFile) {
		input = new FileInput(inputFile);
		branchList = new Vector();
	}

	/**
	 * tree�t�@�C����ǂݍ��݁A��������Tree��Ԃ�
	 * @return ��������Tree
	 */
	public Tree getData() {
		if (tree == null) { //if non-exit, generate
			tree = new Tree(); //create tree
			table = new TableFileReader(tree);
			if (!parseData(tree)) {
				input.close();
				return null;
			}
		}
		input.close();
		return tree;
	}

	/**
	 * tree�t�@�C����parse���āATree���\������f�[�^���\�z����
	 * @param tree Tree
	 * @return ���������true
	 */
	public boolean parseData(Tree tree) {

		double maxX = 0.0, minX = 0.0, maxY = 0.0, minY = 0.0;

		String lineBuffer;
		int numbranch = 0;
		int numnode = 0;
		Branch currentBranch;
		Branch rootBranch;
		int branchID = 0;
		int nodeCountforSize = 0;

		while (input.ready() && (lineBuffer = input.read()) != null) {
			//read .NVW file
			if (lineBuffer.startsWith("#")) {
				continue; //skip comment line
				
			} else if (lineBuffer.startsWith("numbranch")) {
				numbranch = Integer.parseInt(lineBuffer.substring(9).trim());
				tree.setNumBranch(numbranch);
				continue;
			} else if (lineBuffer.startsWith("branchtitle")) {
				String branchName =
					setBranchName(tree, lineBuffer.substring(11).trim());
				continue;
			} else if (lineBuffer.startsWith("numnode")) {
				branchID = setNumNode(tree, lineBuffer.substring(7).trim());
				continue;
			} else if (lineBuffer.startsWith("numlink")) {
				setNumLink(tree, lineBuffer.substring(7).trim());
				continue;
			} else if (lineBuffer.startsWith("nodepos")) {
				Node currentNode =
					setNNodePos(tree, branchID, lineBuffer.substring(7).trim());
				continue;
			} else if (lineBuffer.startsWith("nodesize")) {
				nodeCountforSize++;
				continue;
			} else if (lineBuffer.startsWith("nodeimageurl")) {
				setNodeImageUrl(tree, branchID, lineBuffer.substring(12).trim());
				continue;
			} else if (lineBuffer.startsWith("nodefile")) {
				setNodeFilename(tree, branchID, lineBuffer.substring(8).trim());
				continue;
			} else if (lineBuffer.startsWith("linknode")) {
				setLinkNode(tree, lineBuffer.substring(8).trim());
				continue;
			} else if (lineBuffer.startsWith("childbranch")) {
				setChild(tree, branchID, lineBuffer.substring(11).trim());
				continue;
			} else 
				table.parseTableData(tree, branchID, lineBuffer);
			
		}

		setRootBranch(tree);

		return true; //if data read successfully!
	}



	/**
	 * Branch��ID���w�肵�āABranch��Ԃ�
	 * @param id Branch��ID
	 * @return Branch
	 */
	public Branch getBranchAt(int id) {
		if (id < 1 || id > branchList.size()) {
			return null;
		} else
			return (Branch) branchList.elementAt(id - 1);

	}

	/**
	 * Branch�̖��O���w�肷��
	 * @param tree Tree
	 * @param lineBuffer tree�t�@�C������ǂݍ��񂾍s
	 * @return ���ݏ������Ă���Branch��ID
	 */
	String setBranchName(Tree tree, String lineBuffer) {

		StringTokenizer tokenBuffer = new StringTokenizer(lineBuffer);
		if (tokenBuffer.countTokens() < 2)
			return null;

		// get ID
		String idStr = tokenBuffer.nextToken();
		int branchId = Integer.parseInt(idStr);
		// get title (title="*** *** ***")
		String tmp = tokenBuffer.nextToken();
		int nameIndex = lineBuffer.indexOf(tmp, idStr.length() + 1);
		String name = lineBuffer.substring(nameIndex);

		Branch currentBranch;
		currentBranch = tree.getBranchAt(branchId);
		currentBranch.setName(name);

		return name;
	}

	/**
	 * Branch�ɑ�����Node�̌����Z�b�g����
	 * @param tree Tree
	 * @param lineBuffer tree�t�@�C������ǂݍ��񂾍s
	 * @return ���ݏ������Ă���Branch��ID
	 */
	int setNumNode(Tree tree, String lineBuffer) {
		Branch currentBranch;
		int numnode;
		int branchID;

		StringTokenizer tokenBuffer = new StringTokenizer(lineBuffer);
		if (tokenBuffer.countTokens() < 2)
			return 0;

		branchID = Integer.parseInt(tokenBuffer.nextToken());
		if ((currentBranch = tree.getBranchAt(branchID)) == null)
			return 0;

		numnode = Integer.parseInt(tokenBuffer.nextToken());
		currentBranch.setNumNode(numnode);

		return branchID;
	}


	/**
	 * Node�̃T�C�Y��ǂݍ��݁A�Y������Node��Ԃ�
	 * @param tree Tree
	 * @param branchID ���ݏ������Ă���Branch��ID
	 * @param lineBuffer nvw�t�@�C������ǂݍ��񂾍s
	 * @return �ǂݍ��񂾍s�Ɏw�肳��Ă���Node
	 */
	Node setNodeSize(
		Tree tree, int branchID,
		String lineBuffer) {

		StringTokenizer tokenBuffer = new StringTokenizer(lineBuffer);
		int nodeID;
		Node currentNode;

		if (tokenBuffer.countTokens() < 4)
			return null;

		nodeID = Integer.parseInt(tokenBuffer.nextToken());
		currentNode = tree.getBranchAt(branchID).getNodeAt(nodeID);
		if (currentNode == null)
			return null;

		currentNode.setNsize(
			Double.parseDouble(tokenBuffer.nextToken()),
			Double.parseDouble(tokenBuffer.nextToken()),
			Double.parseDouble(tokenBuffer.nextToken()));

		return currentNode;
	}

	/**
	 * Node�ɕR�����Ă���t�@�C���̖��O���Z�b�g����
	 * @param tree Tree
	 * @param branchID ���ݏ������Ă���Branch��ID
	 * @param lineBuffer tree�t�@�C������ǂݍ��񂾍s
	 */ 
	void setNodeFilename(
		Tree tree, int branchID,
		String lineBuffer) {

		StringTokenizer tokenBuffer = new StringTokenizer(lineBuffer);
		Node currentNode;

		if (tokenBuffer.countTokens() < 2)
			return;

		int nodeID = Integer.parseInt(tokenBuffer.nextToken());
		currentNode = tree.getBranchAt(branchID).getNodeAt(nodeID);
		if (currentNode == null)
			return;

		currentNode.setChildFilename(tokenBuffer.nextToken());
	}

	/**
	 * Node�ɕR�����Ă���摜URL�̖��O���Z�b�g����
	 * @param tree Tree
	 * @param branchID ���ݏ������Ă���Branch��ID
	 * @param lineBuffer tree�t�@�C������ǂݍ��񂾍s
	 */
	void setNodeImageUrl(Tree tree, int branchID, String lineBuffer) {

		StringTokenizer tokenBuffer = new StringTokenizer(lineBuffer);
		Node currentNode;

		if (tokenBuffer.countTokens() < 2)
			return;

		int nodeID = Integer.parseInt(tokenBuffer.nextToken());
		currentNode = tree.getBranchAt(branchID).getNodeAt(nodeID);
		if (currentNode == null)
			return;

		String surl = tokenBuffer.nextToken();
		currentNode.setImageUrl(surl);
		//System.out.println(surl);
		
		/*
		Image image = null;
		
		try {
			if(surl.startsWith("http")) {
				URL url = new URL(surl);
				image = ImageIO.read(url);
			}
			else {
				image = ImageIO.read(new File(surl));
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		currentNode.setImage(image);
		*/
	}
		
	/**
	 * �A�[�N�̖{�����w�肷��
	 * @param Tree
	 * @param lineBuffer
	 */
	void setNumLink(Tree tree, String lineBuffer) {
		Branch currentBranch;
		int numlink;
		int branchID;

		StringTokenizer tokenBuffer = new StringTokenizer(lineBuffer);
		if (tokenBuffer.countTokens() < 1)
			return;

		numlink = Integer.parseInt(tokenBuffer.nextToken());
		tree.setNumLink(numlink);
	}


	/**
	 * �A�[�N�̗��[�̃m�[�h���w�肷��
	 * @param Tree
	 * @param lineBuffer
	 */
	void setLinkNode(Tree tree, String lineBuffer) {

		StringTokenizer tokenBuffer = new StringTokenizer(lineBuffer);
		int linkID, branchID;
		Branch currentBranch;
		Node node1;
		Node node2;

		if (tokenBuffer.countTokens() < 5)
			return;

		linkID = Integer.parseInt(tokenBuffer.nextToken());

		branchID = Integer.parseInt(tokenBuffer.nextToken());
		currentBranch = tree.getBranchAt(branchID);
		node1 =
			currentBranch.getNodeAt(Integer.parseInt(tokenBuffer.nextToken()));

		branchID = Integer.parseInt(tokenBuffer.nextToken());
		currentBranch = tree.getBranchAt(branchID);
		node2 =
			currentBranch.getNodeAt(Integer.parseInt(tokenBuffer.nextToken()));

		Link link = tree.getLinkAt(linkID);
		link.setNodes(node1, node2);
		
	}


	/**
	 * Branch�ɑ΂���qBranch���Z�b�g����
	 * @param tree Tree
	 * @param branchID ���ݏ������Ă���Branch��ID
	 * @param lineBuffer tree�t�@�C������ǂݍ��񂾍s
	 */ 
	void setChild(Tree tree, int branchID, String lineBuffer) {

		StringTokenizer tokenBuffer = new StringTokenizer(lineBuffer);
		int nodeID;
		Branch currentBranch;
		Node currentNode;
		int childbranchID;
		Branch childBranch;

		if (tokenBuffer.countTokens() < 2)
			return;
		if ((currentBranch = tree.getBranchAt(branchID)) == null)
			return;

		nodeID = Integer.parseInt(tokenBuffer.nextToken());
		currentNode = currentBranch.getNodeAt(nodeID);
		if (currentNode == null)
			return;

		childbranchID = Integer.parseInt(tokenBuffer.nextToken());
		childBranch = tree.getBranchAt(childbranchID);
		if (childBranch == null)
			return;

		childBranch.setLevel(currentBranch.getLevel() + 1);
		currentNode.setChildBranch(childBranch);
		currentNode.getChildBranch().setParentNode(currentNode);
		
	}

	/**
	 * Node�̐��K�����W�n�ł̍��W�l���Z�b�g����
	 * @param tree Tree
	 * @param branchID ���ݏ������Ă���Branch��ID
	 * @param lineBuffer nvw�t�@�C������ǂݍ��񂾍s
	 * @return �ǂݍ��񂾍s�Ɏw�肳��Ă���Node
	 */
	Node setNNodePos(
		Tree tree, int branchID,
		String lineBuffer) {

		StringTokenizer tokenBuffer = new StringTokenizer(lineBuffer);
		Node currentNode = null;
		int nodeID;

		if (tokenBuffer.countTokens() < 4)
			return null;

		Branch branch = (Branch)tree.getBranchList().elementAt(branchID - 1);
		nodeID = Integer.parseInt(tokenBuffer.nextToken());
		currentNode = tree.getBranchAt(branchID).getNodeAt(nodeID);
		double x = Double.parseDouble(tokenBuffer.nextToken());
		double y = Double.parseDouble(tokenBuffer.nextToken());
		double z = Double.parseDouble(tokenBuffer.nextToken());

		currentNode.setNCoordinate(x, y, z);
		return currentNode;
	}

	/**
	 * Tree�ɍ�Branch���Z�b�g����
	 * @param tree Branch
	 */
	void setRootBranch(Tree Tree) {

		if (tree == null)
			return;

		for (int i = 1; i <= tree.getNumBranch(); i++) {
			if (tree.getBranchAt(i).getParentNode() == null) {
				tree.setRootBranch(tree.getBranchAt(i));
			}
		}
	}
}
