package ocha.itolab.hidden2.core.tool;

import java.util.*;
import ocha.itolab.common.pointlump.*;
import ocha.itolab.hidden2.core.data.IndividualSet;

public class DimensionDistanceClumpy {

	public static double calcClumpyOnePair(IndividualSet is, int id1, int id2) {
		double val = 0.0;
		Mesh mesh = DimensionMeshGenerator.generateMesh(is, id1, id2, 0.1);
		
		LengthComparator lcomp = new LengthComparator();
		TreeSet<Edge> treeSet = new TreeSet<Edge>(lcomp);
		
		int nedge = 0;
		for(int i = 0; i < mesh.getNumTriangles(); i++) {
			Triangle t = mesh.getTriangle(i);
			Vertex v[] = t.getVertices();
			for(int j = 0; j < 3; j++) {
				int j1 = (j == 2) ? 0 : (j + 1);
				Edge e = new Edge();
				e.v1 = v[j];
				e.v2 = v[j1];
				e.id = nedge++;
				double dx = e.v1.getPosition()[0] - e.v2.getPosition()[0];
				double dy = e.v1.getPosition()[1] - e.v2.getPosition()[1];
				e.length = Math.sqrt(dx * dx + dy * dy);
				treeSet.add(e);
			}
		}
		
		int nume = treeSet.size();
		if (nume <= 0) return 0.0;
		
		Edge e0 = null, e1;
		Iterator<Edge> iterator = treeSet.iterator();
		while(iterator.hasNext()) {
			e1 = iterator.next();
			if(e1.length < 1.0e-8) continue;
			if(e0 != null) {
				double vv = 1.0 - e0.length / e1.length;
				if(vv > val) {
					val = vv;
					//System.out.println(" e0=" + e0.length + " e1=" + e1.length + " v=" + vv);	
				}
			}
			e0 = e1;
		}
		
		return val;
	}
	
	
	static class LengthComparator implements Comparator {

		public LengthComparator() {
		}

		public int compare(Object obj1, Object obj2) {

			Edge edge1 = (Edge) obj1;
			Edge edge2 = (Edge) obj2;

			if (edge1.length - edge2.length > 1.0e-20)
				return 1;
			if (edge2.length - edge1.length > 1.0e-20)
				return -1;

			int key = (edge1.id > edge2.id) ? 1 : -1;
			return key;
		}

	}
	
	
	static class Edge {
		Vertex v1, v2;
		int id;
		double length;
	}
}
