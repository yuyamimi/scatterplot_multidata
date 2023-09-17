package org.heiankyoview2.core.draw; 

import org.heiankyoview2.core.tree.Tree;
import org.heiankyoview2.core.table.TreeTable;

import java.awt.*;


/**
 * HeianView �̂��߂̕`��̈���Ǘ�����
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
	 * @param width ��ʂ̕�
	 * @param height ��ʂ̍���
	 * @param foregroundColor ��ʂ̑O�ʐF 
	 * @param backgroundColor ��ʂ̔w�i�F
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
	 * @param width ��ʂ̕�
	 * @param height ��ʂ̍���
	 */
	public DefaultCanvas(int width, int height) {
		this(width, height, Color.white, Color.black);
	}


}
