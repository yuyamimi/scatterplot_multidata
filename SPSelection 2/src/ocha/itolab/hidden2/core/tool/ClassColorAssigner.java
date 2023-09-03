package ocha.itolab.hidden2.core.tool;

import ocha.itolab.hidden2.core.data.*;

public class ClassColorAssigner {
	static IndividualSet iset;
	static int classId = -1;
	
	public static int assign(IndividualSet is) {
		iset = is;
		classId = is.getClassId();
		
		// Set color ID for each individual
		if(classId >= 0 && classId < is.getNumCategory()) {
			setCategoryColor();
			return is.categories.categories[classId].length;
		}
		else if(classId >= 0 && classId < (is.getNumBoolean() + is.getNumCategory())) {
			setBooleanColor();
			return 2;
		}
		
		for(int i = 0; i < iset.getNumIndividual(); i++) {
			OneIndividual p = iset.getOneIndividual(i);
			p.setClusterId(-1);
		}
		return -1;
		
	}
	
	
	/**
	 * Set color ID for each individual based on the category variable
	 */
	static void setCategoryColor() {
		String[] cname = iset.categories.categories[classId];
		
		// for each individual
		for(int i = 0; i < iset.getNumIndividual(); i++) {
			OneIndividual p = iset.getOneIndividual(i);
			p.setClusterId(-1);
			String pname[] = p.getCategoryValues();
			for(int j = 0; j < cname.length; j++) {
				if(pname[classId].compareTo(cname[j]) == 0) {
					p.setClusterId(j);  break;
				}
			}
		}
		
		
	}
	
	
	/**
	 * Set color ID for each individual based on the boolean variable
	 */
	static void setBooleanColor() {
		int variableId = classId - iset.getNumCategory();
		
		// for each individual
		for(int i = 0; i < iset.getNumIndividual(); i++) {
			OneIndividual p = iset.getOneIndividual(i);
			p.setClusterId(-1);
			boolean bvalue[] = p.getBooleanValues();
			if(variableId < 0 || variableId >= iset.getNumBoolean())
				p.setClusterId(-1);
			else {
				if(bvalue[variableId] == true) 
					p.setClusterId(1);
				else
					p.setClusterId(0);
			}
		}
		
		
		
	}
}
