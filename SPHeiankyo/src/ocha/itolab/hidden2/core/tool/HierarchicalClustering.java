package ocha.itolab.hidden2.core.tool;

import java.util.ArrayList;
import java.util.List;

public class HierarchicalClustering {
	static int NUMDIST = 4;

	public static int NUMCLUSTER = 1;
	public static List<List<Integer>> clusters;

	// 最短距離法
	public static List<Integer> HCmain(DimensionPair[] parray, int count) {
		// int count = parray.length;
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
				// System.out.print("i:" + i + "j:" + j + "inner" + inner + " ");
			}
			// System.out.println("");
		}

		// クラスタリング
		List<List<Integer>> clusters = hierarchicalClustering(simMatrix);

		// クラスタリング結果の表示
		/*
		 * for (int i = 0; i < clusters.size(); i++) { System.out.print("Group " + (i +
		 * 1) + ": "); List<Integer> cluster = clusters.get(i); for (int j = 0; j <
		 * cluster.size(); j++) { System.out.print(cluster.get(j) + " "); }
		 * System.out.println(); }
		 */
		return clusters.get(0);
	}

	// 階層的クラスタリングを実行するメソッド(最短距離法)
	private static List<List<Integer>> hierarchicalClustering(double[][] simMatrix) {
		int n = simMatrix.length;

		// 初期クラスタ：各要素を1つのクラスタとする
		clusters = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			List<Integer> cluster = new ArrayList<>();
			cluster.add(i);
			clusters.add(cluster);
		}
		// クラスタ数がNUMCLUSTERになるまでマージを繰り返す
		while (clusters.size() > NUMCLUSTER) {
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
