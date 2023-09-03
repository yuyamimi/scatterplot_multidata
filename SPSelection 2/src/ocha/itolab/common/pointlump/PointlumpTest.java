package ocha.itolab.common.pointlump;

import java.util.ArrayList;

import ocha.itolab.hidden2.applet.spset2.IndividualSP;

public class PointlumpTest {
	static double[][] points;
	static double[][][][] pointsall;
	static double[][] setpoints;
	public static ArrayList<double[]> varray, barray, tarray;
	static double threshold;
	/*****/
	IndividualSP dsp;
	/*****/

	/*
	public void pointlump(IndividualSP d) {
		dsp = d;
		pointsall = dsp.getPoints();
		for(int i = 0;i < pointsall[0][0].length;i++) {
			// TODO: Replace by your own function
			generatePoints(dsp, i);

			// Generate mesh
			MeshManager mm = new MeshManager();

			mm.setVertexSet(points, i);

		// TODO: Replace by your own one
		//for(int i = 0; i < 10; i++) {

			// TODO: Replace by your own one
			threshold = 0.1;  //Math.random() * 0.2;

			// Get sets of vertices
			mm.divideVertices(threshold);
			varray = mm.getVertices();
			barray = mm.getBoundaries();
			tarray = mm.getTriangles();
			// TODO: Repace by your own one
			//printResult(dsp);
		}
		//}

	}
	*/

	public void pointlump(IndividualSP d) {
		dsp = d;
		pointsall = dsp.getPoints();
		if(pointsall != null) {
			if(varray != null) {
				varray.clear();
			}
			if(barray != null) {
				barray.clear();
			}
			if(tarray != null) {
				tarray.clear();
			}
			for(int i = 0;i < pointsall[0][0].length;i++) {
				for(int j = 0;j < pointsall[0][0][0].length;j++) {
				// TODO: Replace by your own function
					generatePoints(dsp, i, j);

				// Generate mesh
					MeshManager mm = new MeshManager();

					mm.setVertexSet(points, i, j);

				// TODO: Replace by your own one
				//for(int i = 0; i < 10; i++) {

				// TODO: Replace by your own one
				//threshold = v;  //Math.random() * 0.2;

				// Get sets of vertices
					mm.divideVertices(threshold);
					if(varray == null) {
						varray = mm.getVertices();
					}
					else {
						varray.addAll(mm.getVertices());
					}
					if(barray == null) {
						barray = mm.getBoundaries();
					}
					else {
						barray.addAll(mm.getBoundaries());
					}

					if(tarray == null) {
						tarray = mm.getTriangles();
					}
					else {
						tarray.addAll(mm.getTriangles());
					}

				// TODO: Repace by your own one

				//printResult(dsp);
				}
			}
			
			dsp.setVarray(varray);
			dsp.setBarray(barray);
			dsp.setTarray(tarray);
		}

	}

	
	public void pointlump(double[][][][] p) {
		pointsall = p;
		if(pointsall != null) {
			if(varray != null) {
				varray.clear();
			}
			if(barray != null) {
				barray.clear();
			}
			if(tarray != null) {
				tarray.clear();
			}
			for(int i = 0;i < pointsall[0][0].length;i++) {
				for(int j = 0;j < pointsall[0][0][0].length;j++) {
				// TODO: Replace by your own function
					generatePoints(dsp, i, j);

				// Generate mesh
					MeshManager mm = new MeshManager();

					mm.setVertexSet(points, i, j);

				// TODO: Replace by your own one
				//for(int i = 0; i < 10; i++) {

				// TODO: Replace by your own one
				//threshold = v;  //Math.random() * 0.2;

				// Get sets of vertices
					mm.divideVertices(threshold);
					if(varray == null) {
						varray = mm.getVertices();
					}
					else {
						varray.addAll(mm.getVertices());
					}
					if(barray == null) {
						barray = mm.getBoundaries();
					}
					else {
						barray.addAll(mm.getBoundaries());
					}

					if(tarray == null) {
						tarray = mm.getTriangles();
					}
					else {
						tarray.addAll(mm.getTriangles());
					}

				// TODO: Repace by your own one

				//printResult(dsp);
				}
			}
			
		}

	}

	
	
	public void setThreshold(double v) {
		threshold = v;
	}



	/**
	 * Calculate positions of points randomly
	 * (TODO: Replace this function by your own one)
	 */
	static void generatePoints(IndividualSP dsp, int pid, int cid) {
		/*int NUMP = 200;
		points = new double[NUMP][3];

		for(int i = 0; i < NUMP; i++) {
			points[i][0] = Math.random();
			points[i][1] = Math.random();
			points[i][2] = 0.0;
		}*/
		int count = 0, countmax = 0;
		for(int i = 0;i < pointsall.length;i++) {
			for(int j = 0;j < pointsall[0].length;j++) {
				if(pointsall[i][j][pid][cid] != 0) {
					count++;
				}
			}
			if(count > countmax) countmax = count;
		}
		points = new double[countmax][pointsall[0].length];
		//System.out.println("points[" + count + "][" + pointsall[0].length + "], pid:" + pid + ", cid:" + cid);
		count = 0;
		for(int i = 0;i < pointsall.length;i++) {
			for(int j = 0;j < pointsall[0].length;j++) {
				if(pointsall[i][j][pid][cid] != 0) {
					//if(count/2>=points.length)
					//	System.out.println("???? count=" + count);
					points[count/2][j] = pointsall[i][j][pid][cid];
					//System.out.println("points[" + count/2 + "][" + j + "]:" + points[count/2][j]);
					//System.out.println("pointsall[" + i + "][" + j + "][" + pid + "][" + cid + "]:" + pointsall[i][j][pid][cid]);
					count++;
				}
				//System.out.println(i + ", " + j + ", " + pid + ", " + cid + ", " + points[i][j]);
				//System.out.println("points[" + i + "][" + j + "] = " + points[i][j]);
			}
		}
		//System.out.println(cid + ": " + count/2);
	}


	/**
	 * Print the result
	 * (TODO: Replace this function by your own one)
	 */
	static void printResult(IndividualSP dsp) {
		//System.out.println("********************* threshold=" + threshold);

		// print num. vertices
		System.out.print("  v=" + varray.size());
		for(int i = 0; i < varray.size(); i++) {
			double pos[] = varray.get(i);
			System.out.println(", [" + pos[0] + "," + pos[1] + "] ");
		}

		System.out.println("");

		// print boundary vertices
		//System.out.println("  b=" + barray.size());
		for(int i = 0; i < barray.size(); i++) {
			double pos[] = barray.get(i);
			//System.out.print("[" + pos[0] + "," + pos[1] + "," + pos[3] + "," + pos[4] + "] ");
		}
		//System.out.println("");

		// print triangle vertices
		//System.out.println("  t=" + tarray.size());
		for(int i = 0; i < tarray.size(); i++) {
			double pos[] = tarray.get(i);
			//System.out.print("[" + pos[0] + "," + pos[1] + "," + pos[3] + "," + pos[4] +  "," + pos[6] + "," + pos[7] + "] ");
		}
		//System.out.println("");
	}


}
