package ocha.itolab.hidden2.applet.spset3awt;


import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

//import com.jogamp.opengl.awt.GLCanvas;

import ocha.itolab.hidden2.core.data.OneIndividual;
import ocha.itolab.hidden2.core.tool.OutlierDetector;


public class CursorListener implements MouseListener, MouseMotionListener, MouseWheelListener  {

	IndividualCanvas icanvas = null;
	IndividualSelectionPanel iselection = null;
	int initX = 0, initY = 0, itotal = 0, dtotal = 0;
	long icount = 0, dcount = 0;

	/**
	 * @param c Canvas
	 */
	public void setIndividualCanvas(Object c) {
		icanvas = (IndividualCanvas) c;
	}


	public void setSelectionPanel(IndividualSelectionPanel p) {
		iselection = p;
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}


	public void mouseClicked(MouseEvent e) {
		//initX = e.getX();
		//initY = e.getY();

		int cX = e.getX();
		int cY = e.getY();
		Object picked = icanvas.pick(cX, cY);
		icanvas.display();
		if(picked != null && iselection != null)
			OutlierDetector.detect(icanvas.getIndividualSet(), (OneIndividual)picked);

	}


	public void mousePressed(MouseEvent e) {
		initX = e.getX();
		initY = e.getY();

		icanvas.mousePressed(initX, initY);
	}


	public void mouseReleased(MouseEvent e) {
		initX = e.getX();
		initY = e.getY();

		icanvas.mouseReleased(initX,initY);
		icanvas.display();

	}


	public void mouseMoved(MouseEvent e){
		int cX = e.getX();
		int cY = e.getY();
		Object picked = icanvas.pick(cX, cY);
		icanvas.display();
		if(picked != null && iselection != null)
			iselection.setPickedObject(picked);
	}


	public void mouseDragged(MouseEvent e) {
		int cX = e.getX();
		int cY = e.getY();
		int m = e.getModifiers();

		if((m & MouseEvent.BUTTON1_MASK) != 0)
			icanvas.setDragMode(1); // ZOOM mode
		else if((m & MouseEvent.BUTTON3_MASK) != 0)
			icanvas.setDragMode(2); // SHIFT mode
		icanvas.drag(initX, cX, initY, cY);
		icanvas.display();

	}



	public void mouseWheelMoved(MouseWheelEvent e) {
		if(icanvas == null) return;
		
		icount++;
		icanvas.mousePressed(initX, initY);
		icanvas.setDragMode(1); // ZOOM mode
		int r = e.getWheelRotation();
		itotal -= (r * 20);
		icanvas.drag(0, itotal, 0, itotal);
		icanvas.display();
		IndividualWheelThread wt = new IndividualWheelThread(icount);
		wt.start();

	}

	class IndividualWheelThread extends Thread {
		long count;
		IndividualWheelThread(long c) {
             this.count = c;
        }

         public void run() {
        	 try {
        		 Thread.sleep(100);
        	 } catch(Exception e) {
        	 	e.printStackTrace();
        	 }
        	 if(count != icount) return;

        	 itotal = 0;
         }
	}

	class DimensionWheelThread extends Thread {
		long count;
		DimensionWheelThread(long c) {
             this.count = c;
        }

         public void run() {
        	 try {
        		 Thread.sleep(100);
        	 } catch(Exception e) {
        	 	e.printStackTrace();
        	 }
        	 if(count != dcount) return;

        	 dtotal = 0;
         }
	}
}
