
package org.heiankyoview2.core.window;

import java.awt.*;

import org.heiankyoview2.core.tree.Tree;
import org.heiankyoview2.core.draw.Canvas;

public interface FileOpener {


	/**
	 * Container ���Z�b�g����
	 * @param c Component
	 */
	public void setContainer(Component c);
	
	/**
	 * Canvas ���Z�b�g����
	 * @param c Canvas
	 */
	public void setCanvas(Canvas c);
	
	/**
	 * Tree ��Ԃ�
	 * @return tree
	 */
	public Tree getTree(); 
	

	/*
	 * tree�t�@�C����ǂݍ���
	 */
	public Tree readTreeFile(); 
	
	/*
	 * frame�t�@�C����ǂݍ���
	 */
	public void readFrameFile(); 
	
	/*
	 * �e���v���[�g�t�@�C������������
	 */
	public void writeTemplateFile();
	
	/*
	 * �e���v���[�g�t�@�C����ǂݍ���
	 */
	public void readTemplateFile(); 
	
	/*
	 * tree�t�@�C������������
	 */
	public void writeTreeFile(); 
	
	/*
	 * �摜�t�@�C������������
	 */
	public void saveImageFile();
	
	/*
	 * TableAttributePanel ���Q�b�g����
	 */
	public TablePanel getTablePanel(); 
	
	/*
	 * NodeValuePanel ���Q�b�g����
	 */
	public NodeValuePanel getNodeValuePanel();
	
}
