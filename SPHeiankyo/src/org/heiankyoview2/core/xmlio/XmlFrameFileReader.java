package org.heiankyoview2.core.xmlio;

import org.heiankyoview2.core.tree.Tree;
import org.heiankyoview2.core.tree.Branch;
import org.heiankyoview2.core.tree.Node;
import org.heiankyoview2.core.frame.Frame;
import org.heiankyoview2.core.frame.TreeFrame;

import java.io.*;
import java.util.StringTokenizer;
import javax.xml.parsers.*;
import org.w3c.dom.*;


/**
 * xml�t�@�C����ǂݍ���
 * 
 * @author itot
 */
public class XmlFrameFileReader {

	File xmlFile;
	Document doc;
	Tree tree;
	int numnode = 0, numdimension = 0;
	
	/**
	 * Constructor
	 * 
	 * @param inputFile
	 *            �ǂݍ��݃t�@�C��
	 */
	public XmlFrameFileReader(File inputFile) {

		try {
			// �h�L�������g�r���_�[�t�@�N�g���𐶐�
			DocumentBuilderFactory dbfactory = DocumentBuilderFactory
					.newInstance();
			// �h�L�������g�r���_�[�𐶐�
			DocumentBuilder builder = dbfactory.newDocumentBuilder();
			// �p�[�X�����s����Document�I�u�W�F�N�g���擾
			doc = builder.parse(inputFile);
			// ���[�g�v�f���擾�i�^�O���Fmessage�j

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * DOM�\����parse���āATree���\������f�[�^���\�z����
	 * @param tree Tree
	 * @return ���������true
	 */
	/**
	 * DOM�\����ǂݍ��݁A��������Frame��Tree�ɐݒ肷��
	 * @param tree
	 */
	public void getFrame(Tree tree) {
		org.w3c.dom.Element element;
		
		/*
		 * Document�̐擪�m�[�h�𓾂�
		 */
		org.w3c.dom.Node framesNode = doc.getFirstChild();
		String framesName = framesNode.getNodeName();
		if(framesName.compareTo("frames") != 0) return;
		
		/*
		 * TreeFrame���m�ۂ���
		 */
		TreeFrame tf = new TreeFrame();
		tree.setTreeFrame(tf);
		
		/*
		 * frames�m�[�h����numnode��numdimension�𓾂�
		 */
		element = (org.w3c.dom.Element)framesNode;
		numnode = Integer.parseInt(element.getAttribute("numnode"));
		numdimension = Integer.parseInt(element.getAttribute("numdimension"));
		tf.setNumNodes(numnode);
		tf.setNumValues(numdimension);
		
		/*
		 * frames�m�[�h����frame�m�[�h�𓾂�
		 */
		org.w3c.dom.NodeList  nodelist = framesNode.getChildNodes();
		for(int i = 0; i < nodelist.getLength(); i++) {
			org.w3c.dom.Node frameNode = nodelist.item(i);
			if(frameNode.getNodeName().compareTo("frame") != 0) continue;
			Frame frame = tf.addOneFrame();
			element = (org.w3c.dom.Element)frameNode;
			String time = element.getAttribute("name");
			frame.setTime(Double.parseDouble(time));
			
			/*
			 * frame�m�[�h����framenode�m�[�h�𓾂�
			 */
			org.w3c.dom.NodeList  nodelist2 = frameNode.getChildNodes();
			for(int j = 0; j < nodelist2.getLength(); j++) {
				org.w3c.dom.Node node = nodelist2.item(j);
				if(node.getNodeName().compareTo("framenode") != 0) continue;
				element = (org.w3c.dom.Element)frameNode;
				int nodeId = Integer.parseInt(element.getAttribute("id"));
				StringTokenizer st = new StringTokenizer(
						element.getAttribute("value"), ",");
				for(int k = 1; k <= numdimension; k++) {
					double dvalue = Double.parseDouble(st.nextToken());
					frame.set(k, nodeId, dvalue);
				}
			}
		}
		
		
		return;
	}
	
}