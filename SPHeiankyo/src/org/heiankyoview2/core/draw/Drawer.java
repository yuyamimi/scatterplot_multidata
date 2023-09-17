package org.heiankyoview2.core.draw;

import org.heiankyoview2.core.tree.Tree;

import java.awt.*;

/**
 * �`�揈������������N���X�̂��߂̃C���^�t�F�[�X
 * @author itot
 */
public interface Drawer {

	/**
	 * Tree���Z�b�g����
	 * @param tree Tree
	 */
	public void setTree(Tree tree);

	/**
	 * Transformer���Z�b�g����
	 * @param transformer 
	 */
	public void setTransformer(Transformer view);

	/**
	 * Buffer ���Z�b�g����
	 * @param dbuf Buffer
	 */
	public void setBuffer(Buffer dbuf);
	
	/**
	 * �`��̈�̃T�C�Y��ݒ肷��
	 * @param width �`��̈�̕�
	 * @param height �`��̈�̍���
	 */
	public void setWindowSize(int width, int height);

	/**
	 * �}�E�X�{�^����ON/OFF��ݒ肷��
	 * @param isMousePressed �}�E�X�{�^����������Ă����true
	 */
	public void setMousePressSwitch(boolean isMousePressed);

	/**
	 * ���̑������Z�b�g����
	 * @param lw ���̑����i��f���j
	 */
	public void setLinewidth(double lw);

	/**
	 * �A�m�e�[�V�����\����ON/OFF����
	 * @param flag �\������Ȃ�true, �\�����Ȃ��Ȃ�false
	 */
	public void setAnnotationSwitch(boolean flag);
		
								
	/**
	 * �`������s����
	 * @param g2 Graphics2D
	 */
	public void draw(Graphics2D g2);

	/**
	 * ���̂��s�b�N����
	 * @param px �s�b�N�������̂̉�ʏ��x���W�l
	 * @param py �s�b�N�������̂̉�ʏ�̍��W�l
	 */
	public void pickObjects(int px, int py);
		
	/**
	 * ���_����̐[���Ń\�[�g����Node�z��𐶐����� �i���s���\�[�g�@�ɂ��`��̂��߁j
	 */
	public void createSortedNodeArray();
}