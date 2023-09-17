package org.heiankyoview2.core.window;

import org.heiankyoview2.core.tree.Tree;


/**
 * Table�ɋL�q���ꂽ������p���ĕ\�������i���O�A�F�A�����j��ݒ肷�邽�߂̃p�l��
 * @author itot
 */
public interface TablePanel {


	/**
	 * Tree���Z�b�g����
	 * @param tree Tree
	 */
	public void setTree(Tree tree); 

	/**
	 * Canvas���Z�b�g����
	 * @param c Canvas
	 */
	public void setCanvas(Object c); 

	
	/**
	 * �������Z�b�g����
	 * @param flag
	 */
	public void setVisible(boolean flag); 

}
