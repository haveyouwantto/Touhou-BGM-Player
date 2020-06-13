package hywt.music.touhou;

import hywt.music.touhou.gui.Visualizer;
import hywt.music.touhou.gui.test.LoopEditorWindow;
import hywt.music.touhou.io.MusicInputStream;
import hywt.music.touhou.io.MusicSystem;
import hywt.music.touhou.savedata.BGMPath;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class Test {
    public static void main(String[] args) throws UnsupportedAudioFileException {
        try {
            Constants.init();
            MusicInputStream mis = MusicSystem.getInputStream(null, null, null);
            System.out.println(Test.class.getResource("/assets/hywt/music/touhou/icon.png"));
            Visualizer vis = new Visualizer(1024);
            BGMPath path = BGMPath.load();
            LoopEditorWindow lop = new LoopEditorWindow(new File("u:\\\u4e8c\u521b\\\u4e1c\u65b9\u590f\u591c\u796d \uff5e Shining Shooting Star\\BGM\\Boss03.wav"));
            lop.setVisible(true);
            lop.getWaveGraph().setStart(6771544);
            lop.getWaveGraph().setEnd(26729900);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
