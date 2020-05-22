package hywt.music.touhou.gui;

import javax.swing.JFrame;
import java.awt.Toolkit;

public class BaseFrame extends JFrame {

    /**
     *
     */
    private static final long serialVersionUID = 8608567595596266004L;
    public BaseFrame(){
        super();
		this.setIconImage(Toolkit.getDefaultToolkit()
				.getImage(MusicExporter.class.getResource("/assets/hywt/music/touhou/icon.png"))); //$NON-NLS-1$
    }
}