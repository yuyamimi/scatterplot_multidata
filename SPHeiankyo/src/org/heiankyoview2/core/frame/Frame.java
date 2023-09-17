package org.heiankyoview2.core.frame;

public class Frame {
	TreeFrame tf = null;
	Object valuearray[] = null;
	double time = 0.0;
	int id;
	
	/**
	 * Constructor
	 */
	public Frame() {
	}
	
	
	/**
	 * Constructor
	 */
	public Frame(TreeFrame tf) {
		this.tf = tf;
		valuearray = new Object[tf.getNumValues()];
		for(int i = 0; i < valuearray.length; i++) {
			int type = tf.getValueType(i + 1);
			switch(type) {
			case TreeFrame.FRAME_STRING:
				FrameString fstring = new FrameString(tf.getNumNodes());
				valuearray[i] = (Object)fstring;
				break;
			case TreeFrame.FRAME_DOUBLE:
				FrameDouble fdouble = new FrameDouble(tf.getNumNodes());
				valuearray[i] = (Object)fdouble;
				break;
			case TreeFrame.FRAME_INT:
				FrameInt fint = new FrameInt(tf.getNumNodes());
				valuearray[i] = (Object)fint;
				break;
			}
		}
	}
	
	
	/**
	 * TreeFrame���Z�b�g����
	 * @param tf
	 */
	public void setTreeFrame(TreeFrame tf) {
		this.tf = tf;
	}
	
	
	/**
	 * TreeFrame���Q�b�g����
	 * @return
	 */
	public TreeFrame getTreeFrame() {
		return tf;
	}
	
	
	/**
	 * ID���Z�b�g����
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	
	/**
	 * ID���Q�b�g����
	 * @return
	 */
	public int getId() {
		return id;
	}
	
	
	/**
	 * �������Z�b�g����
	 * @param time
	 */
	public void setTime(double time) {
		this.time = time;
	}
	
	
	/**
	 * �������Q�b�g����
	 * @return
	 */
	public double getTime() {
		return time;
	}
	
	
	/**
	 * String�^��Value���Z�b�g����
	 * @param valueid
	 * @param nodeid
	 * @param value
	 */
	public void set(int valueid, int nodeid, String value) {
		if(valueid <= 0 || valueid > valuearray.length) {
			System.out.println("  Frame: valueid " + valueid + " is out of range.");
			return;
		}
		FrameString fstring = (FrameString)valuearray[valueid - 1];
		fstring.set(nodeid, value);
	}
	
	
	/**
	 * double�^��Value���Z�b�g����
	 * @param valueid
	 * @param nodeid
	 * @param value
	 */
	public void set(int valueid, int nodeid, double value) {
		if(valueid <= 0 || valueid > valuearray.length) {
			System.out.println("  Frame: valueid " + valueid + " is out of range.");
			return;
		}
		FrameDouble fdouble = (FrameDouble)valuearray[valueid - 1];
		fdouble.set(nodeid, value);
	}
	
	
	/**
	 * int�^��Value���Z�b�g����
	 * @param valueid
	 * @param nodeid
	 * @param value
	 */
	public void set(int valueid, int nodeid, int value) {
		if(valueid <= 0 || valueid > valuearray.length) {
			System.out.println("  Frame: valueid " + valueid + " is out of range.");
			return;
		}
		FrameInt fint = (FrameInt)valuearray[valueid - 1];
		fint.set(nodeid, value);
	}
	
	
	/**
	 * String�^��Value���Q�b�g����
	 * @param valueid
	 * @param nodeid
	 */
	public String getString(int valueid, int nodeid) {
		if(valueid <= 0 || valueid > valuearray.length) {
			System.out.println("  Frame: valueid " + valueid + " is out of range.");
			return null;
		}
		FrameString fstring = (FrameString)valuearray[valueid - 1];
		return fstring.get(nodeid);
	}
	
	
	/**
	 * double�^��Value���Q�b�g����
	 * @param valueid
	 * @param nodeid
	 */
	public double getDouble(int valueid, int nodeid) {
		if(valueid <= 0 || valueid > valuearray.length) {
			System.out.println("  Frame: valueid " + valueid + " is out of range.");
			return 0.0;
		}
		FrameDouble fdouble = (FrameDouble)valuearray[valueid - 1];
		return fdouble.get(nodeid);
	}
	
	
	/**
	 * int�^��Value���Q�b�g����
	 * @param valueid
	 * @param nodeid
	 */
	public int getInt(int valueid, int nodeid) {
		if(valueid <= 0 || valueid > valuearray.length) {
			System.out.println("  Frame: valueid " + valueid + " is out of range.");
			return 0;
		}
		FrameInt fint = (FrameInt)valuearray[valueid - 1];
		return fint.get(nodeid);
	}
	
	/**
	 * �i�[����l�̍ő�l�� double �`���Ŏw�肷��
	 */
	public void setMaxDValue(int valueid, double maxd) {
		if(valueid <= 0 || valueid > valuearray.length) {
			System.out.println("  Frame: valueid " + valueid + " is out of range.");
			return;
		}
		FrameDouble fdouble = (FrameDouble)valuearray[valueid - 1];
		fdouble.setMaxDValue(maxd);
	}

	/**
	 * �i�[����l�̍ŏ��l�� double �`���Ŏw�肷��
	 */
	public void setMinDValue(int valueid, double mind) {
		if(valueid <= 0 || valueid > valuearray.length) {
			System.out.println("  Frame: valueid " + valueid + " is out of range.");
			return;
		}
		FrameDouble fdouble = (FrameDouble)valuearray[valueid - 1];
		fdouble.setMinDValue(mind);
	}

	/**
	 * �i�[����l�̍ő�l�� int �`���Ŏw�肷��
	 */
	public void setMaxIValue(int valueid, int maxi) {
		if(valueid <= 0 || valueid > valuearray.length) {
			System.out.println("  Frame: valueid " + valueid + " is out of range.");
			return;
		}
		FrameInt fint = (FrameInt)valuearray[valueid - 1];
		fint.setMaxIValue(maxi);
	}

	/**
	 * �i�[����l�̍ŏ��l�� int �`���Ŏw�肷��
	 */
	public void setMinIValue(int valueid, int mini) {
		if(valueid <= 0 || valueid > valuearray.length) {
			System.out.println("  Frame: valueid " + valueid + " is out of range.");
			return;
		}
		FrameInt fint = (FrameInt)valuearray[valueid - 1];
		fint.setMinIValue(mini);
	}

	
	/**
	 * Node�̐F�⍂�����Z�o���邽�߂̒l��Ԃ�
	 * @param valueid
	 * @param nodeid
	 * @return Node�̐F�⍂�����Z�o���邽�߂̒l�i�����Ƃ���0.0�`1.0�A�s����ID�̏ꍇ�͕��l�j
	 */
	public double getAppearanceValue(int valueid, int nodeid) {
		double value = -1.0;
		
		switch(tf.getValueType(valueid)) {
		case TreeFrame.FRAME_STRING:
			FrameString fstring = (FrameString)valuearray[valueid - 1];
			value = fstring.getAppearanceValue(nodeid);
			break;
		case TreeFrame.FRAME_DOUBLE:
			FrameDouble fdouble = (FrameDouble)valuearray[valueid - 1];
			value = fdouble.getAppearanceValue(nodeid);
			break;
		case TreeFrame.FRAME_INT:
			FrameInt fint = (FrameInt)valuearray[valueid - 1];
			value = fint.getAppearanceValue(nodeid);
			break;
		}

		return value;
	}

	
}
