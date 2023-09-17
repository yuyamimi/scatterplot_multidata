package org.heiankyoview2.core.frame;

public class FrameString {
	String array[];

	
	/**
	 * Constructor
	 * @param numnodes
	 */
	public FrameString(int numnodes) {
		array = new String[numnodes];
	}
	
	
	/**
	 * �l��1�Z�b�g����
	 * @param nodeid
	 * @param value
	 */
	public void set(int nodeid, String value) {
		if(nodeid <= 0 || nodeid > array.length) {
			System.out.println("  FrameString: nodeid " + nodeid + " is out of range.");
			return;
		}
		array[nodeid - 1] = value;
	}
	
	
	/**
	 * �l��1�Q�b�g����
	 * @param nodeid
	 * @return
	 */
	public String get(int nodeid) {
		if(nodeid <= 0 || nodeid > array.length) {
			System.out.println("  FrameString: nodeid " + nodeid + " is out of range.");
			return null;
		}
		return array[nodeid - 1];
	}
	
	
	/**
	 * Node�̐F�⍂�����Z�o���邽�߂̒l��Ԃ�
	 * @param id �l���i�[���Ă���ID
	 * @return Node�̐F�⍂�����Z�o���邽�߂̒l�i�����Ƃ���0.0�`1.0�A�s����ID�̏ꍇ�͕��l�j
	 */
	double getAppearanceValue(int id) {
		if(id <= 0 || id > array.length)
			return -1.0;
		double value = (double) (id - 1) / (double) array.length;
		return value;
	}
}
