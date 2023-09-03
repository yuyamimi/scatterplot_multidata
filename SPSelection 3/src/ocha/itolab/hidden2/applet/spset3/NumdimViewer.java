package ocha.itolab.hidden2.applet.spset3;


import java.awt.*;
import javax.swing.*;
import java.util.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.Animator;

public class NumdimViewer extends JApplet {

	// GUI element
	MenuBar menuBar;
	IndividualSelectionPanel  iSelection = null; 
	CursorListener cl;
	IndividualCanvas icanvas;
	Container windowContainer ;

	
	
	/**
	 * applet を初期化し、各種データ構造を初期化する
	 */
	public void init() {
		setSize(new Dimension(800,500));
		buildGUI();
	}

	/**
	 * applet の各イベントの受付をスタートする
	 */
	public void start() {
	}

	/**
	 * applet の各イベントの受付をストップする
	 */
	public void stop() {
	}

	/**
	 * applet等を初期化する
	 */
	private void buildGUI() {

		// Canvas
		icanvas = new IndividualCanvas(750, 750);
		icanvas.requestFocus();
		GLCanvas glci = icanvas.getGLCanvas();
	
		// ViewingPanel
		iSelection = new IndividualSelectionPanel();
		iSelection.setIndividualCanvas(icanvas);
					// MenuBar
		menuBar = new MenuBar();
		menuBar.setIndividualCanvas(icanvas);
		menuBar.setIndividualSelectionPanel(iSelection);
		
		// CursorListener
		cl = new CursorListener();
		cl.setIndividualCanvas(icanvas, glci);
		cl.setSelectionPanel(iSelection);
		icanvas.addCursorListener(cl);
		
		// CanvasとViewingPanelのレイアウト
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		JPanel p1 = new JPanel();
		p1.setLayout(new GridLayout(1,1));
		p1.add(glci);
		mainPanel.add(p1, BorderLayout.CENTER);
		mainPanel.add(iSelection, BorderLayout.WEST);
		
		// ウィンドウ上のレイアウト
		windowContainer = this.getContentPane();
		windowContainer.setLayout(new BorderLayout());
		windowContainer.add(mainPanel, BorderLayout.CENTER);
		windowContainer.add(menuBar, BorderLayout.NORTH);

	}

	/**
	 * main関数
	 * @param args 実行時の引数
	 */
	public static void main(String[] args) {
		ocha.itolab.hidden2.applet.Window window =
			new ocha.itolab.hidden2.applet.Window(
				"HiddenViewer",800, 600, Color.lightGray); //Windowを作成
		NumdimViewer nv = new NumdimViewer(); //システムを起動

		nv.init(); //システム初期化
		window.getContentPane().add(nv); //windowにシステムを渡す
		window.setVisible(true); //??

		nv.start(); //システムを扱えるようにしている
	}

}
