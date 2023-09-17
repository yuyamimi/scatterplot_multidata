package org.heiankyoview2.core.table;

import org.heiankyoview2.core.tree.Tree;

/**
 * Tree�̑�����\�`���f�[�^�ŕ\�����邽�߂̃N���X
 * @author itot
 */
public class NodeTablePointer {

	int numId = 0;
	int id[] = null;

	/**
	 * Constructor
	 */
	public NodeTablePointer() {
	}

	/**
	 * Node����Table�ւ̃|�C���^�̌��i�ʏ��Table�̌��j���Z�b�g����
	 * @param numId �|�C���^�̌�
	 */
	public void setNumId(int numId) {
		if (numId < 1)
			return;

		this.numId = numId;
		id = new int[numId];
	}

	/**
	 * Node����Table�ւ̃|�C���^�̌��i�ʏ��Table�̌��j��Ԃ�
	 * @return �|�C���^�̌�
	 */
	public int getNumId() {
		return numId;
	}

	/**
	 * Node����Table�ւ̃|�C���^���Z�b�g����
	 * @param attId �Q�Ƃ���Table��ID
	 * @param newId �|�C���^�iTable��̈ʒu��\��ID�j
	 */
	public void setId(int attId, int newId) {
		if (numId < 1)
			return;
		if (attId <= 0 || attId > numId)
			return;

		id[attId - 1] = newId;
		
		//System.out.println("  NodeTablePointer: numId=" + numId + " attId=" + attId + " newId=" + newId);
	}

	/**
	 * Node����Table�ւ̃|�C���^��Ԃ�
	 * @param attId �Q�Ƃ���Table��ID
	 * @return �|�C���^�iTable��̈ʒu��\��ID�j
	 */
	public int getId(int attId) {
		int retId = -1;
		
		if (numId < 1) {
			return -1;
		}
		if (attId <= 0 || attId > numId) {
			System.out.println("NodeTablePointer#getId: numId=" + numId + " attId=" + attId);
			return -1;
		}

		retId = id[attId - 1];
		return retId;
	}

	/**
	 * �ʂ�NodeTablePointer�ɁA����NodeTablePointer�̑������R�s�[����
	 * @param tree Tree
	 * @param another �R�s�[���NodeTablePointer
	 */
	public void copy(Tree tree, NodeTablePointer another) {

		int numId = this.getNumId();
		if (another.getNumId() <= 0)
			another.setNumId(numId);

		for (int i = 1; i <= numId; i++) {
			another.setId(i, this.getId(i));
		}

	}

}
