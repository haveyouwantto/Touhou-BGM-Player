package hywt.music.touhou.gui;

import javax.swing.*;
import java.awt.*;

public class BaseFrame extends JFrame {

    /**
     *
     */
    private static final long serialVersionUID = 8608567595596266004L;

    public BaseFrame() {
        super();
        this.setIconImage(Toolkit.getDefaultToolkit()
                .getImage(ClassLoader.getSystemResource("icon.png"))); //$NON-NLS-1$
    }
}