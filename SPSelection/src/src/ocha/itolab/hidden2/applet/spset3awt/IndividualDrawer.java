package ocha.itolab.hidden2.applet.spset3awt;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import ocha.itolab.hidden2.core.data.IndividualSet;


public class IndividualDrawer {

	public static int PL = 1, SP = 2;
	private int dragMode;

	IndividualTransformer trans = null;
	IndividualSet ps;
	IndividualCanvas canvas;
	
	boolean isMousePressed = false/*, isAnnotation = true, isLigandFlag = true*/;
	int xposId = 0, yposId = 1;
	int drawType = SP;

	DrawerUtility du = null;
	
	double minmaxdiff[];
	ArrayList dimensionlist = null;

	IntBuffer view = IntBuffer.allocate(4);
	DoubleBuffer model = DoubleBuffer.allocate(16);
	DoubleBuffer proj = DoubleBuffer.allocate(16);
	DoubleBuffer objPos = DoubleBuffer.allocate(3);
	int windowX, windowY, windowWidth, windowHeight;
	
	IndividualSP dsp;
	
	public static int SAVE_AS_IS = 0;
	public static int SAVE_UPPER_LEFT = 1;
	public static int SAVE_UPPER_RIGHT = 2;
	public static int SAVE_LOWER_LEFT = 3;
	public static int SAVE_LOWER_RIGHT = 4;
	int savemode = SAVE_AS_IS;
	boolean saveImageFlag = false;
	
	
	public IndividualDrawer(int width, int height, IndividualCanvas c){
		canvas = c;
		windowWidth = width;
		windowHeight = height;
		du = new DrawerUtility(width, height);

	}

	public void setTransformer(IndividualTransformer view, DrawerUtility du) {
		this.trans = view;
		this.du = du;
		du.setTransformer(view);
		trans.setDrawerUtility(du);
		if(dsp == null)
			dsp  = new IndividualSP();
		dsp.setDrawerUtility(du);
	}

	public void setWindowSize(int width, int height) {
		windowWidth = width;
		windowHeight = height;
		du.setWindowSize(width, height);
	}
	
	public void setSaveImage(int mode) {
		savemode = mode;
		saveImageFlag = true;
	}
	

	IndividualSP getSP() {
		if(dsp == null)
			dsp  = new IndividualSP();
		return dsp;
	}

	public void setMousePressSwitch(boolean isMousePressed) {
		this.isMousePressed = isMousePressed;
	}


	public void setIndividualSet(IndividualSet p) {
		ps = p; 

		if(dsp == null)
			dsp  = new IndividualSP();
		dsp.setIndividualSet(ps);
		
	}


	public void setDrawType(int type) {
		drawType = type;
	}

	public void setNumClusters(int n) {
		dsp.setNumClusters(n);
	}

	public void mousePressed(int x, int y, int dragMode,int pressx,int pressy) {
		this.dragMode = dragMode;
	}

	public void mouseReleased(int x,int y,int dragMode,int releasex,int releasey) {
		this.dragMode = dragMode;
	}

	public void mouseDraged(int xStart, int xNow,int yStart, int yNow){
		int x = xNow - xStart;
		int y = yNow - yStart;
	}


	public void setValuePairList(ArrayList<int[]> list) {
		dsp.setValueIdSet(list);;
	}
	
	public void draw(Graphics2D g2) {

		if (ps == null) return;

		BufferedImage image = new BufferedImage(windowWidth*2, windowHeight*2, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D ig2 = image.createGraphics();
	    ig2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		ig2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

	    ig2.setPaint(Color.WHITE);
	    ig2.fillRect(0, 0, windowWidth*2, windowHeight*2);

	    double scaleX = trans.getViewScaleX() * (double)windowWidth / 300.0;
		double scaleY = trans.getViewScaleY() * (double)windowHeight / 300.0;
		double scale0 = (scaleX > scaleY) ? scaleX : scaleY;

	    if(drawType == SP)
			dsp.draw(ig2, scale0);
	    /*
		if(drawType == PL)
			dpl.draw();
		*/
	    
		g2.drawImage(image, 0, 0, null);
	}


	
	public Object pick(int x, int y) {
		if(dsp == null) return null;
		return dsp.pick(x, y);
	}

}