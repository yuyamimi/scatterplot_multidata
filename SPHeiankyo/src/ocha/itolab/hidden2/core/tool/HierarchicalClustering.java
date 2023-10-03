package ocha.itolab.hidden2.core.tool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.List;

public class HierarchicalClustering {
	static int NUMDIST = 4;

	public static int NUMCLUSTER = 1;
	public static List<List<Integer>> clusters;

	public static List<Integer> HCmain(DimensionPair[] parray, int count) {
		// int count = parray.length;
		double[][] simMatrix = new double[count][count];
		System.out.println("count" + count);
		for (int i = 0; i < count; i++) {
			for (int j = (i + 1); j < count; j++) {
				// calculate cosine
				double len1 = 0.0, len2 = 0.0, inner = 0.0;
				for (int k = 0; k < NUMDIST; k++) {
					len1 += (parray[i].score[k] * parray[i].score[k]);
					len2 += (parray[j].score[k] * parray[j].score[k]);
					inner += (parray[i].score[k] * parray[j].score[k]);
					// System.out.print("before!!" + parray[i].score[k]);
				}
				System.out.println("");

				inner /= (Math.sqrt(len1) * Math.sqrt(len2));
				simMatrix[i][j] = inner;
				// System.out.print("i:" + i + "j:" + j + "inner" + inner + " ");
			}
			// System.out.println("");
		}

		// クラスタリング
		List<List<Integer>> clusters = hierarchicalClustering(simMatrix);// 最短距離法
		// List<List<Integer>> clusters = centralClustering(simMatrix, parray);// 重心法
		// List<List<Integer>> clusters = kmeansClustering(simMatrix, count,
		// parray);//kmeans法
		// List<List<Integer>> clusters = WardMethodClustering(simMatrix);//ウォード法
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
			if (minJ < minI) {
				clusters.remove(minI);
				clusters.remove(minJ);
			} else {
				clusters.remove(minJ);
				clusters.remove(minI);
			}
			clusters.add(mergedCluster);
		}

		return clusters;
	}

	private static double computeClusterDistance(List<Integer> cluster1, List<Integer> cluster2, double[][] simMatrix) {
		double min = Double.MIN_VALUE;

		for (int i : cluster1) {
			for (int j : cluster2) {
				if (simMatrix[i][j] > min) {
					min = simMatrix[i][j];
				}
			}
		}

		return min;
	}

	// 重心法
	private static List<List<Integer>> centralClustering(double[][] simMatrix, DimensionPair[] parray) {
		int n = simMatrix.length;

		List<List<Double>> simCentroidMatrix = new ArrayList<>();

		for (int i = 0; i < n; i++) {
			List<Double> foo = new ArrayList<>();
			for (int j = 0; j < n; j++) {
				foo.add(simMatrix[i][j]);
			}
			simCentroidMatrix.add(foo);
		}

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
				for (int j = i + 1; j < clusters.size(); j++) {
					double dist = computeCentroidClusterDistance(i, j, simCentroidMatrix);
					if (dist > minDist) {
						minI = i;
						minJ = j;
						minDist = dist;
					}
				}
			}
			System.out.println("");
			System.out.println("mini:" + minI + "minj" + minJ);
			// マージした新しいクラスタを作成し、既存のクラスタから削除する
			List<Integer> mergedCluster = new ArrayList<>();
			mergedCluster.addAll(clusters.get(minI));
			mergedCluster.addAll(clusters.get(minJ));
			if (minJ < minI) {
				clusters.remove(minI);
				clusters.remove(minJ);
			} else {
				clusters.remove(minJ);
				clusters.remove(minI);
			}
			clusters.add(mergedCluster);
			System.out.println("count" + clusters.size());

			ArrayList<ArrayList<Double>> clusters_num = new ArrayList<>();
			ComputeCentroidDistance(parray, clusters_num);
			UpdateSimmatrix(parray, clusters_num, simCentroidMatrix);
			System.out.println("");
		}

		return clusters;
	}

	// このメソッド内の clusters を clusters_num に変更
	private static void ComputeCentroidDistance(DimensionPair[] parray, ArrayList<ArrayList<Double>> clusters_num) {
		for (List<Integer> cluster : clusters) {
			ArrayList<Double> cluster_num = new ArrayList<>();
			for (int l = 0; l < NUMDIST; l++) {
				double sum = 0.0;
				for (int j : cluster) {
					System.out.println("j" + j);
					sum += parray[j].score[l];
				}
				sum /= cluster.size();
				cluster_num.add(sum);
			}
			clusters_num.add(cluster_num); // 重心をクラスタのリストに追加
			System.out.println("after!!" + cluster_num);
		}
	}

	// このメソッド内の clusters を clusters_num に変更
	private static void UpdateSimmatrix(DimensionPair[] parray, ArrayList<ArrayList<Double>> clusters_num,
			List<List<Double>> simCentroidMatrix) {
		simCentroidMatrix.clear();
		System.out.println("");
		System.out.println("clusters.size()" + clusters.size());
		for (int i = 0; i < clusters.size(); i++) {
			List<Double> sim_cp = new ArrayList<>();
			for (int j = 0; j < clusters.size(); j++) {
				double len1 = 0.0, len2 = 0.0, inner = 0.0;
				if (i >= j) {
					inner = 0.0;
				} else {
					for (int k = 0; k < NUMDIST; k++) {
						len1 += (clusters_num.get(i).get(k) * clusters_num.get(i).get(k));
						len2 += (clusters_num.get(j).get(k) * clusters_num.get(j).get(k));
						inner += (clusters_num.get(i).get(k) * clusters_num.get(j).get(k));
					}
					inner /= (Math.sqrt(len1) * Math.sqrt(len2));
				}
				sim_cp.add(inner);
			}
			simCentroidMatrix.add(sim_cp);
			System.out.print("i:" + i + "simCentroidMatrix.get(i).get(j)" + sim_cp);
		}
	}

	// このメソッド内の simMatrix を simCentroidMatrix に変更
	private static double computeCentroidClusterDistance(int i, int j, List<List<Double>> simCentroidMatrix) {
		System.out.print("i:" + i + "j:" + j + "simCentroidMatrix.get(i).get(j)" + simCentroidMatrix.get(i).get(j));
		double min = Double.MIN_VALUE;
		if (simCentroidMatrix.get(i).get(j) > min) {
			min = simCentroidMatrix.get(i).get(j);
		}
		return min;
	}

	// ウォード法
	private static List<List<Integer>> WardMethodClustering(double[][] simMatrix, int count, DimensionPair[] parray) {
		int n = simMatrix.length;

		List<List<Double>> simCentroidMatrix = new ArrayList<>();
		List<List<Double>> simWordMatrix = new ArrayList<>();

		for (int i = 0; i < n; i++) {
			List<Double> foo = new ArrayList<>();
			for (int j = 0; j < n; j++) {
				foo.add(simMatrix[i][j]);
			}
			simCentroidMatrix.add(foo);
			simWordMatrix.add(foo);
		}

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
				for (int j = i + 1; j < clusters.size(); j++) {
					double dist = computeWordClusterDistance(i, j, simWordMatrix);
					if (dist > minDist) {
						minI = i;
						minJ = j;
						minDist = dist;
					}
				}
			}
			System.out.println("");
			System.out.println("mini:" + minI + "minj" + minJ);

			// マージした新しいクラスタを作成し、既存のクラスタから削除する
			List<Integer> mergedCluster = new ArrayList<>();
			List<Integer> minIcluster = clusters.get(minI);
			List<Integer> minJcluster = clusters.get(minI);
			mergedCluster.addAll(clusters.get(minI));
			mergedCluster.addAll(clusters.get(minJ));

			if (minJ < minI) {
				clusters.remove(minI);
				clusters.remove(minJ);
			} else {
				clusters.remove(minJ);
				clusters.remove(minI);
			}
			clusters.add(mergedCluster);

			UpdateWordSimmatrix(simWordMatrix, simCentroidMatrix, minIcluster, minJcluster, minI, minJ);
			simCentroidMatrix = simWordMatrix;
			System.out.println("count" + clusters.size());

			ArrayList<ArrayList<Double>> clusters_num = new ArrayList<>();

			System.out.println("");
		}

		return clusters;
	}

	// このメソッド内の clusters を clusters_num に変更
	private static void UpdateWordSimmatrix(List<List<Double>> simWordMatrix, List<List<Double>> simCentroidMatrix,
			List<Integer> minIcluster, List<Integer> minJcluster, int minI, int minJ) {

		System.out.println("");
		System.out.println("clusters.size()" + clusters.size());
		simWordMatrix.clear();
		for (int i = 0; i < clusters.size(); i++) {
			List<Double> sim_cp = new ArrayList<>();
			for (int j = 0; j < clusters.size(); j++) {
				double len1 = 0.0, len2 = 0.0, inner = 0.0;
				if (i >= j) {
					inner = 0.0;
				} else if (j == clusters.size() - 1) {
					double A = 0, B = 0.0, C = 0.0;
					if (minI < i) {
						A = (minIcluster.size() / clusters.get(j).size())
								* Math.pow((simCentroidMatrix.get(i + 1).get(minI)), 2);
					} else {
						A = (minIcluster.size() / clusters.get(j).size())
								* Math.pow((simCentroidMatrix.get(i).get(minI)), 2);
					}
					if (minJ < i) {
						B = (minJcluster.size() / clusters.get(j).size())
								* Math.pow((simCentroidMatrix.get(i + 1).get(minJ)), 2);
					} else {
						B = (minJcluster.size() / clusters.get(j).size())
								* Math.pow((simCentroidMatrix.get(minI).get(minJ)), 2);
					}

					C = (clusters.get(i).size() / clusters.get(j).size())
							* Math.pow((simCentroidMatrix.get(i + 1).get(j)), 2);
					inner = Math.sqrt(A + B - C);
				} else {
					int add_i_num = 0;
					int add_j_num = 0;
					if (minI < i) {
						add_i_num++;
					} else if (minJ < i) {
						add_i_num++;
					}
					if (minI < j) {
						add_j_num++;
					} else if (minJ < j) {
						add_j_num++;
					}
					inner = simCentroidMatrix.get(i + add_i_num).get(j + add_j_num);

				}
				sim_cp.add(inner);
			}
			simWordMatrix.add(sim_cp);
			System.out.print("i:" + i + "simCentroidMatrix.get(i).get(j)" + sim_cp);
		}
	}

	// このメソッド内の simMatrix を simCentroidMatrix に変更
	private static double computeWordClusterDistance(int i, int j, List<List<Double>> simCentroidMatrix) {
		System.out.print("i:" + i + "j:" + j + "simCentroidMatrix.get(i).get(j)" + simCentroidMatrix.get(i).get(j));
		double min = Double.MIN_VALUE;
		if (simCentroidMatrix.get(i).get(j) > min) {
			min = simCentroidMatrix.get(i).get(j);
		}
		return min;
	}

	// kmeans法
	private static List<List<Integer>> kmeansClustering(double[][] simMatrix, int count, DimensionPair[] parray) {
		int n = simMatrix.length;
		// クラスタの数
		int k = 4;

		// 初期クラスタ：ランダムでk個のクラスタを作ってしまう
		clusters = new ArrayList<>();

		for (int i = 0; i < k; i++) {
			clusters.add(new ArrayList<>()); // 各クラスタを初期化
		}

		for (int i = 0; i < n; i++) {
			Random rand = new Random();
			int num = rand.nextInt(k);
			// System.out.println(num);
			clusters.get(num).add(i);
		}
		for (int j = 0; j < clusters.size(); j++) {
			System.out.println(j + " " + clusters.get(j));
			System.out.println(j + " " + clusters.get(j).get(0));
		}

		boolean flag = true;
		while (flag == true) {
			ArrayList<ArrayList<Double>> clusters_num = new ArrayList<>();// クラスタごとの数値指標の重心を格納
			CalculateCenterIndicators(parray, clusters_num);
			flag = CreateCluster(parray, clusters_num, k, flag);
		}

		return clusters;
	}

	private static void CalculateCenterIndicators(DimensionPair[] parray, ArrayList<ArrayList<Double>> clusters_num) {
		// それぞれのクラスタの4つの数値指標を重心をとる
		for (List<Integer> cluster : clusters) {
			ArrayList<Double> cluster_num = new ArrayList<>();
			for (int i = 0; i < NUMDIST; i++) {
				cluster_num.add(0.0); // 各クラスタを初期化
			}
			for (int j : cluster) {
				for (int l = 0; l < NUMDIST; l++) {
					double sum = cluster_num.get(l) + parray[j].score[l];
					cluster_num.set(l, sum);
				}
			}
			clusters_num.add(cluster_num); // 重心をクラスタのリストに追加
		}
	}

	private static boolean CreateCluster(DimensionPair[] parray, ArrayList<ArrayList<Double>> clusters_num, int k,
			boolean flag) {
		flag = false;
		// クラスタの再構成
		List<List<Integer>> mergedClusters = new ArrayList<>();
		int origin_index = 0;
		for (List<Integer> cluster : clusters) {
			for (int i = 0; i < cluster.size(); i++) {
				int index = 0;
				int min_index = -1;
				double min = Double.MIN_VALUE;
				for (ArrayList<Double> cluster_num : clusters_num) {
					// calculate cosine
					double len1 = 0.0, len2 = 0.0, inner = 0.0;
					for (int l = 0; l < NUMDIST; l++) {
						len1 += (parray[cluster.get(i)].score[l] * parray[cluster.get(i)].score[l]);
						len2 += (cluster_num.get(l) * cluster_num.get(l));
						inner += (parray[cluster.get(i)].score[l] * cluster_num.get(l));
					}
					inner /= (Math.sqrt(len1) * Math.sqrt(len2));
					if (min < inner) {
						min = inner;
						min_index = index;
					}
					index++;
				}

				// マージした新しいクラスタを作成し、既存のクラスタから削除する
				for (int num = 0; num < k; num++) {
					mergedClusters.add(new ArrayList<>()); // 各クラスタを初期化
				}
				mergedClusters.get(min_index).add(cluster.get(i));
				if (origin_index != min_index) {
					System.out.println("origin_index " + origin_index + "min_index " + min_index);
					flag = true;
				}
			}
			origin_index++;
		}
		clusters.clear();
		clusters.addAll(mergedClusters);

		for (int i = 0; i < clusters.size(); i++) {
			List<Integer> cluster = clusters.get(i);
			for (int j = 0; j < cluster.size(); j++) {
				System.out.print(cluster.get(j) + " ");
			}
		}
		System.out.print(flag);
		return flag;
	}
}
