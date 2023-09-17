package org.heiankyoview2.core.fileio;

import org.heiankyoview2.core.tree.Tree;
import org.heiankyoview2.core.tree.Link;
import org.heiankyoview2.core.tree.Branch;
import org.heiankyoview2.core.tree.Node;
import org.heiankyoview2.core.table.TreeTable;

import java.io.*;
import java.util.*;
import java.awt.Image;
import java.net.URL;
import javax.imageio.*;

/**
 * tree�t�@�C����ǂݍ���
 * @author itot
 */
public class TreeFileReader {

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
	public TreeFileReader(File inputFile) {
		input = new FileInput(inputFile);
		branchList = new Vector();
	}

	/**
	 * Constructor
	 */
	public TreeFileReader(String filename, boolean streamFlag) {
		input = new FileInput(filename, true);
		branchList = new Vector();
	}
	
	/**
	 * tree�t�@�C����ǂݍ��݁A��������Tree��Ԃ�
	 * @return ��������Tree
	 */
	public Tree getTree() {
		tree = new Tree(); //create tree
		table = new TableFileReader(tree);
		readFile(tree);
		input.close();

		return tree;
	}

	/**
	 * tree�t�@�C����parse���āATree���\������f�[�^���\�z����
	 * @param tree Tree
	 * @return ���������true
	 */
	public boolean readFile(Tree tree) {

		String lineBuffer;
		int numbranch = 0;
		int numnode = 0;
		Branch currenBranch;
		Branch rootBranch;
		int branchID = 0;

		while (input.ready() ) {
			if((lineBuffer = input.read()) == null) break;
			
			//read .NVW file
			if (lineBuffer.startsWith("#")) {
				continue; //skip comment line
			} else if (lineBuffer.startsWith("numbranch")) {
				if (tree.getBranchList().size() == 0) {
					numbranch = Integer.parseInt(lineBuffer.substring(9).trim());
					tree.setNumBranch(numbranch);
					continue;
				} else {
					continue;
				}
			} else if (lineBuffer.startsWith("numnode")) {
				branchID = setNumNode(tree, lineBuffer.substring(7).trim());
				continue;
			} else if (lineBuffer.startsWith("numlink")){
				setNumLink(tree, lineBuffer.substring(7).trim());
				continue;
			} else if (lineBuffer.startsWith("branchtitle")) {
				branchID = setBranchName(tree, lineBuffer.substring(11).trim());
			} else if (lineBuffer.startsWith("nodefile")) {
				setNodeFilename(tree, branchID, lineBuffer.substring(8).trim());
				continue;
			} else if (lineBuffer.startsWith("nodesize")) {
				setNodeSize(tree, branchID, lineBuffer.substring(8).trim());
				continue;
			} else if (lineBuffer.startsWith("branchsize")) {
				setBranchSize(tree, branchID, lineBuffer.substring(10).trim());
				continue;
			} else if (lineBuffer.startsWith("nodeimageurl")) {
				setNodeImageUrl(tree, branchID, lineBuffer.substring(12).trim());
				continue;
			} else if (lineBuffer.startsWith("branchimageurl")) {
				setBranchImageUrl(tree, branchID, lineBuffer.substring(14).trim());
				continue;
			} else if (lineBuffer.startsWith("linknode")) {
				setLinkNode(tree, lineBuffer.substring(8).trim());
				continue;
			} else if (lineBuffer.startsWith("childbranch")) {
				setChild(tree, branchID, lineBuffer.substring(11).trim());
				continue;
			} else if(lineBuffer.startsWith("framenodeid")) {
				setFrameNodeId(tree, branchID, lineBuffer.substring(11).trim());
			} else if(lineBuffer.startsWith("fni")) {
				setFrameNodeId(tree, branchID, lineBuffer.substring(3).trim());
			} else if(lineBuffer.startsWith("framefilename")) {
				setTreeFrame(tree, lineBuffer.substring(13).trim());
			} else {
				table.parseTableData(tree, branchID, lineBuffer);
			}
		}

		//find a rootBranch
		setRootBranch(tree);

		return true; //if data read successfully!
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
		currentBranch = (Branch) tree.getBranchList().elementAt(branchID - 1);
		if (currentBranch == null)
			return 0;

		numnode = Integer.parseInt(tokenBuffer.nextToken());
		currentBranch.setNumNode(numnode);

		return branchID;
	}


	/**
	 * Branch�̖��O���w�肷��
	 * @param tree Tree
	 * @param lineBuffer tree�t�@�C������ǂݍ��񂾍s
	 * @return ���ݏ������Ă���Branch��ID
	 */
	int setBranchName(Tree tree, String lineBuffer) {
		int branchID = 0;
		StringTokenizer tokenBuffer = new StringTokenizer(lineBuffer);
		Branch currentBranch;

		if (tokenBuffer.countTokens() < 2)
			return 0;
		branchID = Integer.parseInt(tokenBuffer.nextToken());

		currentBranch = (Branch) tree.getBranchList().elementAt(branchID - 1);
		if (currentBranch == null)
			return 0;

		String name = tokenBuffer.nextToken();
		while(tokenBuffer.countTokens() > 0) {
			name += " ";
			name += tokenBuffer.nextToken();
		}
		currentBranch.setName(name);

		return branchID;
	}

	/**
	 * Node�ɕR�����Ă���t�@�C���̖��O���Z�b�g����
	 * @param tree Tree
	 * @param branchID ���ݏ������Ă���Branch��ID
	 * @param lineBuffer tree�t�@�C������ǂݍ��񂾍s
	 */
	void setNodeFilename(Tree tree, int branchID, String lineBuffer) {

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
	 * Branch�ɕR�����Ă���摜URL�̖��O���Z�b�g����
	 * @param tree Tree
	 * @param branchID ���ݏ������Ă���Branch��ID
	 * @param lineBuffer tree�t�@�C������ǂݍ��񂾍s
	 */
	void setBranchImageUrl(Tree tree, int branchID, String lineBuffer) {

		StringTokenizer tokenBuffer = new StringTokenizer(lineBuffer);
		Node currentNode;

		if (tokenBuffer.countTokens() < 2)
			return;

		tokenBuffer.nextToken();
		currentNode = tree.getBranchAt(branchID).getParentNode();
		if (currentNode == null)
			return;

		String surl = tokenBuffer.nextToken();
		currentNode.setImageUrl(surl);
		
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
	 * Node�̏c�������̑傫�����Z�b�g����
	 * @param tree Tree
	 * @param branchID ���ݏ������Ă���Branch��ID
	 * @param lineBuffer tree�t�@�C������ǂݍ��񂾍s
	 */
	void setNodeSize(Tree tree, int branchID, String lineBuffer) {

		StringTokenizer tokenBuffer = new StringTokenizer(lineBuffer);
		Node currentNode;

		if (tokenBuffer.countTokens() < 2)
			return;

		int nodeID = Integer.parseInt(tokenBuffer.nextToken());
		currentNode = tree.getBranchAt(branchID).getNodeAt(nodeID);
		if (currentNode == null)
			return;

		double width = Double.parseDouble(tokenBuffer.nextToken());
		double height = Double.parseDouble(tokenBuffer.nextToken());
		currentNode.setSize(width, height, 1.0);
	}
	
	
	/**
	 * Branch�̏c�������̑傫�����Z�b�g����
	 * @param tree Branch
	 * @param branchID ���ݏ������Ă���Branch��ID
	 * @param lineBuffer tree�t�@�C������ǂݍ��񂾍s
	 */
	void setBranchSize(Tree tree, int branchID, String lineBuffer) {

		StringTokenizer tokenBuffer = new StringTokenizer(lineBuffer);
		Node currentNode;

		if (tokenBuffer.countTokens() < 2)
			return;

		tokenBuffer.nextToken();
		currentNode = tree.getBranchAt(branchID).getParentNode();
		if (currentNode == null)
			return;

		double width = Double.parseDouble(tokenBuffer.nextToken());
		double height = Double.parseDouble(tokenBuffer.nextToken());
		currentNode.setSize(width, height, 1.0);
	}
	
	/**
	 * Frame����Node�ւ�ID��ݒ肷��
	 * @param tree Tree
	 * @param branchID ���ݏ������Ă���Branch��ID
	 * @param lineBuffer tree�t�@�C������ǂݍ��񂾍s
	 */
	void setFrameNodeId(Tree tree, int branchID, String lineBuffer) {

		StringTokenizer tokenBuffer = new StringTokenizer(lineBuffer);
		Node currentNode;

		if (tokenBuffer.countTokens() < 2)
			return;

		int nodeID = Integer.parseInt(tokenBuffer.nextToken());
		currentNode = tree.getBranchAt(branchID).getNodeAt(nodeID);
		if (currentNode == null)
			return;

		currentNode.setFrameNodeId(Integer.parseInt(tokenBuffer.nextToken()));
	}

	
	/**
	 * �A�[�N�̖{�����w�肷��
	 * @param Tree
	 * @param lineBuffer
	 */
	void setNumLink(Tree tree, String lineBuffer) {
		int numlink;
	
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
		int linkID, gid1, gid2, nid1, nid2;
		Branch branch1, branch2;
		Node node1;
		Node node2;
		
		if (tokenBuffer.countTokens() < 5)
			return;

		linkID = Integer.parseInt(tokenBuffer.nextToken());

		gid1 = Integer.parseInt(tokenBuffer.nextToken());
		branch1 = tree.getBranchAt(gid1);
		nid1 = Integer.parseInt(tokenBuffer.nextToken());
		node1 = branch1.getNodeAt(nid1);

		gid2 = Integer.parseInt(tokenBuffer.nextToken());
		branch2 = tree.getBranchAt(gid2);
		nid2 = Integer.parseInt(tokenBuffer.nextToken());
		node2 = branch2.getNodeAt(nid2);

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
		Branch currentBranch;
		Node currentNode;
		int childbranchID;
		Branch childBranch;

		if (tokenBuffer.countTokens() < 2)
			return;
		if ((currentBranch = tree.getBranchAt(branchID)) == null)
			return;

		int nodeID = Integer.parseInt(tokenBuffer.nextToken());
		currentNode = currentBranch.getOneNewNode();
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
	 * Tree�ɍ�Branch���Z�b�g����
	 * @param tree Branch
	 */
	void setRootBranch(Tree tree) {

		if (tree == null)
			return;

		for (int i = 1; i <= tree.getNumBranch(); i++) {
			Branch branch = tree.getBranchAt(i);
			if (branch.getParentNode() == null) {
				tree.setRootBranch(branch);
				branch.setLevel(1);
				setBranchLevel(branch);
				break;
			}
		}
		
	}
	
	void setBranchLevel(Branch branch) {
		int level = branch.getLevel();
		Vector nodelist = branch.getNodeList();
		for(int i = 0; i < nodelist.size(); i++) {
			Node node = (Node)nodelist.elementAt(i);
			Branch cbranch = node.getChildBranch();
			if(cbranch != null) {
				cbranch.setLevel(level + 1);
				setBranchLevel(cbranch);
			}
		}
		
	}
	
	
	void setTreeFrame(Tree tree, String lineBuffer) {
		StringTokenizer tokenBuffer = new StringTokenizer(lineBuffer);
		String filename = tokenBuffer.nextToken();
		FrameFileReader ffr = new FrameFileReader(new File(filename));
		ffr.getFrame(tree);
	}
	
}
