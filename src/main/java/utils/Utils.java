package utils;

import java.text.DecimalFormat;

public final class Utils {

	public static final float round(float num) {
		final DecimalFormat f = new DecimalFormat("##.00");
		return Float.parseFloat(f.format(num));
	}
}
