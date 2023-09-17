package ocha.itolab.hidden2.applet.spset4awt;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.EventListener;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import ocha.itolab.hidden2.core.data.IndividualSet;
import ocha.itolab.hidden2.core.tool.*;
import org.heiankyoview2.core.tree.*;

public class IndividualCanvas extends JPanel {

	/* var */
	IndividualTransformer trans;
	IndividualDrawer drawer;
	DrawerUtility du;
	BufferedImage image = null;
	IndividualSet ps;

	boolean isMousePressed = false, isAnnotation = true;
	int dragMode, segmentMode, gridMode = 1;
	int width, height, mouseX, mouseY;
	double linewidth = 1.0, bgR = 0.0, bgG = 0.0, bgB = 0.0;

	int pressx,pressy,releasex,releasey;


	public IndividualCanvas(
		int width,
		int height,
		Color foregroundColor,
		Color backgroundColor) {

		this.width = width;
		this.height = height;
		setSize(width, height);
		setColors(foregroundColor, backgroundColor);
		dragMode = 1;

		drawer = new IndividualDrawer(width, height, this);
		trans = new IndividualTransformer();
		du = new DrawerUtility(width, height);
		trans.viewReset();
		drawer.setTransformer(trans, du);
	}


	public IndividualCanvas(int width, int height) {
		this(width, height, Color.white, Color.white);
	}


	public void setDrawer(IndividualDrawer d) {
		drawer = d;
	}

	public void setIndividualSet(IndividualSet p) {
		ps = p;
		drawer.setIndividualSet(p);
	}

	public IndividualSet getIndividualSet() {
		return ps;
	}


	public void setNumClasses(int n) {
		drawer.setNumClasses(n);
	}


	public void setTransparency(double t) {
		//drawer.setTransparency(t);
	}


	public void unlockDisplay() {
		//drawer.unlockDisplay();
	}

	public Object pick(int x, int y) {
		return drawer.pick(x, y);
	}


	public void setNumSP(int num) {
		ArrayList<int[]> list = DimensionDistance.getList(num);
		drawer.setValuePairList(list);
		
		Tree tree = TreeConstructor.generate(list);
		drawer.setTree(tree);
	}


	public void display() {

		Graphics g = getGraphics();
		if (g == null)
			return;

		if (drawer != null) {
			width = (int) getSize().getWidth();
			height = (int) getSize().getHeight();
			drawer.setWindowSize(width, height);
		}

		paintComponent(g);
	}

	
	public void paintComponent(Graphics g) {
		super.paintComponent(g); // clear the background

		Graphics2D g2 = (Graphics2D) g;
		drawer.draw(g2);
	}

	public void saveImageFile(int saveflag) {
		drawer.setSaveImage(saveflag);
	}


	public void setColors(Color foregroundColor, Color backgroundColor) {
		setForeground(foregroundColor);
		setBackground(backgroundColor);
	}


	public void mousePressed(int x, int y) {
		pressx = x;
		pressy = y;
		isMousePressed = true;
		trans.mousePressed();
		drawer.mousePressed(x, y, dragMode, pressx, pressy);
		drawer.setMousePressSwitch(isMousePressed);
	}


	public void mouseReleased(int x,int y) {

		releasex = x;
		releasey = y;
		drawer.mouseReleased(x, y, dragMode, releasex, releasey);

		isMousePressed = false;
		drawer.setMousePressSwitch(isMousePressed);


	}


	public void drag(int xStart, int xNow, int yStart, int yNow) {
		int x = xNow - xStart;
		int y = yNow - yStart;
		if(dragMode >= 1)
			trans.drag(x, y, width, height, dragMode);
		else
			drawer.mouseDraged(xStart, xNow, yStart, yNow);
		display();
	}


	public void setBackground(double r, double g, double b) {
		bgR = r;
		bgG = g;
		bgB = b;
		setBackground(
			new Color((int) (r * 255), (int) (g * 255), (int) (b * 255)));
	}


	public void setDragMode(int newMode) {
		dragMode = newMode;
	}


	public void viewReset() {
		trans.viewReset();
	}


	public void addCursorListener(EventListener eventListener) {
		addMouseListener((MouseListener) eventListener);
		addMouseMotionListener((MouseMotionListener) eventListener);
	}
}
