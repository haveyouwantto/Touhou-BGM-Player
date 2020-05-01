package hywt.music.touhou.gui;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class Visualizer {
    final JFrame jframe;
    final ImagePanel img;

    public Visualizer(int bufferSize) {
        jframe = new JFrame();
        jframe.setTitle(Messages.getString("Visualizer.title"));
        jframe.getContentPane().setPreferredSize(new Dimension(512, 517));
        jframe.pack();
        jframe.setLocationRelativeTo(null);
        jframe.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		jframe.setIconImage(Toolkit.getDefaultToolkit()
				.getImage(PathManager.class.getResource("/assets/hywt/music/touhou/icon.png")));

        img = new ImagePanel(bufferSize);
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
}