package hywt.music.touhou.savedata;

import hywt.music.touhou.io.MusicInputStream;
import hywt.music.touhou.io.OggInputStream;
import hywt.music.touhou.io.PCMInputStream;
import hywt.music.touhou.io.tfpack.TFPackInputStream;
import hywt.music.touhou.io.tfpack.TFV1OggInputStream;
import hywt.music.touhou.io.tfpack.TFV2OggInputStream;
import hywt.music.touhou.io.tfpack.TFV3OggInputStream;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public enum GameFormat {
    WAVE_FILE("WAVE_FILE", -1) {
        @Override
        public MusicInputStream getInputStream(Game g, Music m, File f) throws IOException {
            return new PCMInputStream(g, m, new File(f, m.metadata));
        }
    },
    BGM_FOLDER("BGM_FOLDER", 0) {
        @Override
        public MusicInputStream getInputStream(Game g, Music m, File f) throws IOException {
            return new PCMInputStream(g, m, f);
        }
    },
    THBGM("THBGM", 1) {
        @Override
        public MusicInputStream getInputStream(Game g, Music m, File f) throws IOException {
            return new PCMInputStream(g, m, f);
        }
    },
    TFPACK_1("TFPACK_1", 2) {
        @Override
        public MusicInputStream getInputStream(Game g, Music m, File f) throws IOException, UnsupportedAudioFileException {
            TFPackInputStream tfs = new TFV1OggInputStream(m, f);
            return new OggInputStream(g, m, f, tfs);
        }
    },
    TFPACK_2("TFPACK_2", 3) {
        @Override
        public MusicInputStream getInputStream(Game g, Music m, File f) throws IOException, UnsupportedAudioFileException {
            TFPackInputStream tfs = new TFV2OggInputStream(m, f);
            return new OggInputStream(g, m, f, tfs);
        }
    },
    TFPACK_3("TFPACK_3", 4) {
        @Override
        public MusicInputStream getInputStream(Game g, Music m, File f) throws IOException, UnsupportedAudioFileException {
            TFPackInputStream tfs = new TFV3OggInputStream(m, f);
            return new OggInputStream(g, m, f, tfs);
        }
    };

    public static boolean isTFPack(GameFormat format) {
        return format.ordinal() >= 2;
    }

    GameFormat(final String name, final int ordinal) {
    }

    public abstract MusicInputStream getInputStream(Game g, Music m, File f) throws IOException, UnsupportedAudioFileException;
}
