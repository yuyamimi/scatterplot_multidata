package ocha.itolab.hidden2.applet.spset4awt;

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

import ocha.itolab.hidden2.core.data.IndividualSet;
import ocha.itolab.hidden2.core.data.OneIndividual;
import ocha.itolab.hidden2.core.tool.DataSetDivider;
import ocha.itolab.hidden2.core.tool.DimensionDistance;
import ocha.itolab.hidden2.core.tool.DimensionDistanceCombination;
import ocha.itolab.hidden2.core.tool.HierarchicalClustering;
import ocha.itolab.hidden2.core.tool.OutlierDetector;



public class IndividualSelectionPanel extends JPanel {

	IndividualSet iset = null;
	/*****/
	IndividualSP dsp = null;
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
	/*
	public JRadioButton combinationButton, correlationButton, entropyButton, skinnyButton, clumpyButton,
						saveAIButton, saveULButton, saveURButton, saveLLButton, saveLRButton;
	*/
	/*
	public JCheckBox verticesButton, boundariesButton, trianglesButton, hidegraypointsButton;
	*/
	public JSlider numScatterSlider, numClusterSlider;
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
		JPanel p11 = new JPanel();
		p11.setLayout(new GridLayout(2,1));
		p11.add(new JLabel("Num. Scatterplots"));
		numScatterSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
		p11.add(numScatterSlider);
		p1.add(p11);

		JPanel p12 = new JPanel();
		p12.setLayout(new GridLayout(2,1));
		p12.add(new JLabel("Num. Cluster"));
		numClusterSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
		p12.add(numClusterSlider);
		p1.add(p12);

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
	 * �^�u�ŋ�؂�ꂽ�ʂ̃p�l�������
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
		
	}


	public void addButtonListener(ActionListener actionListener) {
		fileOpenButton.addActionListener(actionListener);
		viewResetButton.addActionListener(actionListener);
		reselectButton.addActionListener(actionListener);
		
	}


	public void addSliderListener(ChangeListener changeListener) {
		numScatterSlider.addChangeListener(changeListener);
		numClusterSlider.addChangeListener(changeListener);
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
			
		}
	}


	class SliderListener implements ChangeListener {
		public void stateChanged(ChangeEvent e) {
			JSlider sliderChanged = (JSlider) e.getSource();
			if (sliderChanged == numScatterSlider) {
				if(iset == null) return;
				double v = (double)numScatterSlider.getValue() / 100.0;
				numSP = (int)(v * iset.getNumExplain() * iset.getNumObjective());
				icanvas.setNumSP(numSP);
				icanvas.display();
			}
			if (sliderChanged == numClusterSlider) {
				if(iset == null) return;
				int n = (int)((double)numClusterSlider.getValue() / 10.0);
				if(n < 2) n = 2;
				HierarchicalClustering.NUMCLUSTER = n;
				DimensionDistanceCombination.callHierarchicalClustering();
				double v = (double)numScatterSlider.getValue() / 100.0;
				numSP = (int)(v * iset.getNumExplain() * iset.getNumObjective());
				icanvas.setNumSP(numSP);
				icanvas.display();
			}
			
		}
	}


    class CheckBoxListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JCheckBox checkboxPushed = (JCheckBox) e.getSource();
			
		}
	}
}