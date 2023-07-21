package ocha.itolab.hidden2.applet.spset2;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/*****/
import ocha.itolab.common.pointlump.PointlumpTest;
/*****/
import ocha.itolab.hidden2.core.data.IndividualSet;
import ocha.itolab.hidden2.core.data.OneIndividual;
import ocha.itolab.hidden2.core.tool.DataSetDivider;
import ocha.itolab.hidden2.core.tool.DimensionDistance;
import ocha.itolab.hidden2.core.tool.OutlierDetector;


public class IndividualSelectionPanel extends JPanel {

	IndividualSet iset = null, child[];
	/*****/
	IndividualSP dsp = null;
	PointlumpTest plt = null;
	/*****/
	int numSP = 0;

	Font font = new Font("Arial", Font.PLAIN, 16);;

	static int colorcount = 0;

	/* Selective canvas */
	IndividualCanvas icanvas;
	IndividualCategoryPanel icategory;
	IndividualTextPanel itext;
	IndividualParettePanel iparette;

	public JButton  fileOpenButton, viewResetButton;
	public JRadioButton correlationButton, entropyButton;
	public JCheckBox verticesButton, boundariesButton, trianglesButton, hidegraypointsButton;
	public JSlider thresholdSlider, outlierSlider, outlierSlider2;
	JTabbedPane pane = null;

	/* Action listener */
	ButtonListener bl = null;
	RadioButtonListener rbl = null;
	SliderListener sl = null;
	CheckBoxListener cbl = null;

	public IndividualSelectionPanel() {
		// super class init
		super();
		setSize(200, 600);


		JPanel p1 = new JPanel();
		//p1.setLayout(new GridLayout(10,1));
		p1.setLayout(new GridLayout(6,1));

		fileOpenButton = new JButton("File Open");
		p1.add(fileOpenButton);
		viewResetButton = new JButton("View Reset");
		p1.add(viewResetButton);

		JPanel p11 = new JPanel();
		p11.setLayout(new GridLayout(2,1));
		p11.add(new JLabel("Threshold"));
		thresholdSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
		p11.add(thresholdSlider);
		p1.add(p11);

		/*
		JPanel p12 = new JPanel();
		p12.setLayout(new GridLayout(2,1));
		p12.add(new JLabel("Outlier"));
		outlierSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
		p12.add(outlierSlider);
		p1.add(p12);
		*/

		JPanel p13 = new JPanel();
		p13.setLayout(new GridLayout(2,1));
		p13.add(new JLabel("Outlier"));
		outlierSlider2 = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
		p13.add(outlierSlider2);
		p1.add(p13);

		correlationButton = new JRadioButton("Correlation");
		entropyButton = new JRadioButton("Entropy");
		ButtonGroup group1 = new ButtonGroup();
		group1.add(correlationButton);
		group1.add(entropyButton);
		p1.add(correlationButton);
		p1.add(entropyButton);

		/*
		verticesButton = new JCheckBox("OutlierVertices");
		boundariesButton = new JCheckBox("OutlierBoundaries");
		trianglesButton = new JCheckBox("OutlierTriangles");
		p1.add(verticesButton);
		p1.add(boundariesButton);
		p1.add(trianglesButton);

		hidegraypointsButton = new JCheckBox("HideGrayPoints");
		p1.add(hidegraypointsButton);
		*/

		pane = new JTabbedPane();
		pane.add(p1);
		pane.setTabComponentAt(0, new JLabel("Main"));
		this.add(pane);

		if (bl == null)
			bl = new ButtonListener();
		addButtonListener(bl);

		if (rbl == null)
			rbl = new RadioButtonListener();
		addRadioButtonListener(rbl);

		if (sl == null)
			sl = new SliderListener();
		addSliderListener(sl);

		if (cbl == null)
			cbl = new CheckBoxListener();
		addCheckBoxListener(cbl);
	}


	public void setIndividualCanvas(Object c) {
		icanvas = (IndividualCanvas) c;
	}


	public void setPickedObject(Object p) {
		if(itext != null)
			itext.setOneIndividual((OneIndividual)p);
	}


	/**
	 * タブで区切られた別のパネルを作る
	 */
	public void generatePanels() {
		if(iset == null) return;


		if (iparette != null) {
			iparette.setVisible(false);
			iparette = null;
			pane.remove(3);
		}
		if (icategory != null) {
			icategory.setVisible(false);
			icategory = null;
			pane.remove(2);
		}
		if (itext != null) {
			itext.setVisible(false);
			itext = null;
			pane.remove(1);
		}

		itext = new IndividualTextPanel(iset);
		pane.add(itext);
		pane.setTabComponentAt(1, new JLabel("Text"));

		icategory = new IndividualCategoryPanel(iset, child);
		icategory.setIndividualCanvas((Object) icanvas);
		icategory.setIndividualSelectionPanel(this);
		icategory.setIndividualTextPanel(itext);
		pane.add(icategory);
		pane.setTabComponentAt(2, new JLabel("Category"));

	}


	public void replaceParettePanel(IndividualParettePanel p) {
		if (iparette != null) {
			iparette.setVisible(false);
			iparette = null;
			pane.remove(3);
		}
		iparette = p;
		pane.add(iparette);
		pane.setTabComponentAt(3, new JLabel("Parette"));
	}


	public void addRadioButtonListener(ActionListener actionListener) {
		correlationButton.addActionListener(actionListener);
		entropyButton.addActionListener(actionListener);
	}


	public void addButtonListener(ActionListener actionListener) {
		fileOpenButton.addActionListener(actionListener);
		viewResetButton.addActionListener(actionListener);
	}


	public void addSliderListener(ChangeListener changeListener) {
		thresholdSlider.addChangeListener(changeListener);
		//outlierSlider.addChangeListener(changeListener);
		outlierSlider2.addChangeListener(changeListener);
	}


	public void addCheckBoxListener(ActionListener actionListener) {
		//verticesButton.addActionListener(actionListener);
		//boundariesButton.addActionListener(actionListener);
		//trianglesButton.addActionListener(actionListener);
		//hidegraypointsButton.addActionListener(actionListener);
	}



	class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JButton buttonPushed = (JButton) e.getSource();

			if(buttonPushed == fileOpenButton) {
				FileOpener fileOpener = new FileOpener();
				iset = fileOpener.readFile();
				child = DataSetDivider.divide(iset, IndividualSet.SHOP_CATEGORY_ID);
				generatePanels();
				icanvas.setIndividualSet(iset, child);
				icanvas.display();
			}

			if(buttonPushed == viewResetButton) {
				OutlierDetector.reset(iset);
				icanvas.viewReset();
				icanvas.display();
			}

		}
	}


	class RadioButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JRadioButton buttonPushed = (JRadioButton) e.getSource();
			if(buttonPushed == correlationButton) {
				if(iset != null) {
					iset.DISTANCE_TYPE = DimensionDistance.DISTANCE_CORRELATION;
					DimensionDistance.calc(iset);
					icanvas.setNumSP(numSP);
					dsp = iset.getIndividualSP();
					dsp.setIsOutlier2(false);
					if(plt != null) {
						//plt.pointlump(dsp);
					}
					icanvas.display();
				}
			}
			if(buttonPushed == entropyButton) {
				if(iset != null) {
					iset.DISTANCE_TYPE = DimensionDistance.DISTANCE_ENTROPY;
					DimensionDistance.calc(iset);
					icanvas.setNumSP(numSP);
					dsp = iset.getIndividualSP();
					dsp.setIsOutlier2(false);
					if(plt != null) {
						//plt.pointlump(dsp);
					}
					icanvas.display();
				}
			}
		}
	}


	class SliderListener implements ChangeListener {
		public void stateChanged(ChangeEvent e) {
			JSlider sliderChanged = (JSlider) e.getSource();
			if (sliderChanged == thresholdSlider) {
				if(iset == null) return;
				double v = (double)thresholdSlider.getValue() / 200.0;
				numSP = (int)(v * iset.getNumExplain() * iset.getNumObjective());
				icanvas.setNumSP(numSP);
				dsp = iset.getIndividualSP();
				dsp.setIsOutlier2(false);
				icanvas.display();
			}
			if (sliderChanged == outlierSlider) {
				if(iset == null) return;
				double v = (double)outlierSlider.getValue() / 100.0;

				if(v < 0.025) iset.setValueRange();
				else {
					double v2 =  5.75 - 5.0 * v;
					OutlierDetector.detect(iset, v2);
				}
				dsp = iset.getIndividualSP();
				dsp.setIsOutlier2(false);
				icanvas.display();
			}
			if (sliderChanged == outlierSlider2) {
				if(iset == null) return;
				double v;
				dsp = iset.getIndividualSP();
				//dsp.clearArray();
				//if(outlierSlider2.getValue() == 0) {
				//	dsp.setIsOutlier2(false);
				//}
				//else {
					dsp.setIsOutlier2(true);
				//}
				v = (double)(130 - outlierSlider2.getValue()) / 500.0;
				dsp.clearArray();
				plt = new PointlumpTest();
				plt.setThreshold(v);
				plt.pointlump(dsp);
				icanvas.display();
			}
		}
	}


    class CheckBoxListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JCheckBox checkboxPushed = (JCheckBox) e.getSource();
			if(checkboxPushed == verticesButton) {
				if(iset != null) {
					dsp = iset.getIndividualSP();
					dsp.changeOutlierVertices();
					icanvas.display();
				}
			}
			if(checkboxPushed == boundariesButton) {
				if(iset != null) {
					dsp = iset.getIndividualSP();
					dsp.changeOutlierBoundaries();
					icanvas.display();
				}
			}
			if(checkboxPushed == trianglesButton) {
				if(iset != null) {
					dsp = iset.getIndividualSP();
					dsp.changeOutlierTriangles();
					icanvas.display();
				}
			}
			if(checkboxPushed == hidegraypointsButton) {
				if(iset != null) {
					dsp = iset.getIndividualSP();
					dsp.hideGrayPoints();
					icanvas.display();
				}

			}

		}
	}
}