package ocha.itolab.hidden2.core.tool;

import ocha.itolab.hidden2.core.data.*;

public class DataSetDivider {
	static IndividualSet parent;

	public static IndividualSet[] divide(IndividualSet p, int categoryId) {

		// allocate child datasets
		IndividualSet child[] = null;
		parent = p;
		int numshop = 1;
		if(parent.categories.categories.length > categoryId) {
			int nums = parent.categories.categories[categoryId].length;
			if(nums > 1) numshop = nums;
		}
		child = new IndividualSet[numshop];
		for(int i = 0; i < numshop; i++) {
			child[i] = new IndividualSet(
					parent.getNumExplain(), parent.getNumObjective(),
					parent.getNumCategory(), parent.getNumBoolean());
		}
		
		// copy individuals
		for(int i = 0; i < parent.getNumIndividual(); i++) {
			OneIndividual oi = parent.getOneIndividual(i);
			int sid = 0;
			if(numshop > 1)
				sid = getShopId(oi, categoryId);
			if(sid < 0) continue;
			child[sid].plots.add(oi);
		}
		
		// finalize the datasets
		for(int i = 0; i < numshop; i++) {
			setAttribute(child[i]);
			child[i].finalize();
		}
		
		return child;
	}
	
	
	static int getShopId(OneIndividual oi, int cid) {
		
		String categories[][] = parent.categories.categories;
		for(int i = 0; i < categories[cid].length; i++) {
			if(oi.category[cid].compareTo(categories[cid][i]) == 0) {
				return i;
			}
		}
		
		return -1;
	}
	
	
	static void setAttribute(IndividualSet child) {
		int ne = child.getNumExplain();
		int no = child.getNumObjective();
		int nc = child.getNumCategory();
		int nb = child.getNumBoolean();
		int total = ne + no + nc + nb;
		
		for(int i = 0; i < total; i++) {
			child.setValueName(i, parent.getValueName(i));
			
			if(i < ne)
				child.setValueType(i, child.TYPE_EXPLAIN);
			else if(i < (ne + no))
				child.setValueType(i, child.TYPE_OBJECTIVE);
			else if(i < (ne + no + nc))
				child.setValueType(i, child.TYPE_CATEGORY);
			else
				child.setValueType(i, child.TYPE_BOOLEAN);
			
			
		}
		
	}
	
}
