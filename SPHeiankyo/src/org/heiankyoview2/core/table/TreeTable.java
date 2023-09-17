package org.heiankyoview2.core.table;

import java.awt.Color;
import org.heiankyoview2.core.tree.Tree;
import org.heiankyoview2.core.tree.Branch;
import org.heiankyoview2.core.tree.Node;
import org.heiankyoview2.core.util.*;


/**
 * Tree�̑�����\�`���f�[�^�ŕ\�����邽�߂̃N���X
 * @author itot
 */
public class TreeTable {

	Tree tree;
	Table table[] = null;
	int numTable = 0;

	int colorType = -1, heightType = -1, nameType = -1, urlType = -1;
	int branchType1 = -1, branchType2 = -1, branchType3 = -1;
	boolean flagarray[] = null;
	
	/**
	 * Constructor
	 * @param tree Tree
	 */
	public TreeTable(Tree tree) {
		this.tree = tree;
	}

	/**
	 * Tree���ۗL����Table�̑������Z�b�g����
	 * @param numId Table�̑���
	 */
	public void setNumTable(int nt) {
		if (nt < 1)
			return;
		
		this.numTable = nt;
		table = new Table[numTable];
		for (int i = 0; i < numTable; i++)
			table[i] = null;
		
		flagarray = new boolean[numTable];
		for(int i = 0; i < numTable; i++)
			flagarray[i] = true;
	}

	/**
	 * Tree���ۗL����Table�̑�����Ԃ�=������
	 * @return Table�̑���
	 */
	public int getNumTable() {
		return numTable;
	}
	
	/**
	 * �V�����e�[�u����1�ǉ����ĕԂ�
	 */
	public Table addNumTable() {
		numTable++;
		Table newtable[] = new Table[numTable];
		boolean newflagarray[] = new boolean[numTable];
		
		for(int i = 0; i < (numTable - 1); i++) {
			newtable[i] = table[i];
			newflagarray[i] = flagarray[i];
		}
		
		Table t = new Table();
		newtable[numTable - 1] = t;
		newflagarray[numTable - 1] = true;
		
		table = newtable;
		flagarray = newflagarray;
		
		return t;
	}
	
	
	/**
	 * Table���Z�b�g����
	 * @param tableId Table��ID
	 * @param newTable �V������������Table
	 */
	public void setTable(int tableId, Table newTable) {

		if (numTable < 1)
			return;
		if (tableId <= 0 || tableId > numTable)
			return;

		table[tableId - 1] = newTable;
	}

	/**
	 * Table��Ԃ�
	 * @param tableId Table��ID
	 * @return ID�ɑΉ�����Table
	 */
	public Table getTable(int tableId) {

		if (numTable < 1)
			return null;
		if (tableId <= 0 || tableId > numTable)
			return null;

		return table[tableId - 1];
	}

	/**
	 * �F�v�Z�ɗp����Table��ID���Z�b�g����
	 * @param type �F�v�Z�ɗp����Table��ID
	 */
	public void setColorType(int type) {
		colorType = type;
	}

	/**
	 * �F�v�Z�ɗp����Table��ID��Ԃ�
	 * @return �F�v�Z�ɗp����Table��ID
	 */
	public int getColorType() {
		return colorType;
	}

	/**
	 * �����v�Z�ɗp����Table��ID���Z�b�g����
	 * @param type �����v�Z�ɗp����Table��ID
	 */
	public void setHeightType(int type) {
		heightType = type;
	}

	/**
	 * �����v�Z�ɗp����Table��ID��Ԃ�
	 * @return �����v�Z�ɗp����Table��ID
	 */
	public int getHeightType() {
		return heightType;
	}

	/**
	 * ���O���w�肷��Table��ID���Z�b�g����
	 * @param type ���O���w�肷��Table��ID
	 */
	public void setNameType(int type) {
		nameType = type;
	}

	/**
	 * ���O���w�肷��Table��ID��Ԃ�
	 * @return ���O���w�肷��Table��ID
	 */
	public int getNameType() {
		return nameType;
	}

	/**
	 * URL���w�肷��Table��ID���Z�b�g����
	 * @param type URL���w�肷��Table��ID
	 */
	public void setUrlType(int type) {
		urlType = type;
	}

	/**
	 * URL���w�肷��Table��ID��Ԃ�
	 * @return URL���w�肷��Table��ID
	 */
	public int getUrlType() {
		return urlType;
	}

	
	/**
	 * Table���Q�Ƃ��Ȃ���Node��Branch�ɕ�����ۂɁA���K�w��Branch�킯�̎Q�Ƃ���Table��ID���Z�b�g����
	 * @param type Table��ID
	 */
	public void setBranchType1(int type) {
		branchType1 = type;
	}

	/**
	 * Table���Q�Ƃ��Ȃ���Node��Branch�ɕ�����ۂɁA���K�w��Branch�킯�̎Q�Ƃ���Table��ID��Ԃ�
	 * @return Table��ID
	 */
	public int getBranchType1() {
		return branchType1;
	}

	/**
	 * Table���Q�Ƃ��Ȃ���Node��Branch�ɕ�����ۂɁA���K�w��Branch�킯�̎Q�Ƃ���Table��ID���Z�b�g����
	 * @param type Table��ID
	 */
	public void setBranchType2(int type) {
		branchType2 = type;
	}

	/**
	 * Table���Q�Ƃ��Ȃ���Node��Branch�ɕ�����ۂɁA���K�w��Branch�킯�̎Q�Ƃ���Table��ID��Ԃ�
	 * @return Table��ID
	 */
	public int getBranchType2() {
		return branchType2;
	}

	/**
	 * Table���Q�Ƃ��Ȃ���Node��Branch�ɕ�����ۂɁA��O�K�w��Branch�킯�̎Q�Ƃ���Table��ID���Z�b�g����
	 * @param type Table��ID
	 */
	public void setBranchType3(int type) {
		branchType3 = type;
	}

	/**
	 * Table���Q�Ƃ��Ȃ���Node��Branch�ɕ�����ۂɁA��O�K�w��Branch�킯�̎Q�Ƃ���Table��ID��Ԃ�
	 * @return Table��ID
	 */
	public int getBranchType3() {
		return branchType3;
	}

	/**
	 * 1��Table�ɑ΂��ăt���O��ݒ肷��
	 * @param id
	 * @param flag
	 */
	public void setFlag(int id, boolean flag) {
		if(flagarray == null) return;
		flagarray[id] = flag;
	}
	
	/**
	 * 1��Table�ɑ΂���t���O��Ԃ�
	 * @param id
	 * @return
	 */
	public boolean getFlag(int id) {
		if(flagarray == null) return false;
		return flagarray[id];
	}
	
	/**
	 * �t���O�̔z���Ԃ�
	 * @return
	 */
	public boolean[] getFlagArray() {
		return flagarray;
	}
	
	/**
	 * Node�̐F�Z�o�̂��߂̒l�i0.0�`1.0�j���Z�o����
	 * @param node Node
	 * @return Node�̐F�Z�o�̂��߂̒l�i0.0�`1.0�j
	 */
	public double calcNodeColorValue(Node node) {
		double value = 0.5;

		Table branchTable;

		if (table == null || colorType < 0)
			return value;
		branchTable = table[colorType];

		NodeTablePointer tn = node.table;
		int id = tn.getId(colorType + 1);
		value = branchTable.getAppearanceValue(id - 1);
		
		return value;
	}

	/**
	 * Node�̐F���Z�o����
	 * @param node Node
	 * @return Node�̐F
	 */
	public Color calcNodeColor(Node node, ColorCalculator cc) {
		double value = -1.0;
		Color color = Color.gray;

		Table branchTable;

		if (table == null || colorType < 0)
			return color;
		branchTable = table[colorType];

		NodeTablePointer tn = node.table;
		int id = tn.getId(colorType + 1);
		value = branchTable.getAppearanceValue(id - 1);
		color = cc.calculate((float) value);

		// temporary
		/*
		Branch cbranch = node.getCurrentBranch();
		Node pnode = cbranch.getParentNode();
		Table branchTable2 = table[table.length - 1];
		int id2 = pnode.table.getId(table.length);
		double value2 = branchTable2.getAppearanceValue(id2 - 1);
		color = colorMap.calcHSBSmoothColor((float) value, (float)value2);
		*/
		return color;
	}

	/**
	 * Node�̍����Z�o�̂��߂̒l�i0.0�`1.0�j���Z�o����
	 * @param node Node
	 * @return Node�̍����Z�o�̂��߂̒l�i0.0�`1.0�j
	 */
	public double calcNodeHeightValue(Node node) {
		double value = -1.0;

		Table branchTable;

		if (table == null || heightType < 0)
			return 0.01;
		branchTable = table[heightType];

		NodeTablePointer tn = node.table;
		int id = tn.getId(heightType + 1);
		value = branchTable.getAppearanceValue(id - 1);
		if (value < 0.0)
			value = 0.01;
		
		return value;
	}

	/**
	 * Table���Q�Ƃ��Ȃ���A1��Branch���\������Node�𕡐��̎qBranch�ɕ��ނ���
	 * @param branch Branch
	 * @param branchType �Q�Ƃ���Table��ID
	 */
	/*
	public void divideOneBranch(Branch branch, int branchType) {

		BranchByAttributeId byId = new BranchByAttributeId(tree);
		byId.divideOneBranch(branch, branchType);

	}
	*/

	/**
	 * Node����Table�ւ̃|�C���^�ƂȂ�ID��Ԃ�
	 * @param node Node
	 * @param type �Q�Ƃ���Table��ID
	 * @return  Node����Table�ւ̃|�C���^�ƂȂ�ID
	 */
	public int getNodeTablePointerId(Node node, int type) {
		NodeTablePointer tn = node.table;
		int id = tn.getId(type);
		return id;
	}

	/**
	 * Table���Q�Ƃ��Ȃ���Node�̖��O��Ԃ�
	 * @param node Node
	 * @param type �Q�Ƃ���Table��ID
	 * @return Node�̖��O
	 */
	public String getNodeAttributeName(Node node, int type) {

		int id;
		String name = null;
		Table branchTable;

		if (table == null)
			return null;
		if (type < 0 || type >= numTable)
			return null;
		branchTable = table[type];

		NodeTablePointer tn = node.table;
		id = tn.getId(type + 1);
		if (id <= 0)
			return null;

		if (branchTable.getType() == branchTable.TABLE_STRING) {
			name = branchTable.getString(id);
		} else if (branchTable.getType() == branchTable.TABLE_DOUBLE) {
			name = Double.toString(branchTable.getDouble(id));
		} else if (branchTable.getType() == branchTable.TABLE_INT) {
			name = Integer.toString(branchTable.getInt(id));
		}

		return name;
	}


}
