package org.heiankyoview2.core.frame;

import java.awt.Color;
import java.util.Vector;

import org.heiankyoview2.core.table.NodeTablePointer;
import org.heiankyoview2.core.table.Table;
import org.heiankyoview2.core.tree.*;
import org.heiankyoview2.core.util.*;

/**
 * Frame �̏W�����i�[����N���X
 */
public class TreeFrame {
	public static final int FRAME_STRING = 1;
	public static final int FRAME_DOUBLE = 2;
	public static final int FRAME_INT    = 3;
	
	Vector framelist = new Vector();
	int valuetypes[] = null;
	Node nodes[] = null;
	Frame currentFrame = null;
	
	int colorType = -1, heightType = -1, nameType = -1, urlType = -1;
	
	
	/**
	 * Frame��1�ǉ��������A�����Ԃ�
	 * @param frame
	 */
	public Frame addOneFrame() {
		Frame frame = new Frame(this);
		framelist.add((Object)frame);
		frame.setId(framelist.size());
		return frame;
	}
	
	
	
	/**
	 * Frame���Z�b�g����
	 * @param frame
	 */
	public void setFrame(Frame frame) {
		framelist.add((Object)frame);
		frame.setTreeFrame(this);
	}
	
	
	/**
	 * Frame���Q�b�g����
	 * @param id
	 * @return
	 */
	public Frame getFrame(int id) {
		return (Frame)framelist.elementAt(id - 1);
	}
	

	/**
	 * Current Frame���Z�b�g����
	 * @param frame
	 */
	public void setCurrentFrame(Frame frame) {
		currentFrame = frame;
	}
	
	
	/**
	 * CurrentFrame���Q�b�g����
	 * @param id
	 * @return
	 */
	public Frame getCurrentFrame() {
		return currentFrame;
	}
	
	
	/**
	 * Frame�̑������Q�b�g����
	 * @return
	 */
	public int getNumFrames() {
		if(framelist == null) return 0;
		return framelist.size();
	}
	
	/**
	 * �ϐ��̐����Z�b�g����
	 * @param num
	 */
	public void setNumValues(int num) {
		valuetypes = new int[num];
		
		// �f�t�H���g��double�^�Ƃ������Ƃɂ���
		for(int i = 0; i < num; i++)
			valuetypes[i] = FRAME_DOUBLE;
	}
	
	
	/**
	 * �ϐ��̐����Q�b�g����
	 * @return
	 */
	public int getNumValues() {
		if(valuetypes == null) return 0;
		return valuetypes.length;
	}
	
	
	/**
	 * �ϐ��̃^�C�v���Z�b�g����
	 * @param id
	 * @param type
	 */
	public void setValueType(int id, int type) {
		valuetypes[id - 1] = type;
	}
	
	
	/**
	 * �ϐ��̃^�C�v���Q�b�g����
	 * @param id
	 * @return
	 */
	public int getValueType(int id) {
		return valuetypes[id - 1];
	}
	
	
	/**
	 * Node�̐����Z�b�g����
	 * @param num
	 */
	public void setNumNodes(int num) {
		nodes = new Node[num];
	}
	
	
	/**
	 * Node�̐����Q�b�g����
	 * @return
	 */
	public int getNumNodes() {
		if(nodes == null) return 0;
		return nodes.length;
	}
	
	
	/**
	 * Node���Z�b�g����
	 * @param id
	 * @param type
	 */
	public void setNode(int id, Node node) {
		nodes[id - 1] = node;
	}
	
	
	/**
	 * Node���Q�b�g����
	 * @param id
	 * @return
	 */
	public Node getNode(int id) {
		return nodes[id - 1];
	}
	
	
	/**
	 * �F�v�Z�ɗp����Table��ID���Z�b�g����
	 * @param type �F�v�Z�ɗp����Table��ID
	 */
	public void setColorType(int type) {
		colorType = type;
	}

	/**
	 * �F�v�Z�ɗp����Table��ID��Ԃ�
	 * @return �F�v�Z�ɗp����Table��ID
	 */
	public int getColorType() {
		return colorType;
	}

	/**
	 * �����v�Z�ɗp����Table��ID���Z�b�g����
	 * @param type �����v�Z�ɗp����Table��ID
	 */
	public void setHeightType(int type) {
		heightType = type;
	}

	/**
	 * �����v�Z�ɗp����Table��ID��Ԃ�
	 * @return �����v�Z�ɗp����Table��ID
	 */
	public int getHeightType() {
		return heightType;
	}

	/**
	 * ���O���w�肷��Table��ID���Z�b�g����
	 * @param type ���O���w�肷��Table��ID
	 */
	public void setNameType(int type) {
		nameType = type;
	}

	/**
	 * ���O���w�肷��Table��ID��Ԃ�
	 * @return ���O���w�肷��Table��ID
	 */
	public int getNameType() {
		return nameType;
	}

	/**
	 * URL���w�肷��Table��ID���Z�b�g����
	 * @param type URL���w�肷��Table��ID
	 */
	public void setUrlType(int type) {
		urlType = type;
	}

	/**
	 * URL���w�肷��Table��ID��Ԃ�
	 * @return URL���w�肷��Table��ID
	 */
	public int getUrlType() {
		return urlType;
	}

	
	
	/**
	 * Node�̐F���Z�o����
	 * @param node Node
	 * @return Node�̐F
	 */
	public Color calcNodeColor(Node node, ColorCalculator cc) {
		double value = -1.0;
		Color color = Color.gray;

		int nodeId = node.getFrameNodeId();
		value = currentFrame.getAppearanceValue(colorType, nodeId);
		color = cc.calculate((float) value);

		return color;
	}

	
	
	/**
	 * Node�̐F�Z�o�̂��߂̒l�i0.0�`1.0�j���Z�o����
	 * @param node Node
	 * @return Node�̐F�Z�o�̂��߂̒l�i0.0�`1.0�j
	 */
	public double calcNodeColorValue(Node node) {
		double value = 0.5;
		if(currentFrame == null)
			return 0.5;
		if(colorType <= 0 || colorType > getNumValues())
			return 0.5;
		
		int nodeId = node.getFrameNodeId();
		value = currentFrame.getAppearanceValue(colorType, nodeId);
		return value;
	}

	
	/**
	 * Node�̍����Z�o�̂��߂̒l�i0.0�`1.0�j���Z�o����
	 * @param node Node
	 * @return Node�̍����Z�o�̂��߂̒l�i0.0�`1.0�j
	 */
	public double calcNodeHeightValue(Node node) {
		double value = 0.5;
		if(currentFrame == null)
			return 0.5;
		if(heightType <= 0 || heightType > getNumValues())
			return 0.5;
		
		int nodeId = node.getFrameNodeId();
		value = currentFrame.getAppearanceValue(heightType, nodeId);
		return value;
	}
	
	
	/**
	 * Frame���Q�Ƃ��Ȃ���Node�̖��O��Ԃ�
	 * @param node Node
	 * @param type �Q�Ƃ���Table��ID
	 * @return Node�̖��O
	 */
	public String getNodeAttributeName(Node node, int type) {
		String name = null;
		int nodeId = node.getFrameNodeId();

		if (valuetypes[type - 1] == FRAME_STRING) {
			name = currentFrame.getString(type, nodeId);
		}
		if (valuetypes[type - 1] == FRAME_DOUBLE) {
			name = Double.toString(currentFrame.getDouble(type, nodeId));
		}
		if (valuetypes[type - 1] == FRAME_INT) {
			name = Integer.toString(currentFrame.getInt(type, nodeId));
		}

		return name;
	}

}
