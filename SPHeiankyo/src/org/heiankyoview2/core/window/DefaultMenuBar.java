package org.heiankyoview2.core.window;

import java.awt.event.*;
import javax.swing.*;

import org.heiankyoview2.core.draw.Canvas;
import org.heiankyoview2.core.tree.Tree;


/*
 * HeianView のためのMenuBarを構築する
 * @author itot
 */
public class DefaultMenuBar extends JMenuBar implements MenuBar {

	/* var */
	/*
	 * Note for programmer: better avoid using 'public' access here, 
	 * rather preferred to use get*() methods with 'public' access.
	 */
	// file menu 
	public JMenu fileMenu;
	public JMenuItem openMenuItem;
	public JMenuItem optionMenuItem;
	public JMenuItem outputTemplateMenuItem;
	public JMenuItem readTemplateMenuItem;
	public JMenuItem writeTreeMenuItem;
	public JMenuItem exitMenuItem;

	// appearance menu 
	public JMenu appearanceMenu;
	public JMenuItem appearanceMenuItem;
	public JMenuItem tableAttributeMenuItem;

	// Listener
	MenuItemListener ml;
	FileOpener fileOpener = null;
	
	// component
	Canvas canvas = null;
	AppearancePanel appearancePanel = null;
	TablePanel tablePanel = null;
	NodeValuePanel nodeValuePanel = null;
	
	
	/**
	 * Constructor
	 * @param withReadyMadeMenu 通常はtrue
	 */
	public DefaultMenuBar(boolean withReadyMadeMenu) {
		super();
		if (withReadyMadeMenu) {
			buildFileMenu();
			buildAppearanceMenu();
		}
		
		ml = new MenuItemListener();
		this.addMenuListener(ml);
	}

	/**
	 * Constructor
	 */
	public DefaultMenuBar() {
		this(true);
	}

	/**
	 * Fileに関するメニューを構築する
	 */
	public void buildFileMenu() {

		// create file menu
		fileMenu = new JMenu("File");
		add(fileMenu);

		// add menu item
		openMenuItem = new JMenuItem("Open Tree...");
		fileMenu.add(openMenuItem);
		fileMenu.addSeparator();
		optionMenuItem = new JMenuItem("Option...");
		fileMenu.addSeparator();
		outputTemplateMenuItem = new JMenuItem("Output Template...");
		fileMenu.add(outputTemplateMenuItem);
		fileMenu.addSeparator();
		readTemplateMenuItem = new JMenuItem("Read Template...");
		fileMenu.add(readTemplateMenuItem);
		fileMenu.addSeparator();
		writeTreeMenuItem = new JMenuItem("Write Tree...");
		fileMenu.add(writeTreeMenuItem);
		fileMenu.addSeparator();
		exitMenuItem = new JMenuItem("Exit");
		fileMenu.add(exitMenuItem);
	}



	/**
	 * Appearance に関するメニューを構築する
	 */
	public void buildAppearanceMenu() {

		// create appearance menu
		appearanceMenu = new JMenu("Appearance");
		add(appearanceMenu);

		// add menu item
		appearanceMenuItem = new JMenuItem("Appearance...");
		appearanceMenu.add(appearanceMenuItem);
		// add menu item
		tableAttributeMenuItem = new JMenuItem("Table attribute...");
		appearanceMenu.add(tableAttributeMenuItem);
	}

	
	/**
	 * Canvas をセットする
	 */
	public void setCanvas(Canvas c) {
		canvas = c;;
	}
	
	
	/**
	 * FileOpener をセットする
	 */
	public void setFileOpener(FileOpener fo) {
		fileOpener = fo;
	}



	/**
	 * 選択されたメニューアイテムを返す
	 * @param name 選択されたメニュー名
	 * @return JMenuItem 選択されたメニューアイテム
	 */
	public JMenuItem getMenuItem(String name) {

		// file menu
		if (openMenuItem.getText().equals(name))
			return openMenuItem;
		if (optionMenuItem.getText().equals(name))
			return optionMenuItem;
		if (outputTemplateMenuItem.getText().equals(name))
			return outputTemplateMenuItem;
		if (readTemplateMenuItem.getText().equals(name))
			return readTemplateMenuItem;
		if (writeTreeMenuItem.getText().equals(name))
			return writeTreeMenuItem;
		if (exitMenuItem.getText().equals(name))
			return exitMenuItem;


		// appearance menu
		if (appearanceMenuItem.getText().equals(name))
			return appearanceMenuItem;
		if (tableAttributeMenuItem.getText().equals(name))
			return tableAttributeMenuItem;

		// other
		return null;
	}

	/**
	 * メニューに関するアクションの検知を設定する
	 * @param actionListener ActionListener
	 */
	public void addMenuListener(ActionListener actionListener) {

		// file menu
		openMenuItem.addActionListener(actionListener);
		optionMenuItem.addActionListener(actionListener);
		outputTemplateMenuItem.addActionListener(actionListener);
		readTemplateMenuItem.addActionListener(actionListener);
		writeTreeMenuItem.addActionListener(actionListener);
		exitMenuItem.addActionListener(actionListener);

		// appearance menu
		appearanceMenuItem.addActionListener(actionListener);
		tableAttributeMenuItem.addActionListener(actionListener);

	}
	
	/**
	 * メニューの各イベントを検出し、それに対応するコールバック処理を呼び出す
	 * 
	 * @author itot
	 */
	class MenuItemListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JMenuItem menuItem = (JMenuItem) e.getSource();

			if (menuItem == openMenuItem) {
				Tree tree = fileOpener.readTreeFile();
				canvas.setTree(tree);
				canvas.display();
			} else if (menuItem == outputTemplateMenuItem) {
				fileOpener.writeTemplateFile();
			} else if (menuItem == readTemplateMenuItem) {
				fileOpener.readTemplateFile();
			} else if (menuItem == writeTreeMenuItem) {
				fileOpener.writeTreeFile();
			} else if (menuItem == exitMenuItem) {
				System.exit(0);
			} else if (menuItem == appearanceMenuItem) {
				if (appearancePanel == null)
					appearancePanel = new AppearancePanel();
				appearancePanel.setVisible(true);
				appearancePanel.setCanvas(canvas);
			} else if (menuItem == tableAttributeMenuItem) {
				tablePanel = fileOpener.getTablePanel();
				if(tablePanel != null)
					tablePanel.setVisible(true);
			} 
		}
	}



}
