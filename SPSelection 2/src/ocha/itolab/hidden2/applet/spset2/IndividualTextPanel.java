package ocha.itolab.hidden2.applet.spset2;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import ocha.itolab.hidden2.core.data.*;

public class IndividualTextPanel extends JPanel {
	JTextArea textArea;
	IndividualSet iset;
	
	public IndividualTextPanel(IndividualSet is) {
		super();
		iset = is;
		
		JPanel p1 = new JPanel();
		p1.setLayout(new GridLayout(1,1));
		textArea = new JTextArea();
		textArea.setPreferredSize(new Dimension(500, 2000));
		JScrollPane scroll1 = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
	        	JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll1.setViewportView(textArea);
		scroll1.setPreferredSize(new Dimension(250, 500));
		p1.add(scroll1);
		
		this.add(p1);
	}

	
	boolean setDimLock = false;
	
	public void setOneIndividual(OneIndividual p) {
		String text = p.getName() + "\n";
		text += p.getCategoryValues()[3] + "\n";
		
		for(int i = 0; i < iset.getNumExplain(); i++) {
			text += (iset.getValueName(i) + " ");
			text += Double.toString(p.getExplainValues()[i]);
			text += '\n';
		}
		for(int i = 0; i < iset.getNumObjective(); i++) {
			text += (iset.getValueName(iset.getNumExplain() + i) + " ");
			text += Double.toString(p.getObjectiveValues()[i]);
			text += '\n';
		}
		
		/*
		text += "\n";
		text += "Revenue: ”„ã\n";
		text += "Guest1: w“ül”\n";
		text += "Guest2: —ˆ‹ql”\n";
		text += "Ratio: ”ƒã—¦\n";
		text += "PerGuest: ‹q’P‰¿\n";
		text += "AveUnit: •½‹Ï”ƒã¤•i’P‰¿\n";
		text += "AveNum: •½‹Ï”ƒã“_”\n";
		 */
		
		textArea.setText(text);
	}
	
	

}
