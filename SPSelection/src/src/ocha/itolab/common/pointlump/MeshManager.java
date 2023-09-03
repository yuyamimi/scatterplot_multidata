package ocha.itolab.common.pointlump;

import java.util.ArrayList;

public class MeshManager {
	public Mesh mesh = new Mesh();
	public ArrayList<double[]> varray, barray, tarray;
	public double pid, cid;

	/**
	 * Set positions of vertices
	 */
	public void setVertexSet(double position[][]) {
		mesh.vertices.clear();
		// for each vertex
		for(int i = 0; i < position.length; i++) {
			Vertex v = mesh.addOneVertex();
			v.setPosition(position[i][0], position[i][1], position[i][2]);
			v.setId(i);
		}

		// generate triangles.
		DelaunayMeshGenerator.generate(mesh);
	}

	public void setVertexSet(double position[][], int i, int j) {
		mesh.vertices.clear();
		pid = (double)i;
		cid = (double)j;
		// for each vertex
		//System.out.println("setVertexSet :nump=" + position.length);
		
		for(int k = 0; k < position.length; k++) {
			Vertex v = mesh.addOneVertex();
			v.setPosition(position[k][0], position[k][1], position[k][2]);
			v.setId(k);
		}

		// generate triangles.
		DelaunayMeshGenerator.generate(mesh);
	}
	/*
	public void setVertexSet(double position[][][], int pid) {
		mesh.vertices.clear();
		id = (double)pid;
		// for each vertex
		for(int i = 0; i < position.length; i++) {
			Vertex v = mesh.addOneVertex();
			v.setPosition(position[i][0][pid], position[i][1][pid], position[i][2][pid]);
			v.setId(i);
		}

		// generate triangles.
		DelaunayMeshGenerator.generate(mesh);
	}
	*/

	/**
	 * Divide near and distant vertices
	 */
	public void divideVertices(double threshold) {
		mesh.resetAttribute();
		MeshOperator.deactivateBigTriangles(mesh, threshold, pid, cid);

		varray = new ArrayList<double[]>();
		for(int i = 0; i < MeshOperator.varray.size(); i++)
			varray.add(MeshOperator.varray.get(i));
		//System.out.println("cid: " + cid + "size: " + varray.size());

		barray = new ArrayList<double[]>();
		for(int i = 0; i < MeshOperator.barray.size(); i++)
			barray.add(MeshOperator.barray.get(i));

		tarray = new ArrayList<double[]>();
		for(int i = 0; i < MeshOperator.tarray.size(); i++)
			tarray.add(MeshOperator.tarray.get(i));
	}


	/**
	 * Get a set of distant vertices
	 */
	public ArrayList getVertices() {
		return varray;
	}


	/**
	 * Get a set of boundary edges
	 */
	public ArrayList getBoundaries() {
		return barray;
	}


	/**
	 * Get a set of triangles to be painted
	 */
	public ArrayList getTriangles() {
		return tarray;
	}
}
