package ocha.itolab.hidden2.core.tool;

import ocha.itolab.hidden2.core.data.*;
import ocha.itolab.common.pointlump.*;

public class DimensionDistanceSkinny {
	static IndividualSet iset = null;
	static int NUM_CELL = 4;
	static double earray[]; 
	static int classId;
	
	public static double calcSkinnyOnePair(IndividualSet is, int id1, int id2) {
		iset = is;
		Mesh mesh = DimensionMeshGenerator.generateMesh(is, id1, id2, 0.1);
		
		double area = calcTriangleArea(mesh);
		double len = calcBorderLength(mesh);
		//double val = 1.0 - area / len;
		double val = 2.0 * Math.sqrt(Math.PI * area) / len;
		//System.out.println("   id1=" + id1 + " id2=" + id2 + " area=" + area + " len=" + len + " val=" + val);
		return val;
	}
	
	
	static double calcTriangleArea(Mesh mesh) {
		double area = 0.0;
		
		// for each triangle
		for(int i = 0; i < mesh.getNumTriangles(); i++) {
			Triangle tri = mesh.getTriangle(i);
			Vertex v[] = tri.getVertices();
			double x10 = v[1].getPosition()[0] - v[0].getPosition()[0];
			double x20 = v[2].getPosition()[0] - v[0].getPosition()[0];
			double y10 = v[1].getPosition()[1] - v[0].getPosition()[1];
			double y20 = v[2].getPosition()[1] - v[0].getPosition()[1];
			area += (0.5 * Math.abs(x10 * y20 - y10 * x20));
		}
		
		return area;
	}
	
	static double calcBorderLength(Mesh mesh) {
		double len = 0.0;
		
		// for each triangle
		for(int i = 0; i < mesh.getNumTriangles(); i++) {
			Triangle tri = mesh.getTriangle(i);
			Triangle adj[] = tri.getAdjacents();
			Vertex vtx[] = tri.getVertices();
			
			// for each adjacency
			for(int j = 0; j < 3; j++) {
				if(adj[j] != null) continue;
				int j1 = (j == 2) ? 0 : (j + 1);
				double dx = vtx[j].getPosition()[0] - vtx[j1].getPosition()[0];
				double dy = vtx[j].getPosition()[1] - vtx[j1].getPosition()[1];
				len += Math.sqrt(dx * dx + dy * dy);
			}
			
		}
		return len;
	}
	
}
