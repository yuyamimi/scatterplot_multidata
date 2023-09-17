package ocha.itolab.hidden2.core.tool;

import java.util.ArrayList;
import ocha.itolab.hidden2.core.data.*;

public class DimensionDistanceCorrelation {
	static IndividualSet iset = null;
	
	static double calcCorrelationOnePair(IndividualSet is, int id1, int id2) {
		iset = is;
		ArrayList<double[]> list1 = new ArrayList();
		ArrayList<double[]> list2 = new ArrayList();
		int classId = iset.getClassId();
		
		// for each individual
		if(classId < 0 || classId >= (iset.getNumCategory() + iset.getNumBoolean())) {
			aggregateValue(list1, list2, -1, -1, id1, id2);
			double r = calcCorrelationWithList(list1, list2);
			return r;
		}
		else if(classId < iset.getNumCategory()) {
			double rmin = 1.0e+30;
			String[] cname = iset.categories.categories[classId];
			for(int i = 0; i < cname.length; i++) {
				list1.clear();   list2.clear();
				aggregateValue(list1, list2, classId, i, id1, id2);
				double r = calcCorrelationWithList(list1, list2);
				rmin = (rmin < r) ? rmin : r;
			}
			return rmin;
		}
		else {
			list1.clear();   list2.clear();
			aggregateValue(list1, list2, iset.getClassId(), 0, id1, id2);
			double r1 = calcCorrelationWithList(list1, list2);
			list1.clear();   list2.clear();
			aggregateValue(list1, list2, iset.getClassId(), 1, id1, id2);
			double r2 = calcCorrelationWithList(list1, list2);
			if(r1 < r2) return r1;
			return r2;
		}
		
	
	}
	
	
	static void aggregateValue(ArrayList<double[]> list1, ArrayList<double[]> list2,
							int classId, int cid, int id1, int id2) {

		list1.clear();   list2.clear();
		String[] cname = null;
		if(classId >= 0) cname= iset.categories.categories[classId];
		
		for(int i = 0; i < iset.getNumIndividual(); i++) {
			OneIndividual p = iset.getOneIndividual(i);
			if(classId >= 0) {
				if(classId < iset.getNumCategory()) {
					String pname[] = p.getCategoryValues();
					if(pname[classId].compareTo(cname[cid]) != 0) 
						continue;
				}
				else {
					boolean bvalue[] = p.getBooleanValues();
					if(bvalue[classId] == true  && cid == 0) continue;
					if(bvalue[classId] == false && cid == 1) continue;
				}
			}
				
			double s1[] = new double[1];
			double s2[] = new double[1];
			double explain[] = p.getExplainValues();
			double objective[] = p.getObjectiveValues();
			s1[0] = explain[id1];
			s2[0] = objective[id2];
			list1.add(s1);
			list2.add(s2);
		}
	}
	
	
	static double calcCorrelationWithList(ArrayList<double[]> list1, ArrayList<double[]> list2) {
		int n = list1.size();
		int count = 0;
		
		for(int i = 0; i < n - 1; i ++){
			for(int j = i + 1; j < n; j ++){
				double f0[] = list1.get(i);
				double f1[] = list1.get(j);
				double f2[] = list2.get(i);
				double f3[] = list2.get(j);
				if (f0[0] > f1[0]) {
					if(f2[0] > f3[0]){
						count ++;
					}
					if(f2[0] < f3[0]){
						count --;
					}
				}
				if (f0[0] < f1[0]) {
					if(f2[0] < f3[0]){
						count ++;
					}
					if(f2[0] > f3[0]){
						count --;
					}
				}
			}
		}
	
		// calculate the rank correlation
		double num_t = (double)(n * (n - 1) / 2);
		//double r = 1.0 - Math.abs((double)count / num_t);
		double r = Math.abs((double)count / num_t);
		return r;
	}
	
}
