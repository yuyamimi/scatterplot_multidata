package ocha.itolab.hidden2.core.tool;

import org.heiankyoview2.core.tree.*;
import org.heiankyoview2.core.placement.*;
import java.util.*;

public class TreeConstructor {
	static ArrayList<int[]> lists[]; 
	
	
	/**
	 * Generate a tree
	 */
	public static Tree generate(ArrayList<int[]> list) {
		makeLists(list);
		Tree tree = makeTree();	
		Packing packing = new Packing();
		packing.placeNodesAllBranch(tree);
		return tree;
	}
	
	
	/**
	 * Make lists of IDs 
	 */
	static void makeLists(ArrayList<int[]> list) {
		List<List<Integer>> clusters = HierarchicalClustering.clusters;
		
		// allocate lists
		lists = new ArrayList[HierarchicalClustering.NUMCLUSTER];
		for(int i = 0; i < lists.length; i++)
			lists[i] = new ArrayList<int[]>();

		// for each selected scatterplot
		for(int i = 0; i < list.size(); i++) {
			int[] sp = list.get(i);
			
			// matching with clustering result
			int clusterId = -1;
			for(int j = 0; j < clusters.size(); j++) {
				List<Integer> cluster = clusters.get(j);
				for(int k = 0; k < cluster.size(); k++) {
					int id = cluster.get(k);
					if(sp[2] == id) {
						clusterId = j; break;
					}
				}
				if(clusterId >= 0) break;
			}

			// if found
			if(clusterId >= 0) {
				lists[clusterId].add(sp);
				//System.out.println("  clusterId=" + clusterId + " spid=" + sp[2]);
			}
			
		}	
	
	}
		
		
	/**
	 * Make a tree structure
	 * 
	 */
	static Tree makeTree() {
		Tree tree = new Tree();
		
		// Initialize tree
		Node rootnode = tree.getRootNode();
		tree.setNumBranch(1);
		Branch rootbranch = tree.getBranchAt(1);
		tree.setRootBranch(rootbranch);
		rootnode.setChildBranch(rootbranch);
		rootbranch.setParentNode(rootnode);
		rootbranch.setLevel(1);
		
		// generate four branches
		for(int i = 0; i < HierarchicalClustering.NUMCLUSTER; i++) {
			
			// make a branch
			Node pnode = rootbranch.getOneNewNode();
			Branch cbranch = tree.getOneNewBranch();
			pnode.setChildBranch(cbranch);
			cbranch.setParentNode(pnode);

			// for each scatterplot
			for(int j = 0; j < lists[i].size(); j++) {
				int sp[] = lists[i].get(j);
				Node node = cbranch.getOneNewNode();
				String name = sp[2] + "," + sp[0] + "," + sp[1];
				node.setName(name);
			}
		}
		
		return tree;
	}
	
}
