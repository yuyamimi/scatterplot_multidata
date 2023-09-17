package org.heiankyoview2.core.tree;

import java.awt.Color;


/**
 * 2��Node��A������Link���`����
 * 
 * @author itot
 */
public class Link {

	Node node1, node2;
	String name;
	Color color;

	/**
	 * Constructor
	 * @param name ���O
	 * @param node1 1�ڂ�Node
	 * @param node2 2�ڂ�Node
	 */
	public Link(String name, Node node1, Node node2) {
		// set up nodes on the both ends
		this.name = name;
		this.node1 = node1;
		this.node2 = node2;
	}

	/**
	 * Constructor
	 * @param name ���O
	 */
	public Link(String name) {
		this(name, null, null);
	}

	/**
	 * Constructor
	 * @param LinkID ID
	 */
	public Link(int LinkID) {
	}

	/**
	 * ���O���Z�b�g����
	 * @param name ���O
	 */
	public void setIndex(String name) {
		this.name = name;
	}

	/**
	 * ���O���Q�b�g����
	 * @return ���O
	 */
	public String getName() {
		return name;
	}

	/**
	 * 2��Node�𓯎��ɓo�^����
	 * @param Node
	 */
	public void setNodes(Node n1, Node n2) {
		node1 = n1;    node2 = n2;
	}
	
	/**
	 * 1�Ԗڂ�Node��o�^����
	 * @param Node
	 */
	public void setNode1(Node node) {
		node1 = node;
	}

	/**
	 * 2�Ԗڂ�Node��o�^����
	 * @param Node
	 */
	public void setNode2(Node node) {
		node2 = node;
	}

	/**
	 * 1�Ԗڂ�Node��Ԃ�
	 * @return Node
	 */
	public Node getNode1() {
		return node1;
	}
	
	/**
	 * 2�Ԗڂ�Node��Ԃ�
	 * @return Node
	 */
	public Node getNode2() {
		return node2;
	}


	/**
	 * �A�[�N�̐F��ݒ肷��
	 * @param �A�[�N�̐F
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * �A�[�N�̐F��Ԃ�
	 * @return �A�[�N�̐F
	 */
	public Color getColor() {
		return color;
	}

}
