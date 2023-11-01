package ocha.itolab.hidden2.applet.spset3awt;

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
import ocha.itolab.hidden2.core.tool.DimensionDistanceCombination;
import ocha.itolab.hidden2.core.tool.OutlierDetector;



public class IndividualSelectionPanel extends JPanel {

	IndividualSet iset = null;
	/*****/
	IndividualSP dsp = null;
	PointlumpTest plt = null;
	/*****/
	int numSP = 0;

	Font font = new Font("Arial", Font.PLAIN, 16);;

	static int colorcount = 0;
	int saveflag = IndividualDrawer.SAVE_AS_IS;
	
	
	/* Selective canvas */
	IndividualCanvas icanvas;
	IndividualCategoryPanel icategory;
	IndividualTextPanel itext;
	IndividualParettePanel iparette;

	public JButton  fileOpenButton, viewResetButton, reselectButton, imageSaveButton;
	public JRadioButton combinationButton, correlationButton, entropyButton, skinnyButton, clumpyButton,
						saveAIButton, saveULButton, saveURButton, saveLLButton, saveLRButton;
	public JCheckBox verticesButton, boundariesButton, trianglesButton, hidegraypointsButton;
	public JSlider numScatterSlider, outlierSlider, outlierSlider2, similaritySlider;
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
		p1.setLayout(new GridLayout(20,1));

		fileOpenButton = new JButton("File Open");
		p1.add(fileOpenButton);
		viewResetButton = new JButton("View Reset");
		p1.add(viewResetButton);
		reselectButton = new JButton("Reselect");
		p1.add(reselectButton);
		combinationButton = new JRadioButton("Combination");
		p1.add(combinationButton);
		correlationButton = new JRadioButton("Correlation");
		p1.add(correlationButton);
		entropyButton = new JRadioButton("Entropy");
		p1.add(entropyButton);
		skinnyButton = new JRadioButton("Skinny");
		p1.add(skinnyButton);
		clumpyButton = new JRadioButton("Clumpy");
		p1.add(clumpyButton);
		ButtonGroup group1 = new ButtonGroup();
		group1.add(combinationButton);
		group1.add(correlationButton);
		group1.add(entropyButton);
		group1.add(skinnyButton);
		group1.add(clumpyButton);
		
		JPanel p11 = new JPanel();
		p11.setLayout(new GridLayout(2,1));
		p11.add(new JLabel("Num. Scatterplots"));
		numScatterSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
		p11.add(numScatterSlider);
		p1.add(p11);

		JPanel p12 = new JPanel();
		p12.setLayout(new GridLayout(2,1));
		p12.add(new JLabel("Similarity"));
		similaritySlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
		p12.add(similaritySlider);
		p1.add(p12);

		JPanel p13 = new JPanel();
		p13.setLayout(new GridLayout(2,1));
		p13.add(new JLabel("Outlier"));
		outlierSlider2 = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
		p13.add(outlierSlider2);
		p1.add(p13);

		imageSaveButton = new JButton("Image Save");
		p1.add(imageSaveButton);
		ButtonGroup group4 = new ButtonGroup();
		saveAIButton = new JRadioButton("As Is");
		saveULButton = new JRadioButton("Upper Left");
		saveURButton = new JRadioButton("Upper Right");
		saveLLButton = new JRadioButton("Lower Left");
		saveLRButton = new JRadioButton("Lower Right");
		p1.add(saveAIButton);
		p1.add(saveULButton);
		p1.add(saveURButton);
		p1.add(saveLLButton);
		p1.add(saveLRButton);
		group4.add(saveAIButton);
		group4.add(saveULButton);
		group4.add(saveURButton);
		group4.add(saveLLButton);
		group4.add(saveLRButton);
	
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
		dsp = icanvas.getSP();
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

		icategory = new IndividualCategoryPanel(iset);
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
		combinationButton.addActionListener(actionListener);
		correlationButton.addActionListener(actionListener);
		entropyButton.addActionListener(actionListener);
		skinnyButton.addActionListener(actionListener);
		clumpyButton.addActionListener(actionListener);
		saveAIButton.addActionListener(actionListener);
		saveULButton.addActionListener(actionListener);
		saveURButton.addActionListener(actionListener);
		saveLLButton.addActionListener(actionListener);
		saveLRButton.addActionListener(actionListener);
	}


	public void addButtonListener(ActionListener actionListener) {
		fileOpenButton.addActionListener(actionListener);
		viewResetButton.addActionListener(actionListener);
		reselectButton.addActionListener(actionListener);
		imageSaveButton.addActionListener(actionListener);
	}


	public void addSliderListener(ChangeListener changeListener) {
		numScatterSlider.addChangeListener(changeListener);
		similaritySlider.addChangeListener(changeListener);
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

				generatePanels();
				icanvas.setIndividualSet(iset);
				iset.DISTANCE_TYPE = DimensionDistance.DISTANCE_COMBINATION;
				DimensionDistance.calc(iset);
				icanvas.display();
			}

			if(buttonPushed == viewResetButton) {
				OutlierDetector.reset(iset);
				icanvas.viewReset();
				icanvas.display();
			}

			if(buttonPushed == reselectButton) {
				iset.DISTANCE_TYPE = DimensionDistance.DISTANCE_COMBINATION;
				DimensionDistance.calc(iset);
				icanvas.setNumSP(numSP);
				dsp.setIsOutlier2(false);
				if(plt != null) {
					plt.pointlump(dsp.getPoints());
				}
				icanvas.display();
			}
			
			if(buttonPushed == imageSaveButton) {
				icanvas.saveImageFile(saveflag);
				icanvas.display();
			}
		}
	}


	class RadioButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JRadioButton buttonPushed = (JRadioButton) e.getSource();
			if(iset == null) return;
			
			if(buttonPushed == saveAIButton) {
				saveflag = IndividualDrawer.SAVE_AS_IS; return;
			}
			if(buttonPushed == saveULButton) {
				saveflag = IndividualDrawer.SAVE_UPPER_LEFT; return;
			}
			if(buttonPushed == saveURButton) {
				saveflag = IndividualDrawer.SAVE_UPPER_RIGHT; return;
			}
			if(buttonPushed == saveLLButton) {
				saveflag = IndividualDrawer.SAVE_LOWER_LEFT; return;
			}
			if(buttonPushed == saveLRButton) {
				saveflag = IndividualDrawer.SAVE_LOWER_RIGHT; return;
			}
			
			if(buttonPushed == combinationButton) 
				iset.DISTANCE_TYPE = DimensionDistance.DISTANCE_COMBINATION;
			if(buttonPushed == correlationButton) 
				iset.DISTANCE_TYPE = DimensionDistance.DISTANCE_CORRELATION;
			if(buttonPushed == entropyButton) 
				iset.DISTANCE_TYPE = DimensionDistance.DISTANCE_ENTROPY;
			if(buttonPushed == skinnyButton) 
				iset.DISTANCE_TYPE = DimensionDistance.DISTANCE_SKINNY;
			if(buttonPushed == clumpyButton) 
				iset.DISTANCE_TYPE = DimensionDistance.DISTANCE_CLUMPY;
	
			DimensionDistance.calc(iset);
			icanvas.setNumSP(numSP);
			dsp.setIsOutlier2(false);
			icanvas.display();
			
			
		}
	}


	class SliderListener implements ChangeListener {
		public void stateChanged(ChangeEvent e) {
			JSlider sliderChanged = (JSlider) e.getSource();
			if (sliderChanged == numScatterSlider) {
				if(iset == null) return;
				double v = (double)numScatterSlider.getValue() / 200.0;
				numSP = (int)(v * iset.getNumExplain() * iset.getNumObjective());
				icanvas.setNumSP(numSP);
				dsp.setIsOutlier2(false);
				icanvas.display();
			}
			if (sliderChanged == similaritySlider) {
				if(iset == null) return;
				double v = (double)similaritySlider.getValue() / 10000.0;
				DimensionDistanceCombination.setMinimimDistance(v);
			}
			if (sliderChanged == outlierSlider2) {
				if(iset == null) return;
				double v;
				v = (double)(130 - outlierSlider2.getValue()) / 500.0;
				dsp.clearArray();
				plt = new PointlumpTest();
				plt.setThreshold(v);
				plt.pointlump(dsp.getPoints());
				icanvas.display();
			}
		}
	}


    class CheckBoxListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JCheckBox checkboxPushed = (JCheckBox) e.getSource();
			if(checkboxPushed == verticesButton) {
				if(iset != null) {
					dsp.changeOutlierVertices();
					icanvas.display();
				}
			}
			if(checkboxPushed == boundariesButton) {
				if(iset != null) {
					dsp.changeOutlierBoundaries();
					icanvas.display();
				}
			}
			if(checkboxPushed == trianglesButton) {
				if(iset != null) {
					dsp.changeOutlierTriangles();
					icanvas.display();
				}
			}
			if(checkboxPushed == hidegraypointsButton) {
				if(iset != null) {
					dsp.hideGrayPoints();
					icanvas.display();
				}

			}

		}
	}
}