package org.heiankyoview2.core.placement;

import org.heiankyoview2.core.table.*;
import org.heiankyoview2.core.tree.*;
import java.util.*;

/**
 * Tree��Template�̊ԂőΉ�����Node��Branch����肷��
 * ���FTable��1�ԖڂɁANode��Branch����肷�閼�O���ݒ肳��Ă��邱��
 */
public class TemplateNodeIdentifier {
	static final int NUMKEY = 101;
	static Vector namearray[];
	static Tree templateTree = null;
	
	
	/**
	 * �e���v���[�g�ƂȂ�؍\����ݒ肷��
	 * @param templateTree
	 */
	static public void setTemplateTree(Tree ttree) {
		templateTree = ttree;
		if(templateTree == null) return;
		Table table = templateTree.table.getTable(1);
		
		// �������̈�̏�����
		namearray = new Vector[NUMKEY];
		for(int i = 0; i < NUMKEY; i++) 
			namearray[i] = new Vector();
		
		// �e�X��Branch�ɂ���
		for(int i = 0; i < templateTree.getBranchList().size(); i++) {
			Branch branch = (Branch)templateTree.getBranchList().elementAt(i);
			
			// Branch�̖��O���i�[����
			int id = branch.getParentNode().table.getId(1);
			if(id > 0) {
				String name = table.getString(id);
				int hvalue = name.hashCode() % NUMKEY;
				if(hvalue < 0) hvalue += NUMKEY;
				TemplateName tn = new TemplateName(name, branch.getParentNode(), branch);
				namearray[hvalue].add(tn);
			}
			
			// �e�X��Node�ɂ���
			for(int j = 0; j < branch.getNodeList().size(); j++) {
				Node node = (Node)branch.getNodeList().elementAt(j);
				if(node.getChildBranch() != null) continue;
				
				// Node�̖��O���i�[����
				id = node.table.getId(1);
				if(id > 0) {
					String name = table.getString(id);
					int hvalue = name.hashCode() % NUMKEY;
					if(hvalue < 0) hvalue += NUMKEY;
					TemplateName tn = new TemplateName(name, node, null);
					namearray[hvalue].add(tn);
				}
			}
		}
		
	}
	
	
	/**
	 * ����node�ɑΉ�����e���v���[�g���Node��Ԃ�
	 * @param tree
	 * @param node
	 * @return
	 */
	static Node findTemplateNode(Tree tree, Node node) {
		if(templateTree == null) return null;
		
		// ����node�̖��O�̃n�b�V���L�[���Z�o����
		String name = getNodeIdentifier(tree, node);
		if (name == null
				|| name.length() <= 0
				|| name.startsWith("null"))
				return null;
		int hvalue = name.hashCode() % NUMKEY;
		if(hvalue < 0) hvalue += NUMKEY;
			
		// ���Y�n�b�V���L�[�ɓo�^���ꂽ���O����������
		for(int i = 0; i < namearray[hvalue].size(); i++) {
			TemplateName tn = (TemplateName)namearray[hvalue].elementAt(i);
			if(tn.name.compareTo(name) == 0) 
				return tn.node;
		}
		
		// �e���v���[�g���Node��������Ȃ����null��Ԃ�
		return null;
	}
	
	
	static Branch findTemplateBranch(Tree tree, Branch branch) {
		if(templateTree == null) return null;

		// ����node�̖��O�̃n�b�V���L�[���Z�o����
		String name = getBranchIdentifier(tree, branch);
		if (name == null
				|| name.length() <= 0
				|| name.startsWith("null"))
				return null;
		int hvalue = name.hashCode() % NUMKEY;
		if(hvalue < 0) hvalue += NUMKEY;

		// ���Y�n�b�V���L�[�ɓo�^���ꂽ���O����������
		for(int i = 0; i < namearray[hvalue].size(); i++) {
			TemplateName tn = (TemplateName)namearray[hvalue].elementAt(i);
			if(tn.name.compareTo(name) == 0) {
				if(tn.branch == null) continue;
				return tn.branch;
			}
		}
		
		// �e���v���[�g���Branch��������Ȃ����null��Ԃ�
		return null;

	}
	
	/**
	 * Branch�̎��ʎq�ƂȂ閼�O��Ԃ�
	 * @param tree
	 * @param branch
	 * @return
	 */
	static String getBranchIdentifier(Tree tree, Branch branch) {
		String name = branch.getName();
		if (name != null && name.length() > 0 && !name.startsWith("null"))
			return name;
		
		if (tree.table.getNameType() < 0)
			tree.table.setNameType(0);

		Node parentNode = branch.getParentNode();
		name = tree.table.getNodeAttributeName(
				parentNode, tree.table.getNameType());
		return name;

	}
	
	/**
	 * Node�̎��ʎq�ƂȂ閼�O��Ԃ�
	 * @param tree
	 * @param node
	 * @return
	 */
	static String getNodeIdentifier(Tree tree, Node node) {
		String name = "";

		if (tree.table.getNameType() < 0)
			tree.table.setNameType(0);

		name = tree.table.getNodeAttributeName(
				node, tree.table.getNameType());
		
		return name;
	}
	
	
	/**
	 * 1�̖��O�Ɋւ��Node��Branch���i�[����
	 */
	static class TemplateName {
		String name;
		Node node;
		Branch branch;
		
		TemplateName() {
			name = null;  node = null; branch = null;
		}
		
		TemplateName(String nm, Node n, Branch b) {
			name = nm;  node = n; branch = b;
		}
	}
	
	
}
