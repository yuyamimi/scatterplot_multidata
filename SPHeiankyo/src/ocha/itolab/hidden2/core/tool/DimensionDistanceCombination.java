package ocha.itolab.hidden2.core.tool;

import ocha.itolab.hidden2.core.data.*;
import java.util.*;

public class DimensionDistanceCombination {
	static DimensionPair[] parray = null;
	static int nparray;
	static IndividualSet iset;
	static int NUMDIST = 4;
	static double scorearray[][];
	static double nscorearray[][];
	static double inner_array[][];
	
	
	public static DimensionPair[] calc(IndividualSet is) {
		iset = is;
		
		scorearray = new double[is.getNumExplain() * is.getNumObjective()][NUMDIST];
		nscorearray = new double[is.getNumExplain() * is.getNumObjective()][NUMDIST];	
		parray = new DimensionPair[scorearray.length];
			
		// Calculate scores
		for(int i = 0, count = 0; i < is.getNumExplain(); i++) {
			for(int j = 0; j < is.getNumObjective(); j++, count++) {
				scorearray[count][0] = DimensionDistanceCorrelation.calcCorrelationOnePair(iset, i, j);
				scorearray[count][1] = DimensionDistanceEntropy.calcEntropyOnePair(iset, i, j);
				scorearray[count][2] = DimensionDistanceSkinny.calcSkinnyOnePair(iset, i, j);
				scorearray[count][3] = DimensionDistanceClumpy.calcClumpyOnePair(iset, i, j);
			}
		}
		
		// Normalize the scores
		for(int i = 0; i < NUMDIST; i++) {
			double min = 1.0e+30, max = -1.0e+30;
			for(int j = 0; j < scorearray.length; j++) {
				min = (min < scorearray[j][i]) ? min : scorearray[j][i];
				max = (max > scorearray[j][i]) ? max : scorearray[j][i];
			}
			double diff = max - min;
			for(int j = 0; j < scorearray.length; j++) {
				nscorearray[j][i] = (scorearray[j][i] - min) / diff;
			}
		}
		
		// Apply graph coloring
		applyGraphColoring();
	
		return parray;
	}


	
	static double MAX_AREA = 0.5;
	static double MIN_VECTOR = 0.995;
	static Vertex vertices[];
	
	public static void setMinimimDistance(double d) {
		MIN_VECTOR = 1.0 - d;
	}
	
	public static void setMaxArea(double d) {
		//MAX_AREA = 0.5 + 0.5 * d;
		MAX_AREA = d;
	}
	
	
	public static void callHierarchicalClustering() {
		List<Integer> array_sim;
		DimensionPair[] parray_ob = new DimensionPair[scorearray.length];
		
		for (int j = 0; j < nparray; j++) {
			parray_ob[j] = parray[j] ;
		}
		array_sim = HierarchicalClustering.HCmain(parray, nparray);
		for (int j = 0; j < array_sim.size(); j++) {
			parray[j] = parray_ob[array_sim.get(j)];
		}
	}
	
  	
	/**
	 * Graph coloring
	 */
	static void applyGraphColoring() {
		int numedge = 0;
		
		// allocate vertices
		Vertex seed = null;
		double maxlen = 0.0;
		vertices = new Vertex[scorearray.length];
		
		for(int i = 0; i < vertices.length; i++) {
			Vertex v = new Vertex();
			v.id = i;
			for (int j = 0; j < NUMDIST; j++) {
				v.scorearray[j] = scorearray[i][j];
			}
			vertices[i] = v;
			
			double len = 0.0;
			for(int j = 0; j < NUMDIST; j++)
				len += (scorearray[i][j] * scorearray[i][j]);
			if(maxlen < len) {
				seed = v;   maxlen = len;
			}
			
			v.maxscore = 0.0;
			for(int j = 0; j < NUMDIST; j++)
				v.maxscore = (v.maxscore > nscorearray[i][j]) ? v.maxscore : nscorearray[i][j];
		}
		
		// connect similar scatterplots
		for(int i = 0; i < vertices.length; i++) {
			Vertex v1 = vertices[i];
			for(int j = (i + 1); j < vertices.length; j++) {
				Vertex v2 = vertices[j];
				
				// calculate cosine
				double len1 = 0.0, len2 = 0.0, inner = 0.0;
				for(int k = 0; k < NUMDIST; k++) {
					len1 += (scorearray[i][k] * scorearray[i][k]);
					len2 += (scorearray[j][k] * scorearray[j][k]);
					inner += (scorearray[i][k] * scorearray[j][k]);
				}
				inner /= (Math.sqrt(len1) * Math.sqrt(len2));
				double aarea = 0.5 * Math.sqrt(len1 * len2 - inner * inner);
				//System.out.println(aarea);
				if(aarea < MAX_AREA) {
					v1.adjacents.add(v2);
					v2.adjacents.add(v1);
					numedge++;
				}
				/*
				if(inner > MIN_VECTOR) {
					v1.adjacents.add(v2);
					v2.adjacents.add(v1);
				}
				*/
			}
		}
		
		// breadth-search to assign color IDs to the vertices
		ArrayList<Vertex> queue = new ArrayList<Vertex>();
		queue.add(seed);
		int numcolor = 0;
		while(queue.size() > 0) {
			Vertex v = queue.get(0);  queue.remove(v);
			
			// for each color ID, for each adjacent vertex
			for(int i = 0; i < numcolor; i++) {
				boolean isFound = false;
				for(int j = 0; j < v.adjacents.size(); j++) {
					Vertex v2 = v.adjacents.get(j);
					if(v2.colorId == i) {
						isFound = true;  break;
					}
				}
				if(isFound == false) {
					v.colorId = i;  break;
				}
			}
			
			// if need to assign the new color ID
			if(v.colorId < 0) {
				v.colorId = numcolor;   numcolor++;
			}

			// if the queue is empty
			if(queue.size() == 0) {
				Vertex maxv = null;  double maxl = 0.0;
				for(int i = 0; i < vertices.length; i++) {
					Vertex vv = vertices[i];
					if(vv.colorId >= 0) continue;
					if(maxl < vv.maxscore) {
						maxv = vv;   maxl = vv.maxscore;
					}
				}
				if(maxv != null) queue.add(maxv);
				else break;
				
			}
		}
		
		
		
		// Set dimension pairs
		nparray = 0;
  		for(int i = 0; i < vertices.length; i++){
  			Vertex v = vertices[i];
  			if(v.colorId != 0) continue;
  			DimensionPair p = new DimensionPair();
  			p.id = i;
  			p.id1 = i / iset.getNumObjective(); 
  			p.id2 = i - p.id1 * iset.getNumObjective();
  			p.r = v.maxscore;
  			for (int l = 0; l < NUMDIST; l++) {
				p.score[l] = v.scorearray[l];
			}
  			
  			// sort
  			int j = 0;
  			for(j = 0; j < nparray; j++) {
  				DimensionPair p2 = parray[j];
  				if(p2.r < p.r) break;
  			}
  			for(int k = nparray; k > j; k--)
  				parray[k] = parray[k - 1];
  			
  			parray[j] = p;
  			nparray++;
  			
  		}
  		
  		for(int i = nparray; i < scorearray.length; i++)
  			parray[i] = null;
		for(int i = 0; i < nparray; i++) {
			DimensionPair p = parray[i];
			if(p == null) continue;
			//System.out.println(nscorearray[p.id][0] + "," + nscorearray[p.id][1] + "," + nscorearray[p.id][2] + "," + nscorearray[p.id][3] + "," + p.r + " ***");
			//System.out.println(p.r);
		}
		double minarea = calcMinArea(nparray);
  		//double minarea = calcMinArea(12);
		//System.out.println("   parameter=" + MAX_AREA + " numcolor=" + numcolor + " numedge=" + numedge + " nums=" + nparray + " minarea=" + minarea);
		
		
		// 階層クラスタリング
		callHierarchicalClustering();
  		
		// temporal
  		/*
  		{ int MIN_COUNT = 12;
  		if(count >= MIN_COUNT) {
  			for(int i = MIN_COUNT; i < scorearray.length; i++)
  				parray[i] = null;
  			for(int i = 0; i < MIN_COUNT; i++) {
  				DimensionPair p = parray[i];
  				System.out.println(nscorearray[p.id][0] + "," + nscorearray[p.id][1] + "," + nscorearray[p.id][2] + "," + nscorearray[p.id][3] + "," + p.r + ",***");
			//System.out.println(p.r);
  			}
  			for(int i = 0; i < nscorearray.length; i++) {
  				for(int j = 0; j < MIN_COUNT; j++) {
  					DimensionPair p = parray[j];
  					if(p == null || i == p.id) break;
  						if(j == MIN_COUNT - 1)
  							System.out.println(nscorearray[i][0] + "," + nscorearray[i][1] + "," + nscorearray[i][2] + "," + nscorearray[i][3] + "," + vertices[i].maxscore + ",+++");
  				}
  			}
  		}
  		}
  		*/
	}
	
	
	static double calcMinArea(int count) {
		double minarea = 1.0e+30;

		for(int i = 0; i < count; i++) {
			DimensionPair p1 = parray[i];
			if(p1 == null) continue;
			for(int j = i + 1; j < count; j++) {
				DimensionPair p2 = parray[j];
				if(p2 == null) continue;
				
				double len1 = 0.0, len2 = 0.0, inner = 0.0;
				for(int k = 0; k < NUMDIST; k++) {
					len1 += (scorearray[p1.id][k] * scorearray[p1.id][k]);
					len2 += (scorearray[p2.id][k] * scorearray[p2.id][k]);
					inner += (scorearray[p1.id][k] * scorearray[p2.id][k]);
				}
				inner /= (Math.sqrt(len1) * Math.sqrt(len2));
				double aarea = 0.5 * Math.sqrt(len1 * len2 - inner * inner);
				if(aarea < minarea) minarea = aarea;
			}
		}
		
		return minarea;
	}
	
	
	
	/**
	 * Vertex of the graph (corresponding to a scatterplot)
	 */
	static class Vertex {
		public double[] scorearray = new double[NUMDIST];
		int id;
		ArrayList<Vertex> adjacents = new ArrayList<Vertex>();
		int colorId = -1;
		double maxscore = 0.0;
	}
	
	
}
