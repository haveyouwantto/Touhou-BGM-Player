package hywt.music.touhou.gui;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Extra extends BaseFrame implements LazyLoader {

    /**
     *
     */
    private static final long serialVersionUID = -6778905484912603197L;
    private List<JFrame> frames;
    private JPanel panel;


    public Extra() {
    }

    public void addFrame(JFrame frame, String name) {
        JButton btn = new JButton(name);
        //btn.addActionListener(new);
        panel.add(btn);
        frames.add(frame);
    }

    @Override
    public void load() {
        this.setTitle(Messages.getString("Extra.title"));
        this.setBounds(100, 100, 440, 320);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        frames = new ArrayList<>();

        panel = new JPanel();
        this.add(panel);
    }
}