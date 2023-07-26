
package ocha.itolab.hidden2.core.tool;

import ocha.itolab.hidden2.core.data.*;
import java.util.*;

public class DimensionDistanceCombination {
	static DimensionPair[] parray = null;
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
		for (int i = 0, count = 0; i < is.getNumExplain(); i++) {
			for (int j = 0; j < is.getNumObjective(); j++, count++) {
				scorearray[count][0] = DimensionDistanceCorrelation.calcCorrelationOnePair(iset, i, j);
				scorearray[count][1] = DimensionDistanceEntropy.calcEntropyOnePair(iset, i, j);
				scorearray[count][2] = DimensionDistanceSkinny.calcSkinnyOnePair(iset, i, j);
				scorearray[count][3] = DimensionDistanceClumpy.calcClumpyOnePair(iset, i, j);
			}
		}

		// Normalize the scores
		for (int i = 0; i < NUMDIST; i++) {
			double min = 1.0e+30, max = -1.0e+30;
			for (int j = 0; j < scorearray.length; j++) {
				min = (min < scorearray[j][i]) ? min : scorearray[j][i];
				max = (max > scorearray[j][i]) ? max : scorearray[j][i];
			}
			double diff = max - min;
			for (int j = 0; j < scorearray.length; j++) {
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
		// MAX_AREA = 0.5 + 0.5 * d;
		MAX_AREA = d;
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

		for (int i = 0; i < vertices.length; i++) {
			Vertex v = new Vertex();
			v.id = i;
			vertices[i] = v;

			double len = 0.0;
			for (int j = 0; j < NUMDIST; j++) {
				v.scorearray[j] = scorearray[i][j];
				len += (scorearray[i][j] * scorearray[i][j]);
			}
			if (maxlen < len) {
				seed = v;
				maxlen = len;
			}

			v.maxscore = 0.0;
			for (int j = 0; j < NUMDIST; j++)
				v.maxscore = (v.maxscore > nscorearray[i][j]) ? v.maxscore : nscorearray[i][j];
		}
		// inner_array = new double[vertices.length][vertices.length];
		// connect similar scatterplots
		for (int i = 0; i < vertices.length; i++) {
			Vertex v1 = vertices[i];
			for (int j = (i + 1); j < vertices.length; j++) {
				Vertex v2 = vertices[j];

				// calculate cosine
				double len1 = 0.0, len2 = 0.0, inner = 0.0;
				for (int k = 0; k < NUMDIST; k++) {
					len1 += (scorearray[i][k] * scorearray[i][k]);
					v1.scorearray[k] = scorearray[i][k];
					len2 += (scorearray[j][k] * scorearray[j][k]);
					v2.scorearray[k] = scorearray[i][k];
					inner += (scorearray[i][k] * scorearray[j][k]);
				}

				inner /= (Math.sqrt(len1) * Math.sqrt(len2));
				// inner_array[i][j] = inner;
				double aarea = 0.5 * Math.sqrt(len1 * len2 - inner * inner);
				// System.out.println(aarea);
				if (aarea < MAX_AREA) {
					v1.adjacents.add(v2);
					v2.adjacents.add(v1);
					numedge++;
				}
				/*
				 * if(inner > MIN_VECTOR) { v1.adjacents.add(v2); v2.adjacents.add(v1); }
				 */
			}
		}

		int[] clusterNames = new int[vertices.length];
		/*
		 * List clusters = new ArrayList(); for (int clusterName : clusterNames) { int
		 * cluster = new int (clusterName); cluster.addLeafName(clusterName);
		 * clusters.add(cluster); }
		 */

		// breadth-search to assign color IDs to the vertices
		ArrayList<Vertex> queue = new ArrayList<Vertex>();
		queue.add(seed);
		int numcolor = 0;

		while (queue.size() > 0) {
			Vertex v = queue.get(0);
			queue.remove(v);

			// for each color ID, for each adjacent vertex
			for (int i = 0; i < numcolor; i++) {
				boolean isFound = false;
				for (int j = 0; j < v.adjacents.size(); j++) {
					Vertex v2 = v.adjacents.get(j);
					if (v2.colorId == i) {
						isFound = true;
						break;
					}
				}
				if (isFound == false) {
					v.colorId = i;
					break;
				}
			}

			// if need to assign the new color ID
			if (v.colorId < 0) {
				v.colorId = numcolor;
				numcolor++;
			}

			// if the queue is empty
			if (queue.size() == 0) {
				Vertex maxv = null;
				double maxl = 0.0;
				for (int i = 0; i < vertices.length; i++) {
					Vertex vv = vertices[i];
					if (vv.colorId >= 0)
						continue;
					if (maxl < vv.maxscore) {
						maxv = vv;
						maxl = vv.maxscore;
					}

					for (int j = 0; j < NUMDIST; j++) {
						vv.scorearray[j] = scorearray[i][j];
					}

				}
				if (maxv != null)
					queue.add(maxv);
				else
					break;

			}
		}

		// Set dimension pairs
		int count = 0;
		for (int i = 0; i < vertices.length; i++) {
			Vertex v = vertices[i];
			if (v.colorId != 0)
				continue;
			DimensionPair p = new DimensionPair();
			System.out.println("hereid" + i);
			p.id = i;
			p.id1 = i / iset.getNumObjective();
			p.id2 = i - p.id1 * iset.getNumObjective();
			p.r = v.maxscore;
			for (int l = 0; l < NUMDIST; l++) {
				System.out.println("here" + v.scorearray[l]);
				p.score[l] = v.scorearray[l];
			}

			// sort
			int j = 0;
			for (j = 0; j < count; j++) {
				DimensionPair p2 = parray[j];
				if (p2.r < p.r)
					break;
			}
			for (int k = count; k > j; k--)
				parray[k] = parray[k - 1];
			parray[j] = p;
			count++;

		}

		for (int i = count; i < scorearray.length; i++)
			parray[i] = null;
		// for(int i = 0; i < count; i++) {
		for (int i = 0; i < 12; i++) {
			DimensionPair p = parray[i];
			if (p == null)
				continue;
		}
		// double minarea = calcMinArea(count);
		double minarea = calcMinArea(12);

		List<Integer> array_sim;
		DimensionPair[] parray_ob = null;
		parray_ob = parray;

		// 階層クラスタリング
		array_sim = HCmain(parray, count);
		for (int j = 0; j < array_sim.size(); j++) {
			parray_ob[j] = parray[array_sim.get(j)];
		}
		parray = parray_ob;

		System.out.println("   parameter=" + MAX_AREA + " numcolor=" + numcolor + " numedge=" + numedge + " nums="
				+ count + " minarea=" + minarea);

	}

	static double calcMinArea(int count) {

		// inner_array = new double[count][count];
		double minarea = 1.0e+30;

		for (int i = 0; i < count; i++) {
			DimensionPair p1 = parray[i];
			if (p1 == null)
				continue;
			for (int j = i + 1; j < count; j++) {
				DimensionPair p2 = parray[j];
				if (p2 == null)
					continue;

				double len1 = 0.0, len2 = 0.0, inner = 0.0;
				for (int k = 0; k < NUMDIST; k++) {
					len1 += (scorearray[p1.id][k] * scorearray[p1.id][k]);
					len2 += (scorearray[p2.id][k] * scorearray[p2.id][k]);
					inner += (scorearray[p1.id][k] * scorearray[p2.id][k]);
				}
				inner /= (Math.sqrt(len1) * Math.sqrt(len2));
				// inner_array[i][j] = inner;
				double aarea = 0.5 * Math.sqrt(len1 * len2 - inner * inner);
				if (aarea < minarea)
					minarea = aarea;
			}
		}
		// HCmain(inner_array);
		return minarea;
	}

	/**
	 * Vertex of the graph (corresponding to a scatterplot)
	 */
	static class Vertex {
		int id;
		ArrayList<Vertex> adjacents = new ArrayList<Vertex>();
		int colorId = -1;
		double maxscore = 0.0;
		double[] scorearray = new double[NUMDIST];
	}

//ここから
	public static List<Integer> HCmain(DimensionPair[] parray, int count) {
		double[][] simMatrix = new double[count][count];
		for (int i = 0; i < count; i++) {
			for (int j = (i + 1); j < count; j++) {
				// calculate cosine
				double len1 = 0.0, len2 = 0.0, inner = 0.0;
				for (int k = 0; k < NUMDIST; k++) {
					len1 += (parray[i].score[k] * parray[i].score[k]);
					len2 += (parray[j].score[k] * parray[j].score[k]);
					inner += (parray[i].score[k] * parray[j].score[k]);
				}
				inner /= (Math.sqrt(len1) * Math.sqrt(len2));
				simMatrix[i][j] = inner;
				System.out.print("i:" + i + "j:" + j + "inner" + inner + " ");
			}
			System.out.println("");
		}

		// クラスタリング
		List<List<Integer>> clusters = hierarchicalClustering(simMatrix);

		// クラスタリング結果の表示
		for (int i = 0; i < clusters.size(); i++) {
			System.out.print("Group " + (i + 1) + ": ");
			List<Integer> cluster = clusters.get(i);
			for (int j = 0; j < cluster.size(); j++) {
				System.out.print(cluster.get(j) + " ");
			}
			System.out.println();
		}
		return clusters.get(0);
	}

	// 階層的クラスタリングを実行するメソッド
	private static List<List<Integer>> hierarchicalClustering(double[][] simMatrix) {
		int n = simMatrix.length;
		// 初期クラスタ：各要素を1つのクラスタとする
		List<List<Integer>> clusters = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			List<Integer> cluster = new ArrayList<>();
			cluster.add(i);
			clusters.add(cluster);
		}
		// クラスタ数が1になるまでマージを繰り返す
		while (clusters.size() > 1) {
			// 最も類似度が高い2つのクラスタをマージする
			int minI = 0, minJ = 1;

			double minDist = Double.MIN_VALUE;
			for (int i = 0; i < clusters.size(); i++) {
				List<Integer> cluster = clusters.get(i);
				for (int j = 0; j < cluster.size(); j++) {
					System.out.print(cluster.get(j) + " ");
				}
				System.out.println("");
				for (int j = i + 1; j < clusters.size(); j++) {
					double dist = computeClusterDistance(clusters.get(i), clusters.get(j), simMatrix);
					if (dist > minDist) {
						minI = i;
						minJ = j;
						minDist = dist;
					}
				}
			}
			// マージした新しいクラスタを作成し、既存のクラスタから削除する
			List<Integer> mergedCluster = new ArrayList<>();
			mergedCluster.addAll(clusters.get(minI));
			mergedCluster.addAll(clusters.get(minJ));
			clusters.remove(minJ);
			clusters.remove(minI);
			clusters.add(mergedCluster);
		}

		return clusters;
	}

	private static double computeClusterDistance(List<Integer> cluster1, List<Integer> cluster2, double[][] simMatrix) {
		double min = Double.MIN_VALUE;
		;

		for (int i : cluster1) {
			for (int j : cluster2) {
				if (simMatrix[i][j] > min) {
					min = simMatrix[i][j];
				}
			}
		}

		return min;
		/*
		 * double sum = 0.0; for (int i : cluster1) { for (int j : cluster2) { sum +=
		 * simMatrix[i][j]; } } return sum / (cluster1.size() * cluster2.size());
		 */
	}
}
