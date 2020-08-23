package hywt.music.touhou.gui;

import hywt.music.touhou.Etc;

import javax.swing.*;
import java.awt.*;

public class WaveGraph extends JPanel {

    /**
     *
     */
    private static final long serialVersionUID = -6452859177867402541L;
    protected int[] ints;
    protected float marker1;
    protected float marker2;

    protected float progress;

    public WaveGraph(final int bufferSize) {
        this.ints = new int[bufferSize / 2];
        this.progress = 0;

        marker1 = 0;
        marker2 = 0;
    }

    public void setData(final byte[] b, final float progress) {
        this.progress = progress;
        for (int i = 0; i < b.length - 1; i += 2) {
            ints[i >> 1] = ((short) (Byte.toUnsignedInt(b[i]) | Byte.toUnsignedInt(b[i + 1]) << 8) >> 7);
        }
    }

    public void paintComponent(final Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, 512, 517);
        if (progress > 0) {
            g.setColor(Color.gray);
            int i;
            for (i = 0; i < ints.length - 3; i++) {
                int offset = i % 2 == 0 ? 128 : 384;
                g.drawLine(i, ints[i] / 2 + offset, i + 2, ints[i + 2] / 2 + offset);
            }
            for (i = 0; i < ints.length - 1; i += 2) {
                //g.setColor(Etc.gen(i << 1));
                //g.drawLine(ints[i] + 256, ints[i + 1] + 256, ints[i + 2] + 256, ints[i + 3] + 256);
                int x = ints[i];
                int y = ints[i + 1];
                float distance = Math.max(Math.abs(x), Math.abs(y)); // Chebyshev distance
                g.setColor(Etc.gen(distance));
                float angle = (float) (i / (float) ints.length * 2 * Math.PI);
                g.drawLine(256, 256, (int) (distance * (Math.cos(angle))) + 256, (int) (distance * (Math.sin(angle))) + 256);
            }
            g.setColor(Color.white);
            g.fillRect(0, 512, (int) (progress * 512), 5);
            g.setColor(Color.red);
            g.drawLine((int) (marker1 * 512), 512, (int) (marker1 * 512), 517);
            g.setColor(Color.green);
            g.drawLine((int) (marker2 * 512), 512, (int) (marker2 * 512), 517);
        }
    }

    public float getMarker1() {
        return marker1;
    }

    public void setMarker1(float marker1) {
        this.marker1 = marker1;
    }

    public float getMarker2() {
        return marker2;
    }

    public void setMarker2(float marker2) {
        this.marker2 = marker2;
    }
}