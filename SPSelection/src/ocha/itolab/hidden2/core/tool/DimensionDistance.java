package ocha.itolab.hidden2.core.tool;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeSet;

import ocha.itolab.hidden2.core.data.IndividualSet;
import ocha.itolab.hidden2.core.data.OneExplain;
import ocha.itolab.hidden2.core.data.OneObjective;

public class DimensionDistance {
	static IndividualSet iset = null;
	static DistanceComparator dcomp;
	static TreeSet treeSet;
	static DimensionPair parray[];

	public static int DISTANCE_CORRELATION = 1;
	public static int DISTANCE_ENTROPY = 2;
	public static int DISTANCE_SKINNY = 3;
	public static int DISTANCE_CLUMPY = 4;
	public static int DISTANCE_COMBINATION = 0;


	/**
	 * Calculate distances
	 */
	public static void calc(IndividualSet is) {
		iset = is;
		
		if(is.DISTANCE_TYPE == DISTANCE_COMBINATION) {
			parray = DimensionDistanceCombination.calc(is);
			return;
		}
		
		
		int type = is.DISTANCE_TYPE;
		dcomp = new DistanceComparator();
		treeSet = new TreeSet(dcomp);

		// for each pair of dimensions
		for(int i = 0; i < is.getNumExplain(); i++) {
			for(int j = 0; j < is.getNumObjective(); j++) {
				calcDistanceOnePair(i, j, type);
			}
		}

		int nump = treeSet.size();
		if (nump <= 0) return;
		parray = new DimensionPair[nump];
		Object array[] = treeSet.toArray();
		for (int i = 0; i < nump; i++) {
			parray[i] = (DimensionPair) array[i];
			System.out.println("   type=" + iset.DISTANCE_TYPE + " r=" + parray[i].r);
		}
	}


	static void calcDistanceOnePair(int id1, int id2, int type) {
		double r = 0.0;

		if(type == DISTANCE_CORRELATION)
			r = DimensionDistanceCorrelation.calcCorrelationOnePair(iset, id1, id2);
		if(type == DISTANCE_ENTROPY)
			r = DimensionDistanceEntropy.calcEntropyOnePair(iset, id1, id2);
		if(type == DISTANCE_SKINNY)
			r = DimensionDistanceSkinny.calcSkinnyOnePair(iset, id1, id2);
		if(type == DISTANCE_CLUMPY)
			r = DimensionDistanceClumpy.calcClumpyOnePair(iset, id1, id2);
		
		/*
		if(id2 != 25)
			r = 1.0e+30;
		*/
		
		// substitute the correlation
		ArrayList<OneExplain>   explains   = iset.explains.dimensions;
		ArrayList<OneObjective> objectives = iset.objectives.dimensions;
		OneExplain ex = explains.get(id1);
		ex.setDissimilarity(r, id2);
		OneObjective ob = objectives.get(id2);
		ob.setDissimilarity(r, id1);

		DimensionPair p = new DimensionPair();
		p.id1 = id1;   p.id2 = id2;   p.r = r + Math.random() * 0.0001;
		treeSet.add((Object)p);
	}




	public static ArrayList<int[]> getList(double threshold) {
		ArrayList<int[]> list = new ArrayList<int[]>();
		list.clear();

		for(int i = 0; i < parray.length; i++) {
			if(parray[i].r > threshold) break;
			int id[] = new int[2];
			id[0] = parray[i].id1;
			id[1] = parray[i].id2;
			list.add(id);

			/*
			String v1 = iset.explains.names[id[0]];
			String v2 = iset.objectives.names[id[1]];
			System.out.println("     Pair" + i + ": [" + v1 + "][" + v2 + "] r=" + parray[i].r);
			*/
		}

		return list;
	}

	public static ArrayList<int[]> getList(int num) {
		ArrayList<int[]> list = new ArrayList<int[]>();
		list.clear();

		for(int i = 0; i < num; i++) {
			int id[] = new int[2];
			if(parray[i] != null) {
				id[0] = parray[i].id1;
				id[1] = parray[i].id2;
				list.add(id);
			}

			/*
			String v1 = iset.explains.names[id[0]];
			String v2 = iset.objectives.names[id[1]];
			System.out.println("     Pair" + i + ": [" + v1 + "][" + v2 + "] r=" + parray[i].r);
			*/
		}

		return list;
	}




	static class DistanceComparator implements Comparator {

		public DistanceComparator() {
		}

		public int compare(Object obj1, Object obj2) {

			DimensionPair p1 = (DimensionPair) obj1;
			DimensionPair p2 = (DimensionPair) obj2;

			if (p1.r - p2.r > 1.0e-10)
				return -1;
			if (p2.r - p1.r > 1.0e-10)
				return 1;

			int v1 = p1.id1 * 10000 + p1.id2;
			int v2 = p2.id1 * 10000 + p2.id2;
			int sub = v1 - v2;

			if(sub > 0) return 1;
			return -1;
		}

	}

}
