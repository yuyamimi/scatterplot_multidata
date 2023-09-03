package ocha.itolab.common.pointlump;

public class Triangle {
	Vertex vertices[] = new Vertex[3];
	Triangle adjacents[] = new Triangle[3];
	int id;
	boolean isActive = true;
	
	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}


	public void setVertices(Vertex v1, Vertex v2, Vertex v3) {
		vertices[0] = v1;   vertices[1] = v2;   vertices[2] = v3;
	}

	public Vertex[] getVertices() {
		return vertices;
	}

	public void setAdjacents(Triangle t1, Triangle t2, Triangle t3) {
		adjacents[0] = t1;   adjacents[1] = t2;   adjacents[2] = t3;
	}

	public Triangle[] getAdjacents() {
		return adjacents;
	}

	public void setActive(boolean a) {
		isActive = a;
	}
	
	public boolean getActive() {
		return isActive;
	}
	
	
	/*
	public double Menseki(){
		double pos1[] = getVertices()[0].getPosition() ;
		double pos2[] = getVertices()[1].getPosition() ;
		double pos3[] = getVertices()[2].getPosition() ;
		double a=Math.sqrt((pos1[0]-pos2[0])*(pos1[0]-pos2[0])+(pos1[1]-pos2[1])*(pos1[1]-pos2[1])+(pos1[2]-pos2[2])*(pos1[2]-pos2[2]));
		double b=Math.sqrt((pos3[0]-pos2[0])*(pos3[0]-pos2[0])+(pos3[1]-pos2[1])*(pos3[1]-pos2[1])+(pos3[2]-pos2[2])*(pos3[2]-pos2[2]));
		double c=Math.sqrt((pos1[0]-pos3[0])*(pos1[0]-pos3[0])+(pos1[1]-pos3[1])*(pos1[1]-pos3[1])+(pos1[2]-pos3[2])*(pos1[2]-pos3[2]));
		double s=(a+b+c)/2;

		if(a <= 1.0e-10 || b <= 1.0e-10 || c <= 1.0e-10){
			return 0.0;
		}

		else{
			double S=0;
			double cosA=(b*b+c*c-a*a)/(2*b*c);
			double cosB=(c*c+a*a-b*b)/(2*c*a);
			double cosC=(a*a+b*b-c*c)/(2*a*b);

			if(cosA*cosA<=1){
				double sinA=Math.sqrt(1-cosA*cosA);
				S=0.5*b*c*sinA;
			}		
			else if(cosB*cosB<=1){
				double sinB=Math.sqrt(1-cosB*cosB);
				S=0.5*c*a*sinB;
			}
			else if(cosC*cosC<=1){
				double sinC=Math.sqrt(1-cosC*cosC);
				S=0.5*a*b*sinC;
			}
			else{
			}
			return S;
		}
	}
	*/
}
