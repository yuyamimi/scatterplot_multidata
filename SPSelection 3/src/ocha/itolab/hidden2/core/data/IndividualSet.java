package ocha.itolab.hidden2.core.data;

import java.util.ArrayList;

import ocha.itolab.hidden2.applet.spset2.IndividualSP;
import ocha.itolab.hidden2.core.tool.DimensionDistance;

public class IndividualSet {
	int numTotal, numExplain, numObjective, numCategory, numBoolean, classId;
	String name[];
	public String filename;
	public String message;
	int type[];
	public ArrayList<OneIndividual> plots;
	public ExplainSet explains;
	public ObjectiveSet objectives;
	public CategorySet categories;
	public BooleanSet booleans;

	/*****/
	IndividualSP dsp;
	/*****/

	public static int TYPE_EXPLAIN = 1;    //1 39
	public static int TYPE_OBJECTIVE = 2;   //2 43
	public static int TYPE_CATEGORY = 3;   //3 18
	public static int TYPE_BOOLEAN = 4;
	public static int TYPE_NAME = 5;

	public static int SHOP_CATEGORY_ID = 3;  //3 18
	public static int DISTANCE_TYPE = DimensionDistance.DISTANCE_COMBINATION;


	/**
	 * Constructor
	 */
	public IndividualSet(int nume, int numo, int numc, int numb) {
		numExplain = nume;
		numObjective = numo;
		numCategory = numc;
		numBoolean = numb;
		numTotal = nume + numo + numc + numb + 1 + 5;  //+10
		name = new String[numTotal];
		type = new int[numTotal];
		plots = new ArrayList();
	}


	/*****/
	public void setIndividualSP(IndividualSP isp) {
		dsp = isp;
	}

	public IndividualSP getIndividualSP() {
		return dsp;
	}
	/*****/

	/**
	 * 数値変数の数を得る
	 */
	public int getNumExplain() {
		//System.out.println("numEx" + numExplain);
		return numExplain;
	}

	/**
	 * 数値変数の数を得る
	 */
	public int getNumObjective() {
		//System.out.println("numOb" + numObjective);
		return numObjective;
	}

	/**
	 * カテゴリ変数の数を得る
	 */
	public int getNumCategory() {
		//System.out.println("numCa" + numCategory);
		return numCategory;
	}

	/**
	 * ブーレアン変数の数を得る
	 */
	public int getNumBoolean() {
		//System.out.println("numBo" + numBoolean);
		return numBoolean;
	}

	/**
	 * 変数の総数を得る
	 */
	public int getNumTotal() {
		System.out.println("numTotal" + numTotal);
		return numTotal;
	}

	/**
	 * 変数の名前をセットする
	 */
	public void setValueName(int i, String n) {
		name[i] = n;
	}

	/**
	 * 変数の名前を返す
	 */
	public String getValueName(int i) {
		return name[i];
	}

	/**
	 * 変数のタイプをセットする
	 */
	public void setValueType(int i, int t) {
		type[i] = t;
	}

	/**
	 * 変数のタイプを返す
	 */
	public int getValueType(int i) {
		return type[i];
	}


	/**
	 *
	 */
	public void setClassId(int id) {
		classId = id;
	}


	/**
	 *
	 */
	public int getClassId() {
		return classId;
	}

	/**
	 * 1個のプロットを追加する
	 */
	public OneIndividual addOneIndividual() {
		OneIndividual p = new OneIndividual(numExplain, numObjective, numCategory, numBoolean, plots.size());
		plots.add(p);
		return p;
	}


	/**
	 * プロット(点)の個数を返す
	 */
	public int getNumIndividual() {
		return plots.size();
	}


	/**
	 * 特定のプロットを返す
	 */
	public OneIndividual getOneIndividual(int id) {
		return (OneIndividual)plots.get(id);
	}


	/**
	 * 灰色表示するか否かを設定する
	 */
	public void setGray(ArrayList graylist) {
		// reset
		for(int i = 0; i < plots.size(); i++)
			plots.get(i).setGray(false);
		if(graylist == null | graylist.size() <= 0) return;
		if(classId < 0) return;

		// for each category value corresponding to gray plot
		for(int j = 0; j < graylist.size(); j++) {
			int gid[] = (int[])graylist.get(j);

			// for each plot
			for(int i = 0; i < plots.size(); i++) {
				OneIndividual oi = plots.get(i);
				if(classId >= numCategory) {
					if(gid[0] == 0 && oi.bool[classId - numCategory] == false)
						plots.get(i).setGray(true);
					if(gid[0] == 1 && oi.bool[classId - numCategory] == true)
						plots.get(i).setGray(true);
				}
				else if(classId >= 0) {
					String cname = categories.categories[classId][gid[0]];
					//System.out.println("cname=[" + cname + "] oi.category=[" + oi.category[classId] + "]");
					if(oi.category[classId].compareTo(cname) == 0)
						plots.get(i).setGray(true);
				}
			}
		}

	}



	/**
	 * ファイルを読み終えたあとに呼び出す
	 */
	public void finalize() {

		// カテゴリ情報を構築する
		//System.out.println("Setup category information ...");
		categories = new CategorySet(this);

		// ブーレアン変数情報を構築する
		//System.out.println("Setup boolean information ...");
		booleans = new BooleanSet(this);
		//BooleanCoOccurrence.calculate(this);

		// 数値情報を構築する
		//System.out.println("Setup numeric information ...");
		explains = new ExplainSet(this);
		objectives = new ObjectiveSet(this);

		setValueRange();
		DimensionDistance.calc(this);

	}

	public void setValueRange() {
		// 最大値・最小値を求める
		for(int i = 0; i < numExplain; i++) {
			explains.min[i] = 1.0e+30;   explains.max[i] = -1.0e+30;
		}
		for(int i = 0; i < numObjective; i++) {
			objectives.min[i] = 1.0e+30;   objectives.max[i] = -1.0e+30;
		}
		for(int j = 0; j < plots.size(); j++) {
			OneIndividual p = (OneIndividual)plots.get(j);
			p.id = j;
			p.setOutlier(false);
			for(int i = 0; i < numExplain; i++) {
				explains.min[i] = (explains.min[i] > p.explain[i]) ? p.explain[i] : explains.min[i];
				explains.max[i] = (explains.max[i] < p.explain[i]) ? p.explain[i] : explains.max[i];
			}
			for(int i = 0; i < numObjective; i++) {
				objectives.min[i] = (objectives.min[i] > p.objective[i]) ? p.objective[i] : objectives.min[i];
				objectives.max[i] = (objectives.max[i] < p.objective[i]) ? p.objective[i] : objectives.max[i];
			}
		}

	}

}

