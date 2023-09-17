package ocha.itolab.common.pointlump;

import java.util.ArrayList;

public class MeshOperator {
	static ArrayList<double[]> varray = new ArrayList<double[]>();
	static ArrayList<double[]> barray = new ArrayList<double[]>();
	static ArrayList<double[]> tarray = new ArrayList<double[]>();

	/**
	 * Remove big triangles
	 */
	public static void deactivateBigTriangles(Mesh mesh, double threshold) {

		// for each triangle
		for(int i = 0; i < mesh.getNumTriangles(); i++) {
			Triangle t = mesh.getTriangle(i);
			Vertex v[] = t.getVertices();

			// calculate distances between vertex pairs
			boolean isRemove = false;
			for(int j0 = 0; j0 < 3; j0++) {
				int j1 = (j0 == 2) ? 0 : (j0 + 1);
				double p0[] = v[j0].getPosition();
				double p1[] = v[j1].getPosition();
				double dx = p0[0] - p1[0];
				double dy = p0[1] - p1[1];
				double dist = Math.sqrt(dx * dx + dy * dy);
				if(dist > threshold) t.setActive(false);
			}
		}

		// for each triangle
		for(int i = 0; i < mesh.getNumTriangles(); i++) {
			Triangle t = mesh.getTriangle(i);
			if(t.getActive() == false) continue;
			Vertex v[] = t.getVertices();
			for(int j = 0; j < 3; j++)
				v[j].setNear(true);
		}


		// generate vertex array
		varray.clear();
		for(int i = 0; i < mesh.getNumVertices(); i++) {
			Vertex v = mesh.getVertex(i);
			if(v.getNear() == true) continue;
			double[] pos = new double[3];
			double[] pv = v.getPosition();
			pos[0] = pv[0];
			pos[1] = pv[1];
			pos[2] = pv[2];
			varray.add(pos);

		}


		// generate boundary array
		barray.clear();
		for(int i = 0; i < mesh.getNumTriangles(); i++) {
			Triangle t = mesh.getTriangle(i);
			if(t.getActive() == false) continue;
			Triangle at[] = t.getAdjacents();
			Vertex v[] = t.getVertices();
			for(int j = 0; j < 3; j++) {
				if(at[j] != null && at[j].getActive() == true)
					continue;
				double[] pos = new double[6];
				int j1 = (j == 2) ? 0 : (j + 1);
				pos[0] = v[j].pos[0];
				pos[1] = v[j].pos[1];
				pos[2] = v[j].pos[2];
				pos[3] = v[j1].pos[0];
				pos[4] = v[j1].pos[1];
				pos[5] = v[j1].pos[2];
				barray.add(pos);
			}
		}


		// generate triangle array
		tarray.clear();
		for(int i = 0; i < mesh.getNumTriangles(); i++) {
			Triangle t = mesh.getTriangle(i);
			if(t.getActive() == false) continue;
			Vertex v[] = t.getVertices();
			double[] pos = new double[9];
			pos[0] = v[0].pos[0];
			pos[1] = v[0].pos[1];
			pos[2] = v[0].pos[2];
			pos[3] = v[1].pos[0];
			pos[4] = v[1].pos[1];
			pos[5] = v[1].pos[2];
			pos[6] = v[2].pos[0];
			pos[7] = v[2].pos[1];
			pos[8] = v[2].pos[2];
			tarray.add(pos);
		}
	}

	static void deactivateBigTriangles(Mesh mesh, double threshold, double pid, double cid) {

		// for each triangle
		for(int i = 0; i < mesh.getNumTriangles(); i++) {
			Triangle t = mesh.getTriangle(i);
			Vertex v[] = t.getVertices();

			// calculate distances between vertex pairs
			boolean isRemove = false;
			for(int j0 = 0; j0 < 3; j0++) {
				int j1 = (j0 == 2) ? 0 : (j0 + 1);
				double p0[] = v[j0].getPosition();
				double p1[] = v[j1].getPosition();
				double dx = p0[0] - p1[0];
				double dy = p0[1] - p1[1];
				double dist = Math.sqrt(dx * dx + dy * dy);
				if(dist > threshold) t.setActive(false);
			}
		}

		// for each triangle
		for(int i = 0; i < mesh.getNumTriangles(); i++) {
			Triangle t = mesh.getTriangle(i);
			if(t.getActive() == false) continue;
			Vertex v[] = t.getVertices();
			for(int j = 0; j < 3; j++)
				v[j].setNear(true);
		}


		// generate vertex array
		varray.clear();
		for(int i = 0; i < mesh.getNumVertices(); i++) {
			Vertex v = mesh.getVertex(i);
			if(v.getNear() == true) continue;
			double[] pos = new double[5];
			double[] pv = v.getPosition();
			pos[0] = pv[0];
			pos[1] = pv[1];
			pos[2] = pv[2];
			pos[3] = pid;
			pos[4] = cid;
			varray.add(pos);
		}


		// generate boundary array
		barray.clear();
		for(int i = 0; i < mesh.getNumTriangles(); i++) {
			Triangle t = mesh.getTriangle(i);
			if(t.getActive() == false) continue;
			Triangle at[] = t.getAdjacents();
			Vertex v[] = t.getVertices();
			for(int j = 0; j < 3; j++) {
				if(at[j] != null && at[j].getActive() == true)
					continue;
				double[] pos = new double[8];
				int j1 = (j == 2) ? 0 : (j + 1);
				pos[0] = v[j].pos[0];
				pos[1] = v[j].pos[1];
				pos[2] = v[j].pos[2];
				pos[3] = v[j1].pos[0];
				pos[4] = v[j1].pos[1];
				pos[5] = v[j1].pos[2];
				pos[6] = pid;
				pos[7] = cid;
				barray.add(pos);
				//System.out.println("MeshOperation: barray [" + barray.size() + "] (" + pos[0] + "," + pos[1] + ")(" + pos[3] + "," + pos[4] + ")");
			}
		}


		// generate triangle array
		tarray.clear();
		for(int i = 0; i < mesh.getNumTriangles(); i++) {
			Triangle t = mesh.getTriangle(i);
			if(t.getActive() == false) continue;
			Vertex v[] = t.getVertices();
			double[] pos = new double[11];
			pos[0] = v[0].pos[0];
			pos[1] = v[0].pos[1];
			pos[2] = v[0].pos[2];
			pos[3] = v[1].pos[0];
			pos[4] = v[1].pos[1];
			pos[5] = v[1].pos[2];
			pos[6] = v[2].pos[0];
			pos[7] = v[2].pos[1];
			pos[8] = v[2].pos[2];
			pos[9] = pid;
			pos[10] = cid;
			tarray.add(pos);
		}
	}
}
