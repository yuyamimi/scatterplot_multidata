package ocha.itolab.common.pointlump;

public class Vertex {
	double pos[] = new double[3];
	int id;
	int counter;
	boolean isNear = true;


	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}


	public void setPosition(double x, double y, double z) {
		pos[0] = x;   pos[1] = y;   pos[2] = z;
	}

	public double[] getPosition() {
		return pos;
	}

	public void setCounter(int c) {
		counter = c;
	}

	public int getCounter() {
		return counter;
	}

	public void setNear(boolean n) {
		isNear = n;
	}

	public boolean getNear() {
		return isNear;
	}
}
