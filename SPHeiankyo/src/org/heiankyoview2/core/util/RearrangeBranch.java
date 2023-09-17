package org.heiankyoview2.core.util;

import org.heiankyoview2.core.table.*;
import org.heiankyoview2.core.tree.*;

public class RearrangeBranch {

	/**
	 * groupId��Table�̒l��Group���č\������
	 * @param tree
	 * @param groupId
	 */
	public static void rearrange(Tree tree, int groupId) {
		cancelAllBranch(tree);
		generateBranch(tree, groupId);
	}
	
	
	/**
	 * Branch�ɑ�����S�Ă�Node���ArootBranch�����Ɉړ����A�qBranch��S�č폜����
	 * @param tree
	 */
	public static void cancelAllBranch(Tree tree) {
		cancelOneBranch(tree, tree.getRootBranch());
	}

	
	/**
	 * 1��Branch�ɑ�����S�Ă�Node���ArootBranch�����Ɉړ����A�qBranch��S�č폜����
	 * @param tree
	 * @param branch
	 */
	static void cancelOneBranch(Tree tree, Branch branch) {
		Branch rootBranch = tree.getRootBranch();
		
		// Node���ꎞ�I�ɔz��Ɋi�[����
		Node narray[] = new Node[branch.getNodeList().size()];
		for(int i = 0; i < branch.getNodeList().size(); i++) {
			narray[i] = (Node)branch.getNodeList().elementAt(i);
		}

		// �eNode�ɂ���
		for(int i = 0; i < narray.length; i++) {
			Node node = narray[i];
			
			// �qBranch���ċA�Ăяo��
			Branch cbranch = node.getChildBranch();
			if(cbranch != null) {
				cancelOneBranch(tree, cbranch);
				tree.deleteOneBranch(cbranch);
				cbranch.getParentNode().getCurrentBranch().deleteOneNode(cbranch.getParentNode());
			}
			// �qNode��rootBranch�Ɉړ�
			else {
				rootBranch.addOneNode(node);
			}
			
			// ���YBranch����Node���폜
			branch.deleteOneNode(node);
		}
		
	}
	
	/**
	 * rootBranch�����̎qNode��Ώۂɂ��āAgroupId��Table�̒l��Group���č\������
	 * @param tree
	 * @param groupId
	 */
	public static void generateBranch(Tree tree, int groupId) {
		Branch rootBranch = tree.getRootBranch();
		Table table = tree.table.getTable(groupId + 1);
		
		// Branch���m�ۂ��A�ꎞ�I�ɔz��Ɋi�[����
		int tableSize = table.getSize();
		Branch barray[] = new Branch[tableSize + 1];
		for(int i = 0; i <= tableSize; i++)
			barray[i] = tree.getOneNewBranch();
		
		// rootBranch������Node���ꎞ�I�ɔz��Ɋi�[����
		Node narray[] = new Node[rootBranch.getNodeList().size()];
		for(int i = 0; i < rootBranch.getNodeList().size(); i++) {
			narray[i] = (Node)rootBranch.getNodeList().elementAt(i);
		}
		
		//  rootBranch�����̊eNode�ɂ���
		for(int i = 0; i < narray.length; i++) {
			Node node = narray[i];
			
			// �Y������Branch����肵�ABranch�ɒǉ�
			int bid = node.table.getId(groupId + 1) - 1;
			if(bid >= 0 && bid < barray.length)
				barray[bid].addOneNode(node);
			else
				barray[tableSize].addOneNode(node);
			
			// rootBranch����폜
			rootBranch.deleteOneNode(node);
		}
		
		// Branch��rootBranch�����ɒǉ�����
		for(int i = 0; i < barray.length; i++) {
			Node node = rootBranch.getOneNewNode();
			node.setChildBranch(barray[i]);
			barray[i].setParentNode(node);
		}
	}
}
