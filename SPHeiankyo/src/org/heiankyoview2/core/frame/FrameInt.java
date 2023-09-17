package org.heiankyoview2.core.frame;

public class FrameInt {
	int array[];
	
	int minIValue = (int) 1.0e+30;
	int maxIValue = (int) -1.0e+30;
	
	/**
	 * Constructor
	 * @param numnodes
	 */
	public FrameInt(int numnodes) {
		array = new int[numnodes];
	}
	
	
	/**
	 * �l��1�Z�b�g����
	 * @param nodeid
	 * @param value
	 */
	public void set(int nodeid, int value) {
		if(nodeid <= 0 || nodeid > array.length) {
			System.out.println("  FrameInt: nodeid " + nodeid + " is out of range.");
			return;
		}
		array[nodeid - 1] = value;
		
		if (maxIValue < value)
			maxIValue = value;
		if (minIValue > value)
			minIValue = value;
	}
	
	
	/**
	 * �l��1�Q�b�g����
	 * @param nodeid
	 * @return
	 */
	public int get(int nodeid) {
		if(nodeid <= 0 || nodeid > array.length) {
			System.out.println("  FrameInt: nodeid " + nodeid + " is out of range.");
			return 0;
		}
		return array[nodeid - 1];
	}
	
	
	/**
	 * �i�[����l�̍ő�l�� int �`���Ŏw�肷��
	 * @param maxi �ő�l
	 */
	public void setMaxIValue(int maxi) {
		maxIValue = maxi;
	}

	/**
	 * �i�[����l�̍ő�l�� int �`���ŕԂ�
	 * @return �ő�l
	 */
	public int getMaxIValue() {
		return maxIValue;
	}

	
	/**
	 * �i�[����l�̍ŏ��l�� int �`���Ŏw�肷��
	 * @param mini �ŏ��l
	 */
	public void setMinIValue(int mini) {
		minIValue = mini;
	}

	/**
	 * �i�[����l�̍ŏ��l�� int �`���ŕԂ�
	 * @return �ŏ��l
	 */
	public int getMinIValue() {
		return minIValue;
	}
	
	/**
	 * Node�̐F�⍂�����Z�o���邽�߂̒l��Ԃ�
	 * @param id �l���i�[���Ă���ID
	 * @return Node�̐F�⍂�����Z�o���邽�߂̒l�i�����Ƃ���0.0�`1.0�A�s����ID�̏ꍇ�͕��l�j
	 */
	double getAppearanceValue(int id) {
		if (maxIValue <= minIValue)
			return 0.5;
		if(id <= 0 || id > array.length)
			return -1.0;

		double value =	(double) ((double)array[id] - minIValue)
				/ (double) (maxIValue - minIValue);
		if (value < 0.0)
			value = 0.0;
		if (value > 1.0)
			value = 1.0;

		return value;
	}
}
