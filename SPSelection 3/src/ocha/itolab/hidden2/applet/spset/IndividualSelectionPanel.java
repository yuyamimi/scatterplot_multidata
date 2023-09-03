package ocha.itolab.hidden2.applet.spset;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ocha.itolab.hidden2.core.data.*;
import ocha.itolab.hidden2.core.tool.*;


public class IndividualSelectionPanel extends JPanel {
	
	IndividualSet iset = null;
	
	Font font = new Font("Arial", Font.PLAIN, 16);;
	
	static int colorcount = 0;
	
	/* Selective canvas */
	IndividualCanvas icanvas;
	IndividualColoringPanel icoloring;
	IndividualTextPanel itext;
	
	public JButton  fileOpenButton, viewResetButton, clusteringButton;
	public JTextField numClusterField;
	public JSlider transparencySlider, thresholdSlider;
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
		p1.setLayout(new GridLayout(10,1));
		
		fileOpenButton = new JButton("File Open");
		p1.add(fileOpenButton);
		viewResetButton = new JButton("View Reset");
		p1.add(viewResetButton);
		p1.add(new JLabel("Transparency"));
		transparencySlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
		p1.add(transparencySlider);
		p1.add(new JLabel("Threshold"));
		thresholdSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
		p1.add(thresholdSlider);
		
		JPanel p11 = new JPanel();
		p11.setLayout(new GridLayout(1,2));
		clusteringButton = new JButton("Clustering");
		p11.add(clusteringButton);
		numClusterField = new JTextField("1");
		p11.add(numClusterField);
		p1.add(p11);
				
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
		
		if (itext != null) {
			itext.setVisible(false);
			itext = null;
			pane.remove(2);
		}

		if (icoloring != null) {
			icoloring.setVisible(false);
			icoloring = null;
			pane.remove(1);
		}
		
		icoloring = new IndividualColoringPanel(iset);
		icoloring.setIndividualCanvas((Object) icanvas);
		pane.add(icoloring);
		pane.setTabComponentAt(1, new JLabel("Coloring"));

		itext = new IndividualTextPanel(iset);
		pane.add(itext);
		pane.setTabComponentAt(2, new JLabel("Text"));

		icoloring.setIndividualTextPanel(itext);
	}
	
	
	
	public void addRadioButtonListener(ActionListener actionListener) {
		
	}
	
	
	public void addButtonListener(ActionListener actionListener) {
		fileOpenButton.addActionListener(actionListener);
		viewResetButton.addActionListener(actionListener);
		clusteringButton.addActionListener(actionListener);
	}
	
	
	public void addSliderListener(ChangeListener changeListener) {
		transparencySlider.addChangeListener(changeListener);
		thresholdSlider.addChangeListener(changeListener);
	}
	
	
	public void addCheckBoxListener(ActionListener actionListener) {
		
	}
	
	

	class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JButton buttonPushed = (JButton) e.getSource();
			
			if(buttonPushed == fileOpenButton) {	
				FileOpener fileOpener = new FileOpener();
				iset = fileOpener.readFile();
				generatePanels();
				icanvas.setIndividualSet(iset);
				icanvas.display();
			}	
			
			if(buttonPushed == viewResetButton) {
				icanvas.viewReset();
				icanvas.display();
			}
			
			if(buttonPushed == clusteringButton) {
				int num = Integer.parseInt(numClusterField.getText());
				/*
				if(iset != null) {
					KmeansByNumeric.execute(iset, num);
					icanvas.setNumClusters(num);
					icanvas.display();
				}
				*/
			}
		}
	}
	
	
	class RadioButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JRadioButton buttonPushed = (JRadioButton) e.getSource();
			
		}
	}
	
	
	class SliderListener implements ChangeListener {
		public void stateChanged(ChangeEvent e) {
			JSlider sliderChanged = (JSlider) e.getSource();
			if (sliderChanged == transparencySlider) {
				double t = (double)transparencySlider.getValue() / 100.0;
				icanvas.setTransparency(t);
				icanvas.display();
			}
			if (sliderChanged == thresholdSlider) {
				double c = (double)thresholdSlider.getValue() / 200.0 + 0.5;
				icanvas.setThreshold(c);
				icanvas.setIndividualSet(iset);
				icanvas.display();
			}
		}
	}
	
	
    class CheckBoxListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			icanvas.display();
		}
	}
}