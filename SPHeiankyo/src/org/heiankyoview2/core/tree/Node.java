package org.heiankyoview2.core.tree;

import java.awt.Color;
import java.awt.Image;
import org.heiankyoview2.core.table.NodeTablePointer;

/**
 * �K�w�^�f�[�^�̍ŏ��P�ʂł���Node���`����
 * 
 * @author itot
 */
public class Node {

	// Node �̈ʒu�A�傫���A�F
	int id; 
	double x, y, z;
	double width, height, depth;
	Color color;
	String name = "";

	// Branch, Image �Ȃ�
	Branch childBranch;
	Branch currentBranch;
	String childFilename;
	String imageUrl;
	Image  image;
	Object texture;

	// Table �ւ̃|�C���^
	public NodeTablePointer table;

	// Frame �ւ̃|�C���^
	int frameNodeId = 0;
	
	// Template �֌W�̕ϐ�
	boolean isTemplateApplied = false;
	double nx, ny, nz;
	double nwidth, nheight, ndepth;
	boolean isPlaced = false;
	boolean isDefaultSize = true;
	double length;

	/**
	 * Constructor
	 * @param id
	 * @param currentBranch
	 * @param x
	 * @param y
	 * @param z
	 */
	public Node(int id, Branch currentBranch, double x, double y, double z) {
		setId(id);
		setCoordinate(x, y, z);
		setCurrentBranch(currentBranch);;
		width = height = depth = 1.0;
		nwidth = nheight = ndepth = 1.0;
		length = 0.0;
		childBranch = null;
		table = new NodeTablePointer();
	}

	/**
	 * Constructor
	 * @param id
	 * @param currentBranch
	 */
	public Node(int id, Branch currentBranch) {
		this(id, currentBranch, 0.0, 0.0, 0.0);
	}

	/**
	 * Constructor
	 *
	 */
	public Node() {
		this(-1, null, 0.0, 0.0, 0.0);
	}

	/**
	 * �m�[�h�̔ԍ���ݒ肷��
	 * @param �m�[�h�̔ԍ�
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * �m�[�h�̔ԍ���Ԃ�
	 * @return �m�[�h�̔ԍ�
	 */
	public int getId() {
		return id;
	}


	/**
	 * �m�[�h�̖��O��ݒ肷��
	 * @param �m�[�h�̖��O
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * �m�[�h�̖��O��Ԃ�
	 * @return �m�[�h�̖��O
	 */
	public String getName() {
		return name;
	}


	/**
	 * Frame��NodeId��ݒ肷��
	 * @param id Frame��NodeId
	 */
	public void setFrameNodeId(int id) {
		frameNodeId = id;
	}
	

	/**
	 * Frame��NodeId��Ԃ�
	 * @return Frame��NodeId
	 */
	public int getFrameNodeId() {
		return frameNodeId;
	}
	
	
	/**
	 * �m�[�h�̍��W�l��ݒ肷��
	 * @param �m�[�h��x���W�l
	 * @param �m�[�h��y���W�l
	 * @param �m�[�h��z���W�l
	 */
	public void setCoordinate(double x, double y, double z) {
		setX(x);
		setY(y);
		setZ(z);
	}

	/**
	 * �m�[�h��x���W�l��ݒ肷��
	 * @param �m�[�h��x���W�l
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * �m�[�h��x���W�l��Ԃ�
	 * @return �m�[�h��x���W�l
	 */
	public double getX() {
		return x;
	}

	/**
	 * �m�[�h��y���W�l��ݒ肷��
	 * @param �m�[�h��y���W�l
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * �m�[�h��y���W�l��Ԃ�
	 * @return �m�[�h��y���W�l
	 * 
	 */
	public double getY() {
		return y;
	}

	/**
	 * �m�[�h��z���W�l��ݒ肷��
	 * @param �m�[�h��z���W�l
	 */
	public void setZ(double z) {
		this.z = z;
	}

	/**
	 * �m�[�h��z���W�l��Ԃ�
	 * @return �m�[�h��z���W�l
	 */
	public double getZ() {
		return z;
	}


	/**
	 * �m�[�h�̎q�O���[�v��ݒ肷��
	 * @param �m�[�h�̎q�O���[�v
	 */
	public void setChildBranch(Branch childBranch) {
		this.childBranch = childBranch;
	}

	/**
	 * �m�[�h�̎q�O���[�v��Ԃ�
	 * @return �m�[�h�̎q�O���[�v
	 */
	public Branch getChildBranch() {
		return childBranch;
	}

	/**
	 * �m�[�h�̏����O���[�v��ݒ肷��
	 * @param �m�[�h�̏����O���[�v
	 */
	public void setCurrentBranch(Branch currentBranch) {
		this.currentBranch = currentBranch;
	}

	/**
	 * �m�[�h�̏����O���[�v��Ԃ�	
	 * @return �m�[�h�̏����O���[�v
	 */
	public Branch getCurrentBranch() {
		return currentBranch;
	}

	/**
	 * �m�[�h�̐F��ݒ肷��
	 * @param �m�[�h�̐F
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * �m�[�h�̐F��Ԃ�
	 * @return �m�[�h�̐F
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * �m�[�h�̃T�C�Y��ݒ肷��
	 * @param �m�[�h�̉���
	 * @param �m�[�h�̏c��
	 * @param �m�[�h�̉��s��
	 */
	public void setSize(double width, double height, double depth) {
		this.width = width;
		this.height = height;
		this.depth = depth;
		isDefaultSize = false;
	}

	/**
	 * �m�[�h�̉�����ݒ肷��
	 * @param �m�[�h�̉���
	 */
	public void setWidth(double width) {
		this.width = width;
		isDefaultSize = false;
	}
	
	/**
	 * �m�[�h�̉�����Ԃ�
	 * @return �m�[�h�̉���
	 */
	public double getWidth() {
		return width;
	}


	/**
	 * �m�[�h�̏c����ݒ肷��
	 * @param �m�[�h�̏c��
	 */
	public void setHeight(double height) {
		this.height = height;
		isDefaultSize = false;
	}
	
	/**
	 * �m�[�h�̏c����Ԃ�
	 * @return �m�[�h�̏c��
	 */
	public double getHeight() {
		return height;
	}

	/**
	 * �m�[�h�̉��s����ݒ肷��
	 * @param �m�[�h�̉��s��
	 */
	public void setDepth(double depth) {
		this.depth = depth;
		isDefaultSize = false;
	}

	/**
	 * �m�[�h�̉��s����Ԃ�
	 * @return �m�[�h�̉��s��
	 */
	public double getDepth() {
		return depth;
	}


	/**
	 * �m�[�h�Ƀf�[�^�t�@�C�����֘A�t�����Ă���Ƃ��A���̃t�@�C����ݒ肷��
	 * @param �֘A�t����ꂽ�f�[�^�t�@�C��
	 */
	public void setChildFilename(String filename) {
		this.childFilename = filename;
	}

	/**
	 * �m�[�h�Ƀf�[�^�t�@�C�����֘A�t�����Ă���Ƃ��A���̃t�@�C����Ԃ�
	 * @return �֘A�t����ꂽ�f�[�^�t�@�C��
	 */
	public String getChildFilename() {
		return childFilename;
	}

	/**
	 * �m�[�h�ɉ摜URL���֘A�t�����Ă���Ƃ��A����URL��ݒ肷��
	 * @param �֘A�t����ꂽ�摜URL
	 */
	public void setImageUrl(String url) {
		this.imageUrl = url;
	}

	/**
	 * �m�[�h�ɉ摜URL���֘A�t�����Ă���Ƃ��A����URL��Ԃ�
	 * @param �֘A�t����ꂽ�A�C�R���t�@�C��
	 */
	public String getImageUrl() {
		return imageUrl;
	}
	
	/**
	 * �摜��ݒ肷��
	 * @param �摜
	 */
	public void setImage(Image image) {
		this.image = image;
	}

	/**
	 * �摜��Ԃ�
	 * @return �摜
	 */
	public Image getImage() {
		return image;
	}
	
	/**
	 * �e�N�X�`����ݒ肷��
	 * @param �e�N�X�`��
	 */
	public void setTexture(Object texture) {
		this.texture = texture;
	}
	
	/**
	 * �e�N�X�`����Ԃ�
	 * @return �e�N�X�`��
	 */
	public Object getTexture() {
		return texture;
	}

	/**
	 * �T�C�Y�������l�ł��邩�ǂ�����Ԃ�
	 */
	public boolean isDefaultSize() {
		return isDefaultSize;
	}
	
	/*
	 * �ȉ��e���v���[�g�O���t�ɌŗL�̃��\�b�h
	 */
	
	public void setNX(double nx) {
		this.nx = nx;
	}
	public double getNX() {
		return nx;
	}
	public void setNY(double ny) {
		this.ny = ny;
	}
	public double getNY() {
		return ny;
	}
	public void setNZ(double nz) {
		this.nz = nz;
	}
	public double getNZ() {
		return nz;
	}
	public void setNCoordinate(double nx, double ny, double nz) {
		this.nx = nx;
		this.ny = ny;
		this.nz = nz;
	}

	public void setNwidth(double nwidth) {
		this.nwidth = nwidth;
	}
	public double getNwidth() {
		return nwidth;
	}
	public void setNheight(double nheight) {
		this.nheight = nheight;
	}
	public double getNheight() {
		return nheight;
	}

	public void setNdepth(double ndepth) {
		this.ndepth = ndepth;
	}
	public double getNdepth() {
		return ndepth;
	}

	public void setNsize(double nwidth, double nheight, double ndepth) {
		this.nwidth = nwidth;
		this.nheight = nheight;
		this.ndepth = ndepth;
	}


	public void setTemplateFlag(boolean i) {
		this.isTemplateApplied = i;
	}
	public boolean getTemplateFlag() {
		return this.isTemplateApplied;
	}

	public void setPlaced(boolean i) {
		this.isPlaced = i;
	}
	public boolean getPlaced() {
		return this.isPlaced;
	}

	public double getLength() {
		return length;
	}
	public void setLength(double l) {
		this.length = Math.abs(l);
	}
	
	public void calcLength(Node maxNode) {
		double l = 0;

		l =
			Math.pow((nx - maxNode.getNX()), 2)
				+ Math.pow((ny - maxNode.getNY()), 2);

		this.length = Math.abs(l);
	}
}
