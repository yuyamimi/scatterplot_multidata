package org.heiankyoview2.core.draw; 

import org.heiankyoview2.core.tree.Tree;
import org.heiankyoview2.core.tree.Branch;
import org.heiankyoview2.core.tree.Node;


/**
 * �`��̈�ւ̕\�����e���o�b�t�@����N���X���������邽�߂̃C���^�t�F�[�X
 * @author itot
 */
public interface Buffer {

	/**
	 * Tree���Z�b�g����
	 * @param tree Tree
	 */
	public void setTree(Tree tree);

	/**
	 * Tree�̑S�̂�\�����邩�A����Node�̉��ʊK�w������\�����邩�A���Z�b�g����
	 * @param sw �\����؂�ւ��邽�߂̐��l�i0:�S�́A1:�s�b�N����Node�̉��ʊK�w�̂݁A2:���݂�1�K�w��܂Łj
	 */
	public void updatePartialDisplay(int sw);
				
	/**
	 * �O���t�̕\����̍�branch��Ԃ�
	 * @return �\����̍�Branch
	 */
	public Branch getRootDisplayBranch();
	
	/**
	 * �s�b�N���ꂽNode���Z�b�g����
	 * @param node �s�b�N���ꂽNode
	 */
	public void setPickedNode(Node node);
	
	/**
	 * �s�b�N���ꂽNode��Ԃ�
	 * @return �s�b�N���ꂽNode
	 */
	public Node getPickedNode();

	/**
	 * �eBranch�����ǂ�A�A�m�e�[�V�����̕\���ΏۂƂȂ�Node��ǉ�����
	 */
	public void addBranchAnnotations();
		
	/**
	 * �A�m�e�[�V�������X�g���ID����͂��āA����ɑΉ�����A�m�e�[�V�����̕������Ԃ�
	 * @param id �A�m�e�[�V�������X�g���ID
	 * @return �A�m�e�[�V�����̕�����
	 */
	public String getAnnotationName(int id);
	
	/**
	 * �A�m�e�[�V�������X�g���ID����͂��āA����ɑΉ�����A�m�e�[�V�����̍��W�l��Ԃ�
	 * @param id �A�m�e�[�V�������X�g���ID
	 * @return �A�m�e�[�V�����̍��W�l
	 */
	public double[] getAnnotationPosition(int id);
	
	/**
	 * �A�m�e�[�V�������X�g���ID����͂��āA����ɑΉ�����A�m�e�[�V�����̃T�C�Y��Ԃ�
	 * @param id �A�m�e�[�V�������X�g���ID
	 * @return �A�m�e�[�V�����̃T�C�Y
	 */
	public double getAnnotationSize(int id);
	
	/**
	 * �A�m�e�[�V�����̑�����Ԃ�
	 * @return �A�m�e�[�V�����̑���
	 */
	public int getNumAnnotation();
}
