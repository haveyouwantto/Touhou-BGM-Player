package hywt.music.touhou.gui;

import javax.swing.*;
import java.awt.*;

public class Visualizer{
    /**
     *
     */
    private static final long serialVersionUID = 185239470341954L;
    final BaseFrame jframe;
    final WaveGraph img;

    public Visualizer(int bufferSize) {
        jframe = new BaseFrame();
        jframe.setTitle(Messages.getString("Visualizer.title"));
        jframe.getContentPane().setPreferredSize(new Dimension(512, 517));
        jframe.pack();
        jframe.setLocationRelativeTo(null);
        jframe.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        img = new WaveGraph(bufferSize);
        jframe.add(img);
    }

    public boolean isVisible() {
        return jframe.isVisible();
    }

    public void setVisible(boolean visible) {
        jframe.setVisible(visible);
    }

    public void update(final byte[] b, final float progress) {
        img.setData(b, progress);
        img.repaint();
    }

    public WaveGraph getWaveGraph() {
        return img;
    }

    public JFrame getFrame() {
        return jframe;
    }
}