package ocha.itolab.hidden2.applet.spset2;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import ocha.itolab.hidden2.core.data.IndividualSet;

public class IndividualParettePanel extends JPanel {
	IndividualSet iset = null;
	static int MAXCATEGORY = 12;
	int numc;
	String categoryName[];
	JCheckBox categoryCheck[];
	CheckBoxListener cbl;
	Color colors[];
	IndividualCanvas canvas;


	public IndividualParettePanel(IndividualSet is) {
		// super class init
		super();
		iset = is;

		// ƒNƒ‰ƒX‚È‚µ
		if(iset.getClassId() >= iset.getNumBoolean() + iset.getNumCategory())
			return;
		if(iset.getClassId() < 0)
			return;

		if(iset.getClassId() >= iset.getNumCategory()) {
			numc = 2;
			categoryName = new String[2];
			categoryName[0] = "False";
			categoryName[1] = "True";
		}
		else if(iset.getClassId() >= 0) {
			String categories[] = iset.categories.categories[iset.getClassId()];
			numc = (categories.length >= MAXCATEGORY) ? MAXCATEGORY : categories.length;
			categoryName = new String[numc];
			for(int i = 0; i < numc; i++)
				categoryName[i] = categories[i];
		}

		colors = new Color[numc];
		for(int i = 0; i < numc; i++)
			colors[i] = calcColor(i, numc);

		categoryCheck = new JCheckBox[numc];

		JPanel pp = new JPanel();
		add(pp);

		JPanel p1 = new JPanel();
		p1.setLayout(new GridLayout(numc, 1));
		for (int i = 0; i < numc; i++) {
			JPanel pp1 = new JPanel();
			pp1.setLayout(new GridLayout(1, 2));
			categoryCheck[i] = new JCheckBox(categoryName[i], true);
			pp1.add(categoryCheck[i]);
			JPanel cp = new JPanel();
			cp.setBackground(colors[i]);
			pp1.add(cp);
			p1.add(pp1);
		}
		pp.add(p1);

		// this setup
		setSize(300, 600);

		if (cbl == null)
			cbl = new CheckBoxListener();
		addCheckBoxListener(cbl);
	}


	void setCanvas(IndividualCanvas c) {
		canvas = c;
	}


	Color calcColor(int id, int num) {
		float hue = (float)id / (float)num + 0.5f;
		Color color = Color.getHSBColor(hue, 1.0f, 1.0f);
		return color;
	}



	public void addCheckBoxListener(CheckBoxListener checkBoxListener) {
		for(int i = 0; i < categoryCheck.length; i++)
			categoryCheck[i].addItemListener(checkBoxListener);
	}



	class CheckBoxListener implements ItemListener {
		public void itemStateChanged(ItemEvent e) {
			ArrayList graylist = new ArrayList();
			for(int i = 0; i < categoryCheck.length; i++) {
				if(categoryCheck[i].isSelected() == false) {
					int id[] = new int[1];
					id[0] = i;
					graylist.add(id);
				}
			}
			System.out.println("graylist.size=" + graylist.size());
			iset.setGray(graylist);
			canvas.display();
		}
	}
}
