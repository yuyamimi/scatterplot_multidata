package ocha.itolab.hidden2.applet.spset3awt;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import ocha.itolab.hidden2.core.data.IndividualSet;
import ocha.itolab.hidden2.core.data.OneIndividual;

public class IndividualSP {

	IndividualSet iset;
	ArrayList<int[]> vlist;
	OneIndividual picked = null;
	DrawerUtility du;

	private boolean flag = false;
	double scale;
	double p1[], p2[], p3[], p4[];

	int numclusters = 1;
	float brightness = 0.75f;
	double SHRINK = 0.925;
	Graphics2D g2;
	boolean hideGrayPoints = true;

	/*****/
	int psnum = 0;
	double[][][][] points;
	double vrad;

	/*****/

	/**
	 * Constructor
	 */
	public IndividualSP() {

	}

	public void setDrawerUtility(DrawerUtility u) {
		du = u;
	}

	/*****/
	public void changeOutlierVertices() {
	}

	public void changeOutlierBoundaries() {
	}

	public void changeOutlierTriangles() {
	}

	public void hideGrayPoints() {
		hideGrayPoints = !hideGrayPoints;
	}

	public void setIsOutlier2(boolean isOut) {
	}

	public void clearArray() {
	}

	public void setValueIdSet(ArrayList<int[]> list) {
		vlist = list;
	}

	public void setIndividualSet(IndividualSet p) {
		iset = p;
	}

	public void setNumClusters(int n) {
		numclusters = n;
	}

	/*****/
	public IndividualSet getIndividualSet() {
		return iset;
	}

	public double[][][][] getPoints() {
		return points;
	}

	public double getVrad() {
		return vrad;
	}

	/******/

	public void draw(Graphics2D gg2, double scl) {

		if (iset == null || vlist == null)
			return;
		g2 = gg2;
		scale = scl;

		int ncell = (int) (Math.sqrt(vlist.size()) - 1.0e-6) + 1;
		double size = 2.0 / (double) ncell;

		int count = 0;
		/*****/
		IndividualSet ps = iset;
		// if(shopId >= 0) ps = child[shopId];
		psnum = ps.getNumIndividual();
		points = new double[psnum][3][ps.getNumExplain() * ps.getNumObjective()][numclusters];
		/*****/
		System.out.println("ncell" + ncell);
		for (int i = 0; i < ncell; i++) {
			double y = ((double) i + 0.5) * size - 1.0;
			y *= -1.0;
			for (int j = 0; j < ncell; j++, count++) {
				if (count >= vlist.size())
					return;
				double x = ((double) j + 0.5) * size - 1.0;
				drawOneSP(x, y, size, count);
			}
		}
	}

	void drawOneSP(double sx, double sy, double size, int pid) {
		int NUMV = 6;
		double VRAD = 0.02 * size / Math.sqrt(scale);
		int xposId = vlist.get(pid)[0];
		int yposId = vlist.get(pid)[1];

		double minx = -0.5 * size * SHRINK + sx;
		double maxx = 0.5 * size * SHRINK + sx;
		double miny = -0.5 * size * SHRINK + sy;
		double maxy = 0.5 * size * SHRINK + sy;

		g2.setPaint(new Color(128, 128, 128));
		g2.setStroke(new BasicStroke(1.0f));

		p1 = du.transformPosition(minx, maxy, 0.0, 1);
		p2 = du.transformPosition(minx, miny, 0.0, 2);
		p3 = du.transformPosition(maxx, miny, 0.0, 3);
		p4 = du.transformPosition(maxx, maxy, 0.0, 4);

		GeneralPath polygon = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 2);
		polygon.moveTo((int) p1[0], (int) p1[1]);
		polygon.lineTo((int) p2[0], (int) p2[1]);
		polygon.lineTo((int) p3[0], (int) p3[1]);
		polygon.lineTo((int) p4[0], (int) p4[1]);
		polygon.lineTo((int) p1[0], (int) p1[1]);
		g2.draw(polygon);

		double rgb[] = new double[3];

		IndividualSet ps = iset;

		// Draw gray dots
		for (int i = 0; i < ps.getNumIndividual(); i++) {
			OneIndividual p = ps.getOneIndividual(i);
			boolean isOut;
			if (hideGrayPoints == true) {
				if (p.isGray() == false)
					continue;
				p.setOutlier(false);
				if (p.isOutlier() == true)
					continue;

				g2.setPaint(new Color(200, 200, 200));
				g2.setStroke(new BasicStroke(1.0f));

				double ex[] = p.getExplainValues();
				double ob[] = p.getObjectiveValues();
				double x = (ex[xposId] - ps.explains.min[xposId]) / (ps.explains.max[xposId] - ps.explains.min[xposId]);
				double y = (ob[yposId] - ps.objectives.min[yposId])
						/ (ps.objectives.max[yposId] - ps.objectives.min[yposId]);
				y *= -1.0;
				x = (x - 0.5) * size * SHRINK + sx;
				y = (y + 0.5) * size * SHRINK + sy;

				p1 = du.transformPosition(x, y, 0.0, 1);

				polygon = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 2);
				for (int j = 0; j < NUMV; j++) {
					double xx = VRAD * Math.cos(2.0 * Math.PI * ((double) j / NUMV)) + x;
					double yy = VRAD * Math.sin(2.0 * Math.PI * ((double) j / NUMV)) + y;
					p1 = du.transformPosition(xx, yy, 0.0, 1);
					if (j == 0)
						polygon.moveTo((int) p1[0], (int) p1[1]);
					else
						polygon.lineTo((int) p1[0], (int) p1[1]);
					// System.out.println("1: i=" + i + " j=" + j + " x=" + p1[0] + " y=" + p1[1]);
				}
				g2.fill(polygon);

			} else {
				if (p.isGray())
					p.setOutlier(true);
			}
		}

		// Draw non-gray dots
		for (int i = 0; i < ps.getNumIndividual(); i++) {
			OneIndividual p = ps.getOneIndividual(i);
			if (p.isGray() == true)
				continue;
			if (p.isOutlier() == true)
				continue;
			rgb[0] = rgb[1] = rgb[2] = 0.5;

			boolean isPicked = false;
			if (p == picked) {
				// System.out.println("p == picked");
				isPicked = true;
				if (numclusters >= 2)
					rgb = calcNodeColor(p.getClusterId(), numclusters, 1.0f, 0.25f);
			} else if (picked != null) {
				if (numclusters >= 2)
					rgb = calcNodeColor(p.getClusterId(), numclusters, 0.75f, 0.75f);
			} else {
				if (numclusters >= 2)
					rgb = calcNodeColor(p.getClusterId(), numclusters, 0.75f, 0.75f);
			}

			int rr = (int) (255.0 * rgb[0]);
			int gg = (int) (255.0 * rgb[1]);
			int bb = (int) (255.0 * rgb[2]);
			g2.setPaint(new Color(rr, gg, bb));
			g2.setStroke(new BasicStroke(1.0f));

			double ex[] = p.getExplainValues();
			double ob[] = p.getObjectiveValues();
			double x = (ex[xposId] - ps.explains.min[xposId]) / (ps.explains.max[xposId] - ps.explains.min[xposId]);
			double y = (ob[yposId] - ps.objectives.min[yposId])
					/ (ps.objectives.max[yposId] - ps.objectives.min[yposId]);
			y *= -1.0;
			x = (x - 0.5) * size * SHRINK + sx;
			y = (y + 0.5) * size * SHRINK + sy;

			/*****/
			int clusterId = p.getClusterId();
			if (clusterId == -1) {
				clusterId = 0;
			}
			for (int j = 0; j < points[i][0][pid].length; j++) {
				// System.out.println(points[i][0][pid].length);
				if (j == clusterId) {
					points[i][0][pid][j] = x;
					points[i][1][pid][j] = y;
					points[i][2][pid][j] = 0.0;
				} else {
					points[i][0][pid][j] = 0.0;
					points[i][1][pid][j] = 0.0;
					points[i][2][pid][j] = 0.0;

				}
				// System.out.println("points[" + i + "][0][" + pid + "][" + j + "] : " +
				// points[i][0][pid][j]);
				// System.out.println("points[" + i + "][1][" + pid + "][" + j + "] : " +
				// points[i][1][pid][j]);
			}

			polygon = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 2);
			for (int j = 0; j < NUMV; j++) {
				double xx = vrad * Math.cos(2.0 * Math.PI * ((double) j / NUMV)) + x;
				double yy = vrad * Math.sin(2.0 * Math.PI * ((double) j / NUMV)) + y;
				p1 = du.transformPosition(xx, yy, 0.0, 1);
				if (j == 0)
					polygon.moveTo((int) p1[0], (int) p1[1]);
				else
					polygon.lineTo((int) p1[0], (int) p1[1]);
				// System.out.println("2: i=" + i + " j=" + j + " xx=" + xx + " yy=" + yy + "
				// x=" + p1[0] + " y=" + p1[1]);

			}
			g2.fill(polygon);

		}
		flag = false;

		g2.setPaint(new Color(0, 0, 0));
		g2.setStroke(new BasicStroke(1.0f));

		double x1 = 0.0 * size + sx;
		double y1 = 0.5 * size + sy;
		p1 = du.transformPosition(x1, y1, 0.0, 1);
		writeOneString(p1[0], p1[1], ps.getValueName(xposId));
		double x2 = -0.5 * size + sx;
		double y2 = 0.0 * size + sy;
		p1 = du.transformPosition(x2, y2, 0.0, 1);
		writeOneString(p1[0], p1[1], ps.getValueName(ps.getNumExplain() + yposId));

	}

	double[] calcNodeColor(int cid, int num, float s, float v) {
		double c[] = new double[3];
		// float hue = (float)p.getClusterId() / (float)numclusters;
		float hue = (float) cid / (float) num + 0.5f;
		Color color = Color.getHSBColor(hue, s, v);
		c[0] = (double) color.getRed() / 255.0;
		c[1] = (double) color.getGreen() / 255.0;
		c[2] = (double) color.getBlue() / 255.0;
		return c;
	}

	void writeOneString(double x, double y, String word) {
		if (word == null || word.length() <= 0)
			return;
		Font font = new Font("MS�S�V�b�N", Font.BOLD, 11);
		g2.setFont(font);
		g2.drawString(word, (int) p1[0], (int) p1[1]);
	}

	public Object pick(int cx, int cy) {
		if (iset == null || vlist == null)
			return null;

		/*
		 * int ncell = (int)(Math.sqrt(vlist.size()) - 1.0e-6) + 1; double size = 2.0 /
		 * (double)ncell;
		 * 
		 * int count = 0; for(int i = 0; i < ncell; i++) { double y = ((double)i + 0.5)
		 * * size - 1.0; for(int j = 0; j < ncell; j++, count++) { if(count >=
		 * vlist.size()) return null; double x = ((double)j + 0.5) * size - 1.0; Object
		 * p = pickOneSP(cx, cy, x, y, size, count); //System.out.println("cx:" + cx +
		 * ", cy:" + cy + ", x:" + x + ", y:" + y + ", size:" + size + ", count:" +
		 * count); if(p != null) return p; } }
		 */

		picked = null;
		return null;
	}

	/*
	 * Object pickOneSP(int cx, int cy, double sx, double sy, double size, int pid)
	 * { double THRESHOLD = 10.0; DoubleBuffer p1 = DoubleBuffer.allocate(3);
	 * 
	 * int xposId = vlist.get(pid)[0]; int yposId = vlist.get(pid)[1]; cy =
	 * view.get(3) - cy + 1;
	 * 
	 * double minx = -0.5 * size + sx; double maxx = 0.5 * size + sx; double miny =
	 * -0.5 * size + sy; double maxy = 0.5 * size + sy;
	 * 
	 * IndividualSet ps = iset; if(shopId >= 0) ps = child[shopId];
	 * 
	 * for(int i = 0; i < ps.getNumIndividual(); i++) { OneIndividual p =
	 * ps.getOneIndividual(i);
	 * 
	 * double ex[] = p.getExplainValues(); double ob[] = p.getObjectiveValues();
	 * double x = (ex[xposId] - ps.explains.min[xposId]) / (ps.explains.max[xposId]
	 * - ps.explains.min[xposId]); double y = (ob[yposId] -
	 * ps.objectives.min[yposId]) / (ps.objectives.max[yposId] -
	 * ps.objectives.min[yposId]); x = (x - 0.5) * size * SHRINK + sx; y = (y - 0.5)
	 * * size * SHRINK + sy; glu2.gluProject(x, y, 0.0, model, proj, view, p1);
	 * double xx = p1.get(0); double yy = p1.get(1); double dist = (cx - xx) * (cx -
	 * xx) + (cy - yy) * (cy - yy); //System.out.println("cx:" + cx + ", cy:" + cy +
	 * ", xx:" + xx + ", yy:" + yy); if(dist < THRESHOLD) { picked = p; return p; }
	 * 
	 * }
	 * 
	 * picked = null; return null; }
	 */

}
