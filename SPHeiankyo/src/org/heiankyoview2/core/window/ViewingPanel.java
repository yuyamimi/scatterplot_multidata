/*
 * Created on 2006/03/26
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.heiankyoview2.core.window;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import org.heiankyoview2.core.draw.Canvas;
import org.heiankyoview2.core.tree.Tree;


public interface ViewingPanel {
	
	public final int DOUBLE_CLICK_PANEL = 1;
	public final int DOUBLE_CLICK_BROWSER = 2;
	
	/**
	 * Canvas���Z�b�g����
	 * @param c Canvas
	 */
	public void setCanvas(Object c); 
	
	/**
	 * FileOpener ���Z�b�g����
	 */
	public void setFileOpener(FileOpener fo); 

	
	/**
	 * �_�u���N���b�N���̓�����w�肷��t���O��Ԃ�
	 * @return doubleClickFlag
	 */
	public int getDoubleClickFlag();
	
	/**
	 * Cursor Sensor �� ON/OFF ���w�肷��t���O��Ԃ�
	 * @return cursorSensorFlag
	 */
	public boolean getCursorSensorFlag();
	
	
	/**
	 * ���W�I�{�^���̃A�N�V�����̌��o��ݒ肷��
	 * @param actionListener ActionListener
	 */
	public void addRadioButtonListener(ActionListener actionListener);

	/**
	 * �{�^���̃A�N�V�����̌��o��ݒ肷��
	 * @param actionListener ActionListener
	 */
	public void addButtonListener(ActionListener actionListener);

}
