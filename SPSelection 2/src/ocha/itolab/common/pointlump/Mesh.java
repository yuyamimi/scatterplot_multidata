package ocha.itolab.common.pointlump;

import java.util.ArrayList;

public class Mesh {
	ArrayList<Vertex> vertices;
	ArrayList<Triangle> triangles; 
	
	public Mesh() {
		vertices = new ArrayList<Vertex>();
		triangles = new ArrayList<Triangle>();
	}
	
	
	public Vertex addOneVertex() {
		Vertex vertex = new Vertex();
		vertex.setId(vertices.size());
		vertices.add(vertex);
		return vertex;
	}
	
	public void removeOneVertex(Vertex vertex) {
		int id = vertex.getId();
		vertices.remove(vertex);
		for(int i = id; i < vertices.size(); i++) {
			Vertex v = (Vertex)vertices.get(i);
			v.setId(i);
		}
	}
	
	public ArrayList getVertices() {
		return vertices;
	}
	
	public Vertex getVertex(int id) {
		return (Vertex)vertices.get(id);
	}

	public int getNumVertices() {
		return vertices.size();
	}
	
	public Triangle addOneTriangle() {
		Triangle triangle = new Triangle();
		triangle.setId(triangles.size());
		triangles.add(triangle);
		return triangle;
	}
	
	public void removeOneTriangle(Triangle triangle) {
		
		for(int i = 0; i < 3; i++) {
			Triangle adj = triangle.adjacents[i];
			if(adj != null) {
				for(int j = 0; j < 3; j++) {
					if(adj.adjacents[j] == triangle)
						adj.adjacents[j] = null;
				}
			}
		}
		
		int id = triangle.getId();
		triangles.remove(triangle);
		for(int i = id; i < triangles.size(); i++) {
			Triangle t = (Triangle)triangles.get(i);
			t.setId(i);
		}
	}
	
	public ArrayList getTriangles() {
		return triangles;
	}
	
	public Triangle getTriangle(int id) {
		return (Triangle)triangles.get(id);
	}

	public int getNumTriangles() {
		return triangles.size();
	}
	
	public void resetAttribute() {
		for(int i = 0; i < vertices.size(); i++)
			vertices.get(i).setNear(false);
		for(int i = 0; i < triangles.size(); i++)
			triangles.get(i).setActive(true);
	}
	

}
