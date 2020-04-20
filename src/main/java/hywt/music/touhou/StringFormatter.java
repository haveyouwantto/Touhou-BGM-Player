package hywt.music.touhou;

import java.text.DecimalFormat;

public class StringFormatter {
    public static String formatFileName(String template, int index) {
        ++index;

        String id = new DecimalFormat("00").format(index);

        return String.format(template, id);
    }

    public static String getMusicLengthTime(int sampleRate, long l) {
        long lengthSec = l / (sampleRate * 16 * 2 / 8);
        long second = lengthSec % 60;
        long minute = lengthSec / 60 % 60;
        DecimalFormat df = new DecimalFormat("00");
        return String.format("%s:%s", df.format(minute), df.format(second));
    }
}
