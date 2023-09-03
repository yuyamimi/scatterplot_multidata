package ocha.itolab.hidden2.applet.spset2;

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


public class IndividualPL {

	private GL gl;
	private GLUT glut;
	private GLU glu;
	private GL2 gl2;
	private GLUgl2 glu2;
	GLAutoDrawable glAD;

	IntBuffer view;
	DoubleBuffer model, proj;

	IndividualSet ps;
	ArrayList<int[]> vlist;
	OneIndividual picked = null;

	private boolean flag = false;

	int numclusters = 1;
	float brightness = 0.75f;

	/**
	 * Constructor
	 */
	public IndividualPL(GL gl, GL2 gl2, GLU glu, GLUgl2 glu2, GLUT glut, GLAutoDrawable glAD) {
		this.gl = gl;
		this.gl2 = gl2;
		this.glu = glu;
		this.glu2 = glu2;
		this.glut = glut;
		this.glAD = glAD;
	}



	public void setValueIdSet(ArrayList<int[]> list) {
		vlist = list;
	}


	public void setIndividualSet(IndividualSet p) {
		ps = p;
	}

	public void setNumClusters(int n) {
		numclusters = n;
	}


	public void draw(IntBuffer v, DoubleBuffer m, DoubleBuffer p) {
		if(ps == null || vlist == null) return;
		view = v;  model = m;  proj = p;

		int ncell = (int)(Math.sqrt(vlist.size()) - 1.0e-6) + 1;
		double size = 2.0 / (double)ncell;

		int count = 0;
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
		int xposId = vlist.get(pid)[0];
		int yposId = vlist.get(pid)[1];

		double minx = -0.5 * size + sx;
		double maxx =  0.5 * size + sx;
		double miny = -0.5 * size + sy;
		double maxy =  0.5 * size + sy;
		gl2.glColor3d(0.5, 0.5, 0.5);
		gl2.glBegin(GL.GL_LINE_LOOP);
		gl2.glVertex3d(minx, maxy, 0.0);
		gl2.glVertex3d(minx, miny, 0.0);
		gl2.glVertex3d(maxx, miny, 0.0);
		gl2.glVertex3d(maxx, maxy, 0.0);
		gl2.glEnd();

		double rgb[] = new double[3];

		for(int i = 0; i < ps.getNumIndividual(); i++) {
			OneIndividual p = ps.getOneIndividual(i);

			rgb[0] = rgb[1] = rgb[2] = 0.5;
			if(p == picked) {
				gl2.glPointSize(8.0f);
				if(numclusters >= 2)
					rgb = calcNodeColor(p.getClusterId(), numclusters, 1.0f, 0.25f);
				else rgb[0] = rgb[1] = rgb[2] = 0.25;
			}
			else if(picked != null){
				gl2.glPointSize(4.0f);
				if(numclusters >= 2)
					rgb = calcNodeColor(p.getClusterId(), numclusters, 0.75f, 0.75f);
				else rgb[0] = rgb[1] = rgb[2] = 0.5;
			}
			else {
				gl2.glPointSize(4.0f);
				if(numclusters >= 2)
					rgb = calcNodeColor(p.getClusterId(), numclusters, 0.75f, 0.75f);
				else rgb[0] = rgb[1] = rgb[2] = 0.5;
			}
			gl2.glColor3d(rgb[0], rgb[1], rgb[2]);

			double ex[] = p.getExplainValues();
			double ob[] = p.getObjectiveValues();
			double x = (ex[xposId] - ps.explains.min[xposId]) / (ps.explains.max[xposId] - ps.explains.min[xposId]);
			double y = (ob[yposId] - ps.objectives.min[yposId]) / (ps.objectives.max[yposId] - ps.objectives.min[yposId]);
			x = (x - 0.5) * size + sx;
			y = (y - 0.5) * size + sy;
			gl2.glBegin(GL.GL_POINTS);
			gl2.glVertex3d(x, y, 0.0);
			gl2.glEnd();
		}
		flag = false;


		gl2.glColor3d(0.0, 0.0, 0.0);
		double x1 =  0.0 * size + sx;
		double y1 = -0.5 * size + sy;
		writeOneString(x1, y1, ps.getValueName(xposId));
		double x2 = -0.5 * size + sx;
		double y2 =  0.0 * size + sy;
		writeOneString(x2, y2, ps.getValueName(ps.getNumExplain() + yposId));


	}


	double[] calcNodeColor(int cid, int num, float s, float v) {
		double c[] = new double[3];
		//float hue = (float)p.getClusterId() / (float)numclusters;
		float hue = (float)cid / (float)num;
		Color color = Color.getHSBColor(hue, s, v);
		c[0] = (double)color.getRed() / 255.0;
		c[1] = (double)color.getGreen() / 255.0;
		c[2] = (double)color.getBlue() / 255.0;
		return c;
	}


	void writeOneString(double x, double y, String word) {
		gl2.glRasterPos3d(x, y, 0.0);
		glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, word);
	}

	public Object pick(int cx, int cy) {
		if(ps == null || vlist == null) return null;

		int ncell = (int)(Math.sqrt(vlist.size()) - 1.0e-6) + 1;
		double size = 2.0 / (double)ncell;

		int count = 0;
		for(int i = 0; i < ncell; i++) {
			double y = ((double)i + 0.5) * size - 1.0;
			for(int j = 0; j < ncell; j++, count++) {
				if(count >= vlist.size()) return null;
				double x = ((double)j + 0.5) * size - 1.0;
				Object p = pickOneSP(cx, cy, x, y, size, count);
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

		for(int i = 0; i < ps.getNumIndividual(); i++) {
			OneIndividual p = ps.getOneIndividual(i);

			double ex[] = p.getExplainValues();
			double ob[] = p.getObjectiveValues();
			double x = (ex[xposId] - ps.explains.min[xposId]) / (ps.explains.max[xposId] - ps.explains.min[xposId]);
			double y = (ob[yposId] - ps.objectives.min[yposId]) / (ps.objectives.max[yposId] - ps.objectives.min[yposId]);
			x = (x - 0.5) * size + sx;
			y = (y - 0.5) * size + sy;
			glu2.gluProject(x, y, 0.0, model, proj, view, p1);
			double xx = p1.get(0);
			double yy = p1.get(1);
			double dist = (cx - xx) * (cx - xx) + (cy - yy) * (cy - yy);
			if(dist < THRESHOLD) {
				picked = p;  return p;
			}

		}

		picked = null;
		return null;
	}



}
