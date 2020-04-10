package hywt.music.touhou.io;

import hywt.music.touhou.io.tfpack.TFPack;
import hywt.music.touhou.io.tfpack.TFPackInputStream;
import hywt.music.touhou.savedata.Game;
import hywt.music.touhou.savedata.Music;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class MusicSystem {

    public static MusicInputStream getInputStream(Game g, Music m, File f) throws IOException, UnsupportedAudioFileException {
        if (g.format == 0) {
            return new PCMInputStream(g, m, f);
        } else if (g.format == 1) {
            return new PCMInputStream(g, m, f);
        } else if (g.format == 2 || g.format == 3) {
            TFPack tfp = new TFPack(g, m, f);
            TFPackInputStream tfs = tfp.getInputStream();
            return new OggInputStream(g, m, f, tfs);
        }
        throw new UnsupportedAudioFileException();
    }

    public static long getLength(MusicInputStream musicIn) {
        int format = musicIn.game.format;
        Music music = musicIn.music;
        if (format == 0 || format == 1) {
            return music.preludeLength + music.loopLength;
        } else {
            return music.loopPos + music.loopLength;
        }
    }

    public static long getLoopPos(MusicInputStream musicIn){
        int format = musicIn.game.format;
        Music music = musicIn.music;
        if (format == 0 || format == 1) {
            return music.preludeLength;
        } else {
            return music.loopPos;
        }
    }

}
