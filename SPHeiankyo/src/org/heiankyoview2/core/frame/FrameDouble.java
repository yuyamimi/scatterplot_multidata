package org.heiankyoview2.core.frame;

public class FrameDouble {
	double array[];
	double minDValue = 1.0e+30;
	double maxDValue = -1.0e+30;
	
	/**
	 * Constructor
	 * @param numnodes
	 */
	public FrameDouble(int numnodes) {
		array = new double[numnodes];
	}
	
	
	/**
	 * �l��1�Z�b�g����
	 * @param nodeid
	 * @param value
	 */
	public void set(int nodeid, double value) {
		if(nodeid <= 0 || nodeid > array.length) {
			System.out.println("  FrameDouble: nodeid " + nodeid + " is out of range.");
			return;
		}
		array[nodeid - 1] = value;
		
		if (maxDValue < value)
			maxDValue = value;
		if (minDValue > value)
			minDValue = value;
	}
	
	
	/**
	 * �l��1�Q�b�g����
	 * @param nodeid
	 * @return
	 */
	public double get(int nodeid) {
		if(nodeid <= 0 || nodeid > array.length) {
			System.out.println("  FrameDouble: nodeid " + nodeid + " is out of range.");
			return 0.0;
		}
		return array[nodeid - 1];
	}
	
	/**
	 * �i�[����l�̍ő�l�� double �`���Ŏw�肷��
	 * @param maxd �ő�l
	 */
	public void setMaxDValue(double maxd) {
		maxDValue = maxd;
	}

	/**
	 * �i�[����l�̍ő�l�� double �`���ŕԂ�
	 * @return �ő�l
	 */
	public double getMaxDValue() {
		return maxDValue;
	}

	
	/**
	 * �i�[����l�̍ŏ��l�� double �`���Ŏw�肷��
	 * @param mind �ŏ��l
	 */
	public void setMinDValue(double mind) {
		minDValue = mind;
	}

	/**
	 * �i�[����l�̍ŏ��l�� double �`���ŕԂ�
	 * @return �ŏ��l
	 */
	public double getMinDValue() {
		return minDValue;
	}

	
	/**
	 * Node�̐F�⍂�����Z�o���邽�߂̒l��Ԃ�
	 * @param id �l���i�[���Ă���ID
	 * @return Node�̐F�⍂�����Z�o���邽�߂̒l�i�����Ƃ���0.0�`1.0�A�s����ID�̏ꍇ�͕��l�j
	 */
	double getAppearanceValue(int id) {
		if (maxDValue <= minDValue)
			return 0.5;
		if(id <= 0 || id > array.length)
			return -1.0;
		
		double value = (array[id - 1] - minDValue) / (maxDValue - minDValue);
		if (value < 0.0)
			value = 0.0;
		if (value > 1.0)
			value = 1.0;

		return value;
	}
}
