package ocha.itolab.hidden2.applet.spset3;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;

import ocha.itolab.hidden2.core.data.IndividualSet;
import ocha.itolab.hidden2.core.tool.ClassColorAssigner;
import ocha.itolab.hidden2.core.tool.DimensionDistance;


public class IndividualCategoryPanel extends JPanel {
	IndividualSet iset, child[];
	IndividualCanvas icanvas;
	IndividualSelectionPanel iselection;
	IndividualParettePanel parette;
	IndividualTextPanel itext;
	int categoryId = -1;

	public JRadioButton categoryButtons[];

	RadioButtonListener rbl = null;

	public IndividualCategoryPanel(IndividualSet is, IndividualSet c[]) {
		super();
		if(is == null) return;
		iset = is;
		child = c;

		int numc = iset.getNumCategory() + iset.getNumBoolean();
		categoryButtons = new JRadioButton[numc + 1];

		ButtonGroup groups = new ButtonGroup();
		ButtonGroup groupc = new ButtonGroup();
		JPanel p1 = new JPanel();
		p1.setLayout(new GridLayout((numc + 4), 1));

		// for each shop
		JLabel l1 = new JLabel("Shop selection");
		p1.add(l1);
		
		// for each category
		JLabel l2 = new JLabel("Category selection");
		p1.add(l2);
		for(int i = 0; i <= numc; i++) {
			String name = "None";
			if(i < numc)
				name = iset.getValueName(i + iset.getNumExplain() + iset.getNumObjective());
			categoryButtons[i] = new JRadioButton(name);
			groupc.add(categoryButtons[i]);
			p1.add(categoryButtons[i]);
		}

		JScrollPane scroll = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
		        	JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setViewportView(p1);
		scroll.setPreferredSize(new Dimension(250, 500));
		scroll.setVisible(true);

		//this.setLayout(new GridLayout(2, 1));
		add(scroll);

		if (rbl == null)
			rbl = new RadioButtonListener();
		addRadioButtonListener(rbl);
	}


	public void setIndividualCanvas(Object c) {
		icanvas = (IndividualCanvas)c;
	}

	public void setIndividualSelectionPanel(IndividualSelectionPanel p) {
		iselection = p;
	}

	public void setIndividualTextPanel(IndividualTextPanel p) {
		itext = p;
	}


	/**
	 * ラジオボタンのアクションの検出を設定する
	 * @param actionListener ActionListener
	 */
	public void addRadioButtonListener(ActionListener actionListener) {

		for(int i = 0; i < categoryButtons.length; i++)
			categoryButtons[i].addActionListener(actionListener);
	}


	/**
	 * ラジオボタンのアクションを検知するActionListener
	 * @or itot
	 */
	public class RadioButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JRadioButton buttonPushed = (JRadioButton) e.getSource();

			for(int i = 0; i < categoryButtons.length; i++) {
				if (buttonPushed == categoryButtons[i]) {

					categoryId = (i == categoryButtons.length - 1) ? -1 : i;
					iset.setClassId(categoryId);
					IndividualParettePanel p = new IndividualParettePanel(iset);
					p.setCanvas(icanvas);
					iselection.replaceParettePanel(p);
					DimensionDistance.calc(iset);
					int nclass = ClassColorAssigner.assign(iset);
					icanvas.setNumClusters(nclass);
					icanvas.display();

					break;
				}
			}
		}
	}



}
