package hywt.music.touhou;

import hywt.music.touhou.gui.LoopEditorWindow;
import hywt.music.touhou.gui.Visualizer;
import hywt.music.touhou.io.MusicInputStream;
import hywt.music.touhou.io.MusicSystem;
import hywt.music.touhou.savedata.BGMPath;
import hywt.music.touhou.savedata.Music;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Test {
    public static void main(String[] args) throws UnsupportedAudioFileException {
        try {
            Constants.init();
            MusicInputStream mis = MusicSystem.getInputStream(null,null,null);
            System.out.println(Test.class.getResource("/assets/hywt/music/touhou/icon.png"));
            Visualizer vis = new Visualizer(1024);
            BGMPath path = BGMPath.load();
            LoopEditorWindow lop = new LoopEditorWindow(new File("u:\\\u4e8c\u521b\\\u4e1c\u65b9\u590f\u591c\u796d \uff5e Shining Shooting Star\\BGM\\OP.wav"));
            lop.setVisible(true);
            lop.getWaveGraph().setStart(25397388-21772800);
            lop.getWaveGraph().setEnd(25397388);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
