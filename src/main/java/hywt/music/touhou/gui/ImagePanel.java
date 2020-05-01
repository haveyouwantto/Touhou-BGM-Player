package hywt.music.touhou.gui;

import java.awt.Color;

import javax.swing.JPanel;

import hywt.music.touhou.Etc;

import java.awt.Graphics;

public class ImagePanel extends JPanel {

    /**
     *
     */
    private static final long serialVersionUID = -6452859177867402541L;
    byte[] b;
    int[] ints;

    final Color black = new Color(0, 0, 0);
    final Color gray = new Color(128, 128, 128);
    final Color white = new Color(255, 255, 255);

    float progress;

    public ImagePanel(final int bufferSize) {
        this.ints = new int[bufferSize / 2];
        b = new byte[bufferSize];
        this.progress = 0;
    }

    public void setData(final byte[] b, final float progress) {
        this.b = b;
        this.progress = progress;
        for (int i = 0; i < b.length - 1; i += 2) {
            ints[i >> 1] = ((short) (Byte.toUnsignedInt(b[i]) | Byte.toUnsignedInt(b[i + 1]) << 8) >> 7);
        }
    }

    public void paintComponent(final Graphics g) {
        g.setColor(black);
        g.fillRect(0, 0, 512, 517);
        if (progress > 0) {
            g.setColor(gray);
            int i;
            for (i = 0; i < ints.length - 3; i++) {
                int offset = i % 2 == 0 ? 128 : 384;
                g.drawLine(i, ints[i] / 2 + offset, i + 2, ints[i + 2] / 2 + offset);
            }
            for (i = 0; i < ints.length >> 1; i += 2) {
                g.setColor(Etc.gen(i << 1));
                g.drawLine(ints[i] + 256, ints[i + 1] + 256, ints[i + 2] + 256, ints[i + 3] + 256);
            }
            g.setColor(white);
            g.fillRect(0, 512, (int) (progress * 512), 5);
        }
    }

}