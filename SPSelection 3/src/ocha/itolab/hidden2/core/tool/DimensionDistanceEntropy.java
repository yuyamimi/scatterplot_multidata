package ocha.itolab.hidden2.core.tool;

import ocha.itolab.hidden2.core.data.*;

public class DimensionDistanceEntropy {
	static IndividualSet iset = null;
	static int NUM_CELL = 4;
	static double earray[]; 
	static int classId;
	
	static double calcEntropyOnePair(IndividualSet is, int id1, int id2) {
		iset = is;
		classId = iset.getClassId();
		
		
		// if classId is not specified
		if(classId < 0 || classId >= (iset.getNumCategory() + iset.getNumBoolean())) {
			return 0.0;
		}

		// allocate arrays
		int numc = 0;
		if(classId < iset.getNumCategory())
			numc = iset.categories.categories[classId].length;
		else
			numc = 2;
		int counter[][] = new int[NUM_CELL * NUM_CELL][numc];
	
		double ediff = iset.explains.max[id1] - iset.explains.min[id1];
		double odiff = iset.objectives.max[id2] - iset.objectives.min[id2];
		
		// for each individual
		for(int i = 0; i < iset.getNumIndividual(); i++) {
			
			// specify the position of the individual in the grid
			OneIndividual oi = iset.getOneIndividual(i);
			double eval = oi.explain[id1];
			double oval = oi.objective[id2];
			eval = (eval - iset.explains.min[id1]) / ediff;
			oval = (oval - iset.objectives.min[id2]) / odiff;
			int evid = (int)(eval * NUM_CELL);
			if(evid < 0) evid = 0;
			if(evid >= NUM_CELL) evid = NUM_CELL - 1;
			int ovid = (int)(oval * NUM_CELL);
			if(ovid < 0) ovid = 0;
			if(ovid >= NUM_CELL) ovid = NUM_CELL - 1;
			
			//System.out.println(" vid[" + id1 + "," + id2 + "] evid=" + evid + " ovid=" + ovid);
			
			// increment the counter
			int cid = -1;
			if(classId < iset.getNumCategory()) {
				cid = getCategoryId(oi, classId);
				if(cid < 0) continue;
			}
			else {
				boolean bval = oi.bool[classId - iset.getNumCategory()];
				cid = (bval == true) ? 1 : 0;
			}
			counter[ovid * NUM_CELL + evid][cid] += 1;
		}
		
		// for each cell
		double vsum = 0.0;  int ccount = 0;
		for(int i = 0; i < NUM_CELL * NUM_CELL; i++) {
			int countsum = 0;
			for(int j = 0; j < counter[i].length; j++)
				countsum += counter[i][j];
			if(countsum <= 0) continue;
			
			// calculate entropy
			double psum = 0.0;
			for(int j = 0; j < counter[i].length; j++) {
				if(counter[i][j] == 0) continue;
				double p = (double)counter[i][j] / (double)countsum;
				psum += p * Math.log(p);
			}
			vsum += psum;
			ccount++;
		}
		
		if(ccount == 0) return 1.0e+30;
		//double ret = vsum / (double)(-ccount);
		double ret = vsum / (double)ccount + 1.0;
		return ret;
	}
	
	
	/**
	 * Return category ID corresponding to the category value of an individual
	 */
	static int getCategoryId(OneIndividual oi, int classId) {
		String cval = oi.category[classId];
		String carray[] = iset.categories.categories[classId];
		for(int i = 0; i < carray.length; i++) {
			if(cval.compareTo(carray[i]) == 0)
				return i;
		}
		
		return -1;
	}
	
	
}
