package org.heiankyoview2.core.util;

import java.awt.Color;

public class DefaultColorCalculator implements ColorCalculator {

	/**
	 * ���͐��l�ɉ����Ċ��炩�ɕω�����悤�ɐF���Z�o����
	 * @param value ���͐��l
	 * @return �F
	 */
	public Color calculate(float value) {
		Color color;

		// Calculate color gradation 
		// if the value is -1 or 1, the color of the value is gray.
		if (value < 0 || value > 1) {
			color = Color.gray;
			return color;
		}

		//red->yellow->green->blue
		float hue = (1.0f - value) * 160.f / 240.f;
		color = Color.getHSBColor(hue, 1.0f, 1.0f);

		return color;
	}

}
