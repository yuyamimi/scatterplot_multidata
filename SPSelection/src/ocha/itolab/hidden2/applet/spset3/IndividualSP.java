package ocha.itolab.hidden2.applet.spset3;

import java.awt.Color;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.gl2.GLUgl2;
import com.jogamp.opengl.util.gl2.GLUT;

import ocha.itolab.hidden2.core.data.IndividualSet;
import ocha.itolab.hidden2.core.data.OneIndividual;


public class IndividualSP {

	private GL gl;
	private GLUT glut;
	private GLU glu;
	private GL2 gl2;
	private GLUgl2 glu2;
	GLAutoDrawable glAD;

	IntBuffer view;
	DoubleBuffer model, proj;
	double scale;

	IndividualSet iset;
	ArrayList<int[]> vlist;
	OneIndividual picked = null;

	private boolean flag = false;

	int numclusters = 1;
	float brightness = 0.75f;
	double SHRINK = 0.9;

	/*****/
	int psnum = 0;
	double[][][][] points;
	double vrad;
	double[][][] varray = null, barray = null, tarray = null;
	ArrayList<double[]> varraylist, barraylist, tarraylist;
	boolean outlierVertices = true, outlierBoundaries = true, outlierTriangles = true, hideGrayPoints = true;
	boolean isOutlier2 = false;
	/*****/

	/**
	 * Constructor
	 */
	public IndividualSP(GL gl, GL2 gl2, GLU glu, GLUgl2 glu2, GLUT glut, GLAutoDrawable glAD) {
		this.gl = gl;
		this.gl2 = gl2;
		this.glu = glu;
		this.glu2 = glu2;
		this.glut = glut;
		this.glAD = glAD;
	}

	/*****/
	public void changeOutlierVertices() {
		outlierVertices = !outlierVertices;
	}

	public void changeOutlierBoundaries() {
		outlierBoundaries = !outlierBoundaries;
	}

	public void changeOutlierTriangles() {
		outlierTriangles = !outlierTriangles;
	}

	public void hideGrayPoints() {
		hideGrayPoints = !hideGrayPoints;
	}


	public void setIsOutlier2(boolean isOut) {
		isOutlier2 = isOut;
	}

	public void clearArray() {
		if(varraylist != null) {
		varraylist.clear();
		}
		if(barraylist != null) {
		barraylist.clear();
		}
		if(tarraylist != null) {
		tarraylist.clear();
		}
	}


	public void setVarray(ArrayList<double[]> v_array) {
		if(varraylist == null) {
			varraylist = v_array;
		}
		else {
			varraylist.addAll(v_array);
		}
	}

	public void setBarray(ArrayList<double[]> b_array) {
		if(barraylist == null) {
			barraylist = b_array;
		}
		else {
			barraylist.addAll(b_array);
		}
	}

	public void setTarray(ArrayList<double[]> t_array) {
		if(tarraylist == null) {
			tarraylist = t_array;
		}
		else {
			tarraylist.addAll(t_array);
		}
	}

	public void setDrawArg(IntBuffer v, DoubleBuffer m, DoubleBuffer p, double s) {
		if(iset == null || vlist == null) return;
		view = v;  model = m;  proj = p;  scale = s;
	}
	/*****/


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

	public void draw() {
		if(iset == null || vlist == null) return;
		//view = v;  model = m;  proj = p;  scale = s;

		int ncell = (int)(Math.sqrt(vlist.size()) - 1.0e-6) + 1;
		double size = 2.0 / (double)ncell;

		int count = 0;
		/*****/
		IndividualSet ps = iset;
		//if(shopId >= 0) ps = child[shopId];
		psnum = ps.getNumIndividual();
		points = new double[psnum][3][ps.getNumExplain() * ps.getNumObjective()][numclusters];
		/*****/
		for(int i = 0; i < ncell; i++) {
			double y = ((double)i + 0.5) * size - 1.0;
			for(int j = 0; j < ncell; j++, count++) {
				if(count >= vlist.size()) return;
				double x = ((double)j + 0.5) * size - 1.0;
				drawOneSP(x, y, size, count);
			}
		}
	}


	void drawOneSP(double sx, double sy, double size, int pid) {
		int NUMV = 8;
		double VRAD = 0.02 * size / Math.sqrt(scale);
		int xposId = vlist.get(pid)[0];
		int yposId = vlist.get(pid)[1];

		double minx = -0.5 * size * SHRINK + sx;
		double maxx =  0.5 * size * SHRINK + sx;
		double miny = -0.5 * size * SHRINK + sy;
		double maxy =  0.5 * size * SHRINK + sy;
		gl2.glEnable(GL.GL_BLEND);
		gl2.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		gl2.glDepthMask(false);
		gl2.glColor4d(0.5, 0.5, 0.5, 1.0);
		gl2.glBegin(GL.GL_LINE_LOOP);
		gl2.glVertex3d(minx, maxy, 0.0);
		gl2.glVertex3d(minx, miny, 0.0);
		gl2.glVertex3d(maxx, miny, 0.0);
		gl2.glVertex3d(maxx, maxy, 0.0);
		gl2.glEnd();

		double rgb[] = new double[3];

		IndividualSet ps = iset;
		
		//System.out.println(vlist.size());
		/*****/
		//if(psnum == 0) {
			//psnum = ps.getNumIndividual();
		//}
		//points = new double[psnum][3][ps.getNumExplain() * ps.getNumObjective()];
	//}
		/*****/

		// Draw gray dots
		for(int i = 0; i < ps.getNumIndividual(); i++) {
			OneIndividual p = ps.getOneIndividual(i);
			boolean isOut;
			if(hideGrayPoints == true) {
			if(p.isGray() == false) continue;
			p.setOutlier(false);
			if(p.isOutlier() == true) continue;
			rgb[0] = rgb[1] = rgb[2] = 0.8;
			gl2.glColor4d(rgb[0], rgb[1], rgb[2], 1.0);
			double ex[] = p.getExplainValues();
			double ob[] = p.getObjectiveValues();
			double x = (ex[xposId] - ps.explains.min[xposId]) / (ps.explains.max[xposId] - ps.explains.min[xposId]);
			double y = (ob[yposId] - ps.objectives.min[yposId]) / (ps.objectives.max[yposId] - ps.objectives.min[yposId]);
			x = (x - 0.5) * size * SHRINK + sx;
			y = (y - 0.5) * size * SHRINK + sy;
			gl2.glBegin(gl2.GL_POLYGON);
			for (int j = 0; j < NUMV; j++) {
				double xx = VRAD * Math.cos(2.0 * Math.PI * ((double)j/NUMV) ) + x;
				double yy = VRAD * Math.sin(2.0 * Math.PI * ((double)j/NUMV) ) + y;
				gl2.glVertex3d(xx, yy, 0.0);		// 頂点の座標
			}
			gl2.glEnd();
			}
			else {
				if(p.isGray())
				p.setOutlier(true);
			}
		}


		// Draw non-gray dots
		for(int i = 0; i < ps.getNumIndividual(); i++) {
			OneIndividual p = ps.getOneIndividual(i);
			if(p.isGray() == true) continue;
			if(p.isOutlier() == true) continue;
			rgb[0] = rgb[1] = rgb[2] = 0.5;

			boolean isPicked = false;
			if(p == picked) {
				//System.out.println("p == picked");
				isPicked = true;
				if(numclusters >= 2)
					rgb = calcNodeColor(p.getClusterId(), numclusters, 1.0f, 0.25f);
			}
			else if(picked != null){
				if(numclusters >= 2)
					rgb = calcNodeColor(p.getClusterId(), numclusters, 0.75f, 0.8f);
			}
			else {
				if(numclusters >= 2)
					rgb = calcNodeColor(p.getClusterId(), numclusters, 0.75f, 0.8f);
			}
			gl2.glColor4d(rgb[0], rgb[1], rgb[2], 0.9);

			double ex[] = p.getExplainValues();
			double ob[] = p.getObjectiveValues();
			double x = (ex[xposId] - ps.explains.min[xposId]) / (ps.explains.max[xposId] - ps.explains.min[xposId]);
			double y = (ob[yposId] - ps.objectives.min[yposId]) / (ps.objectives.max[yposId] - ps.objectives.min[yposId]);
			x = (x - 0.5) * size * SHRINK + sx;
			y = (y - 0.5) * size * SHRINK + sy;
			/*****/
			int clusterId = p.getClusterId();
			if(clusterId == -1) {
				clusterId = 0;
			}
			for(int j = 0; j < points[i][0][pid].length; j++)  {
				//System.out.println(points[i][0][pid].length);
				if(j == clusterId) {
					points[i][0][pid][j] = x;
					points[i][1][pid][j] = y;
					points[i][2][pid][j] = 0.0;
				}
				else {
					points[i][0][pid][j] = 0.0;
					points[i][1][pid][j] = 0.0;
					points[i][2][pid][j] = 0.0;

				}
				//System.out.println("points[" + i + "][0][" + pid + "][" + j + "] : " + points[i][0][pid][j]);
				//System.out.println("points[" + i + "][1][" + pid + "][" + j + "] : " + points[i][1][pid][j]);
			}

			boolean isOutlier = false;
			if(isOutlier2 == true) {
				if(varraylist != null) {
					for(int j = 0; j < varraylist.size(); j++) {
						if((double)pid == varraylist.get(j)[3]) {
							if(clusterId == varraylist.get(j)[4]) {
								if(varraylist.get(j)[0] == x && varraylist.get(j)[1] == y) {
									isOutlier = true;
									continue;
								}
							}
						}
					}
				}

			}
			/*****/

			gl2.glBegin(gl2.GL_POLYGON);
			vrad = (isPicked == true) ? (VRAD * 1.5) : VRAD;
			if(outlierVertices) {
				vrad = (isOutlier == true) ? (vrad * 2.5) : vrad;
			}
			for (int j = 0; j < NUMV; j++) {
				double xx = vrad * Math.cos(2.0 * Math.PI * ((double)j/NUMV) ) + x;
				double yy = vrad * Math.sin(2.0 * Math.PI * ((double)j/NUMV) ) + y;
				gl2.glVertex3d(xx, yy, 0.1);		// 頂点の座標
			}
			gl2.glEnd();

		}
		flag = false;

		/*****/
		if(isOutlier2 == true) {
			if(outlierBoundaries) {
				if(barraylist != null) {
					rgb[0] = rgb[1] = rgb[2] = 0.25;

					gl2.glBegin(GL.GL_LINES);
					for(int j = 0; j < barraylist.size(); j++) {
						if(numclusters >= 2) {
							rgb = calcNodeColor((int)(barraylist.get(j)[7]), numclusters, 0.75f, 0.50f);
						}
							gl2.glColor4d(rgb[0], rgb[1], rgb[2], 0.5);
							if((double)pid == barraylist.get(j)[6]) {
								gl2.glVertex3d(barraylist.get(j)[0], barraylist.get(j)[1], 0.0);
								gl2.glVertex3d(barraylist.get(j)[3], barraylist.get(j)[4], 0.0);
								//System.out.println("barraylist [0]: " + barraylist.get(j)[0] + ", [1]: " + barraylist.get(j)[1] + ", [3]: " + barraylist.get(j)[3] + ", [4]: " + barraylist.get(j)[4] + ", [6]: " + barraylist.get(j)[6] + ", [7]: " + barraylist.get(j)[7]);
							}
					}
				gl2.glEnd();
				}
			}
			if(outlierTriangles) {
				if(tarraylist != null) {
					rgb[0] = rgb[1] = rgb[2] = 0.5;
					gl2.glBegin(GL.GL_TRIANGLES);
					for(int j = 0; j < tarraylist.size(); j++) {
						if((double)pid == tarraylist.get(j)[9]) {
							double x1 = tarraylist.get(j)[0];
							double y1 = tarraylist.get(j)[1];
							double x2 = tarraylist.get(j)[3];
							double y2 = tarraylist.get(j)[4];
							double x3 = tarraylist.get(j)[6];
							double y3 = tarraylist.get(j)[7];
							double area = 100000 * Math.abs((x1 - x3) * (y2 - y3) - (x2 - x3) * (y1 -y3));
							//if(area > 150) {
							rgb[0] = rgb[1] = rgb[2] = 0.9;
							if(numclusters >= 2) {
								rgb = calcNodeColor((int)(tarraylist.get(j)[10]), numclusters, 0.25f, 1.00f);
							}
							gl2.glColor4d(rgb[0], rgb[1], rgb[2], 0.3);
						/*}
						if(area > 60 && area <= 150) {
							rgb[0] = rgb[1] = rgb[2] = 0.8;
							if(numclusters >= 2) {
								rgb = calcNodeColor((int)(tarraylist.get(j)[10]), numclusters, 0.25f, 0.90f);
							}
							gl2.glColor4d(rgb[0], rgb[1], rgb[2], 0.3);
						}
						if(area > 30 && area <= 60) {
							rgb[0] = rgb[1] = rgb[2] = 0.7;
							if(numclusters >= 2) {
								rgb = calcNodeColor((int)(tarraylist.get(j)[10]), numclusters, 0.25f, 0.80f);
							}
							gl2.glColor4d(rgb[0], rgb[1], rgb[2], 0.3);
						}
						if(area > 0 && area <= 30) {
							rgb[0] = rgb[1] = rgb[2] = 0.6;
							if(numclusters >= 2) {
								rgb = calcNodeColor((int)(tarraylist.get(j)[10]), numclusters, 0.25f, 0.70f);
							}
							gl2.glColor4d(rgb[0], rgb[1], rgb[2], 0.3);
						}*/

						gl2.glVertex3d(x1, y1, 0.0);
						gl2.glVertex3d(x2, y2, 0.0);
						gl2.glVertex3d(x3, y3, 0.0);
					}
				}
				gl2.glEnd();
			}
		}
		}
		/*****/

		gl2.glColor4d(0.0, 0.0, 0.0, 1.0);
		double x1 =  0.0 * size + sx;
		double y1 = -0.5 * size + sy;
		writeOneString(x1, y1, ps.getValueName(xposId));
		double x2 = -0.5 * size + sx;
		double y2 =  0.4 * size + sy;
		writeOneString(x2, y2, ps.getValueName(ps.getNumExplain() + yposId));

	}


	double[] calcNodeColor(int cid, int num, float s, float v) {
		double c[] = new double[3];
		//float hue = (float)p.getClusterId() / (float)numclusters;
		float hue = (float)cid / (float)num + 0.5f;
		Color color = Color.getHSBColor(hue, s, v);
		c[0] = (double)color.getRed() / 255.0;
		c[1] = (double)color.getGreen() / 255.0;
		c[2] = (double)color.getBlue() / 255.0;
		return c;
	}


	void writeOneString(double x, double y, String word) {
		gl2.glRasterPos3d(x, y, 0.0);
		glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, word);
		//glut.glutBitmapString(GLUT.BITMAP_TIMES_ROMAN_24, word);
	}

	public Object pick(int cx, int cy) {
		if(iset == null || vlist == null) return null;

		int ncell = (int)(Math.sqrt(vlist.size()) - 1.0e-6) + 1;
		double size = 2.0 / (double)ncell;

		int count = 0;
		for(int i = 0; i < ncell; i++) {
			double y = ((double)i + 0.5) * size - 1.0;
			for(int j = 0; j < ncell; j++, count++) {
				if(count >= vlist.size()) return null;
				double x = ((double)j + 0.5) * size - 1.0;
				Object p = pickOneSP(cx, cy, x, y, size, count);
				//System.out.println("cx:" + cx + ", cy:" + cy + ", x:" + x + ", y:" + y + ", size:" + size + ", count:" + count);
				if(p != null) return p;
			}
		}

		picked = null;
		return null;
	}




	Object pickOneSP(int cx, int cy, double sx, double sy, double size, int pid) {
		double THRESHOLD = 10.0;
		DoubleBuffer p1 = DoubleBuffer.allocate(3);

		int xposId = vlist.get(pid)[0];
		int yposId = vlist.get(pid)[1];
		cy = view.get(3) - cy + 1;

		double minx = -0.5 * size + sx;
		double maxx =  0.5 * size + sx;
		double miny = -0.5 * size + sy;
		double maxy =  0.5 * size + sy;

		IndividualSet ps = iset;
		
		for(int i = 0; i < ps.getNumIndividual(); i++) {
			OneIndividual p = ps.getOneIndividual(i);

			double ex[] = p.getExplainValues();
			double ob[] = p.getObjectiveValues();
			double x = (ex[xposId] - ps.explains.min[xposId]) / (ps.explains.max[xposId] - ps.explains.min[xposId]);
			double y = (ob[yposId] - ps.objectives.min[yposId]) / (ps.objectives.max[yposId] - ps.objectives.min[yposId]);
			x = (x - 0.5) * size * SHRINK + sx;
			y = (y - 0.5) * size * SHRINK + sy;
			glu2.gluProject(x, y, 0.0, model, proj, view, p1);
			double xx = p1.get(0);
			double yy = p1.get(1);
			double dist = (cx - xx) * (cx - xx) + (cy - yy) * (cy - yy);
			//System.out.println("cx:" + cx + ", cy:" + cy + ", xx:" + xx + ", yy:" + yy);
			if(dist < THRESHOLD) {
				picked = p;  return p;
			}

		}

		picked = null;
		return null;
	}



}
