package org.heiankyoview2.core.window;

import java.awt.event.*;
import javax.swing.*;

/**
 * MenuBar���\�z����N���X�̂��߂̃C���^�t�F�[�X
 * @author itot
 */
public interface MenuBar {


	/**
	 * �I�����ꂽ���j���[�A�C�e����Ԃ�
	 * @param name �I�����ꂽ���j���[��
	 * @return JMenuItem �I�����ꂽ���j���[�A�C�e��
	 */
	public JMenuItem getMenuItem(String name);
		
	/**
	 * ���j���[�Ɋւ���A�N�V�����̌��m��ݒ肷��
	 * @param actionListener ActionListener
	 */
	public void addMenuListener(ActionListener actionListener);

}
