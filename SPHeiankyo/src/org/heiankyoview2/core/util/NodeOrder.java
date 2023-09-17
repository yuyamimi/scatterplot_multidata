package org.heiankyoview2.core.util;

import java.util.Comparator;
import java.util.TreeSet;
import java.util.Vector;


import org.heiankyoview2.core.table.*;
import org.heiankyoview2.core.tree.*;

/*	
 * �m�[�h�̕��ёւ����s���B
 */
public class NodeOrder {
	
	StringComparator  scomp = null;
	DoubleComparator  dcomp = null; 
	IntegerComparator icomp = null;
	
	
	//	�R���X�g���N�^
	public void order(int attribute, Tree tree){
		
		// ����ID���������ݒ肳��Ă��邱�Ƃ��m�F����
		if(attribute <= 0) return;
		Table table = tree.table.getTable(attribute);
		int tabletype = table.getType();
		
	
		// �\�[�g�̂��߂ɕK�v�ȃc�[��������������
		if(tabletype == table.TABLE_STRING) 
			scomp = new StringComparator();
		if(tabletype == table.TABLE_DOUBLE) 
			dcomp = new DoubleComparator();
		if(tabletype == table.TABLE_INT) 
			icomp = new IntegerComparator();
		TreeSet stree = null; 
		TreeSet dtree = null;
		TreeSet itree = null;
		
		
		// �e�X��Branch�ɂ���
		for(int i = 0; i < tree.getBranchList().size(); i++) {
			Branch branch = (Branch)tree.getBranchList().elementAt(i);
			
			// ���YBranch���\�[�g�Ώۂł��邩�ۂ����m�F����
			boolean isLeafOnly = true;
			for(int j = 0; j < branch.getNodeList().size(); j++) {
				Node node = (Node)branch.getNodeList().elementAt(j);
				if(node.getChildBranch() != null) {
					isLeafOnly = false;  break;
				}
			}
			if(isLeafOnly == false) continue;
			
			if(tabletype == table.TABLE_STRING) 
				stree = new TreeSet(scomp);
			if(tabletype == table.TABLE_DOUBLE) 
				dtree = new TreeSet(dcomp);
			if(tabletype == table.TABLE_INT) 
				itree = new TreeSet(icomp);
			
			//	Node��TreeSet�ɓo�^����
			Vector nodeList = branch.getNodeList();
			int numnodes = nodeList.size();
			int tsize = 0;
			for(int j = 0; j < nodeList.size(); j ++){	//	�e�m�[�h�ɂ���
				Node node = (Node) nodeList.elementAt(j);
				int tid = node.table.getId(attribute);
				NodeValue nv = new NodeValue();
				nv.id = j;
				nv.node = node;
				
				if (tabletype == table.TABLE_STRING) { /* STRING */    
					nv.ivalue = tid;
					stree.add((Object)nv);
					tsize = stree.size();
				}
				if (tabletype == table.TABLE_DOUBLE) { /* DOUBLE */
					nv.dvalue = table.getDouble(tid);
					dtree.add((Object)nv);
					tsize = dtree.size();
				}
				if (tabletype == table.TABLE_INT) { /* INT */
					nv.ivalue = table.getInt(tid);
					itree.add((Object)nv);
					tsize = itree.size();
				}
			}
			
			// Node�̃��X�g����������N���A����
			nodeList.clear();
			
			// Node��TreeSet������o��
			for (int j = 1; j <= numnodes; j++) {
				Object obj = null;
				if (tabletype == table.TABLE_STRING) { /* STRING */    
					obj = stree.first();
					stree.remove(obj);
					tsize = stree.size();
				}
				if (tabletype == table.TABLE_DOUBLE) { /* DOUBLE */
					obj = dtree.first();
					dtree.remove(obj);
					tsize = dtree.size();
				}
				if (tabletype == table.TABLE_INT) { /* INT */
					obj = itree.first();
					itree.remove(obj);
					tsize = itree.size();
				}
				NodeValue nv = (NodeValue)obj;
				Node node = nv.node;
				node.setId(j);
				nodeList.add(node);
			}
		}
	}
			
	
	
	/**
	 * �ꎞ�I��Node�ƒl���i�[����N���X
	 */
	class NodeValue {
		int id;
		Node node;
		int ivalue;
		double dvalue;
	}
	
	
	/**
	 * ������̑O��֌W���Ǘ�����N���X
	 * @author itot
	 */
	class StringComparator implements Comparator {

		/**
		 * Constructor
		 */
		public StringComparator() {
		}

		/**
		 * 2��Node�̐[�����r����
		 * @param obj1 1�ڂ�Node
		 * @param obj2 2�ڂ�Node
		 */
		public int compare(Object obj1, Object obj2) {

			NodeValue nv1 = (NodeValue)obj1;
			NodeValue nv2 = (NodeValue)obj2;
			
			// �召�֌W��Ԃ�
			int sub = nv1.ivalue - nv2.ivalue;
			if(sub > 0) return 1;
			if(sub < 0) return -1;
			
			if(nv1.id > nv2.id) return 1;
			if(nv1.id < nv2.id) return -1;
			
			return 0;
		}

	}
	
	/**
	 * �����̑召�֌W���Ǘ�����N���X
	 * @author itot
	 */
	class DoubleComparator implements Comparator {

		/**
		 * Constructor
		 */
		public DoubleComparator() {
		}

		/**
		 * 2��Node�̐[�����r����
		 * @param obj1 1�ڂ�Node
		 * @param obj2 2�ڂ�Node
		 */
		public int compare(Object obj1, Object obj2) {

			NodeValue nv1 = (NodeValue)obj1;
			NodeValue nv2 = (NodeValue)obj2;
			
			// �召�֌W��Ԃ�
			double sub = nv1.dvalue - nv2.dvalue;
			if(sub > 1.0e-5) return 1;
			if(sub < -1.0e-5) return -1;
			
			if(nv1.id > nv2.id) return 1;
			if(nv1.id < nv2.id) return -1;
			return 0;
		}

	}
	
	
	/**
	 * �����̑召�֌W���Ǘ�����N���X
	 * @author itot
	 */
	class IntegerComparator implements Comparator {

		/**
		 * Constructor
		 */
		public IntegerComparator() {
		}

		/**
		 * 2��Node�̐[�����r����
		 * @param obj1 1�ڂ�Node
		 * @param obj2 2�ڂ�Node
		 */
		public int compare(Object obj1, Object obj2) {
			
			NodeValue nv1 = (NodeValue)obj1;
			NodeValue nv2 = (NodeValue)obj2;
			
			// �召�֌W��Ԃ�
			int sub = nv1.ivalue - nv2.ivalue;
			if(sub > 0) return 1;
			if(sub < 0) return -1;
			
			if(nv1.id > nv2.id) return 1;
			if(nv1.id < nv2.id) return -1;
			return 0;
		}

	}
}
