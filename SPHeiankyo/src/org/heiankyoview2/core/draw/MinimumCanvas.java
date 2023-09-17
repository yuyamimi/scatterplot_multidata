package org.heiankyoview2.core.draw;

import org.heiankyoview2.core.tree.Tree;
import org.heiankyoview2.core.tree.Node;
import org.heiankyoview2.core.table.TreeTable;

import java.util.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * HeianView �̂��߂̕`��̈���Ǘ�����
 * @author itot
 */
public class MinimumCanvas extends JPanel implements Canvas {

	/* var */
	Tree tree;
	TreeTable tg;
	Transformer view;
	Drawer drawer;
	Buffer dbuf;
	ViewFile vfile = null;
	BufferedImage image = null;
	
	
	boolean isMousePressed = false, isAnnotation = true;
	int dragMode;
	int width, height, mouseX, mouseY;
	double linewidth = 1.0, bgR = 0.0, bgG = 0.0, bgB = 0.0;


	/**
	 * Constructor
	 * @param width ��ʂ̕�
	 * @param height ��ʂ̍���
	 * @param foregroundColor ��ʂ̑O�ʐF
	 * @param backgroundColor ��ʂ̔w�i�F
	 */
	public MinimumCanvas(
		int width,
		int height,
		Color foregroundColor,
		Color backgroundColor) {

		super(true);
		this.width = width;
		this.height = height;
		setSize(width, height);
		setColors(foregroundColor, backgroundColor);
		dragMode = 2; // SHIFT

		/*
		drawer = new DefaultDrawer(width, height);
		view = new View();
		dbuf = new DefaultBuffer();
		drawer.setView(view);
		drawer.setBuffer(dbuf);
		*/
	}

	/**
	 * Constructor
	 * @param width ��ʂ̕�
	 * @param height ��ʂ̍���
	 */
	public MinimumCanvas(int width, int height) {
		this(width, height, Color.white, Color.black);
	}


	/**
	 * Drawer ���Z�b�g����
	 * @param d Drawer
	 */
	public void setDrawer(Drawer d) {
		drawer = d;
	}

	/**
	 * Buffer ���Z�b�g����
	 * @param b Buffer
	 */
	public void setBuffer(Buffer b) {
		dbuf = b;
	}
	
	/**
	 * View ���Z�b�g����
	 * @param v View
	 */
	public void setTransformer(Transformer v) {
		view = v;
	}


	/**
	 * Tree���Z�b�g����
	 * @param tree Tree
	 */
	public void setTree(Tree tree) {
		this.tree = tree;
		if(tree == null) return;
		tg = tree.table;
		dbuf.setTree(tree);
		view.setTree(tree);
		
		//
		// CAUTION !! this must be called AFTER dbuf.setTree()
		//
		drawer.setTree(tree);
	}

	/**
	 * Tree��Ԃ�
	 * @return Tree
	 */
	public Tree getTree() {
		return tree;
	}

	/**
	 * ������Ԃ�
	 */
	public int getWidth() {
		return (int)getSize().getWidth();
	}
	
	/**
	 * �c����Ԃ�
	 */
	public int getHeight() {
		return (int)getSize().getHeight();
	}
	
	
	/**
	 * �`������s����
	 * @param g Graphics
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g); // clear the background

		if (tree == null)
			return;

		Graphics2D g2 = (Graphics2D) g;
		drawer.draw(g2);
	}
	
	/**
	 * �ĕ`��
	 */
	public void repaint() {
		display();
	}
	
	/**
	 * �ĕ`��
	 */
	public void display() {

		Graphics g = getGraphics();
		if (g == null)
			return;

		if (drawer != null) {
			width = (int) getSize().getWidth();
			height = (int) getSize().getHeight();
			drawer.setWindowSize(width, height);
		}

		if (isMousePressed == false && dragMode == 3)
			drawer.createSortedNodeArray();

		paintComponent(g);
	}

	/**
	 * �摜�t�@�C���ɏo�͂���
	 */
	public void saveImageFile(File file) {

		width = (int) getSize().getWidth();
		height = (int) getSize().getHeight();
		image = new BufferedImage(width, height, 
                BufferedImage.TYPE_INT_BGR);
		
		Graphics2D gg2 = image.createGraphics();
		gg2.clearRect(0, 0, width, height);
		drawer.draw(gg2);
		try {
			ImageIO.write(image, "bmp", file);
		} catch(Exception e) {
			e.printStackTrace();
		}		
	}
	
	
	/**
	 * �O�ʐF�Ɣw�i�F���Z�b�g����
	 * @param foregroundColor �O�ʐF
	 * @param backgroundColor �w�i�F
	 */
	public void setColors(Color foregroundColor, Color backgroundColor) {
		setForeground(foregroundColor);
		setBackground(backgroundColor);
	}


	/**
	 * �}�E�X�{�^���������ꂽ���[�h��ݒ肷��
	 */
	public void mousePressed(int x, int y) {
		isMousePressed = true;
		view.mousePressed(dragMode, x, y);
		drawer.setMousePressSwitch(isMousePressed);
	}

	/**
	 * �}�E�X�{�^���������ꂽ���[�h��ݒ肷��
	 */
	public void mouseReleased() {
		isMousePressed = false;
		drawer.setMousePressSwitch(isMousePressed);
	}

	/**
	 * �}�E�X���h���b�O���ꂽ���[�h��ݒ肷��
	 * @param xStart ���O��X���W�l
	 * @param xNow ���݂�X���W�l
	 * @param yStart ���O��Y���W�l
	 * @param yNow ���݂�Y���W�l
	 */
	public void drag(int xStart, int xNow, int yStart, int yNow) {
		int x = xNow - xStart;
		int y = yNow - yStart;

		view.drag(x, y, width, height, dragMode);
	}

	/**
	 * Tree�̕����\����ݒ肷��
	 * @param sw �X�C�b�`�ƂȂ鐮���l
	 */
	public void setPartialDisplay(int sw) {
		if (tree == null)
			return;

		dbuf.updatePartialDisplay(sw);
		this.setBranchAnnotations();
		drawer.createSortedNodeArray();
		repaint();
	}

	/**
	 * ���̑������Z�b�g����
	 * @param linewidth ���̑����i��f���j
	 */
	public void setLinewidth(double linewidth) {
		this.linewidth = linewidth;
		drawer.setLinewidth(linewidth);
	}


	/**
	 * �w�i�F��r,g,b��3�l�Őݒ肷��
	 * @param r �ԁi0�`1�j
	 * @param g �΁i0�`1�j
	 * @param b �i0�`1�j
	 */
	public void setBackground(double r, double g, double b) {
		bgR = r;
		bgG = g;
		bgB = b;
		setBackground(
			new Color((int) (r * 255), (int) (g * 255), (int) (b * 255)));
	}

	/**
	 * �A�m�e�[�V�����\����ON/OFF����
	 * @param flag �\������Ȃ�true, �\�����Ȃ��Ȃ�false
	 */
	public void setAnnotationSwitch(boolean flag) {
		isAnnotation = flag;
		drawer.setAnnotationSwitch(flag);
	}

	/**
	 * �}�E�X�h���b�O�̃��[�h��ݒ肷��
	 * @param dragMode (1:ZOOM  2:SHIFT  3:ROTATE)
	 */
	public void setDragMode(int newMode) {
		dragMode = newMode;
	}

	/**
	 * �}�E�X�h���b�O�̃��[�h�𓾂�
	 * @return dragMode (1:ZOOM  2:SHIFT  3:ROTATE)
	 */
	public int getDragMode() {
		return dragMode;
	}
	
	/**
	 * ��ʕ\���̊g��k���E��]�E���s�ړ��̊e��Ԃ����Z�b�g����
	 */
	public void viewReset() {
		view.viewReset();
	}

	/**
	 * ��ʏ�̓��蕨�̂��s�b�N����
	 * @param px �s�b�N�������̂̉�ʏ��X���W�l
	 * @param py �s�b�N�������̂̉�ʏ��Y���W�l
	 */
	public void pickObjects(int px, int py) {
		drawer.pickObjects(px, py);
	}

	/**
	 * �A�m�e�[�V������ݒ肷��
	 */
	public void setBranchAnnotations() {
		if (dbuf != null)
			dbuf.addBranchAnnotations();
	}

	/**
	 * �s�b�N���ꂽ�m�[�h���O���w�肷��
	 * @return �s�b�N���ꂽNode
	 */
	public void setPickedNode(Node node) {
		if (dbuf != null)
			dbuf.setPickedNode(node);
	}

	/**
	 * �s�b�N���ꂽ�m�[�h����肷��
	 * @return �s�b�N���ꂽNode
	 */
	public Node getPickedNode() {
		if (dbuf != null) {
			Node node = dbuf.getPickedNode();
			return node;
		}
		return null;
	}
	
	/**
	 * �g��k���E��]�E���s�ړ��Ɋւ���p�����[�^���t�@�C���ɕۑ�����
	 */
	public void saveViewingFile() {

		boolean ret;

		ret = vfile.saveStart();
		if (ret == false)
			return;

		vfile.setAnnotationSwitch(isAnnotation);
		vfile.setLinewidth(linewidth);
		vfile.setBackground(bgR, bgG, bgB);

		vfile.saveEnd();
	}

	
	/**
	 * �g��k���E��]�E���s�ړ��Ɋւ���p�����[�^���t�@�C������ǂݍ���
	 */
	public void openViewingFile() {

		boolean ret;

		ret = vfile.openStart();
		if (ret == false)
			return;

		isAnnotation = vfile.getAnnotationSwitch();
		linewidth = vfile.getLinewidth();
		bgR = vfile.getBackgroundR();
		bgG = vfile.getBackgroundG();
		bgB = vfile.getBackgroundB();

		vfile.openEnd();

	}

	/**
	 * �}�E�X�J�[�\���̃C�x���g�����m����ݒ���s��
	 * @param eventListener EventListner
	 */
	public void addCursorListener(EventListener eventListener) {
		addMouseListener((MouseListener) eventListener);
		addMouseMotionListener((MouseMotionListener) eventListener);
		addMouseWheelListener((MouseWheelListener) eventListener);
	}
}
