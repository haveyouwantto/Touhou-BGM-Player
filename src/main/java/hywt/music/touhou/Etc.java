package hywt.music.touhou;

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
}
