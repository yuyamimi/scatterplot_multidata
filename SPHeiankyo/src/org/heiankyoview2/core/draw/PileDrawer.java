package org.heiankyoview2.core.draw;

import org.heiankyoview2.core.tree.Tree;
import org.heiankyoview2.core.tree.Branch;
import org.heiankyoview2.core.tree.Node;
import org.heiankyoview2.core.table.NodeTablePointer;
import org.heiankyoview2.core.table.TreeTable;
import org.heiankyoview2.core.table.Table;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;



/**
 * �`�揈���̃N���X
 * @author itot
 */
public class PileDrawer implements Drawer {

	Tree tree = null;
	TreeTable tg = null;
	Transformer view = null;
	Buffer dbuf = null;
	
	Node nodearray[];

	double p1[], p2[], p3[], p4[];
	int imageSize[] = new int[2];

	boolean isMousePressed = false, isAnnotation = true;

	Node pickedNode = null;
	double linewidth = 1.0;
	DrawerUtility du = null;
	Canvas canvas;
	
	double xmax, ymax, xmin, ymin, zmax, zmin;
	
	int pileId[] = null;
	Color colors[] = null;
	int maxsum = 0;
	
	
	/**
	 * Constructor
	 * @param width �`��̈�̕�
	 * @param height �`��̈�̍���
	 */
	public PileDrawer(int width, int height, Canvas c) {
		canvas = c;
		imageSize[0] = width;
		imageSize[1] = height;
		du = new DrawerUtility(width, height);
	}

	/**
	 * Transformer���Z�b�g����
	 * @param transformer 
	 */
	public void setTransformer(Transformer view) {
		this.view = view;
		du.setTransformer(view);
	}

	/**
	 * Buffer ���Z�b�g����
	 * @param dbuf Buffer
	 */
	public void setBuffer(Buffer dbuf) {
		this.dbuf = dbuf;
		du.setBuffer(dbuf);
	}

	/**
	 * �`��̈�̃T�C�Y��ݒ肷��
	 * @param width �`��̈�̕�
	 * @param height �`��̈�̍���
	 */
	public void setWindowSize(int width, int height) {
		imageSize[0] = width;
		imageSize[1] = height;
		du.setWindowSize(width, height);
	}
	

	/**
	 * �}�E�X�{�^����ON/OFF��ݒ肷��
	 * @param isMousePressed �}�E�X�{�^����������Ă����true
	 */
	public void setMousePressSwitch(boolean isMousePressed) {
		this.isMousePressed = isMousePressed;
	}

	/**
	 * Tree���Z�b�g����
	 * @param tree Tree
	 */
	public void setTree(Tree tree) {
		this.tree = tree;
		tg = tree.table;
		view.setTree(tree);
		du.setTree(tree);
		nodearray = du.createSortedNodeArray();
		
		maxsum = 0;
		calcMaxSumOneGroup(tree.getRootBranch());
	}

	/**
	 * ���̑������Z�b�g����
	 * @param lw ���̑����i��f���j
	 */
	public void setLinewidth(double lw) {
		linewidth = lw;
	}

	/**
	 * �A�m�e�[�V�����\����ON/OFF����
	 * @param flag �\������Ȃ�true, �\�����Ȃ��Ȃ�false
	 */
	public void setAnnotationSwitch(boolean flag) {
		isAnnotation = flag;
	}

	/**
	 * ���_����̐[���Ń\�[�g����Node�z��𐶐����� �i���s���\�[�g�@�ɂ��`��̂��߁j
	 */
	public void createSortedNodeArray() {
		nodearray = du.createSortedNodeArray();
	}

	/**
	 *  ���Ԗڂ̃e�[�u�����瓾���鐔�l��ςݏd�˂邩�A�Ƃ��������Z�b�g����
	 */
	public void setPileId(int p[]) {
		pileId = new int[p.length];
		colors = new Color[p.length];
		
		for(int i = 0; i < p.length; i++) {
			pileId[i] = p[i];
			//int ii = (i < p.length / 2) ? (i - 1) : (i + 1);
			float hue = (1.0f - (float)i / (float)p.length) * 160.f / 240.f; 
			colors[i] = Color.getHSBColor(hue, 1.0f, 1.0f);
			System.out.println("  i=" + i + " color=" + colors[i]);
		}
		
	}
	
	
	/**
	 * 1��Branch�ɑ�����Node��T�����A�A�N�Z�X�����̍ő�l��T���o��
	 */
	void calcMaxSumOneGroup(Branch branch) {
		
		
		// �e�X��Node�ɂ���
		for(int i = 0; i < branch.getNodeList().size(); i++) {
			Node node = (Node)branch.getNodeList().elementAt(i);
			if(node.getChildBranch() != null) {
				calcMaxSumOneGroup(node.getChildBranch());
				continue;
			}
			
			// �W�v�ΏۂƂȂ�eTable�ɂ���
			int sum = 0;
			for(int j = 0; j < pileId.length; j++) {
				Table table = tg.getTable(pileId[j]);
				int tid = node.table.getId(pileId[j]);
				sum += table.getInt(tid);
			}
			
			// ���Z�l���ő�l���傫����΁A�ő�l���X�V����
			if(maxsum < sum) maxsum = sum;
		}
	}
	
	
	
	/**
	 * �`������s����
	 * @param g2 Graphics2D
	 */
	public void draw(Graphics2D g2) {

		if (tree == null || view == null)
			return;

		Branch rootBranch = tree.getRootBranch();
		Branch rootDisplayBranch = dbuf.getRootDisplayBranch();

		//
		// Draw borders
		//
		if (rootBranch == rootDisplayBranch || rootDisplayBranch == null)
			drawBorders(rootBranch, g2);
		else
			drawBorders(rootDisplayBranch, g2);

		//
		// Paint nodes
		//
		if (isMousePressed == false)
			paintNodes(g2);

		//
		// Write annotations
		//
		if (isAnnotation == true)
			writeAnnotation(g2);

		//
		// Draw axis
		//
		//drawAxis(g2);

		
	}

	/**
	 * Branch �̋��E����`�悷��
	 * @param branch Branch
	 * @param g2 Graphics2D
	 */
	void drawBorders(Branch branch, Graphics2D g2) {
		Node parentNode = branch.getParentNode();
		double xmax, ymax, xmin, ymin;

		int level = branch.getLevel();
		if (isMousePressed == true
			&& level > 2 + dbuf.getRootDisplayBranch().getLevel())
			return;

		//
		// Skip if display flag is false 
		//
		g2.setPaint(new Color(128, 128, 128));
		g2.setStroke(new BasicStroke((float) linewidth));

		//
		// write the border line of the branch
		//
		xmax = parentNode.getX() + parentNode.getWidth();
		xmin = parentNode.getX() - parentNode.getWidth();
		ymax = parentNode.getY() + parentNode.getHeight();
		ymin = parentNode.getY() - parentNode.getHeight();
		p1 = du.transformPosition(xmax, ymax, 0.0, 1);
		p2 = du.transformPosition(xmax, ymin, 0.0, 2);
		p3 = du.transformPosition(xmin, ymin, 0.0, 3);
		p4 = du.transformPosition(xmin, ymax, 0.0, 4);

		GeneralPath polygon = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 4);
		polygon.moveTo((int) p1[0], (int) p1[1]);
		polygon.lineTo((int) p2[0], (int) p2[1]);
		polygon.lineTo((int) p3[0], (int) p3[1]);
		polygon.lineTo((int) p4[0], (int) p4[1]);
		polygon.closePath();
		g2.draw(polygon);

		//
		// for each (PARENT) node:
		//     Recursive call for child branches
		//
		for (int i = 1; i <= branch.getNodeList().size(); i++) {
			Node node = branch.getNodeAt(i);
			Branch childBranch = node.getChildBranch();

			if (childBranch == null)
				continue;
			drawBorders(childBranch, g2);
		}
	}

	/**
	 * Node��1���ʁi�܂���1��ʁj��h��Ԃ�
	 * @param g2 Graphics2D
	 * @param color �F
	 * @param brightness ���邳
	 */
	void paintOneNodeFace(Graphics2D g2, Color color, double brightness) {

		int r1 = (int) (color.getRed() * brightness);
		if (r1 > 255)
			r1 = 255;
		int g1 = (int) (color.getGreen() * brightness);
		if (g1 > 255)
			g1 = 255;
		int b1 = (int) (color.getBlue() * brightness);
		if (b1 > 255)
			b1 = 255;

		g2.setPaint(new Color(r1, g1, b1));

		GeneralPath polygon = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 4);

		polygon.moveTo((int) p1[0], (int) p1[1]);
		polygon.lineTo((int) p2[0], (int) p2[1]);
		polygon.lineTo((int) p3[0], (int) p3[1]);
		polygon.lineTo((int) p4[0], (int) p4[1]);
		polygon.closePath();
		g2.fill(polygon);

	}

	
	/**
	 * Node�̒�ʂɉ摜��`�悷��
	 */
	void drawNodeImage(Graphics2D g2, BufferedImage image) {
		double xmin = 1.0e+30,
		       ymin = 1.0e+30,
		       xmax = -1.0e+30,
		       ymax = -1.0e+30;
		double w,h;
		
		if(xmin > p1[0]) xmin = p1[0];
		if(xmin > p2[0]) xmin = p2[0];
		if(xmin > p3[0]) xmin = p3[0];
		if(xmin > p4[0]) xmin = p4[0];
		if(xmax < p1[0]) xmax = p1[0];
		if(xmax < p2[0]) xmax = p2[0];
		if(xmax < p3[0]) xmax = p3[0];
		if(xmax < p4[0]) xmax = p4[0];
		if(ymin > p1[1]) ymin = p1[1];
		if(ymin > p2[1]) ymin = p2[1];
		if(ymin > p3[1]) ymin = p3[1];
		if(ymin > p4[1]) ymin = p4[1];
		if(ymax < p1[1]) ymax = p1[1];
		if(ymax < p2[1]) ymax = p2[1];
		if(ymax < p3[1]) ymax = p3[1];
		if(ymax < p4[1]) ymax = p4[1];
		w = xmax - xmin;
		h = ymax - ymin;
		
		AffineTransform at = new AffineTransform();
		at.translate(xmin, ymin);
		at.scale(w / (double)image.getWidth(),
				 h / (double)image.getHeight());
		g2.drawImage((BufferedImage)image, at, (ImageObserver)canvas);

	}
	
	/**
	 * Node������킷�����̂̃T�C�Y�����߂�
	 * @param node
	 */
	boolean calcBarSize(Node node, int id, double totalZ) {
	
		//
		// calculate positions of vertices of the node
		//
		xmax = node.getX() + node.getWidth();
		xmin = node.getX() - node.getWidth();
		ymax = node.getY() + node.getHeight();
		ymin = node.getY() - node.getHeight();
		zmax = totalZ; // + node.getDepth();
		zmin = totalZ; // - node.getDepth();

		Table table = tg.getTable(pileId[id]);
		NodeTablePointer tn = node.table;
		int nid = tn.getId(pileId[id]);
		int value = table.getInt(nid);
		if(value <= 0) return false;
		double height = (double)value / (double)maxsum;
		if (height < 0.0) return false;
		if (height > 1.0) height = 1.0;
		zmax += (height * view.getTreeSize() * 0.5);
		
		return true;
	}
	
	
	/**
	 * Node��h��Ԃ�
	 * @param g2 Graphics2D
	 */
	void paintNodes(Graphics2D g2) {

		int r1, g1, b1;
		double totalZ;
		String rr, gg, bb;
		Node node;

		//
		// Definition of picks, and polygon drawing
		//
		for (int i = 0; i < nodearray.length; i++) {
			node = nodearray[i];
			totalZ = 0.0;
			for(int j = 0; j < pileId.length; j++) {
			
				if(calcBarSize(node, j, totalZ) == false) continue;
				totalZ += (zmax - zmin);

				//
				// first polygon
				//
				p1 = du.transformPosition(xmax, ymax, zmax, 1);
				p2 = du.transformPosition(xmax, ymin, zmax, 2);
				p3 = du.transformPosition(xmin, ymin, zmax, 3);
				p4 = du.transformPosition(xmin, ymax, zmax, 4);
				if(node.getImage() != null) {
					drawNodeImage(g2, (BufferedImage)node.getImage());
				}
				else {
					paintOneNodeFace(g2, colors[j], 1.1);
				}

			
				//
				// second polygon
				//
				if (view.getViewRotate(9) > 0.0) {
					p1 = du.transformPosition(xmax, ymax, zmax, 1);
					p2 = du.transformPosition(xmax, ymax, zmin, 2);
					p3 = du.transformPosition(xmin, ymax, zmin, 3);
					p4 = du.transformPosition(xmin, ymax, zmax, 4);
				} else {
					p1 = du.transformPosition(xmax, ymin, zmax, 1);
					p2 = du.transformPosition(xmax, ymin, zmin, 2);
					p3 = du.transformPosition(xmin, ymin, zmin, 3);
					p4 = du.transformPosition(xmin, ymin, zmax, 4);
				}
				paintOneNodeFace(g2, colors[j], 0.9);

				//
				// third polygon
				//
				if (view.getViewRotate(8) > 0.0) {
					p1 = du.transformPosition(xmax, ymin, zmax, 1);
					p2 = du.transformPosition(xmax, ymin, zmin, 2);
					p3 = du.transformPosition(xmax, ymax, zmin, 3);
					p4 = du.transformPosition(xmax, ymax, zmax, 4);
				} else {
					p1 = du.transformPosition(xmin, ymin, zmax, 1);
					p2 = du.transformPosition(xmin, ymin, zmin, 2);
					p3 = du.transformPosition(xmin, ymax, zmin, 3);
					p4 = du.transformPosition(xmin, ymax, zmax, 4);
				}
				paintOneNodeFace(g2, colors[j], 0.6);
			}
		}
	}

	/**
	 * �A�m�e�[�V������`�悷��
	 * @param g2 Graphics2D
	 */
	void writeAnnotation(Graphics2D g2) {

		String name = null;

		//
		// Calculate the number of annotations to write
		//
		int numAnnotation =
			(int) (view.getViewScale()
				* view.getViewScale()
				* (float) imageSize[1]
				* 0.01f);
		Branch rootDisplayBranch = dbuf.getRootDisplayBranch();
		if (rootDisplayBranch != null
			&& tree.getRootBranch() != rootDisplayBranch) {
			Node node1 = tree.getRootBranch().getParentNode();
			Node node2 = rootDisplayBranch.getParentNode();
			numAnnotation =
				(int) ((double) numAnnotation
					* (node2.getWidth() * node2.getHeight())
					/ (node1.getWidth() * node1.getHeight()));
		}
		if (numAnnotation > dbuf.getNumAnnotation()) {
			numAnnotation = dbuf.getNumAnnotation();
		}

		//
		// for each annotation in the sorted order
		//
		g2.setPaint(new Color(255, 255, 0));
		for (int i = 0; i < numAnnotation; i++) {
			double pos[] = dbuf.getAnnotationPosition(i);
			name = dbuf.getAnnotationName(i);
			p1 = du.transformPosition(pos[0], pos[1], pos[2], 1);
			writeOneString(name, g2);
		}

		if (pickedNode != null) {
			g2.setPaint(new Color(255, 0, 255));
			p1 =
				du.transformPosition(
					pickedNode.getX(),
					pickedNode.getY(),
					pickedNode.getZ(),
					1);
			
			name = tg.getNodeAttributeName(pickedNode, tg.getNameType());
			writeOneString(name, g2);
		}
	}

	/**
	 * 1�̕������`�悷��
	 * @param name �`�悳��镶����
	 * @param g2 Graphics2D
	 */
	void writeOneString(String name, Graphics2D g2) {
		if (name == null || name.length() <= 0)
			return;
		Font font = new Font("MS�S�V�b�N", Font.BOLD, 14);
		g2.setFont(font);
		g2.drawString(name, (int) p1[0], (int) p1[1]);
	}

	/**
	 * ���̂��s�b�N����
	 * @param px �s�b�N�������̂̉�ʏ��x���W�l
	 * @param py �s�b�N�������̂̉�ʏ�̍��W�l
	 */
	public void pickObjects(int px, int py) {

		if (tree == null || view == null)
			return;
		pickBorders(tree.getRootBranch(), px, py);
		pickNodes(px, py);

		if (dbuf != null)
			dbuf.setPickedNode(pickedNode);

	}

	/**
	 * Branch���E�����s�b�N����
	 * @param branch Branch
	 * @param px �s�b�N�������̂̉�ʏ��x���W�l
	 * @param py �s�b�N�������̂̉�ʏ�̍��W�l
	 */
	public Node pickBorders(Branch branch, int px, int py) {
		Node parentNode = branch.getParentNode();
		double xmax, ymax, xmin, ymin;
		boolean flag = false;

		//
		// write the border line of the branch
		//
		xmax = parentNode.getX() + parentNode.getWidth();
		xmin = parentNode.getX() - parentNode.getWidth();
		ymax = parentNode.getY() + parentNode.getHeight();
		ymin = parentNode.getY() - parentNode.getHeight();
		p1 = du.transformPosition(xmax, ymax, 0.0, 1);
		p2 = du.transformPosition(xmax, ymin, 0.0, 2);
		p3 = du.transformPosition(xmin, ymin, 0.0, 3);
		p4 = du.transformPosition(xmin, ymax, 0.0, 4);

		flag = du.isInside(px, py, p1, p2, p3, p4);
		if (flag == true)
			pickedNode = parentNode;

		//
		// for each (PARENT) node:
		//     Recursive call for child branches
		//
		for (int i = 1; i <= branch.getNodeList().size(); i++) {
			Node node = branch.getNodeAt(i);
			Branch childBranch = node.getChildBranch();

			if (childBranch == null)
				continue;
			pickedNode = pickBorders(childBranch, px, py);
		}

		return pickedNode;
	}

	/**
	 * Node���s�b�N����
	 * @param px �s�b�N�������̂̉�ʏ��x���W�l
	 * @param py �s�b�N�������̂̉�ʏ�̍��W�l
	 */
	public Node pickNodes(int px, int py) {

		boolean flag = false;
		Node node;
		double totalZ;

		//
		// Definition of picks, and polygon drawing
		//
		for (int i = 0; i < nodearray.length; i++) {
			node = nodearray[i];
			totalZ = 0.0;
			
			for(int j = 0; j < pileId.length; j++) {
			
				//
				// calculate positions of vertices of the node
				//
				if(calcBarSize(node, j, totalZ) == false) continue;
				totalZ += (zmax - zmin);

				//
				// first polygon
				//
				p1 = du.transformPosition(xmax, ymax, zmax, 1);
				p2 = du.transformPosition(xmax, ymin, zmax, 2);
				p3 = du.transformPosition(xmin, ymin, zmax, 3);
				p4 = du.transformPosition(xmin, ymax, zmax, 4);
				flag = du.isInside(px, py, p1, p2, p3, p4);
				if (flag == true)
					pickedNode = node;

				//
				// second polygon
				//
				p1 = du.transformPosition(xmax, ymax, zmax, 1);
				p2 = du.transformPosition(xmax, ymax, zmin, 2);
				p3 = du.transformPosition(xmin, ymax, zmin, 3);
				p4 = du.transformPosition(xmin, ymax, zmax, 4);
				flag = du.isInside(px, py, p1, p2, p3, p4);
				if (flag == true)
					pickedNode = node;

				//
				// third polygon
				//
				p1 = du.transformPosition(xmax, ymin, zmax, 1);
				p2 = du.transformPosition(xmax, ymin, zmin, 2);
				p3 = du.transformPosition(xmax, ymax, zmin, 3);
				p4 = du.transformPosition(xmax, ymax, zmax, 4);
				flag = du.isInside(px, py, p1, p2, p3, p4);
				if (flag == true)
					pickedNode = node;
			}
		}

		return pickedNode;
	}

	/**
	 * ���W����`�悷��
	 * @param g2 Graphics2D
	 */
	void drawAxis(Graphics2D g2) {
		
		double c1 = 1.1, c2 = 1.02, c3 = 1.0;
		
		double x1 = view.getTreeCenter(0) - view.getTreeSize() * c1;
		double x2 = view.getTreeCenter(0) - view.getTreeSize() * c2;
		double y1 = view.getTreeCenter(1) - view.getTreeSize() * c1;
		double y2 = view.getTreeCenter(1) - view.getTreeSize() * c2;
		GeneralPath polygon = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 2);

		p1 = du.transformPosition(x1, y1, 0.0, 1);
		p2 = du.transformPosition(x2, y1, 0.0, 2);
		polygon.moveTo((int) p1[0], (int) p1[1]);
		polygon.lineTo((int) p2[0], (int) p2[1]);
		g2.draw(polygon);

		p1 = du.transformPosition(x1, y1, 0.0, 1);
		p2 = du.transformPosition(x1, y2, 0.0, 2);
		polygon.moveTo((int) p1[0], (int) p1[1]);
		polygon.lineTo((int) p2[0], (int) p2[1]);
		g2.draw(polygon);
		
		double x3 = view.getTreeCenter(0) - view.getTreeSize() * c3;
		p1 = du.transformPosition(x3, y1, 0.0, 1);
		writeOneString("x", g2);
		
		double y3 = view.getTreeCenter(1) - view.getTreeSize() * c3;
		p1 = du.transformPosition(x1, y3, 0.0, 1);
		writeOneString("y", g2);
	}

}
