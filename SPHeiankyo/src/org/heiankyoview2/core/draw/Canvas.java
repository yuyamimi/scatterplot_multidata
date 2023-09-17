package org.heiankyoview2.core.draw;

import org.heiankyoview2.core.tree.Tree;
import org.heiankyoview2.core.tree.Node;
import java.io.File;

/**
 * �`��̈���Ǘ�����N���X�̂��߂̃C���^�t�F�[�X
 * @author itot
 */
public interface Canvas  {

	/**
	 * Tree���Z�b�g����
	 * @param tree Tree
	 */
	public void setTree(Tree tree);
	
	/**
	 * Tree���Q�b�g����
	 * @return Tree
	 */
	public Tree getTree();
	
	/**
	 * �����A�c����Ԃ�
	 */
	public int getWidth();
	public int getHeight();
	
	/**
	 * �ĕ`��
	 */
	public void display();
	
	/**
	 * ���̑������Z�b�g����
	 * @param linewidth ���̑����i��f���j
	 */
	public void setLinewidth(double linewidth);

	/**
	 * �w�i�F��r,g,b��3�l�Őݒ肷��
	 * @param r �ԁi0�`1�j
	 * @param g �΁i0�`1�j
	 * @param b �i0�`1�j
	 */
	public void setBackground(double r, double g, double b);
	
	/**
	 * �A�m�e�[�V�����\����ON/OFF����
	 * @param flag �\������Ȃ�true, �\�����Ȃ��Ȃ�false
	 */
	public void setAnnotationSwitch(boolean flag);
	
	/**
	 * �A�m�e�[�V������ݒ肷��
	 */
	public void setBranchAnnotations();
	
	/**
	 * �s�b�N���ꂽ�m�[�h����肷��
	 * @return �s�b�N���ꂽNode
	 */
	public Node getPickedNode();
	
	/**
	 * �}�E�X�h���b�O�̃��[�h��ݒ肷��
	 * @param dragMode (1:ZOOM  2:SHIFT  3:ROTATE)
	 */
	public void setDragMode(int newMode);

	/**
	 * �}�E�X�h���b�O�̃��[�h�𓾂�
	 * @return dragMode (1:ZOOM  2:SHIFT  3:ROTATE)
	 */
	public int getDragMode();
	
	/**
	 * ��ʕ\���̊g��k���E��]�E���s�ړ��̊e��Ԃ����Z�b�g����
	 */
	public void viewReset();
	
	/**
	 * �}�E�X�{�^���������ꂽ���[�h��ݒ肷��
	 */
	public void mousePressed(int x, int y);
	
	/**
	 * �}�E�X�{�^���������ꂽ���[�h��ݒ肷��
	 */
	public void mouseReleased();
	
	/**
	 * �摜�t�@�C�����o�͂���
	 */
	public void saveImageFile(File file);
	
	/**
	 * �}�E�X���h���b�O���ꂽ���[�h��ݒ肷��
	 * @param xStart ���O��X���W�l
	 * @param xNow ���݂�X���W�l
	 * @param yStart ���O��Y���W�l
	 * @param yNow ���݂�Y���W�l
	 */
	public void drag(int xStart, int xNow, int yStart, int yNow);
	
	/**
	 * ��ʏ�̓��蕨�̂��s�b�N����
	 * @param px �s�b�N�������̂̉�ʏ��X���W�l
	 * @param py �s�b�N�������̂̉�ʏ��Y���W�l
	 */
	public void pickObjects(int px, int py);
}
