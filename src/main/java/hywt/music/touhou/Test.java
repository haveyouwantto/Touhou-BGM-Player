package hywt.music.touhou;

import hywt.music.touhou.io.MusicSystem;
import hywt.music.touhou.savedata.BGMPath;
import hywt.music.touhou.savedata.Game;
import hywt.music.touhou.savedata.Music;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class Test {
    public static void main(String[] args) throws UnsupportedAudioFileException {
        try {
            Constants.init();
            Game g = Constants.bgmdata.games.get(7);
            Music m = g.music.get(0);
            BGMPath bgmpath = BGMPath.load();
            MusicSystem.getInputStream(g, m, new File(bgmpath.path.get(g.no)));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Logger.error(e);
        }
    }

    public static void printInputStream(InputStream input) throws IOException {
        printInputStream(input, 16);
    }

    public static void printInputStream(InputStream input, int count) throws IOException {
        byte[] buffer = new byte[16];
        for (int i = 0; i < count; i++) {
            input.read(buffer);
            System.out.printf("%08x   ", i << 4);
            for (byte b : buffer) {
                System.out.printf("%02x ", b);
            }
            System.out.print("  ");
            for (byte b : buffer) {
                System.out.print(b >= 0x20 ? (char) b : '.');
            }
            System.out.println();
        }
        System.out.println();
    }
}
