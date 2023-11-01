package ocha.itolab.hidden2.core.tool;

import ocha.itolab.hidden2.core.data.IndividualSet;
import ocha.itolab.hidden2.core.data.OneIndividual;

public class OutlierDetector {
	static IndividualSet iset;
	static double RATIO_MIN = -1.0;
	static double RATIO_MAX =  2.0;

	public static void detect(IndividualSet is, double v) {
		iset = is;
		for(int i = 0; i < iset.getNumIndividual(); i++)
			iset.getOneIndividual(i).setOutlier(false);

		// for each individual: detect outliers
		for(int i = 0; i < 10; i++)
			if(detectOneProcess(v) <= 0) break;

		// re-define min-max values
		for(int i = 0; i < iset.getNumExplain(); i++) {
			iset.explains.min[i] = 1.0e+30;   iset.explains.max[i] = -1.0e+30;
		}
		for(int i = 0; i < iset.getNumObjective(); i++) {
			iset.objectives.min[i] = 1.0e+30;   iset.objectives.max[i] = -1.0e+30;
		}
		for(int i = 0; i < iset.getNumIndividual(); i++) {
			OneIndividual oi = iset.getOneIndividual(i);
			if(oi.isOutlier() == true) continue;
			for(int j = 0; j < iset.getNumExplain(); j++) {
				if(iset.explains.min[j] > oi.explain[j])
					iset.explains.min[j] = oi.explain[j];
				if(iset.explains.max[j] < oi.explain[j])
					iset.explains.max[j] = oi.explain[j];
			}
			for(int j = 0; j < iset.getNumObjective(); j++) {
				if(iset.objectives.min[j] > oi.objective[j])
					iset.objectives.min[j] = oi.objective[j];
				if(iset.objectives.max[j] < oi.objective[j])
					iset.objectives.max[j] = oi.objective[j];
			}
		}

	}

	public static void detect(IndividualSet is, OneIndividual oi1) {
		iset = is;
//		for(int i = 0; i < iset.getNumIndividual(); i++)
//			iset.getOneIndividual(i).setOutlier(false);

		oi1.setOutlier(true);

		// re-define min-max values
		for(int i = 0; i < iset.getNumExplain(); i++) {
			iset.explains.min[i] = 1.0e+30;   iset.explains.max[i] = -1.0e+30;
		}
		for(int i = 0; i < iset.getNumObjective(); i++) {
			iset.objectives.min[i] = 1.0e+30;   iset.objectives.max[i] = -1.0e+30;
		}
		for(int i = 0; i < iset.getNumIndividual(); i++) {
			OneIndividual oi = iset.getOneIndividual(i);
			if(oi.isOutlier() == true) continue;
			for(int j = 0; j < iset.getNumExplain(); j++) {
				if(iset.explains.min[j] > oi.explain[j])
					iset.explains.min[j] = oi.explain[j];
				if(iset.explains.max[j] < oi.explain[j])
					iset.explains.max[j] = oi.explain[j];
			}
			for(int j = 0; j < iset.getNumObjective(); j++) {
				if(iset.objectives.min[j] > oi.objective[j])
					iset.objectives.min[j] = oi.objective[j];
				if(iset.objectives.max[j] < oi.objective[j])
					iset.objectives.max[j] = oi.objective[j];
			}
		}

	}

	public static void reset(IndividualSet is){
		iset = is;
		for(int i = 0; i < iset.getNumIndividual(); i++)
			iset.getOneIndividual(i).setOutlier(false);

		// re-define min-max values
		for(int i = 0; i < iset.getNumExplain(); i++) {
			iset.explains.min[i] = 1.0e+30;   iset.explains.max[i] = -1.0e+30;
		}
		for(int i = 0; i < iset.getNumObjective(); i++) {
			iset.objectives.min[i] = 1.0e+30;   iset.objectives.max[i] = -1.0e+30;
		}
		for(int i = 0; i < iset.getNumIndividual(); i++) {
			OneIndividual oi = iset.getOneIndividual(i);
			if(oi.isOutlier() == true) continue;
			for(int j = 0; j < iset.getNumExplain(); j++) {
				if(iset.explains.min[j] > oi.explain[j])
					iset.explains.min[j] = oi.explain[j];
				if(iset.explains.max[j] < oi.explain[j])
					iset.explains.max[j] = oi.explain[j];
			}
			for(int j = 0; j < iset.getNumObjective(); j++) {
				if(iset.objectives.min[j] > oi.objective[j])
					iset.objectives.min[j] = oi.objective[j];
				if(iset.objectives.max[j] < oi.objective[j])
					iset.objectives.max[j] = oi.objective[j];
			}
		}

	}



	static int detectOneProcess(double v) {
		int counter = 0;

		for(int i = 0; i < iset.getNumIndividual(); i++) {
			OneIndividual oi = iset.getOneIndividual(i);
			if(oi.isOutlier() == true) continue;

			// for each explanatory
			for(int j = 0; j < iset.getNumExplain(); j++) {
				double min = 1.0e+30, max = -1.0e+30;
				for(int k = 0; k < iset.getNumIndividual(); k++) {
					if(i == k) continue;
					OneIndividual oi2 = iset.getOneIndividual(k);
					if(oi2.isOutlier() == true) continue;
					double exp = oi2.explain[j];
					min = (min < exp) ? min : exp;
					max = (max > exp) ? max : exp;
				}
				double ratio = (oi.explain[j] - min) / (max - min);
				if(ratio < (0.5 - v) || ratio > (0.5 + v)) {
					oi.setOutlier(true);  counter++;   break;
				}
			}
			if(oi.isOutlier() == true) continue;

			// for each objective
			for(int j = 0; j < iset.getNumObjective(); j++) {
				double min = 1.0e+30, max = -1.0e+30;
				for(int k = 0; k < iset.getNumIndividual(); k++) {
					if(i == k) continue;
					OneIndividual oi2 = iset.getOneIndividual(k);
					if(oi2.isOutlier() == true) continue;
					double obj = oi2.objective[j];
					if(obj < 1.0e-10) continue;
					min = (min < obj) ? min : obj;
					max = (max > obj) ? max : obj;
				}
				//System.out.println("[" + i + "," + j + "]    obj=" + oi.objective[j] + " min=" + min + " max=" + max);
				double ratio = (oi.objective[j] - min) / (max - min);
				if(ratio < (0.5 - v) || ratio > (0.5 + v)) {
					oi.setOutlier(true);  counter++;   break;
				}
			}
		}

		return counter;
	}


}
