package hywt.music.touhou;

import java.awt.*;

public class Etc {
    public static Color gen(final float i) {
        final float v0 = -i % 360;
        return HSVtoRGB(v0 + 240, 1, 1);
    }

    public static Color HSVtoRGB(float h /* 0~360 degrees */, final float s /* 0 ~ 1.0 */,
                                 final float v /* 0 ~ 1.0 */) {
        float f, p, q, t;
        if (s == 0) { // achromatic (grey)
            return new Color(v, v, v);
        }
        h /= 60; // sector 0 to 5
        final int i = (int) Math.floor(h);
        f = h - i; // factorial part of h
        p = v * (1 - s);
        q = v * (1 - s * f);
        t = v * (1 - s * (1 - f));
        switch (i) {
            case 0:
                return new Color(v, t, p);
            case 1:
                return new Color(q, v, p);
            case 2:
                return new Color(p, v, t);
            case 3:
                return new Color(p, q, v);
            case 4:
                return new Color(t, p, v);
            default:
                return new Color(v, p, q);
        }
    }
}