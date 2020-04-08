package hywt.music.touhou;

import java.text.DecimalFormat;

public class Etc {
	public static String getEoSDFilename(int index) {
		++index;

		String filename;
		if (index < 10) {
			filename = "th06_0" + index + ".wav"; //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			filename = "th06_" + index + ".wav"; //$NON-NLS-1$ //$NON-NLS-2$
		}
		return filename;
	}

	public static String getMusicLengthTime(int sampleRate, long l) {
		long lengthSec = l / (sampleRate * 16 * 2 / 8);
		long second = lengthSec % 60;
		long minute = lengthSec / 60 % 60;
		return new DecimalFormat("00").format(minute) + ":" + new DecimalFormat("00").format(second);
	}
}
