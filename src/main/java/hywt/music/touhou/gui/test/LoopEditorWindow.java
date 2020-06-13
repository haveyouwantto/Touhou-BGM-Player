package hywt.music.touhou.gui.test;

import hywt.music.touhou.gui.BaseFrame;
import hywt.music.touhou.gui.KeyboardListener;
import hywt.music.touhou.gui.Messages;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class LoopEditorWindow {
    final BaseFrame jframe;
    final LoopEditor img;

    public LoopEditorWindow(File file) throws IOException {
        jframe = new BaseFrame();
        jframe.setTitle(Messages.getString("Visualizer.title"));
        jframe.getContentPane().setPreferredSize(new Dimension(512, 517));
        jframe.pack();
        jframe.setLocationRelativeTo(null);
        jframe.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        KeyboardListener keyboardListener = new KeyboardListener();
        jframe.addKeyListener(keyboardListener);

        img = new LoopEditor(file, keyboardListener);
        jframe.add(img);


    }

    public boolean isVisible() {
        return jframe.isVisible();
    }

    public void setVisible(boolean visible) {
        jframe.setVisible(visible);
    }

    public LoopEditor getWaveGraph() {
        return img;
    }

    public JFrame getFrame() {
        return jframe;
    }
}
