package hywt.music.touhou.io;

import hywt.music.touhou.Constants;
import hywt.music.touhou.savedata.Game;
import hywt.music.touhou.savedata.GameFormat;
import hywt.music.touhou.savedata.Music;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class MusicSystem {

    // TODO reflect
    public static MusicInputStream getInputStream(Game g, Music m, File f)
            throws IOException, UnsupportedAudioFileException {
        Class<? extends MusicInputStream> c = Constants.formatMap.get("touhou_bgm_player:wave_files");
        try {
            Constructor<? extends MusicInputStream> constructor = c.getConstructor();
            return constructor.newInstance(g, m, f);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static int getPreludeLength(MusicInputStream musicIn) {
        GameFormat format = musicIn.game.format;
        Music music = musicIn.music;
        if (format == GameFormat.BGM_FOLDER || format == GameFormat.THBGM) {
            return music.preludeLength;
        } else {
            return (int) music.loopPos;
        }
    }

    public static int getPreludeLength(Game g, Music m) {
        GameFormat format = g.format;
        if (format == GameFormat.BGM_FOLDER || format == GameFormat.THBGM) {
            return m.preludeLength;
        } else {
            return (int) m.loopPos;
        }
    }

    public static long getLength(MusicInputStream musicIn) {
        return getLength(musicIn.game, musicIn.music);
    }

    public static long getLength(Game g, Music m) {
        GameFormat format = g.format;
        if (format == GameFormat.BGM_FOLDER || format == GameFormat.THBGM) {
            return m.preludeLength + m.loopLength;
        } else {
            return m.loopPos + m.loopLength;
        }
    }

    public static long getLoopPos(MusicInputStream musicIn) {
        return getLoopPos(musicIn.game, musicIn.music);
    }

    public static long getLoopPos(Game g, Music m) {
        GameFormat format = g.format;
        if (format == GameFormat.BGM_FOLDER || format == GameFormat.THBGM) {
            return m.preludeLength;
        } else {
            return m.loopPos;
        }
    }

    public static float getLoopPercentage(Game g, Music m) {
        return (float) (getLoopPos(g, m)) / getLength(g, m);
    }

    public static long roundByte(long src, int minBytes) {
        return src / minBytes * minBytes;
    }

}
