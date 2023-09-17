package org.heiankyoview2.core.draw; 

import org.heiankyoview2.core.tree.Tree;
import org.heiankyoview2.core.table.TreeTable;

import java.awt.*;


/**
 * HeianView �̂��߂̕`��̈���Ǘ�����
 * @author itot
 */
public class PileCanvas extends MinimumCanvas {

	/* var */
	Tree tree;
	TreeTable tg;
	Transformer view;
	PileDrawer drawer;
	DefaultBuffer dbuf;
	ViewFile vfile = null;



	/**
	 * Constructor
	 * @param width ��ʂ̕�
	 * @param height ��ʂ̍���
	 * @param foregroundColor ��ʂ̑O�ʐF 
	 * @param backgroundColor ��ʂ̔w�i�F
	 */
	public PileCanvas(
		int width,
		int height,
		Color foregroundColor,
		Color backgroundColor) {

		super(width, height, foregroundColor, backgroundColor);

		drawer = new PileDrawer(width, height, this);
		view = new Transformer();
		dbuf = new DefaultBuffer();
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
	public PileCanvas(int width, int height) {
		this(width, height, Color.white, Color.black);
	}

	/**
	 * �ǂ̃e�[�u���̐��l��ςݏd�˂邩�A���w�肷��
	 */
	public void setPileId(int p[]) {
		drawer.setPileId(p);
	}

}
