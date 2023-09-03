package ocha.itolab.hidden2.applet.spset3;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.gl2.GLUgl2;
import com.jogamp.opengl.util.gl2.GLUT;

import ocha.itolab.hidden2.core.data.IndividualSet;


public class IndividualDrawer implements GLEventListener {

	public static int PL = 1, SP = 2;

	private GL gl;
	private GLUT glut;
	private GLU glu;
	private GL2 gl2;
	private GLUgl2 glu2;
	GLAutoDrawable glAD;

	private int dragMode;

	IndividualTransformer trans = null;
	IndividualSet ps;

	boolean isMousePressed = false/*, isAnnotation = true, isLigandFlag = true*/;
	int xposId = 0, yposId = 1;
	int drawType = SP;

	DrawerUtility du = null;
	GLCanvas glcanvas;

	double minmaxdiff[];
	ArrayList dimensionlist = null;

	IntBuffer view = IntBuffer.allocate(4);
	DoubleBuffer model = DoubleBuffer.allocate(16);
	DoubleBuffer proj = DoubleBuffer.allocate(16);
	DoubleBuffer objPos = DoubleBuffer.allocate(3);
	int windowX, windowY, windowWidth, windowHeight;
	
	IndividualSP dsp;
	IndividualPL dpl;

	public static int SAVE_AS_IS = 0;
	public static int SAVE_UPPER_LEFT = 1;
	public static int SAVE_UPPER_RIGHT = 2;
	public static int SAVE_LOWER_LEFT = 3;
	public static int SAVE_LOWER_RIGHT = 4;
	int savemode = SAVE_AS_IS;
	boolean saveImageFlag = false;
	
	
	
	public IndividualDrawer(int width, int height, GLCanvas glc){
		glcanvas = glc;
		windowWidth = width;
		windowHeight = height;
		du = new DrawerUtility(width, height);

		view = IntBuffer.allocate(4);
		model = DoubleBuffer.allocate(16);
		proj = DoubleBuffer.allocate(16);

		glcanvas.addGLEventListener(this);
	}

	public GLAutoDrawable getGLAutoDrawable(){
		return glAD;
	}



	public void setTransformer(IndividualTransformer view) {
		this.trans = view;
		du.setTransformer(view);
		trans.setDrawerUtility(du);
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
			dsp  = new IndividualSP(gl, gl2, glu, glu2, glut, glAD);

		return dsp;
	}

	public void setMousePressSwitch(boolean isMousePressed) {
		this.isMousePressed = isMousePressed;
	}


	public void setIndividualSet(IndividualSet p) {
		ps = p;

		if(dsp == null)
			dsp  = new IndividualSP(gl, gl2, glu, glu2, glut, glAD);

		dsp.setIndividualSet(ps);
	}


	public void setDrawType(int type) {
		drawType = type;
	}

	public void setNumClusters(int n) {
		dsp.setNumClusters(n);
	}

	public void init(GLAutoDrawable drawable) {
		gl = drawable.getGL();
		glut = new GLUT();
		glu = new GLU();
		gl2= drawable.getGL().getGL2();
		glu2 = new GLUgl2();
		this.glAD = drawable;

		gl.glEnable(GL.GL_RGBA);
		gl2.glEnable(GL2.GL_DEPTH);
		gl2.glEnable(GL2.GL_DOUBLE);
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl2.glEnable(GL2.GL_NORMALIZE);
		gl.glEnable(GL.GL_CULL_FACE);
		gl2.glDisable(GL2.GL_LIGHTING);

		gl.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);

		dsp  = new IndividualSP(gl, gl2, glu, glu2, glut, glAD);
	}


	public void reshape(GLAutoDrawable drawable,
			int x, int y,
			int width, int height) {

		
		windowX = x;
		windowY = y;
		windowWidth = width;
		windowHeight = height;
		float ratio = (float)height / (float)width;
		du = new DrawerUtility(width, height);
		trans.setDrawerUtility(du);
		
		
		gl.glViewport(0, 0, width, height);
		
		gl2.glMatrixMode(GL2.GL_PROJECTION);
		gl2.glLoadIdentity();
		//gl2.glFrustum(-1.0f, 1.0f, -ratio, ratio, 5.0f, 40.0f);
		gl2.glOrtho(-2.0, 2.0, -2.0, 2.0, 0.0, 100.0);

		gl2.glMatrixMode(GL2.GL_MODELVIEW);
		gl2.glLoadIdentity();
		gl2.glTranslatef(0.0f, 0.0f, -20.0f);
		
		
		gl.glGetIntegerv(GL.GL_VIEWPORT, view);
		gl2.glGetDoublev(GL2.GL_MODELVIEW_MATRIX, model);
		gl2.glGetDoublev(GL2.GL_PROJECTION_MATRIX, proj);

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


	public void display(GLAutoDrawable drawable) {

		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

		gl2.glLoadIdentity();
		glu.gluLookAt( 0.0, 0.0, 20.0,
		           0.0, 0.0, 0.0,
		           0.0, 1.0, 0.0 );

		double shiftX = trans.getViewShift(0);
		double shiftY = trans.getViewShift(1);
		double scaleX = trans.getViewScaleX() * (double)windowWidth / 300.0;
		double scaleY = trans.getViewScaleY() * (double)windowHeight / 300.0;
		double scale0 = (scaleX > scaleY) ? scaleX : scaleY;

			
		if(saveImageFlag == true) {
			int ww2 = windowWidth * 2;
			int wh2 = windowHeight * 2;
			if(savemode == SAVE_UPPER_LEFT)
				gl.glViewport(0, 0, ww2, wh2);
			if(savemode == SAVE_UPPER_RIGHT)
				gl.glViewport(-windowWidth, 0, ww2, wh2);
			if(savemode == SAVE_LOWER_LEFT)
				gl.glViewport(0, -windowHeight, ww2, wh2);
			if(savemode == SAVE_LOWER_RIGHT)
				gl.glViewport(-windowWidth, -windowHeight, ww2, wh2);
			scaleY *= 0.8;
			//System.out.println("   scaleX=" + scaleX + " scaleY=" + scaleY + " ww=" + windowWidth + " wh=" + windowHeight + " tX=" + trans.getViewScaleX() + " tY=" + trans.getViewScaleY());
			
		}
		
		
		gl2.glPushMatrix();

		gl2.glTranslated(shiftX, shiftY, 0.0);

		gl2.glScaled(scaleX, scaleY, 1.0);

		gl.glGetIntegerv(GL.GL_VIEWPORT, view);
		gl2.glGetDoublev(GL2.GL_MODELVIEW_MATRIX, model);
		gl2.glGetDoublev(GL2.GL_PROJECTION_MATRIX, proj);

		if(drawType == SP)
			dsp.setDrawArg(view, model, proj, scale0);
			dsp.draw();
		if(drawType == PL)
			dpl.draw(view, model, proj);

		gl2.glPopMatrix();
		

		if(saveImageFlag) {
			saveImage();
			saveImageFlag = false;
			gl.glViewport(0, 0, windowWidth, windowHeight);
		}

	}

	public Object pick(int x, int y) {
		if(dsp == null) return null;
		return dsp.pick(x, y);
	}

	
	void saveImage() {
		
		// RGBなら3, RGBAなら4
        int channelNum = 4;
        int allocSize = windowWidth * windowHeight * channelNum;
        ByteBuffer byteBuffer = ByteBuffer.allocate(allocSize);
        //gl2.glFlush();
        // 読み取るOpneGLのバッファを指定 GL_FRONT:フロントバッファ　GL_BACK:バックバッファ
        gl2.glReadBuffer( GL2.GL_BACK );
 
        // OpenGLで画面に描画されている内容をバッファに格納
        gl2.glReadPixels(0,             // 読み取る領域の左下隅のx座標
                0,                      // 読み取る領域の左下隅のy座標
                windowWidth,             // 読み取る領域の幅
                windowHeight,            // 読み取る領域の高さ
                GL2.GL_BGRA,            // 取得したい色情報の形式
                GL2.GL_UNSIGNED_BYTE,   // 読み取ったデータを保存する配列の型
                (Buffer) byteBuffer     // ビットマップのピクセルデータ（実際にはバイト配列）へのポインタ
        );
      
        // glReadBufferで取得したデータ(ByteBuffer)をDataBufferに変換する
        byte[] buff = byteBuffer.array();
    	BufferedImage imageBuffer =
				new BufferedImage(windowWidth, windowHeight, BufferedImage.TYPE_INT_RGB);
    	
    	for(int y = 0; y < windowHeight; y++){
    		for(int x = 0; x < windowWidth; x++){
    			
    			int offset = windowWidth * (windowHeight - y - 1) * channelNum;
    			// R
    			int rr = (int)buff[x * channelNum + offset + 2];
    			if(rr < 0) rr += 256;
    			// G
    			int gg = (int)buff[x * channelNum + offset + 1];
    			if(gg < 0) gg += 256;
    			// B
    			int bb = (int)buff[x * channelNum + offset + 0];
    			if(bb < 0) bb += 256;
    			
    			Color color = new Color(rr, gg, bb);
    			int value = color.getRGB();
    			imageBuffer.setRGB(x, y, value);
            }
        }
        
    	String filename = "tmp" + Integer.toString(savemode) + ".png";
        try {
            ImageIO.write(imageBuffer, "png", new File(filename));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub

	}


}