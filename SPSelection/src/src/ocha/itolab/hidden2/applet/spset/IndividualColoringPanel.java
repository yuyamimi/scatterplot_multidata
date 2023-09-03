package ocha.itolab.hidden2.applet.spset;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import javax.swing.*;

import ocha.itolab.hidden2.core.data.*;
import ocha.itolab.hidden2.core.tool.*;


public class IndividualColoringPanel extends JPanel {
	IndividualSet iset;
	IndividualCanvas icanvas;
	IndividualParettePanel parette;
	IndividualTextPanel itext;
	
	public JRadioButton variableButtons[];
	
	RadioButtonListener rbl = null;
	
	public IndividualColoringPanel(IndividualSet is) {
		super();
		
		if(is == null) return;
		iset = is;
		int numb = iset.getNumCategory() + iset.getNumBoolean();
		variableButtons = new JRadioButton[numb + 1];
		ButtonGroup group1 = new ButtonGroup();
		JPanel p1 = new JPanel();
		p1.setLayout(new GridLayout((numb + 1), 1));
		
		// for each button
		for(int i = 0; i <= numb; i++) {
			String name = "None";
			if(i < numb)
				name = iset.getValueName(i + iset.getNumExplain() + iset.getNumObjective());
			variableButtons[i] = new JRadioButton(name);
			group1.add(variableButtons[i]);
			p1.add(variableButtons[i]);
		}
		
		parette = new IndividualParettePanel(iset);
				
		JScrollPane scroll = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
		        	JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setViewportView(p1);
		scroll.setPreferredSize(new Dimension(250, 300));
		scroll.setVisible(true);
				
		this.setLayout(new GridLayout(2, 1));
		add(scroll);
		add(parette);
		
		if (rbl == null)
			rbl = new RadioButtonListener();
		addRadioButtonListener(rbl);
	}
	
	
	public void setIndividualCanvas(Object c) {
		icanvas = (IndividualCanvas)c;
	}
	
	
	public void setIndividualTextPanel(IndividualTextPanel p) {
		itext = p;
	}
	
	
	/**
	 * ���W�I�{�^���̃A�N�V�����̌��o��ݒ肷��
	 * @param actionListener ActionListener
	 */
	public void addRadioButtonListener(ActionListener actionListener) {

		for(int i = 0; i < variableButtons.length; i++) 
			variableButtons[i].addActionListener(actionListener);
	}

	
	/**
	 * ���W�I�{�^���̃A�N�V���������m����ActionListener
	 * @or itot
	 */
	public class RadioButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JRadioButton buttonPushed = (JRadioButton) e.getSource();
			
			for(int i = 0; i < variableButtons.length; i++) {
				if (buttonPushed == variableButtons[i]) {
					
					int id = (i == variableButtons.length - 1) ? -1 : i;
					iset.setClassId(id);
					DimensionDistance.calc(iset);
					int nclass = ClassColorAssigner.assign(iset);
					icanvas.setNumClusters(nclass);
					icanvas.display();
					parette.draw(i);

					break;
				}
			}
			
		}
	}
	
		
			
}
