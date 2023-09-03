package ocha.itolab.hidden2.core.tool;

import ocha.itolab.common.pointlump.*;
import ocha.itolab.hidden2.core.data.*;

public class DimensionMeshGenerator {

	public static Mesh generateMesh(IndividualSet iset, int id1, int id2, double threshold) {
		Mesh mesh = new Mesh();
		double xmin = 1.0e+30, xmax = -1.0e+30, ymin = 1.0e+30, ymax = -1.0e+30; 
		
		// determin min-max of x and y
		for(int i = 0; i < iset.getNumIndividual(); i++) {
			OneIndividual oi = iset.getOneIndividual(i);
			double x = oi.explain[id1];
			double y = oi.objective[id2];
			xmin = (xmin < x) ? xmin : x;
			xmax = (xmax > x) ? xmax : x;
			ymin = (ymin < y) ? ymin : y;
			ymax = (ymax > y) ? ymax : y;
		}
		
		// generates vertices of the mesh
		double dx = xmax - xmin;
		double dy = ymax - ymin;
		for(int i = 0; i < iset.getNumIndividual(); i++) {
			OneIndividual oi = iset.getOneIndividual(i);
			double x = oi.explain[id1];
			double y = oi.objective[id2];
			x = (x - xmin) / dx;
			y = (y - ymin) / dy;
			Vertex vtx = mesh.addOneVertex();
			vtx.setPosition(x, y, 0.0);
		}
		
		DelaunayMeshGenerator.generate(mesh);
		MeshOperator.deactivateBigTriangles(mesh, threshold);
		return mesh;
	}

}
