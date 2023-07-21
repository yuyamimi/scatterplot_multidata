
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
			// System.out.println(nscorearray[p.id][0] + "," + nscorearray[p.id][1] + "," +
			// nscorearray[p.id][2] + "," + nscorearray[p.id][3] + "," + p.r + " ***");
			// System.out.println(p.r);
		}
		// double minarea = calcMinArea(count);
		double minarea = calcMinArea(12);

		List<Integer> array_sim;
		DimensionPair[] parray_ob = null;
		parray_ob = parray;
		array_sim = HCmain(parray, count);
		for (int j = 0; j < array_sim.size(); j++) {
			parray_ob[j] = parray[array_sim.get(j)];
		}
		parray = parray_ob;

		System.out.println("   parameter=" + MAX_AREA + " numcolor=" + numcolor + " numedge=" + numedge + " nums="
				+ count + " minarea=" + minarea);

		// temporal
		/*
		 * { int MIN_COUNT = 12; if(count >= MIN_COUNT) { for(int i = MIN_COUNT; i <
		 * scorearray.length; i++) parray[i] = null; for(int i = 0; i < MIN_COUNT; i++)
		 * { DimensionPair p = parray[i]; System.out.println(nscorearray[p.id][0] + ","
		 * + nscorearray[p.id][1] + "," + nscorearray[p.id][2] + "," +
		 * nscorearray[p.id][3] + "," + p.r + ",***"); //System.out.println(p.r); }
		 * for(int i = 0; i < nscorearray.length; i++) { for(int j = 0; j < MIN_COUNT;
		 * j++) { DimensionPair p = parray[j]; if(p == null || i == p.id) break; if(j ==
		 * MIN_COUNT - 1) System.out.println(nscorearray[i][0] + "," + nscorearray[i][1]
		 * + "," + nscorearray[i][2] + "," + nscorearray[i][3] + "," +
		 * vertices[i].maxscore + ",+++"); } } } }
		 */
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
				//System.out.println("herej" + j);
				//System.out.println("herepa" + parray.length);
				//System.out.println("herepea" + parray[j].score[0]);
				// calculate cosine
				double len1 = 0.0, len2 = 0.0, inner = 0.0;
				for (int k = 0; k < NUMDIST; k++) {
					len1 += (parray[i].score[k] * parray[i].score[k]);
					len2 += (parray[j].score[k] * parray[j].score[k]);
					inner += (parray[i].score[k] * parray[j].score[k]);
				}
				inner /= (Math.sqrt(len1) * Math.sqrt(len2));
				simMatrix[i][j] = inner;
				System.out.print("i:"+i+"j:"+j+"inner"+inner + " ");
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
	/*
	 * for (int i = 0; i < clusters.size(); i++) { System.out.print("Group " + (i+1)
	 * + ": "); List<Integer> cluster = clusters.get(i); for (int j = 0; j <
	 * cluster.size(); j++) { System.out.print(j); } System.out.println(); } }
	 */

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
			
			 double minDist = Double.MAX_VALUE;
		        for (int i = 0; i < clusters.size(); i++) {
		        	List<Integer> cluster = clusters.get(i);
					for (int j = 0; j < cluster.size(); j++) {
						System.out.print(cluster.get(j) + " ");
					}
					System.out.println("");
		            for (int j = i + 1; j < clusters.size(); j++) {
		                double dist = computeClusterDistance(clusters.get(i), clusters.get(j), simMatrix);
		                if (dist < minDist) {
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
		    double sum = 0.0;
		    for (int i : cluster1) {
		        for (int j : cluster2) {
		            sum += simMatrix[i][j];
		        }
		    }
		    return sum / (cluster1.size() * cluster2.size());
		}
		/*
			double minSim = simMatrix[0][1];
			for (int i = 0; i < clusters.size(); i++) {
				for (int j = i + 1; j < clusters.size(); j++) {
					double sim = computeClusterSim(clusters.get(i), clusters.get(j), simMatrix);
					if (sim > minSim) {
						minI = i;
						minJ = j;
						minSim = sim;
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

	// 階層的クラスタリングにおける2つ
	private static double computeClusterSim(List<Integer> cluster1, List<Integer> cluster2, double[][] simMatrix) {
		double sum = 0.0;
		for (int i = 0; i < cluster1.size(); i++) {
			for (int j = 0; j < cluster2.size(); j++) {
				sum += simMatrix[cluster1.get(i)][cluster2.get(j)];
			}
		}
		return (cluster1.size() * cluster2.size()) / sum;
		// return sum / (cluster1.size() * cluster2.size());
	}
*/
	/*
	 * public static void HCmain(double distanceMatrix[][]) { // 入力となる距離行列を定義する
	 * 
	 * // クラスタリングを行う List<Set<Integer>> clusters =
	 * hierarchicalClustering(distanceMatrix);
	 * 
	 * 
	 * // 結果を表示する for (Set<Integer> cluster : clusters) {
	 * System.out.println(cluster); } }
	 * 
	 * public static List<Set<Integer>> hierarchicalClustering(double[][]
	 * distanceMatrix) { List<Set<Integer>> clusters = new ArrayList<>();
	 * 
	 * // 初期状態では、各要素が1つのクラスターとなる for (int i = 0; i < distanceMatrix.length; i++) {
	 * Set<Integer> cluster = new HashSet<>(); cluster.add(i);
	 * clusters.add(cluster); }
	 * 
	 * while (clusters.size() > 1) { // 最小距離のクラスターを探す double minDistance =
	 * Double.MAX_VALUE;
	 * 
	 * int minI = -1, minJ = -1; for (int i = 0; i < clusters.size(); i++) { for
	 * (int j = i + 1; j < clusters.size(); j++) { double distance =
	 * getDistance(clusters.get(i), clusters.get(j), distanceMatrix);
	 * //System.out.println("here"); //System.out.println(distance); if (distance <
	 * minDistance) { minDistance = distance; minI = i; minJ = j; } } } //
	 * 2つのクラスターを統合する //System.out.println(clusters.get(minJ)); Set<Integer>
	 * mergedCluster = new HashSet<>(clusters.get(minI));
	 * mergedCluster.addAll(clusters.get(minJ)); clusters.remove(minJ);
	 * clusters.set(minI, mergedCluster); } //System.out.println(clusters); return
	 * clusters; }
	 * 
	 * private static double getDistance(Set<Integer> cluster1, Set<Integer>
	 * cluster2, double[][] distanceMatrix) { double minDistance = Double.MAX_VALUE;
	 * for (int i : cluster1) { for (int j : cluster2) { double distance =
	 * distanceMatrix[i][j]; if (distance < minDistance) { minDistance = distance; }
	 * } } return minDistance; }
	 */
	/*
	 * static void applyGraphColoring() {
	 * 
	 * features = new double[NUMDIST][]; for(int i = 0; i < NUMDIST; i++)
	 * features[i] = new double[distarray.length];
	 * 
	 * sum = new double[distarray.length]; sum2 = new double[distarray.length]; sort
	 * = new int[distarray.length]; for(int i = 0; i < distarray.length; i++)
	 * sort[i] = i;
	 * 
	 * s = new double[distarray.length][]; adj = new int[distarray.length][];
	 * for(int i = 0; i < distarray.length; i++) { s[i] = new
	 * double[distarray.length]; adj[i] = new int[distarray.length]; }
	 * 
	 * naarray = new double[NUMDIST]; nbarray = new double[NUMDIST]; inparray = new
	 * double[NUMDIST];
	 * 
	 * for(int i= 0; i < distarray.length; i++){ for(int j = 0; j < NUMDIST; j++){
	 * double onedim = distarray[i][j] * distarray[i][j]; sum[i] += onedim; sum2[i]
	 * += onedim; } }
	 * 
	 * 
	 * for(int i = 0; i < distarray.length; i++){ for(int j = (i + 1); j <
	 * distarray.length; j++){ if(sum2[i] < sum2[j]){ double tmp = sum2[i]; sum2[i]
	 * = sum2[j]; sum2[j] = tmp;
	 * 
	 * int tp = sort[i]; sort[i] = sort[j]; sort[j] = tp; } } }
	 * 
	 * for(int j = 0; j < distarray.length; j++){ for(int i = 0; i < NUMDIST; i++){
	 * features[i][j] = distarray[sort[j]][i]; } }
	 * 
	 * double na = 0.0, nb = 0.0, inp = 0.0; for(int j = 0; j < distarray.length;
	 * j++){ for(int j2 = 0; j2 < distarray.length; j2++){ for(int i = 0; i <
	 * NUMDIST; i++){ naarray[i] = features[i][j] * features[i][j]; nbarray[i] =
	 * features[i][j2] * features[i][j2]; inparray[i] = features[i][j] *
	 * features[i][j2]; na = naarray[i] + na; nb = nbarray[i] + nb; inp = inp +
	 * inparray[i]; } s[j][j2] = na * nb - inp * inp; na = nb = inp = 0.0; } }
	 * 
	 * int pcounter = 0, ncounter = 0; double sup = MIN_VECTOR; for(int j = 0; j <
	 * distarray.length; j++){ for(int j2 = 0; j2 < distarray.length; j2++){
	 * if(s[j][j2] > sup){ adj[j][j2] = 0; ncounter++; } else{ adj[j][j2] = 1;
	 * pcounter++; } } }
	 * 
	 * int d[][]; d = new int[distarray.length][]; for(int i = 0; i <
	 * distarray.length; i++) d[i] = new int[2];
	 * 
	 * for(int j = 0; j < distarray.length; j++){ d[j][1] = 0; d[j][0] = j; for(int
	 * j2 = 0; j2 < distarray.length; j2++){ d[j][1] = d[j][1] + adj[j][j2]; } }
	 * 
	 * for(int p = 0; p < distarray.length; p++){ for(int q = (p + 1); q <
	 * distarray.length; q++){ if(d[p][1] < d[q][1]){ int tmp = d[p][1]; d[p][1] =
	 * d[q][1]; d[q][1] = tmp;
	 * 
	 * tmp = d[p][0]; d[p][0] = d[q][0]; d[q][0] = tmp; } } }
	 * 
	 * int c[]; c = new int[distarray.length]; int numcolor = 1, numfix = 0;
	 * 
	 * for(int j = 0; j < distarray.length; j++){ if(c[d[j][0]] < 1){ c[d[j][0]] =
	 * numcolor; for(int j2 = (j + 1); j2 < distarray.length; j2++){
	 * if(adj[d[j][0]][d[j2][0]] == 0){ if(c[d[j2][0]] < 1){ c[d[j2][0]] = numcolor;
	 * } } } numcolor++; } } System.out.println("   numcolor=" + numcolor +
	 * "  numedge=" + pcounter + "/" + (pcounter+ncounter) + " d=" + MIN_VECTOR);
	 * 
	 * int count = 0; //for(int j = 0; j < distarray.length; j++){ for(int j =
	 * distarray.length - 1; j >= 0; j--){ //System.out.println("         j=" + j +
	 * " sort[j]=" + sort[j] + " c[j]=" + c[j]); if(c[j] == 1){ DimensionPair p =
	 * new DimensionPair(); p.id1 = sort[j] / iset.getNumObjective(); p.id2 =
	 * sort[j] - p.id1 * iset.getNumObjective(); p.r = sum[sort[j]]; parray[count++]
	 * = p; //System.out.println(distarray[sort[j]][0] + "," + distarray[sort[j]][1]
	 * + "," + distarray[sort[j]][2] + "," + distarray[sort[j]][3]);
	 * System.out.println(p.r); } } for(int j = count; j < distarray.length; j++)
	 * parray[j] = null;
	 * 
	 * }
	 * 
	 */
}
