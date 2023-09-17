package ocha.itolab.hidden2.applet.spset4awt;

import java.lang.Math;

import org.heiankyoview2.core.tree.*;

public class IndividualTransformer {

	double viewShift[] = new double[3];
	double viewRotate[] = new double[16];
	double viewScaleX, viewScaleY;
	double viewShiftBak[] = new double[3];
	double center[] = new double[3];
	double size;
	double viewScaleBakX, viewScaleBakY;
	double Xrotate, Yrotate, XrotateBak, YrotateBak;
	double shiftX, shiftZ;
	
	double treeMin[] = new double[3];
	double treeMax[] = new double[3];
	double treeCenter[] = new double[3];
	double treeSize;
	
	DrawerUtility du = null;

	/**
	 * Constructor
	 */
	public IndividualTransformer() {
		viewReset();
	}

	public void setDrawerUtility(DrawerUtility du){
		this.du = du;
	}
	
	
	public void setTree(Tree tree) {

		treeMin[0] = treeMin[1] = treeMin[2] = 1.0e+20;
		treeMax[0] = treeMax[1] = treeMax[2] = -1.0e+20;

		double tmp;
		Branch rootBranch = tree.getRootBranch();

		for (int i = 1; i <= rootBranch.getNodeList().size(); i++) {
			Node node = rootBranch.getNodeAt(i);
			double x = node.getX();
			double y = node.getY();
			double z = node.getZ();
			double width = node.getWidth();
			double height = node.getHeight();
			double depth = node.getDepth();

			tmp = x + width;
			if (treeMax[0] < tmp)
				treeMax[0] = tmp;
			tmp = y + height;
			if (treeMax[1] < tmp)
				treeMax[1] = tmp;
			tmp = z + depth;
			if (treeMax[2] < tmp)
				treeMax[2] = tmp;

			tmp = x - width;
			if (treeMin[0] > tmp)
				treeMin[0] = tmp;
			tmp = y - height;
			if (treeMin[1] > tmp)
				treeMin[1] = tmp;
			tmp = z - depth;
			if (treeMin[2] > tmp)
				treeMin[2] = tmp;

		}

		treeCenter[0] = (treeMin[0] + treeMax[0]) * 0.5;
		treeCenter[1] = (treeMin[1] + treeMax[1]) * 0.5;
		treeCenter[2] = (treeMin[2] + treeMax[2]) * 0.5;
		treeSize = treeMax[0] - treeMin[0];
		tmp = treeMax[1] - treeMin[1];
		if (treeSize < tmp)
			treeSize = tmp;
		treeSize *= 0.5;

	}
	
	
	public double getTreeSize() {
		return treeSize;
	}
	
	public double getTreeCenter(int i) {
		return treeCenter[i];
	}
	
	
	
	public void viewReset() {
		for (int i = 0; i < 16; i++) {
			if (i % 5 == 0)
				viewRotate[i] = 1.0;
			else
				viewRotate[i] = 0.0;
		}
		viewScaleX = viewScaleBakX = 1.0;
		viewScaleY = viewScaleBakY = 1.0;
		viewShift[0] = viewShiftBak[0] = 0.0;
		viewShift[1] = viewShiftBak[1] = 0.0;
		viewShift[2] = viewShiftBak[2] = 0.0;
		Xrotate = XrotateBak = 0.0;
		Yrotate = YrotateBak = 0.0;

	}


	public void mousePressed() {
		viewScaleBakX = viewScaleX;
		viewScaleBakY = viewScaleY;
		viewShiftBak[0] = viewShift[0];
		viewShiftBak[1] = viewShift[1];
		viewShiftBak[2] = viewShift[2];
		XrotateBak = Xrotate;
		YrotateBak = Yrotate;
	}

	
	public void getPickedPosition(double x, double y){
		double objX, objY;
		
		// �g��k���E�ړ��O��object���W�l
		objX = (x - viewShiftBak[0]) / viewScaleBakX;
		objY = (y - viewShiftBak[1]) / viewScaleBakY;
	}
	
	
	public void drag(int x, int y, int width, int height, int dragMode) {
		
		if(dragMode == 1) { // ZOOM
			
			if (x > 0) {
				viewScaleX =
					viewScaleBakX * (1 + (double) (2 * x) / (double) width);
			} else {
				viewScaleX = viewScaleBakX * (1 + (double) x / (double) width);
			}
			if (viewScaleX < 0.2)
				viewScaleX = 0.2;
			
			if (y > 0) {
				viewScaleY =
					viewScaleBakY * (1 + (double) (2 * y) / (double) height);
			} else {
				viewScaleY = viewScaleBakY * (1 + (double) y / (double) height);
			}
			viewShift[0] = viewShiftBak[0] * viewScaleX / viewScaleBakX;
			viewShift[1] = viewShiftBak[1] * viewScaleY / viewScaleBakY;
		}
		
		if (dragMode == 2) { // SHIFT
			
			 float diffX = (float)x * 3.0f / width;
             float diffY = (-3.0f) * (float)y / height;
           
            viewShift[0] = viewShiftBak[0] + diffX;
 			viewShift[1] = viewShiftBak[1] + diffY;
		}
		
		if (dragMode == 3) { // ROTATE
			Xrotate = XrotateBak + (double) x * Math.PI / (double) width;
			Yrotate = YrotateBak + (double) y * Math.PI / (double) height;
			double cosX = Math.cos(Yrotate);
			double sinX = Math.sin(Yrotate);
			double cosY = Math.cos(Xrotate);
			double sinY = Math.sin(Xrotate);

			viewRotate[0] = cosY;
			viewRotate[1] = 0;
			viewRotate[2] = -sinY;
			viewRotate[4] = sinX * sinY;
			viewRotate[5] = cosX;
			viewRotate[6] = sinX * cosY;
			viewRotate[8] = cosX * sinY;
			viewRotate[9] = -sinX;
			viewRotate[10] = cosX * cosY;
		}

	}
	

	
	public double getSize() {
		return size;
	}
	
	
	public void setSize(double t) {
		size = t;
	}
	
	
	
	public double getViewScale() {
		double ret = (viewScaleX > viewScaleY) ? viewScaleX : viewScaleY;
		return ret;
	}
	

	public double getViewScaleX() {
		return viewScaleX;
	}
	
	
	public double getViewScaleY() {
		return viewScaleY;
	}
	

	public void setViewScaleX(double v) {
		viewScaleX = v;
	}
	
	
	public void setViewScaleY(double v) {
		viewScaleY = v;
	}


	public double getCenter(int i) {
		return center[i];
	}
	
	
	public void setCenter(double g, int i) {
		center[i] = g;
	}

	
	public double getViewRotate(int i) {
		return viewRotate[i];
	}
	
	/**
	 * ���_�̉�]�̍s��l���Z�b�g����
	 * @param v �s��l
	 * @param i �s�񒆂̗v�f�̈ʒu
	 */
	public void setViewRotate(double v, int i) {
		viewRotate[i] = v;
	}

	/**
	 * ���_�̕��s�ړ��ʂ�Ԃ�
	 * @param i ���W�� (0:X, 1:Y, 2:Z)
	 * @return ���s�ړ���
	 */
	public double getViewShift(int i) {
		return viewShift[i];
	}
	
	/**
	 * ���_�̕��s�ړ��ʂ��Z�b�g����
	 * @param v ���s�ړ���
	 * @param i ���W�� (1:X, 2:Y, 3:Z)
	 */
	public void setViewShift(double v, int i) {
		viewShift[i] = v;
	}

}
