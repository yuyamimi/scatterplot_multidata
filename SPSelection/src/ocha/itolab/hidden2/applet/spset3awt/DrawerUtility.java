package ocha.itolab.hidden2.applet.spset3awt;


/**
 * 描画処理のクラス
 * @author itot
 */
public class DrawerUtility {
	
	IndividualTransformer trans = null;
	int numNode;
	
	double p1[] = new double[3];
	double p2[] = new double[3];
	double p3[] = new double[3];
	double p4[] = new double[3];
	int imageSize[] = new int[2];
	
	/**
	 * Constructor
	 * @param width 描画領域の幅
	 * @param height 描画領域の高さ
	 */
	public DrawerUtility(int width, int height) {
		imageSize[0] = width;
		imageSize[1] = height;
	}
	
	/**
	 * Transformerをセットする
	 * @param t Transformer
	 */
	public void setTransformer(IndividualTransformer t) {
		this.trans = t;
	}
	

	
	/**
	 * 描画領域のサイズを設定する
	 * @param width 描画領域の幅
	 * @param height 描画領域の高さ
	 */
	public void setWindowSize(int width, int height) {
		imageSize[0] = width;
		imageSize[1] = height;
	}
	
	/**
	 * 塗りつぶす物体面がオモテかウラかを判定する
	 * @param px 画面上のx座標値
	 * @param py 画面上のy座標値
	 * @param e1 1個目の頂点の座標値
	 * @param e2 2個目の頂点の座標値
	 * @return オモテならtrue
	 */
	int whichSide(int px, int py, double e1[], double e2[]) {
		double a = (e1[1] - (double) py) * (e2[0] - (double) px);
		double b = (e1[0] - (double) px) * (e2[1] - (double) py);
		if (a > b)
			return -1;
		if (a < b)
			return 1;
		
		return 0;
	}
	
	/**
	 * ピックした位置が四角形面の内部かを判定する
	 * @param px 画面上のx座標値
	 * @param py 画面上のy座標値
	 * @param pp1 1個目の頂点の座標値
	 * @param pp2 2個目の頂点の座標値
	 * @param pp3 3個目の頂点の座標値
	 * @param pp4 4個目の頂点の座標値
	 * @return 内部ならtrue
	 */
	public boolean isInside(
			int px,
			int py,
			double pp1[],
			double pp2[],
			double pp3[],
			double pp4[]) {
		
		boolean flag1 = false, flag2 = false;
		int ret;
		
		ret = whichSide(px, py, pp1, pp2);
		if (ret > 0)
			flag1 = true;
		if (ret < 0)
			flag2 = true;
		
		ret = whichSide(px, py, pp2, pp3);
		if (ret > 0)
			flag1 = true;
		if (ret < 0)
			flag2 = true;
		
		ret = whichSide(px, py, pp3, pp4);
		if (ret > 0)
			flag1 = true;
		if (ret < 0)
			flag2 = true;
		
		ret = whichSide(px, py, pp4, pp1);
		if (ret > 0)
			flag1 = true;
		if (ret < 0)
			flag2 = true;
		
		if (flag1 == false || flag2 == false)
			return true;
		return false;
	}
	
	
	/**
	 * 座標値を変換する
	 * @param x 実座標系のx座標値
	 * @param y 実座標系のy座標値
	 * @param z 実座標系のz座標値
	 * @param select 四角形の頂点を特定するID（1～4のいずれかの整数）
	 */
	public double[] transformPosition(
		double x,
		double y,
		double z,
		int select) {
		double pos[];

		pos = p1;
		if (select == 2)
			pos = p2;
		if (select == 3)
			pos = p3;
		if (select == 4)
			pos = p4;

		//
		// Centering
		//
		if (trans.getSize() > 0.0000001) {
			x = (x - trans.getCenter(0)) / trans.getSize();
			y = (y - trans.getCenter(1)) / trans.getSize();
			z = (z - trans.getCenter(2)) / trans.getSize();
		}

		//
		// Fit to the image size
		//
		int wh = (imageSize[0] < imageSize[1]) ? imageSize[0] : imageSize[1];
		wh /= 2;
		x *= wh;
		y *= wh;
		z *= wh;

		//
		// Rotation & Shift
		//
		pos[0] =
			x * trans.getViewRotate(0)
				+ y * trans.getViewRotate(1)
				+ z * trans.getViewRotate(2);
//				+ trans.getViewShift(0);
		pos[1] =
			x * trans.getViewRotate(4)
				+ y * trans.getViewRotate(5)
				+ z * trans.getViewRotate(6);
//				+ trans.getViewShift(1);
		pos[2] =
			x * trans.getViewRotate(8)
				+ y * trans.getViewRotate(9)
				+ z * trans.getViewRotate(10);
//				+ trans.getViewShift(2);
		
		
		//
		// Scaling
		//
		pos[0] = pos[0] * trans.getViewScale() + trans.getViewShift(0) + 0.5 * (double) imageSize[0];
		pos[1] = pos[1] * trans.getViewScale() + trans.getViewShift(1) + 0.5 * (double) imageSize[1];
		pos[2] = pos[2] * trans.getViewScale();

		return pos;
	}


}
