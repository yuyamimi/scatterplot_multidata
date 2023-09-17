package ocha.itolab.hidden2.core.data;

import java.util.ArrayList;

public class ExplainSet {
	public String names[];
	public ArrayList<OneExplain> dimensions;
	public double min[], max[];
	
	
	/**
	 * Constructor
	 */
	public ExplainSet(IndividualSet iset) {
		
		// Copy names
		names = new String[iset.numExplain];
		for(int i = 0; i < names.length; i++) {
			names[i] = iset.getValueName(i);
		}
		
		dimensions = new ArrayList();
		int counter = -1;
		for(int i = 0; i < iset.numExplain; i++) {
			OneExplain d = new OneExplain(iset.numObjective);
			d.id = i;
			dimensions.add(d);
			while(true) {
				counter++;
				if(counter >= iset.numTotal) break;
				if(iset.type[counter] == iset.TYPE_EXPLAIN) {
					d.setName(iset.getValueName(counter));
					break;
				}
			}
		}
		
		min = new double[iset.numExplain];
		max = new double[iset.numExplain];
	}
	
	
	public int getNumNumeric() {
		return dimensions.size();
	}
	
}
