package org.heiankyoview2.core.draw; 

import org.heiankyoview2.core.tree.Tree;
import org.heiankyoview2.core.table.TreeTable;

import java.awt.*;


/**
 * HeianView のための描画領域を管理する
 * @author itot
 */
public class DefaultCanvas extends MinimumCanvas {

	/* var */
	Tree tree;
	TreeTable tg;
	Transformer view;
	DefaultDrawer drawer;
	DefaultBuffer dbuf;
	ViewFile vfile = null;



	/**
	 * Constructor
	 * @param width 画面の幅
	 * @param height 画面の高さ
	 * @param foregroundColor 画面の前面色 
	 * @param backgroundColor 画面の背景色
	 */
	public DefaultCanvas(
		int width,
		int height,
		Color foregroundColor,
		Color backgroundColor) {

		super(width, height, foregroundColor, backgroundColor);

		DefaultDrawer drawer = new DefaultDrawer(width, height, this);
		Transformer view = new Transformer();
		DefaultBuffer dbuf = new DefaultBuffer();
		drawer.setTransformer(view);
		drawer.setBuffer(dbuf);

		setDrawer((Drawer)drawer);
		setBuffer((Buffer)dbuf);
		setTransformer((Transformer)view);

		view.setDefaultValue();
	}

	/**
	 * Constructor
	 * @param width 画面の幅
	 * @param height 画面の高さ
	 */
	public DefaultCanvas(int width, int height) {
		this(width, height, Color.white, Color.black);
	}


}
