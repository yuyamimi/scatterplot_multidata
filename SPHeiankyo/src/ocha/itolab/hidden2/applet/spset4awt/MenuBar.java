package ocha.itolab.hidden2.applet.spset4awt;

import java.awt.event.*;
import javax.swing.*;
import ocha.itolab.hidden2.core.data.*;
import ocha.itolab.hidden2.core.tool.DataSetDivider;


/*
 * HeianView �̂��߂�MenuBar���\�z����
 * @author itot
 */
public class MenuBar extends JMenuBar {

	/* var */
	// file menu 
	public JMenuItem openMenuItem, exitMenuItem, helpMenuItem;

	// help menu 
	public JMenu fileMenu, helpMenu;

	// Listener
	MenuItemListener ml;
	
	// component
	IndividualCanvas icanvas = null;
	IndividualSelectionPanel  iSelection = null;

	/**
	 * Constructor
	 * @param withReadyMadeMenu �ʏ��true
	 */
	public MenuBar(boolean withReadyMadeMenu) {
		super();
		if (withReadyMadeMenu) {
			buildFileMenu();
			buildHelpMenu();
		}
		
		ml = new MenuItemListener();
		this.addMenuListener(ml);
	}

	/**
	 * Constructor
	 */
	public MenuBar() {
		this(true);
	}

	
	/**
	 * Panel���Z�b�g����
	 */
	public void setIndividualSelectionPanel(IndividualSelectionPanel p) {
		iSelection = p;
	}
	
	 
	/**
	 * File�Ɋւ��郁�j���[���\�z����
	 */
	public void buildFileMenu() {

		// create file menu
		fileMenu = new JMenu("File");
		add(fileMenu);

		// add menu item
		openMenuItem = new JMenuItem("Open");
		fileMenu.add(openMenuItem);
		exitMenuItem = new JMenuItem("Exit");
		fileMenu.add(exitMenuItem);
	}



	/**
	 * Appearance �Ɋւ��郁�j���[���\�z����
	 */
	public void buildHelpMenu() {

		// create help menu
		helpMenu = new JMenu("Help");
		add(helpMenu);

		// add menu item
		helpMenuItem = new JMenuItem("Help...");
		helpMenu.add(helpMenuItem);

	}

	
	/**
	 * Canvas ���Z�b�g����
	 */
	public void setIndividualCanvas(IndividualCanvas c) {
		icanvas = c;
	}
	
	

	/**
	 * �I�����ꂽ���j���[�A�C�e����Ԃ�
	 * @param name �I�����ꂽ���j���[��
	 * @return JMenuItem �I�����ꂽ���j���[�A�C�e��
	 */
	public JMenuItem getMenuItem(String name) {

		if (openMenuItem.getText().equals(name))
			return openMenuItem;
		if (exitMenuItem.getText().equals(name))
			return exitMenuItem;
		if (helpMenuItem.getText().equals(name))
			return helpMenuItem;

		// other
		return null;
	}

	/**
	 * ���j���[�Ɋւ���A�N�V�����̌��m��ݒ肷��
	 * @param actionListener ActionListener
	 */
	public void addMenuListener(ActionListener actionListener) {
		openMenuItem.addActionListener(actionListener);
		exitMenuItem.addActionListener(actionListener);
		helpMenuItem.addActionListener(actionListener);
	}
	
	/**
	 * ���j���[�̊e�C�x���g�����o���A����ɑΉ�����R�[���o�b�N�������Ăяo��
	 * 
	 * @author itot
	 */
	class MenuItemListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JMenuItem menuItem = (JMenuItem) e.getSource();
			if(openMenuItem == menuItem) {
				FileOpener fileOpener = new FileOpener();
				IndividualSet ps = fileOpener.readFile();
				icanvas.setIndividualSet(ps);
				icanvas.display();
			}
			if(exitMenuItem == menuItem) 
				System.exit(0);
		}
	}



}
