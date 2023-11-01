package ocha.itolab.hidden2.core.data;

public class OneIndividual {
	public double explain[];
	public double objective[];
	public String category[];
	public boolean bool[];
	public int id;
	public String name;
	public double dissim[];

	boolean isGray = false, isOutlier = false;
	int clusterId;
	double x, y;

	/**
	 * Constructor
	 * @param numTotal
	 */
	public OneIndividual(int numExplain, int numObjective, int numCategory, int numBoolean, int id) {
		explain = new double[numExplain];
		objective = new double[numObjective];
		category = new String[numCategory];
		bool = new boolean[numBoolean];
		dissim = null;
		this.id = id;
		this.clusterId = -1;
	}


	public void setExplainValue(int id, double v) {
		explain[id] = v;
	}

	public void setObjectiveValue(int id, double v) {
		objective[id] = v;
	}


	public void setCategoryValue(int id, String v) {
		category[id] = v;
	}

	public void setBooleanValue(int id, boolean v) {
		bool[id] = v;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double[] getExplainValues() {
		return explain;
	}

	public double[] getObjectiveValues() {
		return objective;
	}

	public String[] getCategoryValues() {
		return category;
	}

	public boolean[] getBooleanValues() {
		return bool;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getClusterId() {
		return clusterId;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public void setClusterId(int id) {
		clusterId = id;
	}

	public void setPosition(double x, double y) {
		this.x = x;   this.y = y;
	}


	public void setDissimilarity(double v, int id, int num) {
		if(dissim == null || dissim.length != num)
			dissim = new double[num];
		dissim[id] = v;
	}

	public double getDissimilarity(int id) {
		return dissim[id];
	}

	public void setGray(boolean g) {
		isGray = g;
	}

	public boolean isGray() {
		if(isGray) {
			//System.out.println("isGraytrue");
		}
		/*if(!isGray) {
			System.out.println("false");
		}*/
		return isGray;
	}

	public void setOutlier(boolean o) {
		isOutlier = o;
	}

	public boolean isOutlier() {
		return isOutlier;
	}
}
