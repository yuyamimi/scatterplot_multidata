package ocha.itolab.hidden2.core.data;

import java.util.ArrayList;

public class ObjectiveSet {
	public String names[];
	public ArrayList<OneObjective> dimensions;
	public double min[], max[];


	/**
	 * Constructor
	 */
	public ObjectiveSet(IndividualSet iset) {

		// Copy names
		names = new String[iset.numObjective];
		for(int i = 0; i < names.length; i++) {
			names[i] = iset.getValueName(i + iset.numExplain);
		}

		dimensions = new ArrayList();
		int counter = -1;
		for(int i = 0; i < iset.numObjective; i++) {
			OneObjective d = new OneObjective(iset.numExplain);
			d.id = i;
			dimensions.add(d);
			while(true) {
				counter++;
				if(counter >= iset.numTotal) break;
				if(iset.type[counter] == iset.TYPE_OBJECTIVE) {
					d.setName(iset.getValueName(counter));
					break;
				}
			}
		}

		min = new double[iset.numObjective];
		max = new double[iset.numObjective];
	}


	public int getNumObjective() {
		return dimensions.size();
	}

}
